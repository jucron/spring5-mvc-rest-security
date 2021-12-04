package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    private final String URL = "/api/v1/vendors/";

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorListDTO getAllVendors() {
        List<Vendor> vendorList = vendorRepository.findAll();
        List<VendorDTO> vendorDTOList = new ArrayList<>();
        for (Vendor vendor : vendorList) {
            vendorDTOList.add(vendorMapper.vendorToVendorDTO(vendor));
        }
        return new VendorListDTO(vendorDTOList);
    }

@Override
public VendorDTO getVendorById(Long id) {
    if (vendorRepository.findById(id).isEmpty()) throw new ResourceNotFoundException();

    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(
            vendorRepository.findById(id).get());
    vendorDTO.setVendorUrl(URL+id);
    return vendorDTO;
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        return saveAndReturnDTO(vendorMapper.vendorDTOToVendor(vendorDTO));
    }

    @Override
    public VendorDTO saveVendorByDTO(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);
        vendor.setId(id);
        return saveAndReturnDTO(vendor);
    }
    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    //todo if more properties, add more if statements

                    if(vendorDTO.getName() != null){
                        vendor.setName(vendorDTO.getName());
                    }

                    return saveAndReturnDTO(vendor);
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO saveAndReturnDTO (Vendor vendor) {
        Vendor vendorSaved = vendorRepository.save(vendor);
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendorSaved);
        vendorDTO.setVendorUrl(URL+vendorSaved.getId());
        return vendorDTO;
    }
}
