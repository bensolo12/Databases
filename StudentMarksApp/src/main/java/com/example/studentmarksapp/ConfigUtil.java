// src/main/java/com/example/studentmarksapp/ConfigUtil.java
package com.example.studentmarksapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static DBType getDbType() {
        String dbType = properties.getProperty("dbType");
        return DBType.valueOf(dbType);
    }
}