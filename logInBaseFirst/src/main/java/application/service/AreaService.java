package application.service;

import java.util.Optional;
import java.util.Set;

import application.entity.Area;
import application.entity.Square;


public interface AreaService {
	Area createArea(int col, int row,int  sizex,int sizey, int left, int top, int height, int width, String color, String type,
			String areaName, String description);

	Area createAreaOnBooking(long bookingId, int col, int row,int  sizex,int sizey, int left, int top, int height, int width, String color, String type,String areaName,
			String description);	

	Square createSquare( int left, int top, int height, int width, String color, String type);

	boolean editAreaInfo(long id,int col, int row,int  sizex,int sizey,int left,int top, int height, int width, String color, String type,String areaName,String description);

	boolean editSquareInfo(long id,int left, int top, int height,int width, String color,String type);

	boolean deleteArea(long id, long bookingId);

	Optional<Area> getAreaById(long id);

	Optional<Square> getSquareById(long id);

	Set<Area> getAllAreas();

	Set<Square> getAllSquares();

	Set<Area> getAreasByBookingId(long bookingId);

	boolean deleteAreasFromBooking(Set<Long> areaIds, long bookingId);

	boolean checkEventOrganiserByID(long requestPersonId, long bookingId);

	boolean passOverlapCheckTwoAreas(Area area1,Area area2);

	boolean passOverlapCheckTwoAreasOneWay(Area area1,Area area2);

	boolean passOverlapCheckWithExistingAreas(long bookingId, Area area);

	boolean addAreaOnBooking(long bookingId);

	boolean addDefaultIconOnBooking(long bookingId,String type);

	boolean addCustIconOnBooking(long bookingId,long iconId);

	Square createSquareWithIcon( long iconId,int left, int top, int height, int width, String color, String type);
	
	
	
	
	
	Set<Area> getAreasByUnitId(long unitId);

	boolean addAreaOnUnit(long unitId);

	boolean addDefaultIconOnUnit(long unitId,String type);

	boolean addCustIconOnUnit(long unitId,long iconId);
	
	boolean deleteAreaDefault(long id, long unitId);
	
	boolean passOverlapCheckWithExistingAreasUnit(long unitId, Area area) ;
	

}
