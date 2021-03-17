package com.paxovision.selenium.driver;

import com.typesafe.config.Config;

public class ConfigTest {

    public static void main(String[] args) {
        Config config = Configs.newBuilder()
                            .withResource("driver-factory.conf")
                            .withSystemEnvironment()
                            .withSystemProperties()
                            .build();

        System.out.println(config.getList("browser.list"));

        Config chromeConfig = config.getConfig("chrome");
        System.out.println(chromeConfig.getString("driver.win.path"));
        System.out.println(chromeConfig.getString("driver.mac.path"));
        System.out.println(chromeConfig.getList("options"));
        System.out.println("****************************************");
        Config chromeHeadlessConfig = config.getConfig("chrome-headless");
        System.out.println(chromeHeadlessConfig.getString("driver.win.path"));
        System.out.println(chromeHeadlessConfig.getString("driver.mac.path"));
        System.out.println(chromeHeadlessConfig.getList("options"));
    }
}
