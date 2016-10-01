package application.service.user;
import java.util.Optional;
import java.util.Set;

import application.domain.Level;
//import application.domain.Building;
public interface LevelService {
	boolean create(long buildingId, int levelNum, int length, int width, String filePath);

	Set<Level> getAllLevels(long buildingId);

	boolean editLevelInfo(long id, int levelNum, int length, int width, String filePath);

	boolean deleteLevel(long id);

	Optional<Level> getLevelById(long id);
	
	boolean updateBuilding(long buildingId, long levelId);
	
	boolean updateBuildingWithOnlyLevelId(long levelId);
	
	 long getBuildingByLevelId(long levelId);
}
