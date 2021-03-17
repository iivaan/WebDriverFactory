package com.paxovision.selenium.driver;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebDriverManagerTest2 {
    private static ChromeDriverService service;
    //private static WebDriverFactory driverFactory = WebDriverFactory.getInstance("CHROME");
    //private static WebDriverFactory driverFactory = WebDriverFactory.getInstance("CHROME-HEADLESS");
    //private static WebDriverFactory driverFactory = WebDriverFactory.getInstance("FIREFOX");
    protected WebDriver driver;

    @BeforeAll
    public void beforeAll() throws IOException {
        /*service  = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(System.getProperty("user.dir") + "/src/test/resources/drivers/mac/chrome/chromedriver"))
                .usingAnyFreePort()
                .build();
        service.start();*/
    }

    @BeforeEach
    public void setUp(){
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--ignore-certificate-errors");
//        driver = new RemoteWebDriver(service.getUrl(), options);

        String geckoDriver = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/firefox/geckodriver";
        System.setProperty("webdriver.gecko.driver",geckoDriver);
        FirefoxProfile profile = new FirefoxProfile();
        //profile.setPreference("browser.shell.checkDefaultBrowser", true);
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setAcceptUntrustedCertificates(true);

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        //firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        //firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.setProfile(profile);

        driver = new FirefoxDriver(firefoxOptions);

        //driver = new FirefoxDriver();
        driver.navigate().to("http://heatclinic.shiftedtech.com/");
    }

    @Test
    public void test1(){
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.name("j_username")).sendKeys("shiftqa01@gmail.com");
        driver.findElement(By.name("j_password")).sendKeys("shiftedtech");
        driver.findElement(By.cssSelector(".login_button")).click();

        String accountName = driver.findElement(By.cssSelector(".my-account")).getText();
        assertThat(accountName).isEqualToIgnoringCase("Shift");
    }
    @Test
    public void test2(){
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.name("j_username")).sendKeys("shiftqa01@gmail.com");
        driver.findElement(By.name("j_password")).sendKeys("shiftedtech");
        driver.findElement(By.cssSelector(".login_button")).click();

        String accountName = driver.findElement(By.cssSelector(".my-account")).getText();
        assertThat(accountName).isEqualToIgnoringCase("Shift");
    }

    @Test
    public void test3(){
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.name("j_username")).sendKeys("shiftqa01@gmail.com");
        driver.findElement(By.name("j_password")).sendKeys("shiftedtech");
        driver.findElement(By.cssSelector(".login_button")).click();

        String accountName = driver.findElement(By.cssSelector(".my-account")).getText();
        assertThat(accountName).isEqualToIgnoringCase("Shift");
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
        driver = null;
    }

    @AfterAll
    public void afterAll(){
        //service.stop();
    }

}
