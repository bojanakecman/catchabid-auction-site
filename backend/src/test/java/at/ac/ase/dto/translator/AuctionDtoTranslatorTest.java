package at.ac.ase.dto.translator;

import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.entities.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AuctionDtoTranslatorTest {

    private AuctionDtoTranslator translator = new AuctionDtoTranslator();

    @Test
    public void testToDto() {
        AuctionPost entity = createAuctionPost();
        AuctionPostSendDTO dto = translator.toSendDto(entity, true);
        assertEqualValues(dto, entity);
    }

    @Test
    public void testToDtoWithNullValues() {
        AuctionPost entity = createAuctionPost();
        entity.setCreator(null);
        entity.setHighestBid(null);

        AuctionPostSendDTO dto = translator.toSendDto(entity, true);

        assertNull(dto.getHighestBid());
        assertNull(dto.getHighestBid());
    }

    @Test
    public void testToDtoList() {
        List<AuctionPost> auctions = Arrays.asList(createAuctionPost(), createAuctionPost());
        List<AuctionPostSendDTO> auctionPostSendDTOS = translator.toDtoList(auctions);
        auctionPostSendDTOS.forEach((dto) -> assertEqualValues(dto, auctions.get(0)));
    }

    private void assertEqualValues(AuctionPostSendDTO dto, AuctionPost entity)
    {
        AuctionHouse auctionHouse = (AuctionHouse) entity.getCreator();
        Bid bid = entity.getHighestBid();

        assertEquals(dto.getCreatorName(), auctionHouse.getName());
        assertEquals(dto.getHighestBid(), bid.getOffer());
        assertEquals(dto.getCategory(), entity.getCategory().name());
        assertEquals(dto.getStatus(), entity.getStatus().name());
        assertEquals(dto.getStartTime(), entity.getStartTime());
        assertEquals(dto.getEndTime(), entity.getEndTime());
    }

    private AuctionPost createAuctionPost()
    {
        AuctionHouse auctionHouse = new AuctionHouse();
        auctionHouse.setName("name");

        Bid bid = new Bid();
        bid.setOffer(2.1);

        Address address = new Address();
        address.setStreet("Resselgasse");
        address.setHouseNr(1);
        address.setCity("Vienna");
        address.setCountry("Austria");

        AuctionPost post = new AuctionPost();
        post.setStartTime(LocalDateTime.of(2020, 1, 1, 0, 0));
        post.setEndTime(LocalDateTime.of(2020, 1, 1, 1, 0));
        post.setCreator(auctionHouse);
        post.setHighestBid(bid);
        post.setCategory(Category.CARS);
        post.setStatus(Status.ACTIVE);
        post.setAddress(address);

        return post;
    }
}
