package at.ac.ase.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Represents an area or part of
 * an homepage in browser
 * -> PageObjectPattern
 */
public abstract class PageObject {

    private WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public <T extends PageObject> T initPage(Class<T> pageObject) {
        return PageFactory.initElements(driver, pageObject);
    }

    public void waitUntil(ExpectedCondition<?> condition) {
        WebDriverWait wait = new WebDriverWait(driver,5);
        wait.until(condition);
    }
}
