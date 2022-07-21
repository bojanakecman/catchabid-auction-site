package at.ac.ase.dto.translator;

import at.ac.ase.dto.AddressDTO;
import at.ac.ase.entities.Address;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoTranslator {

    public AddressDTO toAddressDTO(Address address){
        AddressDTO addressDTO = new AddressDTO();
        if(address==null){
            return addressDTO;
        }
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setHouseNr(address.getHouseNr());
        addressDTO.setStreet(address.getStreet());
        return addressDTO;
    }


    public Address toAddress(AddressDTO addressDTO){
        Address address = new Address();
        if (addressDTO==null){
            return address;
        }
        if (StringUtils.isNotBlank(addressDTO.getCity())) {
            address.setCity(addressDTO.getCity());
        }
        if (StringUtils.isNotBlank(addressDTO.getCountry())) {
            address.setCountry(addressDTO.getCountry());
        }
        if (addressDTO.getHouseNr()!=null) {
            address.setHouseNr(addressDTO.getHouseNr());
        }
        if (StringUtils.isNotBlank(addressDTO.getStreet())) {
            address.setStreet(addressDTO.getStreet());
        }
        return address;
    }

}
