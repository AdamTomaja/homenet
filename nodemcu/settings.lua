function readSettings() 
    if file.open("settings.json", "r") then
        text = file.read()
        print(text)
        local settings = sjson.decode(text)
        file.close()
        return settings;
    else 
        print("Settings file not found!")
    end
end

function writeSettings(settings)
    if file.open("settings.json", "w+") then
        file.write(sjson.encode(settings))
        file.close()
    else
        print("Unable to save settings")
    end
end

