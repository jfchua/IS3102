package application.service;

import java.util.Optional;
import java.util.Set;

import application.entity.Area;
import application.entity.Square;

public interface AreaService {
		Area createArea(int left, int top, int height, int width, String color, String type,
				String areaName, String description);
	
		Area createAreaOnBooking(long bookingId, int left, int top, int height, int width, String color, String type,String areaName,
				String description);	
		
		Square createSquare( int left, int top, int height, int width, String color, String type);
		
		boolean editAreaInfo(long id,int left,int top, int height, int width, String color, String type,String areaName,String description);
		
		boolean editSquareInfo(long id,int left, int top, int height,int width, String color,String type);
		
		boolean deleteArea(long id, long bookingId);
		
		Optional<Area> getAreaById(long id);
		
		Optional<Square> getSquareById(long id);
		
		Set<Area> getAllAreas();
		
		Set<Square> getAllSquares();
		
		Set<Area> getAreasByBookingId(long bookingId);
		
		boolean deleteAreasFromBooking(Set<Long> areaIds, long bookingId);
		
		boolean checkEventOrganiserByID(long requestPersonId, long bookingId);
}
