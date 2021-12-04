package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VendorMapperTest {
    public static final String NAME = "Joe";
    public static final long ID = 1L;

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    void vendorToVendorDTO() {
        Vendor vendor = new Vendor();
        vendor.setName(NAME); vendor.setId(ID);

        VendorDTO vendorDTOCreated = vendorMapper.vendorToVendorDTO(vendor);

        Assertions.assertEquals(NAME, vendorDTOCreated.getName());
        Assertions.assertNull(vendorDTOCreated.getVendorUrl());
    }

    @Test
    void vendorDTOToVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor vendorCreated = VendorMapper.INSTANCE.vendorDTOToVendor(vendorDTO);

        Assertions.assertEquals(NAME, vendorCreated.getName());
        Assertions.assertNull(vendorCreated.getId());

    }
}