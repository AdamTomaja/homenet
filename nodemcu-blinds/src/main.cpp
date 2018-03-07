#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>

/** Node configuration **/
const char* SSID = "<SSID>";
const char* PASSWORD = "<PASSWORD>";
const char* MQTT_SERVER = "192.168.1.10";
const char* MQTT_USERNAME = "yourMQTTusername";
const char* MQTT_PASSWORD = "yourMQTTpassword";
const int   MQTT_PORT = 1883;

/** MQTT Constant **/
const char* NODE_NAME = "adamRoomWindowShutter";
const char* SET_TOPIC = "cmnd/adamRoomWindowShutter";
const char* STATUS_TOPIC = "stat/adamRoomWindowShutter/status";
const char* UP_MESSAGE = "UP";
const char* DOWN_MESSAGE = "DOWN";
const char* STOP_MESSAGE = "STOP";

/** PINOUT **/
const int   DIRECTION_PIN = 14;
const int   POWER_PIN     = 4;

/** Internal **/
const int ON = 0;
const int OFF = 1;

/** Timing **/
const unsigned long FULL_MOVEMENT_TIME = 40 * 1000;

unsigned long lastCommandTime = 0;
bool stateModified = false;

WiFiClient espClient;
PubSubClient client(espClient);

void setupWifi();
void checkWifi();
void setupMQTT();
void checkMQTT();
void reconnectMQTT();
void callback(char* topic, byte* payload, unsigned int length);

void setup() {
  Serial.begin(115200);
  Serial.println("Starting ...");

  pinMode(POWER_PIN, OUTPUT);
  pinMode(DIRECTION_PIN, OUTPUT);
  digitalWrite(POWER_PIN, OFF);
  digitalWrite(DIRECTION_PIN, OFF);

  setupWifi();
  setupMQTT();
}

void setupWifi() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(SSID, PASSWORD);

  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print("Connecting to: ");
    Serial.println(SSID);
  }

  Serial.println("WiFi connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void markStateModified() {
  stateModified = true;
  lastCommandTime = millis();
}

void writeState(int power, int direction) {
  digitalWrite(DIRECTION_PIN, direction);
  digitalWrite(POWER_PIN, power);
  markStateModified();
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.println("Message received");

  char message[length + 1];
  for(int i = 0; i < length; i++) {
    message[i] = (char) payload[i];
  }

  message[length] = '\0';

  String messageString = String(message);

  if(messageString == "UP") {
    writeState(ON, OFF);
    client.publish(STATUS_TOPIC, "UP");
  }

  if(messageString == "DOWN") {
    writeState(ON, ON);
    client.publish(STATUS_TOPIC, "DOWN");
  }

  if(messageString == "STOP") {
    writeState(OFF, OFF);
    client.publish(STATUS_TOPIC, "STOP");
  }

  Serial.println(message);
}

void loop() {

  checkWifi();
  if(!client.connected()) {
    Serial.println("MQTT disconnected.");
    reconnectMQTT();
  }

  client.loop();

  if(stateModified && (millis() - lastCommandTime > FULL_MOVEMENT_TIME)) {
    Serial.println("Turning off");
    digitalWrite(POWER_PIN, OFF);
    digitalWrite(DIRECTION_PIN, OFF);
    stateModified = false;
    client.publish(STATUS_TOPIC, "STOP");
  }
}

void checkWifi() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.print("WIFI Disconnected. Attempting reconnection.");
    setupWifi();
  }
}

void setupMQTT() {
  client.setServer(MQTT_SERVER, MQTT_PORT);
  client.setCallback(callback);
}

void reconnectMQTT() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.println("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(NODE_NAME, MQTT_USERNAME, MQTT_PASSWORD)) {
      Serial.println("MQTT Connected");
      client.subscribe(SET_TOPIC);
      client.publish(STATUS_TOPIC,"ACTIVE");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
