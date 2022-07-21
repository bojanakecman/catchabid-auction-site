package at.ac.ase.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AuctionsListArea extends PageObject{

    @FindBy(xpath = "(//*[contains(text(), 'Load More')])[1]")
    private WebElement loadMoreRecentAuctionsButton;

    @FindBy(xpath = "(//*[contains(text(), 'Load More')])[2]")
    private WebElement loadMoreUpcomingAuctionsButton;

    public AuctionsListArea(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> getRecentAuctions() {
        return getDriver().findElements(By.className("recent-auction"));
    }

    public List<WebElement> getUpcomingAuctions() {
        return getDriver().findElements(By.className("upcoming-auction"));
    }

    public String getCreatorText(WebElement auction) {
        return auction.findElement(By.xpath("(article/div[@class='info']/p)[1]")).getText();
    }

    public String getAuctionNameText(WebElement auction) {
        return auction.findElement(By.xpath("(article/div[@class='info']/p)[2]")).getText();
    }

    public String getPriceText(WebElement auction) {
        return auction.findElement(By.xpath("(article/div[@class='info']/p)[4]")).getText();
    }

    public String getCountdownTest(WebElement auction) {
        return auction.findElement(By.xpath("(article/div[@class='info']/div/div/app-auction-countdown)")).getText();
    }

    public void clickLoadMoreRecentAuctions() {
        loadMoreRecentAuctionsButton.click();
    }

    public void waitUntilRecentAuctionsHasMoreThan(int n) {
        waitUntil(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("recent-auction"), n));
    }

    public void waitUntilRecentAuctionsSizeIs(int n) {
        waitUntil(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("recent-auction"), n-1));
        waitUntil(ExpectedConditions.numberOfElementsToBeLessThan(By.className("recent-auction"), n+1));
    }

    public void clickLoadMoreUpcomingAuctions() {
        loadMoreUpcomingAuctionsButton.click();
    }
}
