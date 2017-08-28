package com.cydercode.homenet.server;

import java.util.Date;

public class Time {

    public static long getTimestamp() {
        return new Date().getTime();
    }
}
