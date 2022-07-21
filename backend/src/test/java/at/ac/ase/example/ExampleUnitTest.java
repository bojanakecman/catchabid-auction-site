package at.ac.ase.example;

import at.ac.ase.repository.auction.AuctionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ExampleUnitTest {

    @Autowired
    private AuctionRepository autowiredAuctionRepository;

    @Test
    public void testAutowired() {
        assertNull(autowiredAuctionRepository);
    }

    @Test
    public void simpleTest() {
        assertThat(String.valueOf(2), is("2"));
    }
}
