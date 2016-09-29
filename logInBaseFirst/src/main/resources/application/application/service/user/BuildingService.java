package application.service.user;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import application.domain.Building;
//import application.domain.BuildingCreateForm;

public interface BuildingService {
//Building create(BuildingCreateForm form);

public Building create(String name, String address, int postalCode, String city, int numFloor,
		String filePath);

public Set<Building> getAllBuildings();

boolean editBuildingInfo(long id, String name, String address, int postalCode, String city, int numFloor,
		String filePath);

boolean deleteBuilding(long id);

public Optional<Building> getBuildingById(long id);
}