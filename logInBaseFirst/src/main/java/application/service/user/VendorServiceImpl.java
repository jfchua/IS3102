package application.service.user;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.Vendor;
import application.repository.ClientOrganisationRepository;
import application.repository.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {
    private final ClientOrganisationRepository clientOrganisationRepository;
	private final VendorRepository vendorRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
	
	@Autowired
	public VendorServiceImpl( VendorRepository vendorRepository,ClientOrganisationRepository clientOrganisationRepository) {
		//super();
		this.clientOrganisationRepository=clientOrganisationRepository;
		this.vendorRepository = vendorRepository;
	}
	
	@Override
	public void createVendor(ClientOrganisation client, String email, String name, String description, String contact) {
		// TODO Auto-generated method stub	
		Vendor vendor = new Vendor();
		vendor.setEmail(email);
		vendor.setName(name);
		vendor.setDescription(description);
		vendor.setContact(contact);
		vendorRepository.save(vendor);	
		Set<Vendor> vendors = client.getVendors();
		vendors.add(vendor);
		clientOrganisationRepository.save(client);
		
	}

	@Override
	public Set<Vendor> getAllVendors(ClientOrganisation client) {
		// TODO Auto-generated method stub
		return client.getVendors();
	}

	@Override
	public boolean deleteVendor(ClientOrganisation client, long id) {
		// TODO Auto-generated method stub
		try{
			Optional<Vendor> vendor1 = getVendorById(id);
			if(vendor1.isPresent()){
				Vendor vendor = vendor1.get();
				
				Set<Vendor> vendors = client.getVendors();
				vendors.remove(vendor);
				client.setVendors(vendors);
			    clientOrganisationRepository.save(client);		
			    vendorRepository.delete(vendor);
			}
			}catch(Exception e){
				return false;
			}
			return true;
		}

	@Override
	public boolean editVendor(long id, String email, String name, String description, String contact) {
		// TODO Auto-generated method stub
		try{
			Optional<Vendor> vendor1 = getVendorById(id);
			if(vendor1.isPresent()){
				Vendor vendor = vendor1.get();
				vendor.setEmail(email);
				vendor.setName(name);
				vendor.setDescription(description);
				vendor.setContact(contact);
				vendorRepository.save(vendor);
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public Optional<Vendor> getVendorById(long id) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting event={}", id);
		return Optional.ofNullable(vendorRepository.findOne(id));
	}

}
