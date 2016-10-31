package application.service;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.*;
import application.repository.*;

@Service
public class AreaServiceImpl implements AreaService {
	private final AreaRepository areaRepository;
	private final BookingApplRepository bookingRepository;
	private final SquareRepository squareRepository;
	private final IconRepository iconRepository;
	private final UnitRepository unitRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(AreaServiceImpl.class);
	private static int scale=2;
	@Autowired
	public AreaServiceImpl(BookingApplRepository bookingRepository,AreaRepository areaRepository, SquareRepository squareRepository,IconRepository iconRepository,UnitRepository unitRepository) {
		this.bookingRepository =bookingRepository;
		this.areaRepository = areaRepository;
		this.squareRepository=squareRepository;
		this.iconRepository=iconRepository;
		this.unitRepository=unitRepository;
	}
	
	@Override
	public Area createArea(int col, int row,int  sizex,int sizey,int left, int top, int height, int width, String color, String type, String areaName,
			String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Area area = new Area();
		area.setSquare(square);
		area.setAreaName(areaName);
		area.setCol(col);
		area.setRow(row);
		area.setSizeX(sizex);
		area.setSizeY(sizey);
		
		area.setDescription(description);
		areaRepository.save(area); 
		area.setSquare(square);
		areaRepository.save(area);
		squareRepository.save(square);
	
		return area;
	}

	@Override
	public Area createAreaOnBooking(long bookingId, int col, int row,int  sizex,int sizey,int left, int top, int height, int width, String color, String type,
			String areaName, String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Area area = new Area();

		area.setAreaName(areaName);		
		area.setDescription(description);
		area.setCol(col);
		area.setRow(row);
		area.setSizeX(sizex);
		area.setSizeY(sizey);
		
		areaRepository.saveAndFlush(area);
		area.setSquare(square);
		areaRepository.saveAndFlush(area);
	
		BookingAppl booking=bookingRepository.findOne(bookingId); 
		Set<Area> areas = booking.getAreas();
		
		areas.add(area);
		booking.setAreas(areas);
		
		bookingRepository.saveAndFlush(booking);
		

		
		areaRepository.saveAndFlush(area);
		
		return area;
	}

	@Override
	public Square createSquare(int left, int top, int height, int width, String color, String type) {
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
	public boolean editAreaInfo(long id, int col, int row,int  sizex,int sizey,int left, int top, int height, int width, String color, String type,
			String areaName, String description) {
		try{
			Optional<Area> areaOpt = getAreaById(id);
			if (areaOpt.isPresent()){
				
				Area area=areaOpt.get();
				
				area.setAreaName(areaName);
				area.setDescription(description);
				area.setCol(col);
				area.setRow(row);
				area.setSizeX(sizex);
				area.setSizeY(sizey);
			
				long SquareId=area.getSquare().getId();
				if(editSquareInfo(SquareId,left, top, height, width,  color, type)){
					System.out.println("Test: Square is updated");
				}else{
					System.out.println("AreaServiceImpl: save square failed");
					return false;
				}
				areaRepository.saveAndFlush(area);
				
			}
			}catch (Exception e){
				return false;
			}
			return true;
	}

	@Override
	public boolean editSquareInfo(long id, int left, int top, int height, int width, String color, String type) {
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
	public boolean deleteArea(long id, long bookingId) {
		try{
			Area area=areaRepository.findOne(id);
			BookingAppl booking = bookingRepository.findOne(bookingId);
		
			
			
				Square square=area.getSquare();
				area.setSquare(null);
				Set<Area> areas=booking.getAreas();
				areas.remove(area);
				booking.setAreas(areas);
				bookingRepository.saveAndFlush(booking);
				
				areaRepository.delete(area);
				areaRepository.flush();
				
				squareRepository.delete(square);
				squareRepository.flush();
				return true;
			
			}catch(Exception e){
				System.out.println("renturn false for deleArea");
				return false;
			}
		
	}

	@Override
	public Optional<Area> getAreaById(long id) {
		LOGGER.debug("Getting area={}", id);
		return Optional.ofNullable(areaRepository.findOne(id));
	}

	@Override
	public Optional<Square> getSquareById(long id) {
		LOGGER.debug("Getting square={}", id);
		return Optional.ofNullable(squareRepository.findOne(id));
	}

	@Override
	public Set<Area> getAllAreas() {
		LOGGER.debug("Getting all areas");
		return areaRepository.fetchAllAreas();
	}

	@Override
	public Set<Square> getAllSquares() {
		LOGGER.debug("Getting all squares");
		return  squareRepository.fetchAllSquares();
	}

	@Override
	public Set<Area> getAreasByBookingId(long bookingId) {
		System.out.println("AreaServiceTest: getAreas of booking "+bookingId);
		LOGGER.debug("Getting all areas by Id"+bookingId);
		BookingAppl booking=bookingRepository.getOne(bookingId);  
		Set<Area> areas = booking.getAreas();
		return  areas;
	}

	@Override
	public boolean deleteAreasFromBooking(Set<Long> areaIds, long bookingId) {
		Set<Area> areas=getAreasByBookingId(bookingId);
	
		//get a set of existing areas ids
		Set<Long> existingAreaIds=new HashSet<Long>();
		for(Area area: areas){
			Long k=area.getId();
			existingAreaIds.add(k);
		}
		for (Long existId : existingAreaIds) {
			
			
			if(areaIds.contains(existId)){
				System.out.println("AreaServiceTest: area "+existId+" exists.");
			}else{
				if(deleteArea(existId,bookingId)){
					System.out.println("Test: area "+existId+" is deleted.");
					
				}else{
					System.out.println("Test: Error, area "+existId+" is not deleted.");
					return false;
				}
			}
		}
			
		
	System.out.println("AreaServiceTest: finish deleting");
		return true;
	}

	@Override
	public boolean checkEventOrganiserByID(long requestPersonId, long bookingId) {
		BookingAppl booking = bookingRepository.findOne(bookingId);
		long bookingOwnerId=booking.getEvent().getEventOrg().getId();
		System.out.println(requestPersonId==bookingOwnerId );
		return requestPersonId==bookingOwnerId;
	}
	
	
	@Override
	public  boolean passOverlapCheckWithExistingAreas(long bookingId, Area area) {
		//GET LEVEL
		BookingAppl booking=bookingRepository.getOne(bookingId);
		Set<Area> areas=booking.getAreas();
		for (Area oneExistArea:areas){
			if(!passOverlapCheckTwoAreas(area,oneExistArea)){
				return false;
			}
		} //END FOR LOOP FOR ALL EXISTING UNITS ON LEVEL
		return true;
	}
	
	@Override
	public boolean passOverlapCheckTwoAreas(Area area1,Area area2) {
		if(passOverlapCheckTwoAreasOneWay(area1,area2)&&passOverlapCheckTwoAreasOneWay(area2,area1)){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public boolean passOverlapCheckTwoAreasOneWay(Area area1,Area area2) {
		int col1=area1.getCol();
		int row1=area1.getRow();
		int sizeX1=area1.getSizeX();
		int sizeY1=area1.getSizeY();
		
		int col2=area2.getCol();
		int row2=area2.getRow();
		int sizeX2=area2.getSizeX();
		int sizeY2=area2.getSizeY();
		
		if( ((col1+sizeX1>=col2)&&(row1+sizeY1>=row2))&&((col1<=col2+sizeX2)&&(row1<=row2+sizeY2))){
			return false;
		}else if (((col1<=col2+sizeX2)&&(row1+sizeY1>=row2))&&((col1+sizeX1>=col2)&&(row1<=row2+sizeY2))){
			return false;
		}else{
			return true;
		}
		
	}
	
	
	@Override
	public boolean addAreaOnBooking(long bookingId) {
		//GET LEVEL
		BookingAppl booking=bookingRepository.getOne(bookingId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int unitLength=booking.getUnit().getSizex()*scale;
		int unitWidth=booking.getUnit().getSizey()*scale;
		int sizex=Math.min(unitLength/10, unitWidth/10);
		int sizey=Math.min(unitLength/10, unitWidth/10);
		int col=0;
		int row=0;
		Area areaTemp=new Area();
		boolean keepChecking=true;
		do{
			areaTemp.setSizeX(sizex);
			areaTemp.setSizeY(sizey);
			areaTemp.setCol(col);
			areaTemp.setRow(row);
			if(passOverlapCheckWithExistingAreas(bookingId,areaTemp)){
				keepChecking=false;
			}else{
				
				int temp1=1 + (int)(Math.random() * sizex);
				int temp2=1 +(int)(Math.random() * sizey);
				sizex=Math.min(temp1,temp2);
				sizey=Math.min(temp1,temp2);
				col=0 + (int)(Math.random() * (unitLength-sizex));
				row=0 + (int)(Math.random() * (unitWidth-sizey));
			}
		}while(keepChecking);
		
		
	
		
		Square square=createSquare(100,100,100,100,"coral","./svg/rect.svg");
		areaTemp.setAreaName("#Name");
		areaTemp.setDescription("# There is no description yet");
		areaRepository.saveAndFlush(areaTemp);
		areaTemp.setSquare(square);
		areaRepository.saveAndFlush(areaTemp);
		Set<Area> areas=booking.getAreas();
		areas.add(areaTemp);
		booking.setAreas(areas);
		bookingRepository.saveAndFlush(booking);
		areaTemp.setBooking(booking);
		areaRepository.saveAndFlush(areaTemp);
		return true;
	}
	
	@Override
	public boolean addDefaultIconOnBooking(long bookingId,String type) {
		//GET LEVEL
		BookingAppl booking=bookingRepository.getOne(bookingId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int unitLength=booking.getUnit().getSizex()*scale;
		int unitWidth=booking.getUnit().getSizey()*scale;
		int sizex=Math.min(unitLength/20, unitWidth/20);
		int sizey=Math.min(unitLength/20+1, unitWidth/20+1);
		int col=0;
		int row=0;
		Area areaTemp=new Area();
		boolean keepChecking=true;
		do{
			areaTemp.setSizeX(sizex);
			areaTemp.setSizeY(sizey);
			areaTemp.setCol(col);
			areaTemp.setRow(row);
			if(passOverlapCheckWithExistingAreas(bookingId,areaTemp)){
				keepChecking=false;
			}else{
				col=0 + (int)(Math.random() * (unitLength-sizex));
				row=0 + (int)(Math.random() * (unitWidth-sizey));
			}
		}while(keepChecking);
		Square square=createSquare(100,100,100,100,"transparent",type);
		areaTemp.setAreaName("");	
		areaTemp.setDescription("# There is no description yet");
		areaRepository.saveAndFlush(areaTemp);
		areaTemp.setSquare(square);
		areaRepository.saveAndFlush(areaTemp);
		Set<Area> areas=booking.getAreas();
		areas.add(areaTemp);
		booking.setAreas(areas);
		bookingRepository.saveAndFlush(booking);
		areaTemp.setBooking(booking);
		areaRepository.saveAndFlush(areaTemp);	
		return true;
	}
	
	
	@Override
	public boolean addCustIconOnBooking(long bookingId,long iconId) {
		//GET LEVEL
		BookingAppl booking=bookingRepository.getOne(bookingId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int unitLength=booking.getUnit().getSizex()*scale;
		int unitWidth=booking.getUnit().getSizey()*scale;
		int sizex=Math.min(unitLength/15, unitWidth/15);
		int sizey=Math.min(unitLength/15+1, unitWidth/15+1);
		int col=0;
		int row=0;
		Area areaTemp=new Area();
		boolean keepChecking=true;
		do{
			areaTemp.setSizeX(sizex);
			areaTemp.setSizeY(sizey);
			areaTemp.setCol(col);
			areaTemp.setRow(row);
			if(passOverlapCheckWithExistingAreas(bookingId,areaTemp)){
				keepChecking=false;
			}else{
				col=0 + (int)(Math.random() * (unitLength-sizex));
				row=0 + (int)(Math.random() * (unitWidth-sizey));
			}
		}while(keepChecking);
		Square square=createSquareWithIcon(iconId,100,100,100,100,"transparent","");
		areaTemp.setAreaName("");
		//hahahaha
		areaTemp.setDescription("# There is no description yet");
		areaRepository.saveAndFlush(areaTemp);
		areaTemp.setSquare(square);
		areaRepository.saveAndFlush(areaTemp);
		Set<Area> areas=booking.getAreas();
		areas.add(areaTemp);
		booking.setAreas(areas);
		bookingRepository.saveAndFlush(booking);
		areaTemp.setBooking(booking);
		areaRepository.saveAndFlush(areaTemp);	
		return true;
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
	public boolean addAreaOnUnit(long unitId) {
		//GET LEVEL
		Unit unit=unitRepository.getOne(unitId);
		System.out.println("AreaServiceImpl: addAreaDefault 458");
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int unitLength=unit.getSizex()*scale;
		int unitWidth=unit.getSizey()*scale;
		int sizex=Math.min(unitLength/10, unitWidth/10);
		int sizey=Math.min(unitLength/10, unitWidth/10);
		int col=0;
		int row=0;
		Area areaTemp=new Area();
		boolean keepChecking=true;
		do{
			System.out.println("AreaServiceImpl: addAreaDefault 469");
			areaTemp.setSizeX(sizex);
			areaTemp.setSizeY(sizey);
			areaTemp.setCol(col);
			areaTemp.setRow(row);
			System.out.println("AreaServiceImpl: addAreaDefault474");
			if(passOverlapCheckWithExistingAreasUnit(unitId,areaTemp)){
				keepChecking=false;
			}else{
				System.out.println("AreaServiceImpl: addAreaDefault477");
				int temp1=1 + (int)(Math.random() * sizex);
				int temp2=1 +(int)(Math.random() * sizey);
				sizex=Math.min(temp1,temp2);
				sizey=Math.min(temp1,temp2);
				col=0 + (int)(Math.random() * (unitLength-sizex));
				row=0 + (int)(Math.random() * (unitWidth-sizey));
			}
		}while(keepChecking);
		
		System.out.println("AreaServiceImpl: addAreaDefault487");
	
		
		Square square=createSquare(100,100,100,100,"coral","./svg/rect.svg");
		areaTemp.setAreaName("#Name");
		areaTemp.setDescription("# There is no description yet");
		System.out.println("AreaServiceImpl: addAreaDefault493");
		areaRepository.saveAndFlush(areaTemp);
		areaTemp.setSquare(square);
		System.out.println("AreaServiceImpl: addAreaDefault496");
		areaRepository.saveAndFlush(areaTemp);
		Set<Area> areas=unit.getAreas();
		areas.add(areaTemp);
		unit.setAreas(areas);
		System.out.println("AreaServiceImpl: addAreaDefault 501");
		unitRepository.saveAndFlush(unit);
		//areaTemp.setUnit(unit);
		System.out.println("AreaServiceImpl: addAreaDefault504");
		//areaRepository.saveAndFlush(areaTemp);
		//System.out.println("AreaServiceImpl: addAreaDefault506");
		return true;
	}
	
	@Override
	public boolean addDefaultIconOnUnit(long unitId,String type) {
		//GET LEVEL
		Unit unit=unitRepository.getOne(unitId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		System.out.println("AreaServiceImpl: addAreaDefault515");
		int unitLength=unit.getSizex()*scale;
		int unitWidth=unit.getSizey()*scale;
		int sizex=Math.min(unitLength/20, unitWidth/20);
		int sizey=Math.min(unitLength/20+1, unitWidth/20+1);
		int col=0;
		int row=0;
		Area areaTemp=new Area();
		boolean keepChecking=true;
		System.out.println("AreaServiceImpl: addAreaDefault524");
		do{
			areaTemp.setSizeX(sizex);
			areaTemp.setSizeY(sizey);
			areaTemp.setCol(col);
			areaTemp.setRow(row);
			System.out.println("AreaServiceImpl: addAreaDefault530");
			if(passOverlapCheckWithExistingAreasUnit(unitId,areaTemp)){
				keepChecking=false;
			}else{
				col=0 + (int)(Math.random() * (unitLength-sizex));
				row=0 + (int)(Math.random() * (unitWidth-sizey));
			}
		}while(keepChecking);
		Square square=createSquare(100,100,100,100,"transparent",type);
		System.out.println("AreaServiceImpl: addAreaDefault539");
		areaTemp.setAreaName("");	
		areaTemp.setDescription("# There is no description yet");
		areaRepository.saveAndFlush(areaTemp);
		System.out.println("AreaServiceImpl: addAreaDefault543");
		areaTemp.setSquare(square);
		areaRepository.saveAndFlush(areaTemp);
		System.out.println("AreaServiceImpl: addAreaDefault546");
		Set<Area> areas=unit.getAreas();
		areas.add(areaTemp);
		unit.setAreas(areas);
		unitRepository.saveAndFlush(unit);
		System.out.println("AreaServiceImpl: addAreaDefault551");
		//areaTemp.setUnit(unit);
		//areaRepository.saveAndFlush(areaTemp);	
		return true;
	}
	
	
	@Override
	public boolean addCustIconOnUnit(long unitId,long iconId) {
		//GET LEVEL
		Unit unit=unitRepository.getOne(unitId);
		//GET DIMENSION AND POSITION THAT WILL NOT OVERLAP WIHT EXISTING UNITS ON THE LEVEL
		int unitLength=unit.getSizex()*scale;
		int unitWidth=unit.getSizey()*scale;
		int sizex=Math.min(unitLength/15, unitWidth/15);
		int sizey=Math.min(unitLength/15+1, unitWidth/15+1);
		int col=0;
		int row=0;
		Area areaTemp=new Area();
		boolean keepChecking=true;
		do{
			areaTemp.setSizeX(sizex);
			areaTemp.setSizeY(sizey);
			areaTemp.setCol(col);
			areaTemp.setRow(row);
			if(passOverlapCheckWithExistingAreasUnit(unitId,areaTemp)){
				keepChecking=false;
			}else{
				col=0 + (int)(Math.random() * (unitLength-sizex));
				row=0 + (int)(Math.random() * (unitWidth-sizey));
			}
		}while(keepChecking);
		Square square=createSquareWithIcon(iconId,100,100,100,100,"transparent","");
		areaTemp.setAreaName("");
		//hahahaha
		areaTemp.setDescription("# There is no description yet");
		areaRepository.saveAndFlush(areaTemp);
		areaTemp.setSquare(square);
		areaRepository.saveAndFlush(areaTemp);
		Set<Area> areas=unit.getAreas();
		areas.add(areaTemp);
		unit.setAreas(areas);
		unitRepository.saveAndFlush(unit);
		//areaTemp.setUnit(unit);
		//areaRepository.saveAndFlush(areaTemp);	
		return true;
	}
	
	
	@Override
	public Set<Area> getAreasByUnitId(long unitId) {

		Unit unit=unitRepository.getOne(unitId);  
		Set<Area> areas = unit.getAreas();
		return  areas;
	}
	
	
	@Override
	public boolean deleteAreaDefault(long id, long unitId) {
		try{
			Area area=areaRepository.findOne(id);
			Unit unit = unitRepository.findOne(unitId);
		
			
			
				Square square=area.getSquare();
				area.setSquare(null);
				Set<Area> areas=unit.getAreas();
				areas.remove(area);
				unit.setAreas(areas);
				unitRepository.saveAndFlush(unit);
				
				areaRepository.delete(area);
				areaRepository.flush();
				
				squareRepository.delete(square);
				squareRepository.flush();
				return true;
			
			}catch(Exception e){
				System.out.println("renturn false for deleArea");
				return false;
			}
		
	}
	
	@Override
	public  boolean passOverlapCheckWithExistingAreasUnit(long unitId, Area area) {
		
		Unit unit=unitRepository.getOne(unitId);
		Set<Area> areas=unit.getAreas();
		for (Area oneExistArea:areas){
			if(!passOverlapCheckTwoAreas(area,oneExistArea)){
				return false;
			}
		} //END FOR LOOP FOR ALL EXISTING UNITS ON LEVEL
		return true;
	}

	
}
