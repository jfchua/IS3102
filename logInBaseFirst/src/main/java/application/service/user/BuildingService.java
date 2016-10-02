package application.service.user;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import application.domain.Building;
import application.domain.ClientOrganisation;
//import application.domain.BuildingCreateForm;

public interface BuildingService {
//Building create(BuildingCreateForm form);

public boolean create(ClientOrganisation client, String name, String address, String postalCode, String city, int numFloor,
		String filePath);

public Set<Building> getAllBuildings(ClientOrganisation client);

boolean editBuildingInfo(ClientOrganisation client, long id, String name, String address, String postalCode, String city, int numFloor,
		String filePath);

boolean deleteBuilding(ClientOrganisation client, long id);

public Optional<Building> getBuildingById(long id);

public boolean checkBuilding(ClientOrganisation client, long id);
}