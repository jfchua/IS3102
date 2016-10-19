package application.service;

import java.util.Optional;
import java.util.Set;

import application.entity.ClientOrganisation;
import application.entity.Vendor;
import application.exception.InvalidEmailException;
import application.exception.VendorNotFoundException;

public interface VendorService {
boolean createVendor(ClientOrganisation client, String email, String name, String description, String contact) throws InvalidEmailException;
	
Set<Vendor> getAllVendors(ClientOrganisation client);
	
boolean deleteVendor(ClientOrganisation client, long id) throws VendorNotFoundException;
	
boolean editVendor(long id, String email, String name, String description, String contact) throws InvalidEmailException, VendorNotFoundException;	

Optional<Vendor> getVendorById(long id) throws VendorNotFoundException;
}
