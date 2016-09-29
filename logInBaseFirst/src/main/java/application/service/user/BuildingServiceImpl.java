package application.service.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.Vendor;
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
	public void create(ClientOrganisation client, String name, String address, int postalCode, String city, int numFloor, String filePath) {
		// TODO Auto-generated method stub
		Building building = new Building();
		building.setName(name);
		building.setAddress(address);
		building.setPostalCode(postalCode);
		building.setCity(city);
		building.setNumFloor(numFloor);
		building.setPicPath(filePath);
		buildingRepository.save(building);
		Set<Building> buildings = client.getBuildings();
		buildings.add(building);
		clientOrganisationRepository.save(client);
	}

	@Override
	public Set<Building> getAllBuildings(ClientOrganisation client) {
		LOGGER.debug("Getting all buildings");
		return client.getBuildings();
	}


	@Override
	public boolean deleteBuilding(ClientOrganisation client, long id)  {
		// TODO Auto-generated method stub
		try{
		Optional<Building> building1 = getBuildingById(id);
		if(building1.isPresent()){
			Building building = building1.get();
			Set<Building> buildings = client.getBuildings();
			buildings.remove(building);
			clientOrganisationRepository.save(client);	
			buildingRepository.delete(building);
		}
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean editBuildingInfo(long id, String name, String address, int postalCode, String city, int numFloor,
			String filePath) {
		// TODO Auto-generated method stub
		try{
		Optional<Building> building1 = getBuildingById(id);
		if (building1.isPresent()){
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
		}catch (Exception e){
			return false;
		}
		return true;
	}

	@Override
	public Optional<Building> getBuildingById(long id) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting building={}", id);
		return Optional.ofNullable(buildingRepository.findOne(id));
	}

}
