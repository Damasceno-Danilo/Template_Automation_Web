package br.com.ddamasceno.core.report.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReportProperties {
    private Properties properties;

    public ReportProperties() {
        properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/report.properties")) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

