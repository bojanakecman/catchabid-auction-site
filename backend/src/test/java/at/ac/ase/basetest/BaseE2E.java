package at.ac.ase.basetest;


import at.ac.ase.e2e.pages.CatchabidBasePage;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

/**
 * Base Spring Boot E2E Test to be used
 * for E2E testing with JUnit5 and SeleniumJupiter
 */

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
public abstract class BaseE2E extends BaseSpringBootTest {

    /**
     * Frontend home URL on local machine
     */
    protected final String FRONTEND_URL_LOCAL = "http://localhost:4200";

    /**
     * generic webdriver used by tests
     * can be configured in initDriver()
     */
    private WebDriver driver;

    /**
     * tell seleniumJupiter to prepare ChromeDriver
     */
    public BaseE2E() {
        WebDriverManager.chromedriver().setup();
    }

    /**
     * reset driver for next test-method
     */
    @BeforeEach
    public void beforeEach() {
        initDriver();
        driver.manage().timeouts().implicitlyWait(getDefaultImplicitWait(), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(getDefaultTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(getDefaultTimeout(), TimeUnit.SECONDS);
    }

    /**
     * close driver after each test-method
     */
    @AfterEach
    public void afterEach() {
        closeDriver();
    }

    /**
     * helper function to navigate to
     * catchabid home page
     */
    protected void navigateToCatchabidPage() {
        getDriver().get(FRONTEND_URL_LOCAL);
    }

    protected void initDriver() {
        driver = new ChromeDriver();
    }

    protected void closeDriver() {
        if(driver != null) {
            driver.manage().deleteAllCookies();
            driver.quit();
            driver = null;
        }
    }

    protected CatchabidBasePage getCatchabidPage() {
        return PageFactory.initElements(driver, CatchabidBasePage.class);
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected Integer getDefaultTimeout() {
        return 5;
    }

    protected Integer getDefaultImplicitWait() {
        return 1;
    }
}
