package com.paxovision.selenium.driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebDriverFactoryConfig {

    private static WebDriverFactoryConfig instance = null;
    private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);


    private WebDriverFactoryConfig(){
        logger.info("Creating WebDriverFactoryConfig instance from config file");
    }

    public static WebDriverFactoryConfig getInstance(){
        if(instance == null){
            instance = new WebDriverFactoryConfig();
        }
        return instance;
    }

    public ChromeConfig getChromeConfig(){
        return new ChromeConfig();
    }

    public static class ChromeConfig{

        public static final String DRIVER_KEY = "webdriver.chrome.driver";

        private List<String> chromeArguments;
        public ChromeConfig(){
            chromeArguments = new ArrayList<>();

            chromeArguments.add("--ignore-certificate-errors");
            chromeArguments.add("--incognito");
            chromeArguments.add("--headless");
        }

        public String[] chromeArguments(){
            String[] options = new String[chromeArguments.size()];
            options =  chromeArguments.toArray(options);
            return options;
        }

        public ChromeOptions getOptions(){
            ChromeOptions options = new ChromeOptions();
            options.addArguments(chromeArguments());
            return options;
        }

        public String getChromeDriverExecutable(){
            String filePath = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/chrome/chromedriver";
            if(new File(filePath).exists()){
                return filePath;
            }
            else{
                throw new RuntimeException("Chrome driver file does not exists: " + filePath);
            }
        }

    }


}
