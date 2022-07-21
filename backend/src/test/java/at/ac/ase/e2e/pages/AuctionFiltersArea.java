package at.ac.ase.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class AuctionFiltersArea extends PageObject {

    public AuctionFiltersArea(WebDriver driver) {
        super(driver);
    }

    public void insertIntoTextSearchInput(String input) {
        WebElement inputField = getDriver().findElement(By.xpath("//nav//input"));
        inputField.sendKeys(input);
    }

    public void clearTextSearchInput() {
        getDriver().findElement(By.xpath("//nav//input")).clear();
    }

    public void textSearchHitEnter() {
        getDriver().findElement(By.xpath("//nav//input")).sendKeys(Keys.ENTER);
    }

    public void textSearchClickX() {
        WebElement inputField = getDriver().findElement(By.xpath("//i[@class='fa fa-times']"));
        inputField.click();
    }

    public void clickCategory(String category) {
        getDriver().findElements(By.className("auction-category-filter-item"))
                .stream()
                .filter(e -> e.getText().equals(category))
                .map(e -> e.findElement(By.xpath(".//label")))
                .findFirst()
                .ifPresent(WebElement::click);
    }

    public void clickCountry(String country) {
        getDriver().findElements(By.className("auction-country-filter-item"))
                .stream()
                .filter(e -> e.getText().equals(country))
                .map(e -> e.findElement(By.xpath(".//label")))
                .findFirst()
                .ifPresent(WebElement::click);
    }

    public List<String> getDisplayedCategories () {
        return getDriver().findElements(By.className("auction-category-filter-item"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<String> getDisplayedCountries () {
        return getDriver().findElements(By.className("auction-country-filter-item"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
