package com.paxovision.selenium.driver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class BrowserStackWebDriverManagerTest {
    static{
        System.setProperty("WEB_DRIVER_TYPE","REMOTE-BROWSERSTACK");
        System.setProperty("CAPABILITY","mac.chrome");
    }
    private  WebDriverFactory driverFactory = WebDriverFactory.getInstance();
    protected WebDriver driver;

    @BeforeEach
    public void setUp(){
        driver = driverFactory.getDriver();
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
        driver.findElement(By.name("j_username")).sendKeys("shiftqa02@gmail.com");
        driver.findElement(By.name("j_password")).sendKeys("shiftedtech");
        driver.findElement(By.cssSelector(".login_button")).click();

        String accountName = driver.findElement(By.cssSelector(".my-account")).getText();
        assertThat(accountName).isEqualToIgnoringCase("ShiftVision");
    }

    @AfterEach
    public void tearDown(){
        WebDriverFactory.getInstance().quite();
        driver = null;
    }

}
