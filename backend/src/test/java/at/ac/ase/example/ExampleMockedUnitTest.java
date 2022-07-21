package at.ac.ase.example;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.repository.auction.AuctionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleMockedUnitTest {

    @Mock
    private AuctionRepository autowiredAuctionRepository;

    @Test
    public void testAutowired() {
        when(autowiredAuctionRepository.findAll())
                .thenReturn(Arrays.asList(new AuctionPost(), new AuctionPost()));

        assertThat(autowiredAuctionRepository.findAll().size(), is(2));
        assertThat(autowiredAuctionRepository.findAll().get(0).getCategory(), nullValue());

        verify(autowiredAuctionRepository, times(2)).findAll();
    }
}
