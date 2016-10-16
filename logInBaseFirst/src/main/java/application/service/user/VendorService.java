package application.service.user;

import java.util.Optional;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.domain.Vendor;
import application.exception.InvalidEmailException;
import application.exception.VendorNotFoundException;

public interface VendorService {
boolean createVendor(ClientOrganisation client, String email, String name, String description, String contact) throws InvalidEmailException;
	
Set<Vendor> getAllVendors(ClientOrganisation client);
	
boolean deleteVendor(ClientOrganisation client, long id) throws VendorNotFoundException;
	
boolean editVendor(long id, String email, String name, String description, String contact) throws InvalidEmailException, VendorNotFoundException;	

Optional<Vendor> getVendorById(long id) throws VendorNotFoundException;
}
