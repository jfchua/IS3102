package application.service.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import application.domain.Level;
import application.repository.BuildingRepository;
import application.repository.LevelRepository;
import application.domain.Building;
@Service
public class LevelServiceImpl implements LevelService {
	private final LevelRepository levelRepository;
	private final BuildingRepository buildingRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(LevelServiceImpl.class);
	
	@Autowired
	public LevelServiceImpl(LevelRepository levelRepository,BuildingRepository buildingRepository) {
		//super();
		this.levelRepository =levelRepository;
		this.buildingRepository = buildingRepository;
	}
	
	@Override
	public Level create(int levelNum, int length, int width, String filePath){
		// TODO Auto-generated method stub
		Level level = new Level();
		level.setLevelNum(levelNum);
		level.setLength(length);
		level.setWidth(width);
		level.setFilePath(filePath);
		levelRepository.save(level);
		return level;
	}

	@Override
	public Set<Level> getAllLevels(long buildingId) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting all levels given the building ID");
		return levelRepository.getLevelsByBuilding(buildingId);
	}

	@Override
	public boolean editLevelInfo(long id, int levelNum, int length, int width, String filePath){
		// TODO Auto-generated method stub
		try{
			Optional<Level> level = getLevelById(id);
			if (level.isPresent()){
				level.get().setLevelNum(levelNum);
				level.get().setLength(length);
				level.get().setWidth(width);
				level.get().setFilePath(filePath);				
				levelRepository.save(level.get());
			}
			}catch (Exception e){
				return false;
			}
			return true;

	}

	@Override
	public boolean deleteLevel(long id, long buildingId) {
		// TODO Auto-generated method stub
		try{
			Optional<Building> building = Optional.ofNullable(buildingRepository.findOne(buildingId));
			Optional<Level> level = getLevelById(id);
			if(level.isPresent()){			
				Set<Level> levels = building.get().getLevels();
				levels.remove(level.get());
				building.get().setLevels(levels);
				levelRepository.delete(level.get());
				buildingRepository.save(building.get());
			}
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public Optional<Level> getLevelById(long id) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting level={}", id);
		return Optional.ofNullable(levelRepository.findOne(id));
	}

	@Override
	public boolean updateBuilding(long buildingId, long levelId) {
		// TODO Auto-generated method stub
		try{
		Optional<Level> level = getLevelById(levelId);
		Optional<Building> building = Optional.ofNullable(buildingRepository.findOne(buildingId));
		if(level.isPresent() && building.isPresent()){
			level.get().setBuilding(building.get());
			Set<Level> levels = building.get().getLevels();
			levels.add(level.get());
			building.get().setLevels(levels);
			levelRepository.save(level.get());
			buildingRepository.save(building.get());
		}
		}
		catch(Exception e){
		return false;
		}
		return true;
	}

}
