package com.paxovision.selenium.driver;

import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
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
    private static final String USERNAME = "bahjatkhan1";
    private static final String AUTOMATE_KEY = "TbVWMFj8YNcEMSkvwoqt";
    private static final String CLOUD_GRID_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);


    private WebDriverFactory(){
    }

    public static WebDriverFactory getInstance(){
        String browser = System.getenv("browser");
        return getInstance(browser);
    }

    public static WebDriverFactory getInstance(String browser){
        WebDriverFactoryConfig config = WebDriverFactoryConfig.getInstance();

        if(instance == null){
            instance = new WebDriverFactory();
        }

        if (browser == null) {
            browser = "CHROME";
            logger.info("Browser is missing. Using default browser {}", browser);
        }

        if(instance.driverCollection.get() == null) {

            logger.info("Creating browser instance {} ", browser);

            WebDriver driver = null;
            if(browser.toUpperCase().contentEquals("CHROME")) {
                System.setProperty(WebDriverFactoryConfig.ChromeConfig.DRIVER_KEY,
                                config.getChromeConfig().getChromeDriverExecutable());
                ChromeOptions options = config.getChromeConfig().getOptions();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);

                options.merge(capabilities);
                driver = new ChromeDriver(options);

                //ChromeOptions options = new ChromeOptions();
                //options.addArguments("--ignore-certificate-errors");

                //ChromeDriverManager.chromedriver().setup();
                //driver = new ChromeDriver(options);
            }
            else if(browser.toUpperCase().contentEquals("CHROME-HEADLESS")) {

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless")
                        .addArguments("--disable-gpu")
                        .addArguments("--window-size=1920,1080")
                        .addArguments("--ignore-certificate-errors");

                //options.setBinary("/Path/to/specific/version/of/Google Chrome");
                ChromeDriverManager.chromedriver().setup();
                driver = new ChromeDriver(options);
            }
            else if(browser.toUpperCase().contentEquals("CHROME-GRID")) {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setPlatform(Platform.ANY);
                caps.setBrowserName("chrome");
                try {
                    driver = new RemoteWebDriver(new URL(LOCAL_GRID_URL),caps);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else if(browser.toUpperCase().contentEquals("CHROME-CLOUD")){
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("browser", "Chrome");
                caps.setCapability("browser_version", "77.0");
                caps.setCapability("os", "Windows");
                caps.setCapability("os_version", "10");
                caps.setCapability("resolution", "1920x1080");
                caps.setCapability("name", "Cloud-Browser");
                try {
                    driver = new RemoteWebDriver(new URL(CLOUD_GRID_URL),caps);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else if(browser.toUpperCase().contentEquals("FIREFOX")) {
                FirefoxDriverManager.firefoxdriver().arch64().setup();
                FirefoxOptions options = new FirefoxOptions();
                options.setCapability("marionette", true);

                driver = new FirefoxDriver(options);
            }
            else if(browser.toUpperCase().contentEquals("FIREFOX-HEADLESS")) {

            }
            else if(browser.toUpperCase().contentEquals("FIREFOX-GRID")) {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setPlatform(Platform.ANY);
                caps.setBrowserName("firefox");
                try {
                    driver = new RemoteWebDriver(new URL(LOCAL_GRID_URL),caps);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //FirefoxOptions options = new FirefoxOptions();
                //options.setCapability("marionette", true);
                //driver = new FirefoxDriver(options);
            }
            else if(browser.toUpperCase().contentEquals("FIREFOX-CLOUD")){
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("browser", "Firefox");
                caps.setCapability("browser_version", "70.0");
                caps.setCapability("os", "Windows");
                caps.setCapability("os_version", "10");
                caps.setCapability("resolution", "1920x1080");
                caps.setCapability("name", "Cloud-Browser");
                try {
                    driver = new RemoteWebDriver(new URL(CLOUD_GRID_URL),caps);
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

    public void removeDriver() {
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
