package flebo;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Specify the path of the GeckoDriver executable
        //System.setProperty(“webdriver.gecko.driver”, “/path/to/geckodriver”);

        // Create a new instance of the Firefox driver
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        //Set implicit wait:
        //wait for WebElement
        driver.manage().timeouts().implicitlyWait(Duration.of(5000, ChronoUnit.MILLIS));

        //wait for loading page
        driver.manage().timeouts().pageLoadTimeout(Duration.of(10000, ChronoUnit.MILLIS));

         //wait for an asynchronous script to finish execution
        driver.manage().timeouts().scriptTimeout(Duration.of(5000, ChronoUnit.MILLIS));


        try {
            // Navigate to a web page
            driver.get("https://flibusta.is/");
            final WebElement input = driver.findElement(By.name("ask"));
            input.click();
            input.sendKeys("Злобин");
            input.submit();

            Thread.sleep(5000);

            final WebElement main = driver.findElement(By.id("main"));
            System.out.println("main: " + main);
            System.out.println("main: " + main.getText());
            System.out.println("Ok");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}


