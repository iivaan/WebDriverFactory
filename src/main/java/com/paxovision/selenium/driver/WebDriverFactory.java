package com.paxovision.selenium.driver;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebDriverFactory {
    private static WebDriverFactory instance = null;
    private ThreadLocal<WebDriver> driverCollection = new ThreadLocal<WebDriver>();


    private static final String LOCAL_GRID_URL = "http://selenium-hub.shiftedtech.com/wd/hub";
    //private static final String USERNAME = "azfarlodi1";
    //private static final String AUTOMATE_KEY = "gaHiesWUTssKhwxGuGG7";
    //private static final String CLOUD_GRID_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    //private static final String USERNAME = "bahjatkhan1";
    //private static final String AUTOMATE_KEY = "TbVWMFj8YNcEMSkvwoqt";
    //private static final String CLOUD_GRID_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);


    private WebDriverFactory(){

    }

    public static WebDriverFactory getInstance(){
        String webDriverType = System.getenv("WEB_DRIVER_TYPE");
        if(webDriverType == null || webDriverType.isEmpty()){
            webDriverType = System.getProperty("WEB_DRIVER_TYPE");
        }
        return getInstance(webDriverType);
    }

    public static WebDriverFactory getInstance(String webDriverType){
        WebDriverFactoryConfig config = WebDriverFactoryConfig.getInstance();

        if(instance == null){
            instance = new WebDriverFactory();
        }

        if (webDriverType == null) {
            webDriverType = "CHROME";
            logger.info("Browser is missing. Using default browser {}", webDriverType);
        }

        if(instance.driverCollection.get() == null) {

            logger.info("Creating browser instance {} ", webDriverType);

            WebDriver driver = null;
            if(webDriverType.toUpperCase().contentEquals("CHROME")) {
                WebDriverFactoryConfig.ChromeConfig chromeConfig = config.getChromeConfig(false);

                logger.info("Driver Key: {}", chromeConfig.getDriverKey());
                logger.info("Driver Path: {}", chromeConfig.getChromeDriverExecutable());
                System.setProperty(chromeConfig.getDriverKey(),chromeConfig.getChromeDriverExecutable());
                ChromeOptions options = chromeConfig.getOptions();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                options.merge(capabilities);

                logger.info("Options: {}",  options.asMap());

                driver = new ChromeDriver(options);
            }
            else if(webDriverType.toUpperCase().contentEquals("CHROME-HEADLESS")) {
                WebDriverFactoryConfig.ChromeConfig chromeConfig = config.getChromeConfig(true);

                logger.info("Driver Key: {}", chromeConfig.getDriverKey());
                logger.info("Driver Path: {}", chromeConfig.getChromeDriverExecutable());
                System.setProperty(chromeConfig.getDriverKey(),chromeConfig.getChromeDriverExecutable());
                ChromeOptions options = chromeConfig.getOptions();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                options.merge(capabilities);

                logger.info("Options: {}",  options.asMap());

                driver = new ChromeDriver(options);
            }
            else if(webDriverType.toUpperCase().contentEquals("FIREFOX")) {
                WebDriverFactoryConfig.FirefoxConfig firefoxConfig = config.getFirefoxConfig(false);

                logger.info("Driver Key: {}", firefoxConfig.getDriverKey());
                logger.info("Driver Path: {}", firefoxConfig.getChromeDriverExecutable());
                System.setProperty(firefoxConfig.getDriverKey(),firefoxConfig.getChromeDriverExecutable());
                FirefoxOptions options = firefoxConfig.getOptions();

                logger.info("Options: {}",  options.asMap());

                driver = new FirefoxDriver(options);
            }
            else if(webDriverType.toUpperCase().contentEquals("FIREFOX-HEADLESS")) {
                WebDriverFactoryConfig.FirefoxConfig firefoxConfig = config.getFirefoxConfig(true);

                logger.info("Driver Key: {}", firefoxConfig.getDriverKey());
                logger.info("Driver Path: {}", firefoxConfig.getChromeDriverExecutable());
                System.setProperty(firefoxConfig.getDriverKey(),firefoxConfig.getChromeDriverExecutable());
                FirefoxOptions options = firefoxConfig.getOptions();

                logger.info("Options: {}",  options.asMap());

                driver = new FirefoxDriver(options);
            }
            else if(webDriverType.toUpperCase().contentEquals("REMOTE-BROWSERSTACK")){
                WebDriverFactoryConfig.RemoteBrowserstackWebDriverConfig remoteWebDriverConfig = config.getRemoteBrowserstackDriverConfig();

                DesiredCapabilities capabilities = remoteWebDriverConfig.getDesiredCapabilities();
                URL remoteWebDriverUrl = remoteWebDriverConfig.getRemoteWebDriverUrl();

                logger.info("DesiredCapabilities: {}", capabilities);
                logger.info("RemoteWebDriverUrl: {}", remoteWebDriverUrl);

                driver = new RemoteWebDriver(remoteWebDriverUrl,capabilities);
            }
            else if(webDriverType.toUpperCase().contentEquals("GRID")) {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setPlatform(Platform.ANY);
                caps.setBrowserName("firefox");
                try {
                    driver = new RemoteWebDriver(new URL(LOCAL_GRID_URL),caps);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            instance.driverCollection.set(driver);
        }
        return instance;
    }

    public WebDriver getDriver() {
        logger.info("Getting the driver instance");
        return driverCollection.get();
    }

    public void quite() {
        logger.info("Quiting the driver and closes the browser");
        try {
            driverCollection.get().close();
            driverCollection.get().quit();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            driverCollection.remove();
        }

    }

}
