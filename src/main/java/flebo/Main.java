package flebo;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Specify the path of the GeckoDriver executable
        //System.setProperty(“webdriver.gecko.driver”, “/path/to/geckodriver”);

        // Create a new instance of the Firefox driver
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        try {
            // Navigate to a web page
            driver.get("http://samlib.ru/cgi-bin/seek");
            final WebElement input = driver.findElement(By.name("FIND"));
            input.click();
            input.sendKeys("Злобин");
            input.submit();
            System.out.println(input.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Close the browser
            driver.quit();
        }
    }
}


