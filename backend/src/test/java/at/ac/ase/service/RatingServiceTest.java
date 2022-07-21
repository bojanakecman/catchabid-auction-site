package at.ac.ase.service;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.dto.AuctionQueryDTO;
import at.ac.ase.dto.RatingDTO;
import at.ac.ase.dto.RatingDataDTO;
import at.ac.ase.entities.RatingPK;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.repository.user.RatingRepository;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.auction.implementation.AuctionService;
import at.ac.ase.service.user.rating.implementation.RatingService;
import at.ac.ase.util.exceptions.InvalidRatingDataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RatingServiceTest extends BaseIntegrationTest {

    @Autowired
    AuctionService auctionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    @Before
    public void setup(){
        insertTestData("multiple-auctions.sql");
    }

    @After
    public void cleanup(){
        cleanDatabase();
    }

    @Test
    @Transactional
    public void testSetValidRating(){
        List<AuctionPostSendDTO> auctions = auctionService.searchAuctions(getRecentQuery(0, 0));
        RegularUser regularUser = userRepository.findByEmail("testUser@test.com");
        RatingDataDTO ratingDataDTO = new RatingDataDTO();
        AuctionPostSendDTO auctionPostSendDTO = auctions.get(0);
        auctionPostSendDTO.setEndTime(LocalDateTime.now());
        ratingDataDTO.setAuctionPost(auctionPostSendDTO);
        System.out.println(auctions.get(0).getEndTime());
        ratingDataDTO.setRatingValue(4);

        assertThat(ratingRepository.findAll().size(), is(0));
        ratingService.setRating(ratingDataDTO, regularUser);
        assertThat(ratingRepository.findAll().size(), is(1));
    }

    @Test(expected = InvalidRatingDataException.class)
    @Transactional
    public void testSetRatingTooHigh(){
        List<AuctionPostSendDTO> auctions = auctionService.searchAuctions(getRecentQuery(0, 0));
        RegularUser regularUser = userRepository.findByEmail("test@test.com");
        RatingDataDTO ratingDataDTO = new RatingDataDTO();
        AuctionPostSendDTO auctionPostSendDTO = auctions.get(0);
        auctionPostSendDTO.setEndTime(LocalDateTime.now());
        ratingDataDTO.setAuctionPost(auctionPostSendDTO);
        ratingDataDTO.setRatingValue(6);
        ratingService.setRating(ratingDataDTO, regularUser);
    }

    @Test(expected = InvalidRatingDataException.class)
    @Transactional
    public void testSetRatingInvalidRange(){
        LocalDateTime date = LocalDateTime.of(2020, Month.DECEMBER,17,6,30,40,50000);
        List<AuctionPostSendDTO> auctions =auctionService.searchAuctions(getRecentQuery(0, 0));
        RegularUser regularUser = userRepository.findByEmail("test@test.com");
        RatingDataDTO ratingDataDTO = new RatingDataDTO();
        AuctionPostSendDTO auctionPostSendDTO = auctions.get(0);
        auctionPostSendDTO.setEndTime(date);
        ratingDataDTO.setAuctionPost(auctionPostSendDTO);
        ratingDataDTO.setRatingValue(4);
        ratingService.setRating(ratingDataDTO, regularUser);
    }

    @Test
    @Transactional
    public void testSetRatingValidRange(){
        LocalDateTime date = LocalDateTime.now();
        List<AuctionPostSendDTO> auctions = auctionService.searchAuctions(getRecentQuery(0, 0));
        RegularUser regularUser = userRepository.findByEmail("testUser@test.com");
        RatingDataDTO ratingDataDTO = new RatingDataDTO();
        AuctionPostSendDTO auctionPostSendDTO = auctions.get(0);
        auctionPostSendDTO.setEndTime(date);
        ratingDataDTO.setAuctionPost(auctionPostSendDTO);
        ratingDataDTO.setRatingValue(4);
        ratingService.setRating(ratingDataDTO, regularUser);
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
