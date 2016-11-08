package application.service;

import java.util.Set;

import application.entity.*;

import java.util.Optional;

public interface UnitService {
	
/*		Unit createUnit(int left, int top, int height, int width, String color, String type,
			String unitNumber, int dimensionLength, int dimensionWidth, Boolean rentable, String description);*/
	
		Unit createUnitOnLevel(long levelId, int left, int top, int height, int width, String color, String type,String unitNumber,
				int col, int row,int  sizex,int sizey, Boolean rentable, String description);	
		Unit createUnitOnLevelWithIcon(long levelId,long iconId, int left, int top, int height, int width, String color, String type,String unitNumber,
				int col, int row,int  sizex,int sizey, Boolean rentable, String description);	
		
		Square createSquare( int left, int top, int height, int width, String color, String type);
		
		Square createSquareWithIcon( long iconId,int left, int top, int height, int width, String color, String type);
		
		boolean editUnitInfo(long id,int left,int top, int height, int width, String color, String type,String unitNumber, int col, int row,int  sizex,int sizey,boolean rentable,String description);
		
		boolean editSquareInfo(long id,int left, int top, int height,int width, String color,String type);
		
		boolean deleteUnit(long id, long levelId);
		
		Optional<Unit> getUnitById(long id);
		
		Optional<Square> getSquareById(long id);
		
		Set<Unit> getAllUnits();
		
		Set<Square> getAllSquares();
		
		Set<Unit> getUnitsByLevelId(long levelId);
		
		boolean deleteUnitsFromLevel(Set<Long> unitIds, long levelId);
		
		boolean updateRent(long unitId, Double rent);
		
		 boolean passOverlapCheckTwoUnits(Unit unit1,Unit unit2);
		 
		 boolean passOverlapCheckTwoUnitsOneWay(Unit unit1,Unit unit2);
		 
		 boolean passOverlapCheckWithExistingUnits(long levelId, Unit unit);
		 
		 boolean addUnitOnLevel(long levelId);
		 
		 boolean addDefaultIconOnLevel(long levelId,String type);
		 
		 boolean addCustIconOnLevel(long levelId,long iconId);
		 
		 boolean checkBookings(long unitId);
		 
		 Unit uploadUnitOnLevel(long levelId, int left, int top, int height, int width, String color, String type,
					String unitNumber, int col, int row,int  sizex,int sizey,Boolean rentable, String description) ;
		
			
}

