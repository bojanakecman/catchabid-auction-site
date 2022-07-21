package at.ac.ase.e2e.pages;

import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

public class LoginOverlay extends PageObject {

    public LoginOverlay(WebDriver driver) {
        super(driver);
    }

    public void insertUsername(String user) {
        throw new NotImplementedException();
    }

    public void insertPassword(String user) {
        throw new NotImplementedException();
    }

    public void clickLoginButton() {
        throw new NotImplementedException();
    }

    public CatchabidBasePage clickOutsideOverlayToCloseOverlay() {
        return initPage(CatchabidBasePage.class);
    }
}
