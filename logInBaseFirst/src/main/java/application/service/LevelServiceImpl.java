package application.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Level;
import application.entity.MaintenanceSchedule;
import application.entity.Square;
import application.entity.Unit;
import application.entity.UnitAttributeValue;
import application.exception.InvalidFileUploadException;
import application.repository.AreaRepository;
import application.repository.BookingApplRepository;
import application.repository.BuildingRepository;
import application.repository.LevelRepository;
import application.repository.MaintenanceScheduleRepository;
import application.repository.SquareRepository;
import application.repository.UnitAttributeValueRepository;
@Service
public class LevelServiceImpl implements LevelService {
	private final LevelRepository levelRepository;
	private final BuildingRepository buildingRepository;
	/*private final BookingApplRepository bookingApplRepository;
	private final MaintenanceScheduleRepository maintRepository;
	private final AreaRepository areaRepository;
	private final SquareRepository squareRepository;
	private final UnitAttributeValueRepository unitAttributeValueRepository;*/
	private final UnitService unitService;
	private static final Logger LOGGER = LoggerFactory.getLogger(LevelServiceImpl.class);
	
	@Autowired
	public LevelServiceImpl(LevelRepository levelRepository,BuildingRepository buildingRepository,
			/*BookingApplRepository bookingApplRepository, MaintenanceScheduleRepository maintRepository,
			AreaRepository areaRepository, UnitAttributeValueRepository unitAttributeValueRepository, SquareRepository squareRepository*/
			UnitService unitService) {
		//super();
		this.levelRepository =levelRepository;
		this.buildingRepository = buildingRepository;
		/*this.bookingApplRepository = bookingApplRepository;
		this.maintRepository = maintRepository;
		this.areaRepository = areaRepository;
		this.unitAttributeValueRepository = unitAttributeValueRepository;
		this.squareRepository = squareRepository;*/
		this.unitService = unitService;
	}
	
	@Override
	public Level create(ClientOrganisation client, long buildingId, int levelNum, int length, int width, String filePath){
		// TODO Auto-generated method stub
		Level level=null;
		try{
		Optional<Building> building1 = Optional.ofNullable(buildingRepository.findOne(buildingId));
		if(building1.isPresent()&&client.getBuildings().contains(building1.get())){
		Building building = building1.get();
		int numFloor = building.getNumFloor();
		boolean isExist = true;
		Set<Level> levels = building.getLevels();
		for(Level l : levels){
			if(levelNum == l.getLevelNum()){
				isExist = false;
				break;
			}				
		}
		if((levelNum>numFloor)||(!isExist)){
			return null;
		}
		level = new Level();
		level.setLevelNum(levelNum);
		level.setLength(length);
		level.setWidth(width);
		level.setFilePath(filePath);
		levelRepository.save(level);
		level.setBuilding(building);
		
		levels.add(level);
		building.setLevels(levels);
		buildingRepository.save(building);
		}
		}catch (Exception e){
			return null;
		}
		return level;
	}

	@Override
	public Set<Level> getAllLevels(ClientOrganisation client, long buildingId) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting all levels given the building ID");	
		Optional<Building> building1;
		try{
			building1= Optional.ofNullable(buildingRepository.findOne(buildingId));
			if(client.getBuildings().contains(building1.get()))
				return building1.get().getLevels();
			else
				return null;
		}catch (Exception e){
			return null;
		}
	}

	@Override
	public boolean editLevelInfo(ClientOrganisation client, long id, int levelNum, int length, int width, String filePath){
		// TODO Auto-generated method stub
		try{
			Optional<Level> level1 = getLevelById(id);
			if (level1.isPresent()){
				Level level = level1.get();
				Building building = level.getBuilding();
				if(client.getBuildings().contains(building)){
				int numFloor = building.getNumFloor();
				boolean isExist = true;
				Set<Level> levels = building.getLevels();
				for(Level l : levels){
					if(levelNum == l.getLevelNum()&&(id!=l.getId())){
						isExist = false;
						break;
					}				
				}
				if((levelNum>numFloor)||(!isExist)){
					return false;
				}
				level.setLevelNum(levelNum);
				level.setLength(length);
				level.setWidth(width);
				level.setFilePath(filePath);				
				levelRepository.save(level);
				levels.add(level);
				building.setLevels(levels);
				buildingRepository.save(building);
			}
			}
			}catch (Exception e){
				return false;
			}
			return true;

	}

	@Override
	public boolean deleteLevel(ClientOrganisation client, long id) {
		// TODO Auto-generated method stub
		try{
			
			Optional<Level> level1 = getLevelById(id);
			if(level1.isPresent()){	
				Level level = level1.get();
				Set<Unit> units = level.getUnits();
				for(Unit unit : units){
					if(!unitService.checkBookings(unit.getId())){
						return false;
					}
					else{
						if(unitService.deleteUnit(unit.getId(),id)){
							System.out.println("DELETED");
						}else{
							return false;
						}
					}	
				}
				Building build = level.getBuilding();
				System.err.println(build.getName());
				System.err.println(client.getBuildings().contains(build));
				if(client.getBuildings().contains(build)){
				Set<Level> levels = build.getLevels();
				System.err.println("get levels "+levels.size());
				levels.remove(level);
				System.err.println("finish delete level from building");
				build.setLevels(levels);
				levelRepository.delete(level);
				buildingRepository.flush();
				System.err.println("finish delete level");
			}
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

	@Override
	public boolean updateBuildingWithOnlyLevelId(long levelId) {
		try{
		Optional<Level> level = getLevelById(levelId);
		if(level.isPresent()){
		Building build = level.get().getBuilding();
		Set<Level> levels = build.getLevels();
		levels.add(level.get());
		build.setLevels(levels);
		buildingRepository.save(build);
		}
		}
		catch(Exception e){
			return false;
			}
		return true;
	}
	
	@Override
	public long getBuildingByLevelId(ClientOrganisation client, long levelId) {
		long buildingId=0;
		try{
		Optional<Level> levelOpt = getLevelById(levelId);
		if(levelOpt.isPresent()){
		Level level=levelOpt.get();
		Building build = level.getBuilding();
		if(client.getBuildings().contains(build)){
		buildingId=build.getId();
		}
		}
		}
		catch(Exception e){
			return buildingId;
			}
		return buildingId;
	}

	@Override
	public boolean checkLevel(ClientOrganisation client, long id) {
		// TODO Auto-generated method stub
		Set<Building> builds = client.getBuildings();
		boolean doesHave = false;
		try{
		Optional<Level> levelOpt = getLevelById(id);
		if(levelOpt.isPresent()){
			Level level = levelOpt.get();
		for(Building b: builds){
			if(b.getLevels().contains(level)){
				doesHave = true;
			break;
			}
		}
		}
		}catch(Exception e){
			return false;
			}
		return doesHave;
	}
	
	
	
	@Override
	public boolean saveImageToLevel(Long levelId, String iconPath) throws InvalidFileUploadException {
		String t = iconPath.toUpperCase();
		System.err.println("creating icon: " + t);
		if ( !t.contains(".JPG") && !t.contains(".JPEG")&& !t.contains(".SVG") && !t.contains(".TIF") && !t.contains(".PNG") ){
			System.err.println("Invalid icon: " + t);
			throw new InvalidFileUploadException("Building image uploaded is invalid");
		}
		try{		
			Level level=levelRepository.getOne(levelId);
			level.setFilePath(iconPath);
			System.err.println("Level pic path saved as " + iconPath);
			levelRepository.saveAndFlush(level);
			return true;
		}catch(Exception e){
			System.err.println("Error at creating building image " + e.getMessage());
			return false;
		
	}
	}
}
