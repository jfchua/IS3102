package application.service.user;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.exception.BuildingNotFoundException;
//import application.domain.BuildingCreateForm;
import application.exception.InvalidPostalCodeException;

public interface BuildingService {
	//Building create(BuildingCreateForm form);

	public boolean create(ClientOrganisation client, String name, String address, String postalCode, String city, int numFloor,
			String filePath) throws InvalidPostalCodeException;

	public Set<Building> getAllBuildings(ClientOrganisation client);

	boolean editBuildingInfo(ClientOrganisation client, long id, String name, String address, String postalCode, String city, int numFloor,
			String filePath) throws BuildingNotFoundException, InvalidPostalCodeException;

	boolean deleteBuilding(ClientOrganisation client, long id) throws BuildingNotFoundException;

	public Optional<Building> getBuildingById(long id) throws BuildingNotFoundException;
	public Building getBuilding(long id) throws BuildingNotFoundException;
	public boolean checkBuilding(ClientOrganisation client, long id) throws BuildingNotFoundException;
}