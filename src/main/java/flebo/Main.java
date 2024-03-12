package flebo;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Specify the path of the GeckoDriver executable
        //System.setProperty(“webdriver.gecko.driver”, “/path/to/geckodriver”);

        // Create a new instance of the Firefox driver
        FirefoxProfile profile = new FirefoxProfile();
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        //options.addArguments("-headless");
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

            WebElement authors = driver.findElement(By.xpath("//div[@id='main-wrapper']/div/h3[2]"));
            System.out.println("[" + authors.getText() + "]");

            // https://flibusta.is/b/558115/fb2
            // https://flibusta.is/booksearch - вот нормальная стартовая страница

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}


