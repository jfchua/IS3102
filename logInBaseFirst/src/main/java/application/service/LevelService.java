package application.service;
import java.util.Optional;
import java.util.Set;

import application.entity.ClientOrganisation;
import application.entity.Level;
import application.exception.InvalidFileUploadException;
//import application.domain.Building;
public interface LevelService {
    Level create(ClientOrganisation client, long buildingId, int levelNum, int length, int width, String filePath);

	Set<Level> getAllLevels(ClientOrganisation client, long buildingId);

	boolean editLevelInfo(ClientOrganisation client, long id, int levelNum, int length, int width, String filePath);

	boolean deleteLevel(ClientOrganisation client, long id);

	Optional<Level> getLevelById(long id);
	
	boolean updateBuilding(long buildingId, long levelId);
	
	boolean updateBuildingWithOnlyLevelId(long levelId);
	
	long getBuildingByLevelId(ClientOrganisation client, long levelId);
	 
	boolean checkLevel(ClientOrganisation client, long id);
	
	boolean saveImageToLevel(Long levelId, String iconPath) throws InvalidFileUploadException;
}
