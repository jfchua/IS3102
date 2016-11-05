package application.service;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Vendor;
import application.exception.InvalidEmailException;
import application.exception.VendorNotFoundException;
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
	public boolean createVendor(ClientOrganisation client, String email, String name, String registration, String description, String contact) throws InvalidEmailException {
		// TODO Auto-generated method stub	
		Pattern pat = Pattern.compile("^.+@.+\\..+$");
		Matcher get = pat.matcher(email);		
		if(!get.matches()){
			throw new InvalidEmailException("The email " + email + " is invalid");
			//return false;
		}
		else{
			Vendor vendor = new Vendor();
			vendor.setEmail(email);
			vendor.setName(name);
			vendor.setRegistration(registration);
			vendor.setDescription(description);
			vendor.setContact(contact);
			System.out.println("finish setting");
			vendorRepository.save(vendor);	
			Set<Vendor> vendors = client.getVendors();
			vendors.add(vendor);
			clientOrganisationRepository.save(client);
			return true;
		}
	}

	@Override
	public Set<Vendor> getAllVendors(ClientOrganisation client) {
		// TODO Auto-generated method stub
		return client.getVendors();
	}

	@Override
	public boolean deleteVendor(ClientOrganisation client, long id) throws VendorNotFoundException {
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
		}
		catch ( VendorNotFoundException e){
			throw e;
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean editVendor(long id, String email, String name, String registration, String description, String contact) throws InvalidEmailException, VendorNotFoundException {
		// TODO Auto-generated method stub
		Pattern pat = Pattern.compile("^.+@.+\\..+$");
		Matcher get = pat.matcher(email);		
		if(!get.matches()){
			throw new InvalidEmailException("The email " + email + " is invalid");
		}
		Optional<Vendor> vendor1 = getVendorById(id);
		try{
			//Optional<Vendor> vendor1 = getVendorById(id);
			if(vendor1.isPresent()){
				
				Vendor vendor = vendor1.get();
				vendor.setEmail(email);
				vendor.setName(name);
				vendor.setRegistration(registration);
				vendor.setDescription(description);
				vendor.setContact(contact);
				vendorRepository.save(vendor);
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Optional<Vendor> getVendorById(long id) throws VendorNotFoundException {
		// TODO Auto-generated method stub
		if ( vendorRepository.findOne(id) == null){
			throw new VendorNotFoundException("Vendor with id " + id + " was not found");
		}
		LOGGER.debug("Getting event={}", id);
		return Optional.ofNullable(vendorRepository.findOne(id));
	}

}
