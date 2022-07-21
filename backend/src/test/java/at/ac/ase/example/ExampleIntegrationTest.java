package at.ac.ase.example;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.repository.auction.AuctionRepository;
import at.ac.ase.repository.user.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExampleIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @MockBean
    private UserRepository mockedUserRepository;

    @After
    public void cleanup() {
        cleanDatabase();
    }

    @Test
    @Transactional
    public void testWithTransactional() {

        insertTestData("example.sql");

        List<AuctionPost> auctionPosts = auctionRepository.findAll();
        assertThat(auctionPosts.size(), is(1));
    }

    @Test
    public void testWithProgrammaticTransaction() {

        insertTestData("example.sql");

        transactionTemplate.executeWithoutResult(status -> {
            List<AuctionPost> auctionPosts = auctionRepository.findAll();
            assertThat(auctionPosts.size(), is(1));;
        });

        tx(status -> {
            List<AuctionPost> auctionPosts = auctionRepository.findAll();
            assertThat(auctionPosts.size(), is(1));;
        });

        List<AuctionPost> auctionPosts2 = txGetResult(status -> auctionRepository.findAll());
        assertThat(auctionPosts2.size(), is(1));
    }

    @Test
    public void testWithMockedBean() {
        assertNotNull(mockedUserRepository);
        when(mockedUserRepository.findById(any())).thenReturn(Optional.empty());
        assertNotNull(mockedUserRepository.findById(null));
    }
}
