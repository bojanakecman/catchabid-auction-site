package at.ac.ase.e2e;

import at.ac.ase.basetest.BaseE2E;
import at.ac.ase.e2e.pages.AuctionsListArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class F10_DisplayRecentAuctionsE2E extends BaseE2E {

    @AfterEach
    void cleanup() {
        cleanDatabase();
    }

    @Test
    void testDisplayedRecentAuctionsList() {

        insertTestData("F10_tc1.sql");

        navigateToCatchabidPage();

        AuctionsListArea auctionsListArea = getCatchabidPage().getAuctionsListArea();
        List<WebElement> recentAuctionsList = auctionsListArea.getRecentAuctions();

        assertNotNull(recentAuctionsList);
        assertEquals(3L, recentAuctionsList.size());

        WebElement auctionCard1 = recentAuctionsList.get(0);
        WebElement auctionCard2 = recentAuctionsList.get(1);
        WebElement auctionCard3 = recentAuctionsList.get(2);

        assertEquals("Created by Testname Testname", auctionsListArea.getCreatorText(auctionCard1));
        assertEquals("Item1", auctionsListArea.getAuctionNameText(auctionCard1));
        assertEquals("3€", auctionsListArea.getPriceText(auctionCard1));
        assertThat(auctionsListArea.getCountdownTest(auctionCard1), containsString("3h"));

        assertEquals("Created by Max", auctionsListArea.getCreatorText(auctionCard2));
        assertEquals("Item2", auctionsListArea.getAuctionNameText(auctionCard2));
        assertEquals("6€", auctionsListArea.getPriceText(auctionCard2));
        assertThat(auctionsListArea.getCountdownTest(auctionCard2), containsString("5h"));

        assertEquals("Created by Max", auctionsListArea.getCreatorText(auctionCard3));
        assertEquals("Item3", auctionsListArea.getAuctionNameText(auctionCard3));
        assertEquals("99€", auctionsListArea.getPriceText(auctionCard3));
        assertThat(auctionsListArea.getCountdownTest(auctionCard3), containsString("7h"));

    }

    @Test
    public void testLoadMoreRecentAuctions() {

        insertTestData("F10_tc2.sql");

        navigateToCatchabidPage();

        AuctionsListArea auctionsListArea = getCatchabidPage().getAuctionsListArea();

        assertEquals(10L, auctionsListArea.getRecentAuctions().size());

        auctionsListArea.clickLoadMoreRecentAuctions();
        auctionsListArea.waitUntilRecentAuctionsHasMoreThan(10);

        assertEquals(14L, auctionsListArea.getRecentAuctions().size());
    }
}
