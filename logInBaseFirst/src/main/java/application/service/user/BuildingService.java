package application.service.user;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import application.domain.Building;
import application.domain.ClientOrganisation;
//import application.domain.BuildingCreateForm;
import application.exception.InvalidPostalCodeException;

public interface BuildingService {
//Building create(BuildingCreateForm form);

public boolean create(ClientOrganisation client, String name, String address, String postalCode, String city, int numFloor,
		String filePath) throws InvalidPostalCodeException;

public Set<Building> getAllBuildings(ClientOrganisation client);

boolean editBuildingInfo(ClientOrganisation client, long id, String name, String address, String postalCode, String city, int numFloor,
		String filePath);

boolean deleteBuilding(ClientOrganisation client, long id);

public Optional<Building> getBuildingById(long id);
public Building getBuilding(long id);
public boolean checkBuilding(ClientOrganisation client, long id);
}