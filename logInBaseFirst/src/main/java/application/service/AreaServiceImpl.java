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
	private static final Logger LOGGER = LoggerFactory.getLogger(LevelServiceImpl.class);
	
	@Autowired
	public AreaServiceImpl(BookingApplRepository bookingRepository,AreaRepository areaRepository, SquareRepository squareRepository) {
		this.bookingRepository =bookingRepository;
		this.areaRepository = areaRepository;
		this.squareRepository=squareRepository;
		
	}
	
	@Override
	public Area createArea(int left, int top, int height, int width, String color, String type, String areaName,
			String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Area area = new Area();
		area.setSquare(square);
		area.setAreaName(areaName);
	
		area.setDescription(description);
		areaRepository.save(area); 
		area.setSquare(square);
		areaRepository.save(area);
		squareRepository.save(square);
	
		return area;
	}

	@Override
	public Area createAreaOnBooking(long bookingId, int left, int top, int height, int width, String color, String type,
			String areaName, String description) {
		Square square=createSquare(left,top,height,width,color,type);
		Area area = new Area();

		area.setAreaName(areaName);		
		area.setDescription(description);
		
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
	public boolean editAreaInfo(long id, int left, int top, int height, int width, String color, String type,
			String areaName, String description) {
		try{
			Optional<Area> areaOpt = getAreaById(id);
			if (areaOpt.isPresent()){
				
				Area area=areaOpt.get();
				
				area.setAreaName(areaName);
				area.setDescription(description);
			
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
			Optional<Area> areaOpt = getAreaById(id);
			BookingAppl booking = bookingRepository.findOne(bookingId);
		
			if(areaOpt.isPresent()){
				Area area= areaOpt.get();
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
			}
			}catch(Exception e){
				return false;
			}
			return true;
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
		LOGGER.debug("Getting all units");
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

}
