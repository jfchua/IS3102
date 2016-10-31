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
			String unitNumber, int col, int row,int  sizex,int sizey,Boolean rentable, String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Unit unit = new Unit();
		System.out.println("UnitService"+1);
		unit.setUnitNumber(unitNumber);
		unit.setCol(col);
		unit.setRow(row);
		unit.setSizex(sizex);
		unit.setSizey(sizey);
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
	public boolean addUnitOnLevel(long levelId) {
		//GET LEVEL
		Level level=levelRepository.getOne(levelId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int levelLength=level.getLength();
		int levelWidth=level.getWidth();
		int sizex=levelLength/10;
		int sizey=levelWidth/10;
		int col=0;
		int row=0;
		Unit unitTemp=new Unit();
		boolean keepChecking=true;
		do{
			unitTemp.setSizex(sizex);
			unitTemp.setSizey(sizey);
			unitTemp.setCol(col);
			unitTemp.setRow(row);
			if(passOverlapCheckWithExistingUnits(levelId,unitTemp)){
				keepChecking=false;
			}else{
				sizex=1 + (int)(Math.random() * sizex);
				sizey=1 +(int)(Math.random() * sizey);
				col=0 + (int)(Math.random() * (levelLength-sizex));
				row=0 + (int)(Math.random() * (levelWidth-sizey));
			}
		}while(keepChecking);
		
		
	
		
		Square square=createSquare(100,100,100,100,"coral","./svg/rect.svg");
		
		System.out.println("UnitService"+1);
		unitTemp.setUnitNumber("#unit");
		unitTemp.setRentable(true);
		System.out.println("test124");
		unitTemp.setDescription("# There is no description yet");
		unitTemp.setRent(100.00);//hard coded rent =100, need to change later
		System.out.println("127");
		unitRepository.saveAndFlush(unitTemp);
		unitTemp.setSquare(square);
		unitRepository.saveAndFlush(unitTemp);
		System.out.println("test131");
		//unit.setSquare(square);
		//unitRepository.save(unit);
		//System.out.println("Test:Get level:"+level);
		System.out.println("teset135");
		Set<Unit> units=level.getUnits();
		System.out.println("test137");
		units.add(unitTemp);
		level.setUnits(units);
		System.out.println("140");
		levelRepository.saveAndFlush(level);
		System.out.println("test142");
		unitTemp.setLevel(level);
		System.out.println("144");
		unitRepository.saveAndFlush(unitTemp);
		System.out.println("UnitService"+9);
		return true;
	}
	
	@Override
	public boolean addDefaultIconOnLevel(long levelId,String type) {
		//GET LEVEL
		Level level=levelRepository.getOne(levelId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int levelLength=level.getLength();
		int levelWidth=level.getWidth();
		int sizex=Math.min(levelLength/20, levelWidth/20);
		int sizey=Math.min(levelLength/20+1, levelWidth/20+1);
		int col=0;
		int row=0;
		Unit unitTemp=new Unit();
		boolean keepChecking=true;
		do{
			unitTemp.setSizex(sizex);
			unitTemp.setSizey(sizey);
			unitTemp.setCol(col);
			unitTemp.setRow(row);
			if(passOverlapCheckWithExistingUnits(levelId,unitTemp)){
				keepChecking=false;
			}else{
				col=0 + (int)(Math.random() * (levelLength-sizex));
				row=0 + (int)(Math.random() * (levelWidth-sizey));
			}
		}while(keepChecking);
		Square square=createSquare(100,100,100,100,"transparent",type);
		unitTemp.setUnitNumber("");
		unitTemp.setRentable(false);
		unitTemp.setDescription("# There is no description yet");
		unitRepository.saveAndFlush(unitTemp);
		unitTemp.setSquare(square);
		unitRepository.saveAndFlush(unitTemp);
		Set<Unit> units=level.getUnits();
		units.add(unitTemp);
		level.setUnits(units);
		levelRepository.saveAndFlush(level);
		unitTemp.setLevel(level);
		unitRepository.saveAndFlush(unitTemp);	
		return true;
	}
	
	
	@Override
	public boolean addCustIconOnLevel(long levelId,long iconId) {
		//GET LEVEL
		Level level=levelRepository.getOne(levelId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int levelLength=level.getLength();
		int levelWidth=level.getWidth();
		int sizex=Math.min(levelLength/15, levelWidth/15);
		int sizey=Math.min(levelLength/15+1, levelWidth/15+1);
		int col=0;
		int row=0;
		Unit unitTemp=new Unit();
		boolean keepChecking=true;
		do{
			unitTemp.setSizex(sizex);
			unitTemp.setSizey(sizey);
			unitTemp.setCol(col);
			unitTemp.setRow(row);
			if(passOverlapCheckWithExistingUnits(levelId,unitTemp)){
				keepChecking=false;
			}else{
				col=0 + (int)(Math.random() * (levelLength-sizex));
				row=0 + (int)(Math.random() * (levelWidth-sizey));
			}
		}while(keepChecking);
		Square square=createSquareWithIcon(iconId,100,100,100,100,"transparent","");
		unitTemp.setUnitNumber("");
		unitTemp.setRentable(false);
		//hahahaha
		unitTemp.setDescription("# There is no description yet");
		unitRepository.saveAndFlush(unitTemp);
		unitTemp.setSquare(square);
		unitRepository.saveAndFlush(unitTemp);
		Set<Unit> units=level.getUnits();
		units.add(unitTemp);
		level.setUnits(units);
		levelRepository.saveAndFlush(level);
		unitTemp.setLevel(level);
		unitRepository.saveAndFlush(unitTemp);	
		return true;
	}
	
	@Override
	public  boolean passOverlapCheckWithExistingUnits(long levelId, Unit unit) {
		//GET LEVEL
		Level level=levelRepository.getOne(levelId);
		Set<Unit> units=level.getUnits();
		for (Unit oneExistUnit:units){
			if(!passOverlapCheckTwoUnits(unit,oneExistUnit)){
				return false;
			}
		} //END FOR LOOP FOR ALL EXISTING UNITS ON LEVEL
		return true;
	}
	
	@Override
	public boolean passOverlapCheckTwoUnits(Unit unit1,Unit unit2) {
		if(passOverlapCheckTwoUnitsOneWay(unit1,unit2)&&passOverlapCheckTwoUnitsOneWay(unit2,unit1)){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public boolean passOverlapCheckTwoUnitsOneWay(Unit unit1,Unit unit2) {
		int col1=unit1.getCol();
		int row1=unit1.getRow();
		int sizeX1=unit1.getSizex();
		int sizeY1=unit1.getSizey();
		
		int col2=unit2.getCol();
		int row2=unit2.getRow();
		int sizeX2=unit2.getSizex();
		int sizeY2=unit2.getSizey();
		
		if( ((col1+sizeX1>=col2)&&(row1+sizeY1>=row2))&&((col1<=col2+sizeX2)&&(row1<=row2+sizeY2))){
			return false;
		}else if (((col1<=col2+sizeX2)&&(row1+sizeY1>=row2))&&((col1+sizeX1>=col2)&&(row1<=row2+sizeY2))){
			return false;
		}else{
			return true;
		}
		
	}
	
	@Override
	public Unit createUnitOnLevelWithIcon(long levelId,long iconId, int left, int top, int height, int width, String color, String type,
			String unitNumber, int col, int row,int  sizex,int sizey, Boolean rentable, String description) {
		Square square=createSquareWithIcon(iconId,left,top,height,width,color,type);
		Unit unit = new Unit();
		System.out.println("UnitService"+1);
		unit.setUnitNumber(unitNumber);
		unit.setCol(col);
		unit.setRow(row);
		unit.setSizex(sizex);
		unit.setSizey(sizey);
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
	public boolean editUnitInfo(long id,int left,int top, int height, int width, String color, String type,String unitNumber, int col, int row,int  sizex,int sizey,boolean rentable,String description) {
		try{
			Optional<Unit> unitOpt = getUnitById(id);
			if (unitOpt.isPresent()){
				
				Unit unit=unitOpt.get();
				unit.setCol(col);
				unit.setRow(row);
				unit.setSizex(sizex);
				unit.setSizey(sizey);
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
	public boolean updateRent(long unitId, Double rent){
		try{
			Optional<Unit> unitOpt = getUnitById(unitId);
			if (unitOpt.isPresent()){
				Unit unit=unitOpt.get();
				unit.setRent(rent);
				unitRepository.saveAndFlush(unit);
		
			}else{
			
			}
			}catch (Exception e){
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
