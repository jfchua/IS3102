package application.service;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.*;
import application.repository.*;

@Service
public class UnitServiceImpl implements UnitService {
	private final UnitRepository unitRepository;
	private final LevelRepository levelRepository;
	private final SquareRepository squareRepository;
	private final IconRepository iconRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(LevelServiceImpl.class);
	
	@Autowired
	public UnitServiceImpl(LevelRepository levelRepository,UnitRepository unitRepository, SquareRepository squareRepository,IconRepository iconRepository) {
		this.levelRepository =levelRepository;
		this.unitRepository = unitRepository;
		this.squareRepository=squareRepository;
		this.iconRepository=iconRepository;
		
	}
/*	@Override
	public Unit createUnit(int left, int top, int height, int width, String color, String type,
			String unitNumber, int dimensionLength, int dimensionWidth, Boolean rentable, String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Unit unit = new Unit();
		unit.setSquare(square);
		unit.setUnitNumber(unitNumber);
		unit.setLength(dimensionLength);
		unit.setWidth(dimensionWidth);
		unit.setRentable(rentable);
		unit.setDescription(description);
		unitRepository.save(unit);
		unit.setSquare(square);
		unitRepository.save(unit);
		squareRepository.save(square);
	
		return unit;
	}*/
	@Override
	public Unit createUnitOnLevel(long levelId, int left, int top, int height, int width, String color, String type,
			String unitNumber, int dimensionLength, int dimensionWidth, Boolean rentable, String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Unit unit = new Unit();
		System.out.println("UnitService"+1);
		unit.setUnitNumber(unitNumber);
		unit.setLength(dimensionLength);
		unit.setWidth(dimensionWidth);
		unit.setRentable(rentable);
		System.out.println("rentable"+rentable);
		unit.setDescription(description);
		unit.setRent(100.00);//hard coded rent =100, need to chagne later
		System.out.println("UnitService"+2);
		unitRepository.saveAndFlush(unit);
		unit.setSquare(square);
		unitRepository.saveAndFlush(unit);
		System.out.println("UnitService"+3);
		//unit.setSquare(square);
		//unitRepository.save(unit);
		Level level=levelRepository.findOne(levelId); 
		//System.out.println("Test:Get level:"+level);
		System.out.println("UnitService"+4);
		Set<Unit> units=level.getUnits();
		System.out.println("UnitService"+5);
		units.add(unit);
		level.setUnits(units);
		System.out.println("UnitService"+6);
		levelRepository.saveAndFlush(level);
		System.out.println("UnitService"+7);
		unit.setLevel(level);
		System.out.println("UnitService"+8);
		unitRepository.saveAndFlush(unit);
		System.out.println("UnitService"+9);
		return unit;
	}
	@Override
	public Unit createUnitOnLevelWithIcon(long levelId,long iconId, int left, int top, int height, int width, String color, String type,
			String unitNumber, int dimensionLength, int dimensionWidth, Boolean rentable, String description) {
		Square square=createSquareWithIcon(iconId,left,top,height,width,color,type);
		Unit unit = new Unit();
		System.out.println("UnitService"+1);
		unit.setUnitNumber(unitNumber);
		unit.setLength(dimensionLength);
		unit.setWidth(dimensionWidth);
		unit.setRentable(rentable);
		unit.setDescription(description);
		unit.setRent(100.00);//hard coded rent =100, need to chagne later
		System.out.println("UnitService"+2);
		unitRepository.saveAndFlush(unit);
		unit.setSquare(square);
		unitRepository.saveAndFlush(unit);
		System.out.println("UnitService"+3);
		//unit.setSquare(square);
		//unitRepository.save(unit);
		Level level=levelRepository.findOne(levelId); 
		//System.out.println("Test:Get level:"+level);
		System.out.println("UnitService"+4);
		Set<Unit> units=level.getUnits();
		System.out.println("UnitService"+5);
		units.add(unit);
		level.setUnits(units);
		System.out.println("UnitService"+6);
		levelRepository.saveAndFlush(level);
		System.out.println("UnitService"+7);
		unit.setLevel(level);
		System.out.println("UnitService"+8);
		unitRepository.saveAndFlush(unit);
		System.out.println("UnitService"+9);
		return unit;
	}
	@Override
	public Square createSquare( int left, int top, int height, int width, String color, String type){
		Square square = new Square();
		square.setLeft(left);
		square.setTop(top);
		square.setHeight(height);
		square.setWidth(width);
		square.setColor(color);
		square.setType(type);
		squareRepository.saveAndFlush(square);
		return square;
	}
	@Override
	public Square createSquareWithIcon( long iconId,int left, int top, int height, int width, String color, String type){
		Icon icon=iconRepository.getOne(iconId);
		Square square = new Square();
		square.setLeft(left);
		square.setTop(top);
		square.setHeight(height);
		square.setWidth(width);
		square.setColor(color);
		square.setType(type);
		square.setIcon(icon);
		squareRepository.saveAndFlush(square);
		return square;
	}
	@Override
	public boolean editUnitInfo(long id,int left,int top, int height, int width, String color, String type,String unitNumber, int dimensionLength, int dimensionWidth,boolean rentable,String description) {
		try{
			Optional<Unit> unitOpt = getUnitById(id);
			if (unitOpt.isPresent()){
				
				Unit unit=unitOpt.get();
				unit.setLength(dimensionLength);
				unit.setWidth(dimensionWidth);
				unit.setUnitNumber(unitNumber);
				unit.setDescription(description);
				System.out.println("UnitServiceImpl: start saving squares");
				long SquareId=unit.getSquare().getId();
				if(editSquareInfo(SquareId,left, top, height, width,  color, type)){
					System.out.println("Test: Square is updated");
				}else{
					System.out.println("UnitServiceImpl: save square failed");
					return false;
				}
				/*Square square= createSquare( left, top, height,  width,  color, type);
				squareRepository.saveAndFlush(square);
				Square oldSquare=unit.getSquare();
				squareRepository.delete(oldSquare);
				unit.setSquare(square);
				
				*/
				unitRepository.saveAndFlush(unit);
				
			}
			}catch (Exception e){
				return false;
			}
			return true;
	}

	@Override
	public boolean editSquareInfo(long id,int left, int top, int height,int width, String color,String type) {
		try{
			Optional<Square> squareOpt = getSquareById(id);
			if (squareOpt.isPresent()){
				Square square=squareOpt.get();
				square.setLeft(left);
				square.setTop(top);
				square.setHeight(height);
				square.setWidth(width);		
				square.setColor(color);	
				square.setType(type);	
				squareRepository.saveAndFlush(square);
			}
			}catch (Exception e){
				return false;
			}
			return true;
	}
	@Override
	public boolean deleteUnitsFromLevel(Set<Long> unitIds, long levelId) {
	
			Set<Unit> units=getUnitsByLevelId(levelId);
			System.out.println("UnitServiceTest: unitIds "+unitIds);
			//get a set of existing units ids
			Set<Long> existingUnitIds=new HashSet<Long>();
			for(Unit unit: units){
				Long k=unit.getId();
				existingUnitIds.add(k);
			}
			for (Long existId : existingUnitIds) {
				
				System.out.println("UnitServiceTest: unitId "+existId+" "+unitIds.contains(existId));
				if(unitIds.contains(existId)){
					System.out.println("UnitServiceTest: unit "+existId+" exists.");
				}else{
					if(deleteUnit(existId,levelId)){
						System.out.println("Test: unit "+existId+" is deleted.");
						
					}else{
						System.out.println("Test: Error, unit "+existId+" is not deleted.");
						return false;
					}
				}
			}
				
			
		System.out.println("UnitServiceTest: deleted unit finish deleting");
			return true;
	}

	@Override
	public boolean deleteUnit(long id, long levelId) {
		try{
			Optional<Unit> unitOpt = getUnitById(id);
			Level level = levelRepository.findOne(levelId);
			//Optional<Level> level = Optional.ofNullable(levelRepository.findOne(levelId));
			//if(unit.isPresent() && level.isPresent()){
			//	Set<Unit> units=level.get().getUnits();
				//units.remove(unit.get());
				//level.get().setUnits(units);
			if(unitOpt.isPresent()){
				Unit unit= unitOpt.get();
				Square square=unit.getSquare();
				unit.setSquare(null);
				
				
				Set<Unit> units=level.getUnits();
				units.remove(unit);
				level.setUnits(units);
				levelRepository.saveAndFlush(level);
				
				unitRepository.delete(unit);
				unitRepository.flush();
				
				squareRepository.delete(square);
				squareRepository.flush();
				return true;
			}
			
				//levelRepository.saveAndFlush(level.get());
				//levelRepository.save(level.get());
				//levelRepository.flush();
				
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public Optional<Unit> getUnitById(long id) {
		LOGGER.debug("Getting unit={}", id);
		return Optional.ofNullable(unitRepository.findOne(id));
	}
	@Override
	public Optional<Square> getSquareById(long id) {
		LOGGER.debug("Getting square={}", id);
		return Optional.ofNullable(squareRepository.findOne(id));
	}

	@Override
	public Set<Unit> getAllUnits() {
		LOGGER.debug("Getting all units");
		return unitRepository.fetchAllUnits();
	}
	@Override
	public Set<Square> getAllSquares() {
		LOGGER.debug("Getting all squares");
		return  squareRepository.fetchAllSquares();
	}
	@Override
	public Set<Unit> getUnitsByLevelId(long levelId) {
		System.out.println("UnitServiceTest: getUnits of level "+levelId);
		LOGGER.debug("Getting all units by Id"+levelId);
		Level level=levelRepository.getOne(levelId);
		return  level.getUnits();
	}
	
	

}
