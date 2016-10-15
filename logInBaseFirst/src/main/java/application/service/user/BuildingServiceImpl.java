package application.service.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.Vendor;
import application.exception.BuildingNotFoundException;
import application.exception.InvalidPostalCodeException;
import application.repository.BuildingRepository;
import application.repository.ClientOrganisationRepository;

@Service
public class BuildingServiceImpl implements BuildingService {
	private final BuildingRepository buildingRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);

	@Autowired
	public BuildingServiceImpl(BuildingRepository buildingRepository, ClientOrganisationRepository clientOrganisationRepository) {
		//super();
		this.buildingRepository = buildingRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
	}

	@Override
	public boolean create(ClientOrganisation client, String name, String address, String postalCode, String city, int numFloor, String filePath)throws InvalidPostalCodeException {
		// TODO Auto-generated method stub
		Building building = new Building();
		building.setName(name);
		building.setAddress(address);
		CharSequence post =String.valueOf(postalCode);
		String regex = "^[0-9]{6}$";
		Pattern pat = Pattern.compile(regex);
		Matcher get = pat.matcher(post);
		if(!get.matches()){
			throw new InvalidPostalCodeException("Postal code of " + postalCode + " is invalid");
		}
		else{
			building.setPostalCode(postalCode);
			building.setCity(city);
			building.setNumFloor(numFloor);
			building.setPicPath(filePath);
			buildingRepository.save(building);
			Set<Building> buildings = client.getBuildings();
			buildings.add(building);
			clientOrganisationRepository.save(client);
			return true;
		}
	}

	@Override
	public Set<Building> getAllBuildings(ClientOrganisation client) {
		LOGGER.debug("Getting all buildings");
		return client.getBuildings();
	}


	@Override
	public boolean deleteBuilding(ClientOrganisation client, long id) throws BuildingNotFoundException  {
		// TODO Auto-generated method stub
		if ( !getBuildingById(id).isPresent()){
			throw new BuildingNotFoundException("Building with id of " + id + " was not found");
		}
		try{
			Optional<Building> building1 = getBuildingById(id);
			Set<Building> buildings = client.getBuildings();
			if(building1.isPresent()&&buildings.contains(building1.get())){
				Building building = building1.get();	
				buildings.remove(building);
				clientOrganisationRepository.save(client);	
				buildingRepository.delete(building);
			}
			else
				return false;
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean editBuildingInfo(ClientOrganisation client, long id, String name, String address, String postalCode, String city, int numFloor,
			String filePath) throws BuildingNotFoundException, InvalidPostalCodeException {
		// TODO Auto-generated method stub
		
		if ( !getBuildingById(id).isPresent()){
			throw new BuildingNotFoundException("Building of id " + id + " was not found");
		}
		CharSequence post =String.valueOf(postalCode);
		String regex = "^[0-9]{6}$";
		Pattern pat = Pattern.compile(regex);
		Matcher get = pat.matcher(post);
		if (!get.matches() ){
			throw new InvalidPostalCodeException("Postal code of " + postalCode + " is invalid");
		}
		try{
			Optional<Building> building1 = getBuildingById(id);
			Set<Building> buildings = client.getBuildings();


			if (building1.isPresent()&&get.matches()&&buildings.contains(building1.get())){
				//uilding building = building1.get();
				//if(building1.get().getName().)
				building1.get().setName(name);
				building1.get().setAddress(address);
				building1.get().setPostalCode(postalCode);
				building1.get().setCity(city);
				building1.get().setNumFloor(numFloor);
				building1.get().setPicPath(filePath);
				//buildingRepository.save(building);
				buildingRepository.flush();
			}
			else
				return false;
		}
		catch ( BuildingNotFoundException e){
			throw e;
		}
		catch (Exception e){
			return false;
		}
		return true;
	}

	@Override
	public Optional<Building> getBuildingById(long id) throws BuildingNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting building={}", id);
		Building building = buildingRepository.findOne(id);
		if ( building == null ){
			throw new BuildingNotFoundException("Building of id " + id + " was not found");
		}
		return Optional.ofNullable(building);
	}

	@Override
	public Building getBuilding(long id) throws BuildingNotFoundException {
		// TODO Auto-generated method stub
		Optional<Building> buildingOpt=getBuildingById(id);
		Building building=buildingOpt.get();
		return building;
	}

	@Override
	public boolean checkBuilding(ClientOrganisation client, long id) throws BuildingNotFoundException {
		if ( !getBuildingById(id).isPresent()){
			throw new BuildingNotFoundException("Building of id " + id + " was not found");
		}
		try{
			Optional<Building> building1 = getBuildingById(id);
			Set<Building> buildings = client.getBuildings();
			if (buildings.contains(building1.get())){
				return true;
			}
			else
				return false;
		}catch (Exception e){
			return false;
		}
	}

}
