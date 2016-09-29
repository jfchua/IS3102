package application.service.user;

import java.util.Optional;
import java.util.Set;

import application.domain.Area;
import application.domain.Square;

public interface AreaService {
		Area createArea(int left, int top, int height, int width, String color, String type,
				String areaName, String description);
	
		Area createAreaOnEvent(long eventId, int left, int top, int height, int width, String color, String type,String areaName,
				String description);	
		
		Square createSquare( int left, int top, int height, int width, String color, String type);
		
		boolean editAreaInfo(long id,int left,int top, int height, int width, String color, String type,String areaName,String description);
		
		boolean editSquareInfo(long id,int left, int top, int height,int width, String color,String type);
		
		boolean deleteArea(long id, long eventId);
		
		Optional<Area> getAreaById(long id);
		
		Optional<Square> getSquareById(long id);
		
		Set<Area> getAllAreas();
		
		Set<Square> getAllSquares();
		
		Set<Area> getAreasByEventId(long eventId);
		
		boolean deleteAreasFromEvent(Set<Long> areaIds, long eventId);
}
