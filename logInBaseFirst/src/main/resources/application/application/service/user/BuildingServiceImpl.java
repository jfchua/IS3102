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
import application.repository.BuildingRepository;

@Service
public class BuildingServiceImpl implements BuildingService {
	private final BuildingRepository buildingRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	
	@Autowired
	public BuildingServiceImpl(BuildingRepository buildingRepository) {
		//super();
		this.buildingRepository = buildingRepository;
	}
	
	@Override
	public Building create(String name, String address, int postalCode, String city, int numFloor, String filePath) {
		// TODO Auto-generated method stub
		Building building = new Building();
		building.setName(name);
		building.setAddress(address);
		building.setPostalCode(postalCode);
		building.setCity(city);
		building.setNumFloor(numFloor);
		building.setPicPath(filePath);
		return buildingRepository.save(building);
	}

	@Override
	public Set<Building> getAllBuildings() {
		LOGGER.debug("Getting all buildings");
		return buildingRepository.fetchAllBuildings();
	}


	@Override
	public boolean deleteBuilding(long id)  {
		// TODO Auto-generated method stub
		try{
		Optional<Building> building = getBuildingById(id);
		if(building.isPresent()){
			buildingRepository.delete(building.get());
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
		Optional<Building> building = getBuildingById(id);
		if (building.isPresent()){
			building.get().setName(name);
			building.get().setAddress(address);
			building.get().setPostalCode(postalCode);
			building.get().setCity(city);
			building.get().setNumFloor(numFloor);
			building.get().setPicPath(filePath);
			buildingRepository.save(building.get());
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
