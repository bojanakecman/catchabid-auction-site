package at.ac.ase.repository;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Category;
import at.ac.ase.repository.auction.AuctionPostQuery;
import at.ac.ase.repository.auction.AuctionRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AuctionRepositoryTest extends BaseIntegrationTest {

    @Autowired
    AuctionRepository auctionRepository;

    @After
    public void cleanup() {
        cleanDatabase();
    }

    @Test
    public void testFindAuctionsBySearchStrings () {

        insertTestData("multiple-auctions.sql");

        assertNotNull(auctionRepository);

        AuctionPostQuery query = new AuctionPostQuery();
        query.setPageNumber(0);
        query.setPageSize(10);
        query.setSortBy("id");
        query.setSortOrder("DESC");

        List<AuctionPost> foundAuctions = auctionRepository.query(query);

        assertNotNull(foundAuctions);
    }

    @Test
    public void testEmptyQuery() {
        insertTestData("auctions-search.sql");

        AuctionPostQuery query = new AuctionPostQuery();
        List<AuctionPost> auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(3));
    }

    @Test
    public void testOrdering() {
        insertTestData("auctions-search.sql");

        AuctionPostQuery query = new AuctionPostQuery();
        query.setSortOrder("ASC");
        query.setSortBy("id");

        List<AuctionPost> auctions = auctionRepository.query(query);

        assertThat(auctions.size(), is(3));
        assertThat(auctions.get(0).getId(), is(10000L));
        assertThat(auctions.get(1).getId(), is(10001L));
        assertThat(auctions.get(2).getId(), is(10002L));


        query = new AuctionPostQuery();
        query.setSortOrder("DESC");
        query.setSortBy("id");

        auctions = auctionRepository.query(query);

        assertThat(auctions.size(), is(3));
        assertThat(auctions.get(0).getId(), is(10002L));
        assertThat(auctions.get(1).getId(), is(10001L));
        assertThat(auctions.get(2).getId(), is(10000L));


        query = new AuctionPostQuery();
        query.setSortOrder("DESC");
        query.setSortBy("description");

        auctions = auctionRepository.query(query);

        assertThat(auctions.size(), is(3));
        assertThat(auctions.get(0).getId(), is(10002L));
        assertThat(auctions.get(1).getId(), is(10000L));
        assertThat(auctions.get(2).getId(), is(10001L));
    }


    @Test
    public void testSearchKeysQueryParameter() {
        insertTestData("auctions-search.sql");

        AuctionPostQuery query = createIdOrderedQuery();
        query.addSearchKeys("Bob");
        List<AuctionPost> auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(2));
        assertThat(auctions.get(0).getId(), is(10001L));
        assertThat(auctions.get(1).getId(), is(10002L));

        query = createIdOrderedQuery();
        query.addSearchKeys("bob marley");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10001L));

        query = createIdOrderedQuery();
        query.addSearchKeys("bob");
        query.addSearchKeys("marley");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(2));
        assertThat(auctions.get(0).getId(), is(10001L));
        assertThat(auctions.get(1).getId(), is(10002L));

        query = createIdOrderedQuery();
        query.addSearchKeys("intel");
        query.addSearchKeys("i7");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.addSearchKeys("ittnel");
        query.addSearchKeys("i7");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.addSearchKeys("obert");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.addSearchKeys("obert");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.addSearchKeys("Richard");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.addSearchKeys("ROBERT");
        query.addSearchKeys("Richard");
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));
    }

    @Test
    public void testCategoryParameter() {
        insertTestData("auctions-search.sql");

        AuctionPostQuery query = createIdOrderedQuery();
        query.addCategories(Category.ELECTRONICS);
        List<AuctionPost> auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.addCategories(Category.ELECTRONICS);
        query.addCategories(Category.OTHER);
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(2));
        assertThat(auctions.get(0).getId(), is(10000L));
        assertThat(auctions.get(1).getId(), is(10002L));

        query = createIdOrderedQuery();
        query.addCategories(Category.FURNITURE);
        query.addCategories(Category.OTHER);
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));

        query = createIdOrderedQuery();
        query.addCategories(Category.FURNITURE);
        query.addCategories(Category.TRAVEL);
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(0));
    }

    @Test
    public void testPaging() {
        insertTestData("auctions-search.sql");

        AuctionPostQuery query = createIdOrderedQuery();
        query.setPageSize(1);
        List<AuctionPost> auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10000L));

        query = createIdOrderedQuery();
        query.setPageSize(1);
        query.setPageNumber(1);
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10001L));

        query = createIdOrderedQuery();
        query.setPageSize(2);
        query.setPageNumber(1);
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10002L));

        query = createIdOrderedQuery();
        query.setPageSize(2);
        query.setPageNumber(1);
        auctions = auctionRepository.query(query);
        assertThat(auctions.size(), is(1));
        assertThat(auctions.get(0).getId(), is(10002L));
    }


    @Test
    public void testGetAllCountries() {
        insertTestData("auctions-search.sql");

        List<String> countries = auctionRepository.getAllCountriesWhereAuctionsExist();

        assertThat(countries.size(), is(2));
        assertTrue(countries.contains("Austria"));
        assertTrue(countries.contains("Germany"));
    }

    private AuctionPostQuery createIdOrderedQuery() {
        AuctionPostQuery query = new AuctionPostQuery();
        query.setSortOrder("ASC");
        query.setSortBy("id");
        return query;
    }
}
