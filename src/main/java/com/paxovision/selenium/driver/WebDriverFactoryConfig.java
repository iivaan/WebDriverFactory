package com.paxovision.selenium.driver;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebDriverFactoryConfig {

    private static WebDriverFactoryConfig instance = null;
    private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    private static Config config = null;
    private String remoteWebDriverCapability = "capabilities";

    private WebDriverFactoryConfig(){
        logger.info("Creating WebDriverFactoryConfig instance from config file");
        config = Configs.newBuilder()
                .withResource("driver-factory.conf")
                .withSystemEnvironment()
                .withSystemProperties()
                .build();
    }

    public static WebDriverFactoryConfig getInstance(){
        if(instance == null){
            instance = new WebDriverFactoryConfig();
        }
        return instance;
    }

    public Config config(){
        return config;
    }

    public void setRemoteWebDriverCapability(String path){
        remoteWebDriverCapability = path;
    }
    public String getRemoteWebDriverCapability(){
        return remoteWebDriverCapability;
    }

    public ChromeConfig getChromeConfig(boolean headless){
        return new ChromeConfig(headless);
    }
    public FirefoxConfig getFirefoxConfig(boolean headless){
        return new FirefoxConfig(headless);
    }
    public RemoteBrowserstackWebDriverConfig getRemoteBrowserstackDriverConfig(){

        String capability = System.getenv("CAPABILITY");
        if(capability == null || capability.isEmpty()){
            capability = System.getProperty("CAPABILITY");
        }
        if(capability != null || !capability.isEmpty()){
            remoteWebDriverCapability = remoteWebDriverCapability + "." + capability;
        }

        return new RemoteBrowserstackWebDriverConfig(remoteWebDriverCapability);
    }

    public static class ChromeConfig{
        private Config chromeConfig;
        private boolean isHeadless = false;
        public ChromeConfig(boolean headless){
            this.isHeadless = headless;
            if(this.isHeadless){
                chromeConfig = config.getConfig("chrome-headless");
            }
            else{
                chromeConfig = config.getConfig("chrome");
            }
        }

        public String getDriverKey(){
            String key = chromeConfig.getString("key");
            return key;
        }

        public ChromeOptions getOptions(){
            ChromeOptions options = new ChromeOptions();
            ConfigList list =  chromeConfig.getList("options");
            for( ConfigValue item :list){
                //System.out.println(item.unwrapped().toString());
                options.addArguments(item.unwrapped().toString());
            }
            return options;
        }

        public String getChromeDriverExecutable(){
            String filePath = null;

            if(config.getString("os.name").contains("Mac")){
                 filePath = chromeConfig.getString("driver.mac.path");
            }
            else if(config.getString("os.name").contains("Windows")){
                 filePath = chromeConfig.getString("driver.win.path");
            }

            if(new File(filePath).exists()){
                return filePath;
            }
            else{
                throw new RuntimeException("Chrome driver file does not exists: " + filePath);
            }
        }

    }

    public static class FirefoxConfig{
        private Config firefoxConfig;
        private boolean isHeadless = false;
        public FirefoxConfig(boolean headless){
            this.isHeadless = headless;
            if(this.isHeadless){
                firefoxConfig = config.getConfig("firefox-headless");
            }
            else{
                firefoxConfig = config.getConfig("firefox");
            }
        }

        public String getDriverKey(){
            String key = firefoxConfig.getString("key");
            return key;
        }

        public FirefoxOptions getOptions(){
            FirefoxOptions options = new FirefoxOptions();
            ConfigList list =  firefoxConfig.getList("options");
            for( ConfigValue item :list){
                //System.out.println("Option: " + item.unwrapped().toString());
                options.addArguments(item.unwrapped().toString());
            }

            ConfigList capabilities=  firefoxConfig.getList("capabilities");
            for( ConfigValue item :capabilities){
                //System.out.println("Capability:" + item);
                Map map = (Map) item.unwrapped();
                String name = (String) map.get("name");
                Object value = map.get("value");
                options.setCapability(name,value);
            }


            return options;
        }

        public String getChromeDriverExecutable(){
            String filePath = null;

            if(config.getString("os.name").contains("Mac")){
                filePath = firefoxConfig.getString("driver.mac.path");
            }
            else if(config.getString("os.name").contains("Windows")){
                filePath = firefoxConfig.getString("driver.win.path");
            }

            if(new File(filePath).exists()){
                return filePath;
            }
            else{
                throw new RuntimeException("Firefox gecko driver file does not exists: " + filePath);
            }
        }
    }

    public static class RemoteBrowserstackWebDriverConfig {
        private Config remoteWebDriverConfig;
        private String remoteWebDriverCapability = "capabilities";

        public RemoteBrowserstackWebDriverConfig(String remoteWebDriverCapability){
            this.remoteWebDriverCapability = remoteWebDriverCapability.toLowerCase();
            remoteWebDriverConfig = config.getConfig("remote-browserstack");
        }

        public URL getRemoteWebDriverUrl(){
            URL url = null;
            String gridUrl = remoteWebDriverConfig.getString("grid-url");

            try {
                 url = new URL(gridUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }
        public DesiredCapabilities getDesiredCapabilities(){
            DesiredCapabilities capabilities = new DesiredCapabilities();
            ConfigList list=  remoteWebDriverConfig.getList(remoteWebDriverCapability);

            for( ConfigValue item :list){
                //System.out.println("Capability:" + item);
                Map map = (Map) item.unwrapped();
                String name = (String) map.get("name");
                Object value = map.get("value");
                capabilities.setCapability(name,value);
            }
            return capabilities;
        }
    }
}
