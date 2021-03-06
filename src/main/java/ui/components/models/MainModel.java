package ui.components.models;

import static config.ApplicationProperties.ApplicationProperty.WAIT_TIMEOUT;
import static config.ApplicationProperties.ApplicationProperty.WAIT_TIMEOUT_LNG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static support.web.WebElementHelper.*;

import static config.ApplicationProperties.*;
import config.webdriver.DriverBase;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DataProvider;
import utils.Utils;

import java.time.LocalDate;
import java.util.List;
import static ui.components.locators.Locators.MainPage.*;

public class MainModel {

    private static final Logger logger = LoggerFactory.getLogger(MainModel.class);
    protected String languagePrefix = "";

    public MainModel(String languagePrefix) {
        this.languagePrefix = languagePrefix.toUpperCase();
    }

    public MainModel closeNewVersionPromo(){
        executeJS("document.cookie='promo=true;; expires=Mon, 27 Aug 2019 18:34:53 GMTpath=/'");
        actionClick(BTN_CLOSE_NEW_VERSION_PROMO.get());
        waitForInvisibilityOfElement(BTN_CLOSE_NEW_VERSION_PROMO.get(), getInteger(WAIT_TIMEOUT_LNG));
        return this;
    }

    @Step
    public MainModel changeLanguage() {
        executeJS("document.cookie='promo=true;; expires=Mon, 27 Aug 2019 18:34:53 GMTpath=/'");
        click(BTN_CLOSE_NEW_VERSION_PROMO.get());
        waitForInvisibilityOfElement(BTN_CLOSE_NEW_VERSION_PROMO.get());
        if (!this.languagePrefix.equals(getText(BTN_CURRENT_LANGUAGES.get()))){
            jsClick(BTN_LANGUAGE.get(this.languagePrefix));
        }
        return this;
    }

    @Step
    public LoginModel navigateToLoginModel(){
        click(LNK_LOGIN.get());
        return new LoginModel(languagePrefix);
    }

    @Step
    public TicketsModel selectMovieWithSessionDateInFuture(int moreThatXDaysInFuture){
        LocalDate dateInFuture=LocalDate.now().plusDays(moreThatXDaysInFuture);
        List<WebElement> sessionDates= DriverBase.getDriver().findElements(LNK_SESSION_DATE.get());
        for(WebElement current:sessionDates){
            LocalDate currentDate=Utils.stringToDate(current.getAttribute("id").replace("date_", ""), "yyyy-MM-dd");
            if (currentDate.isAfter(dateInFuture)){
                scrollToElement(current);
                jsClick(current.findElement(By.xpath("./div/a")));
                break;
            }
        }
        return new TicketsModel(languagePrefix);
    }

    @Step
    public TicketsModel selectRandomMovie(){
        click(LNL_SESSION_LINK.get());
        return new TicketsModel(languagePrefix);
    }

    @Step
    public MainModel doLogout(DataProvider data){
        click(BTN_LOGOUT.get());
        //assertTrue(isElementDisplayed(LBL_LOGIN_INFO.get(data.getData(languagePrefix, "logout.info"))));
        waitForInvisibilityOfElement(BTN_LOGOUT.get(), getInteger(WAIT_TIMEOUT));
        return this;
    }

    @Step
    public MainModel verifyUserIsLoggedIn(DataProvider data){
        assertTrue(isElementDisplayed(BTN_LOGOUT.get()), "Logout button is not displayed");
        assertEquals(data.getData("fullName"), getText(LNK_LOGIN.get()));
        return this;
    }

    @Step
    public MainModel verifyUserIsNotLoggedIn(){
        assertFalse(isElementDisplayed(BTN_LOGOUT.get()), "Logout button is display");
        return this;
    }
}
