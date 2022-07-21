package at.ac.ase.e2e;

import at.ac.ase.basetest.BaseE2E;
import at.ac.ase.e2e.pages.AuctionFiltersArea;
import at.ac.ase.e2e.pages.AuctionsListArea;
import at.ac.ase.e2e.pages.CatchabidBasePage;
import at.ac.ase.repository.auction.AuctionRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class F15_AuctionFiltering extends BaseE2E {

    @Autowired
    AuctionRepository auctionRepository;

    @AfterEach
    void testApplicationContext() {
        cleanDatabase();
    }

    @Test
    void testDbAccess() {
        insertTestData("F12_tc1.sql");
        assertEquals(3L, auctionRepository.findAll().size());
        cleanDatabase();
    }

    @Test
    void testDisplayedCategories() {

        insertTestData("initial-testdata.sql");

        navigateToCatchabidPage();

        CatchabidBasePage catchabidBasePage = getCatchabidPage();
        AuctionFiltersArea auctionFiltersArea = catchabidBasePage.getAuctionsFilterArea();

        assertNotNull(auctionFiltersArea);

        List<String> categories = auctionFiltersArea.getDisplayedCategories();

        assertNotNull(categories);
        assertThat(categories.size(), is(10));
        assertTrue(categories.contains("Jewelry"));
        assertTrue(categories.contains("Electronics"));
        assertTrue(categories.contains("Art"));
        assertTrue(categories.contains("Cars"));
        assertTrue(categories.contains("Antiques"));
        assertTrue(categories.contains("Travel"));
        assertTrue(categories.contains("Furniture"));
        assertTrue(categories.contains("Music"));
        assertTrue(categories.contains("Sport"));
        assertTrue(categories.contains("Other"));
    }

    @Test
    void testFilterByCategory() {

        insertTestData("initial-testdata.sql");

        navigateToCatchabidPage();

        CatchabidBasePage catchabidBasePage = getCatchabidPage();
        AuctionsListArea auctionsListArea = catchabidBasePage.getAuctionsListArea();
        AuctionFiltersArea auctionFiltersArea = catchabidBasePage.getAuctionsFilterArea();


        auctionFiltersArea.clickCategory("Cars");
        auctionsListArea.waitUntilRecentAuctionsSizeIs(1);

        assertEqualAuctions(auctionsListArea.getRecentAuctions(), auctionsListArea,
                new AuctionRepr("Homer Simpson", "BMW M3", "20000€", "5h"));

        assertEqualAuctions(auctionsListArea.getUpcomingAuctions(), auctionsListArea,
                new AuctionRepr("Barney Stinson", "Mustang", "22000€", "1h"));


        auctionFiltersArea.clickCategory("Electronics");
        auctionsListArea.waitUntilRecentAuctionsSizeIs(4);

        assertEqualAuctions(auctionsListArea.getRecentAuctions(), auctionsListArea,
                new AuctionRepr("Homer Simpson", "BMW M3", "20000€", "5h"),
                new AuctionRepr("Homer Simpson", "Gamer PC", "230€", "5h"),
                new AuctionRepr("Auction Master", "Lumix Camera", "100€", "5h"),
                new AuctionRepr("Barney Stinson", "Xbox Series X", "700€", "5h"));

        assertEqualAuctions(auctionsListArea.getUpcomingAuctions(), auctionsListArea,
                new AuctionRepr("Barney Stinson", "Mustang", "22000€", "1h"),
                new AuctionRepr("Homer Simpson", "Samsung S20", "500€", "9h"));

        auctionFiltersArea.clickCategory("Electronics");
        auctionFiltersArea.clickCategory("Cars");

        auctionsListArea.waitUntilRecentAuctionsSizeIs(10);
    }

    @Test
    void testDisplayedCountries() {

        insertTestData("initial-testdata.sql");

        navigateToCatchabidPage();

        CatchabidBasePage catchabidBasePage = getCatchabidPage();
        AuctionFiltersArea auctionFiltersArea = catchabidBasePage.getAuctionsFilterArea();

        List<String> countries = auctionFiltersArea.getDisplayedCountries();

        assertNotNull(countries);
        assertThat(countries.size(), is(2));
        assertTrue(countries.contains("Austria"));
        assertTrue(countries.contains("Germany"));
    }

    @Test
    void testFilterByCountry() {

        insertTestData("initial-testdata.sql");

        navigateToCatchabidPage();

        CatchabidBasePage catchabidBasePage = getCatchabidPage();
        AuctionsListArea auctionsListArea = catchabidBasePage.getAuctionsListArea();
        AuctionFiltersArea auctionFiltersArea = catchabidBasePage.getAuctionsFilterArea();


        auctionFiltersArea.clickCountry("Germany");
        auctionsListArea.waitUntilRecentAuctionsSizeIs(3);

        assertEqualAuctions(auctionsListArea.getRecentAuctions(), auctionsListArea,
                new AuctionRepr("Pro-Shop", "Luxury Watch", "4000€", "3h"),
                new AuctionRepr("Homer Simpson", "BMW M3", "20000€", "5h"),
                new AuctionRepr("Pro-Shop", "Bungee Jumping", "10€", "7h"));

        assertEqualAuctions(auctionsListArea.getUpcomingAuctions(), auctionsListArea,
                new AuctionRepr("Barney Stinson", "Mustang", "22000€", "1h"));

        auctionFiltersArea.clickCountry("Germany");

        auctionsListArea.waitUntilRecentAuctionsSizeIs(10);

        assertEquals(10L, auctionsListArea.getRecentAuctions().size());
        assertEquals(3L, auctionsListArea.getUpcomingAuctions().size());
    }

    @Test
    void testFilterBySearchInput() {

        insertTestData("initial-testdata.sql");

        navigateToCatchabidPage();

        CatchabidBasePage catchabidBasePage = getCatchabidPage();
        AuctionsListArea auctionsListArea = catchabidBasePage.getAuctionsListArea();
        AuctionFiltersArea auctionFiltersArea = catchabidBasePage.getAuctionsFilterArea();

        auctionFiltersArea.insertIntoTextSearchInput("auction master");
        auctionFiltersArea.textSearchHitEnter();


        auctionsListArea.waitUntilRecentAuctionsSizeIs(3);

        assertEqualAuctions(auctionsListArea.getRecentAuctions(), auctionsListArea,
                new AuctionRepr("Auction Master", "Cosy Couch", "40€", "3h"),
                new AuctionRepr("Auction Master", "2 Day Trip to Venice", "45€", "4h"),
                new AuctionRepr("Auction Master", "Lumix Camera", "100€", "5h"));

        assertEquals(0L, auctionsListArea.getUpcomingAuctions().size());

        auctionFiltersArea.clearTextSearchInput();
        auctionFiltersArea.insertIntoTextSearchInput("xxx");
        auctionFiltersArea.textSearchHitEnter();
        auctionsListArea.waitUntilRecentAuctionsSizeIs(0);

        assertEquals(0L, auctionsListArea.getRecentAuctions().size());
        assertEquals(0L, auctionsListArea.getUpcomingAuctions().size());

        auctionFiltersArea.textSearchClickX();

        auctionsListArea.waitUntilRecentAuctionsSizeIs(10);
        assertEquals(10L, auctionsListArea.getRecentAuctions().size());

        auctionFiltersArea.insertIntoTextSearchInput("mustang");
        auctionFiltersArea.textSearchHitEnter();
        auctionsListArea.waitUntilRecentAuctionsSizeIs(0);

        assertEquals(0L, auctionsListArea.getRecentAuctions().size());
        assertEqualAuctions(auctionsListArea.getUpcomingAuctions(), auctionsListArea,
                new AuctionRepr("Barney Stinson", "Mustang", "22000€", "1h"));
    }

    private void assertEqualAuctions(List<WebElement> auctionElements, AuctionsListArea auctionsListArea, AuctionRepr... auctionsToAssert) {

        Assertions.assertEquals(auctionElements.size(), auctionsToAssert.length);

        for(AuctionRepr auctionToAssert : auctionsToAssert) {
            WebElement auctionElement = auctionElements.remove(0);

            Assertions.assertEquals("Created by " + auctionToAssert.creator, auctionsListArea.getCreatorText(auctionElement));
            Assertions.assertEquals(auctionToAssert.auctionName, auctionsListArea.getAuctionNameText(auctionElement));
            Assertions.assertEquals(auctionToAssert.price, auctionsListArea.getPriceText(auctionElement));
            MatcherAssert.assertThat(auctionsListArea.getCountdownTest(auctionElement), StringContains.containsString(auctionToAssert.coutnerstr));
        }

    }

    private static class AuctionRepr {
        public String creator, auctionName, price, coutnerstr;
        public AuctionRepr(String creator, String auctionName, String price, String includedCounterSubstr) {
            this.creator = creator;
            this.auctionName = auctionName;
            this.price = price;
            this.coutnerstr = coutnerstr;
        }
    }
}
