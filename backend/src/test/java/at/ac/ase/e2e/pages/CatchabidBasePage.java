package at.ac.ase.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents Base Catchabid Page
 * with Navbar and Footer
 */
public class CatchabidBasePage extends PageObject {

    @FindBy(id = "navbar-main")
    private WebElement navbar;

    @FindBy(xpath = "//footer")
    private WebElement footerCopyrightLink;

    public CatchabidBasePage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getDriver().getTitle();
    }

    public WebElement getNavbar() {
        return navbar;
    }

    public WebElement getFooterCopyrightLink() {
        return footerCopyrightLink;
    }

    public AuctionsListArea getAuctionsListArea() {
        return initPage(AuctionsListArea.class);
    }

    public AuctionFiltersArea getAuctionsFilterArea() {
        return initPage(AuctionFiltersArea.class);
    }

    public LoginOverlay clickLoginButton() {
        //TODO actually click button
        return initPage(LoginOverlay.class);
    }
}
