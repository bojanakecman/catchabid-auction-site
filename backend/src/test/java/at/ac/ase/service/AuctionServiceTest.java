package at.ac.ase.service;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.dto.AuctionQueryDTO;
import at.ac.ase.entities.*;
import at.ac.ase.repository.auction.AuctionRepository;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.util.exceptions.AuctionCancellationException;
import at.ac.ase.util.exceptions.AuthorizationException;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class AuctionServiceTest extends BaseIntegrationTest {

    @Autowired
    IAuctionService auctionService;

    @Autowired
    IAuctionHouseService auctionHouseService;

    @Autowired
    AuctionRepository auctionRepository;

    @After
    public void cleanup() {
        cleanDatabase();
    }

    @Test
    public void testServiceNotNull() {
        assertNotNull(auctionService);
    }

    @Test
    @Transactional
    public void testGetRecentAuctions() {
        List<AuctionPostSendDTO> auctions;
        insertTestData("multiple-auctions.sql");

        auctions = auctionService.searchAuctions(getRecentQuery(0, 5));

        assertThat(auctions.size(), is(5));
        assertThat(auctions.get(0).getId(), is(1L));
        assertThat(auctions.get(1).getId(), is(2L));
        assertThat(auctions.get(2).getId(), is(3L));
        assertThat(auctions.get(3).getId(), is(4L));
        assertThat(auctions.get(4).getId(), is(5L));

        auctions = auctionService.searchAuctions(getRecentQuery(1, 5));

        assertThat(auctions.size(), is(5));
        assertThat(auctions.get(0).getId(), is(6L));
        assertThat(auctions.get(1).getId(), is(7L));
        assertThat(auctions.get(2).getId(), is(8L));
        assertThat(auctions.get(3).getId(), is(9L));
        assertThat(auctions.get(4).getId(), is(10L));

        auctions = auctionService.searchAuctions(getRecentQuery(2, 5));

        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(11L));
    }

    @Test
    @Transactional
    public void testGetRecentAuctionsWithInvalidParam() {
        insertTestData("multiple-auctions.sql");

        List<AuctionPostSendDTO> auctions = auctionService.searchAuctions(getRecentQuery(0, 0));
        assertThat(auctions.size(), is(11));
        assertThat(auctions.get(10).getId(), is(1L));
        assertThat(auctions.get(0).getId(), is(11L));

        auctions = auctionService.searchAuctions(getRecentQuery(null, null));
        assertThat(auctions.size(), is(11));
        assertThat(auctions.get(0).getId(), is(11L));
        assertThat(auctions.get(10).getId(), is(1L));

        auctions = auctionService.searchAuctions(getRecentQuery(-0, 10));
        assertThat(auctions.size(), is(10));
        assertThat(auctions.get(0).getId(), is(11L));
        assertThat(auctions.get(9).getId(), is(2L));
    }

    @Test
    public void testCreateAuction() {
        insertTestData("auctions.sql");

        AuctionPost auctionPost = new AuctionPost();

        AuctionHouse auctionHouse = auctionHouseService.getAuctionHouseByEmail("test@test.com");

        List<AuctionPost> auctionPosts = auctionService.getAllAuctions();

        assertNotNull(auctionHouse);
        assertThat(auctionPosts.size(), is(1));


        auctionPost.setName("TestAuction");
        auctionPost.setDescription("TestDesc");
        auctionPost.setMinPrice(10.0);
        auctionPost.setStartTime(LocalDateTime.now());
        auctionPost.setEndTime(LocalDateTime.now().plusHours(2));
        auctionPost.setCreator(auctionHouse);
        auctionPost.setCategory(Category.CARS);
        auctionPost.setStatus(Status.ACTIVE);

        auctionService.saveAuction(auctionPost);

        auctionPosts = auctionService.getAllAuctions();
        assertThat(auctionPosts.size(), is(2));
    }

    @Test
    @Transactional
    public void testRecentAuctionsForUser() {
        insertTestData("auctions-filter-preferences.sql");
        AuctionQueryDTO auctions = getRecentQuery(0, 5);
        auctions.setUserEmail("testRegular@test.com");
        auctions.setUseUserPreferences(true);
        List<AuctionPostSendDTO> posts = auctionService.searchAuctions(auctions);
        assertEquals(1, posts.size());
        assertEquals("Desktop PC - Intel i7, AMD RX 580 8GB", posts.get(0).getDescription());
        auctions.setUseUserPreferences(false);
        posts = auctionService.searchAuctions(auctions);
        assertEquals(2, posts.size());
        assertEquals("Bob Marley Tickets", posts.get(0).getDescription());
        assertEquals("Ticket to Paradise CD", posts.get(1).getDescription());

    }

    @Test
    @Transactional
    public void testSubscribeToAuction() {
        insertTestData("multiple-auctions.sql");

        List<AuctionPost> auctionPosts = auctionService.getAllAuctions();
        assertThat(auctionPosts.size(), is(11));

        AuctionPost auctionPost = createAuction("test@test.com");
        auctionService.saveAuction(auctionPost);

        auctionPosts = auctionService.getAllAuctions();
        assertThat(auctionPosts.size(), is(12));

        AuctionPost storedAuctionPost = auctionPosts.get(auctionPosts.size() - 1);
        assertThat(storedAuctionPost.getSubscriptions().size(), is(0));

        User user = new RegularUser();
        user.setId(2L);
        user.setActive(true);
        user.setEmail("test@test.com");

        auctionService.subscribeToAuction(auctionPost, user);

        storedAuctionPost = auctionService.getAllAuctions().get(auctionPosts.size() - 1);

        assertThat(storedAuctionPost.getSubscriptions().size(), is(1));

        Iterator iterator = storedAuctionPost.getSubscriptions().iterator();
        RegularUser subscribedUser = (RegularUser) iterator.next();
        assertThat(subscribedUser.getId(), is(2L));
        assertTrue(subscribedUser.getActive());
        assertThat(subscribedUser.getEmail(), is("test@test.com"));
    }

    @Test
    @Transactional
    public void testUnsubscribeFromAuction() {
        insertTestData("multiple-auctions.sql");

        List<AuctionPost> auctionPosts = auctionService.getAllAuctions();
        assertThat(auctionPosts.size(), is(11));

        AuctionPost auctionPost = createAuction("test@test.com");
        auctionService.saveAuction(auctionPost);

        auctionPosts = auctionService.getAllAuctions();
        assertThat(auctionPosts.size(), is(12));

        AuctionPost storedAuctionPost = auctionPosts.get(auctionPosts.size() - 1);
        assertThat(storedAuctionPost.getSubscriptions().size(), is(0));

        User user = new RegularUser();
        user.setId(2L);
        user.setActive(true);
        user.setEmail("test@test.com");

        auctionService.subscribeToAuction(auctionPost, user);

        storedAuctionPost = auctionService.getAllAuctions().get(auctionPosts.size() - 1);

        assertThat(storedAuctionPost.getSubscriptions().size(), is(1));

        auctionService.unsubscribeFromAuction(auctionPost, user);

        storedAuctionPost = auctionService.getAllAuctions().get(auctionPosts.size() - 1);

        assertThat(storedAuctionPost.getSubscriptions().size(), is(0));
    }

    @Test(expected = AuthorizationException.class)
    public void testCancelAuctionAuthorizationError() {
        insertTestData("initial-testdata.sql");
        User user = new AuctionHouse();
        user.setId(99L);
        auctionService.cancelAuction(user, 100017L);
    }

    @Test(expected = AuctionCancellationException.class)
    public void testCancelAuctionCancellationError() {
        insertTestData("initial-testdata.sql");
        User user = new AuctionHouse();
        user.setId(2L);
        auctionService.cancelAuction(user, 100016L);
    }

    @Test
    public void testCancelAuctionSuccess() {
        insertTestData("initial-testdata.sql");
        User user = new AuctionHouse();
        user.setId(3L);

        AuctionPostSendDTO cancelledAuctionDto = auctionService.cancelAuction(user, 100017L);
        AuctionPost cancelledAuctionDb = auctionRepository.findById(100017L).orElse(null);

        assertNotNull(cancelledAuctionDto);
        assertNotNull(cancelledAuctionDb);
        assertEquals("CANCELLED", cancelledAuctionDto.getStatus());
        assertEquals(Status.CANCELLED, cancelledAuctionDb.getStatus());
    }

    private AuctionPost createAuction(String email) {
        AuctionPost auctionPost = new AuctionPost();
        auctionPost.setEndTime(LocalDateTime.now());
        auctionPost.setStartTime(LocalDateTime.now().minusMinutes(55));
        auctionPost.setName("TestAuction");
        auctionPost.setDescription("TestDesc");
        auctionPost.setMinPrice(10.0);
        auctionPost.setCreator(auctionHouseService.getAuctionHouseByEmail(email));
        auctionPost.setCategory(Category.CARS);
        auctionPost.setStatus(Status.ACTIVE);
        auctionPost.setImage(getImageBytes());

        Address address = new Address();
        address.setStreet("Resselgasse");
        address.setHouseNr(1);
        address.setCity("Vienna");
        address.setCountry("Austria");
        auctionPost.setAddress(address);

        return auctionPost;
    }

    public byte[] getImageBytes() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("catchabid-logo.png")) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[10];
    }

    private AuctionQueryDTO getRecentQuery(Integer pageNr, Integer auctionsPerPage) {
        AuctionQueryDTO queryDTO = new AuctionQueryDTO();
        queryDTO.setAuctionsStartUntil(LocalDateTime.now());
        queryDTO.setAuctionsEndFrom(LocalDateTime.now());
        queryDTO.setPageNumber(pageNr);
        queryDTO.setPageSize(auctionsPerPage);
        return queryDTO;
    }

}

