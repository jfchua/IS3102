package application.service.user;

import java.util.Set;
import java.util.Optional;

import application.domain.*;
//import application.domain.Maintenance;

public interface UnitService {
	
		Unit createUnit(int left, int top, int height, int width, String color, String type,
			String unitNumber, int dimensionLength, int dimensionWidth, Boolean rentable, String description);
	
		Unit createUnitOnLevel(long levelId, int left, int top, int height, int width, String color, String type,String unitNumber,
				int dimensionLength, int dimensionWidth, Boolean rentable, String description);	
		Unit createUnitOnLevelWithIcon(long levelId,long iconId, int left, int top, int height, int width, String color, String type,String unitNumber,
				int dimensionLength, int dimensionWidth, Boolean rentable, String description);	
		
		Square createSquare( int left, int top, int height, int width, String color, String type);
		Square createSquareWithIcon( long iconId,int left, int top, int height, int width, String color, String type);
		
		boolean editUnitInfo(long id,int left,int top, int height, int width, String color, String type,String unitNumber, int dimensionLength, int dimensionWidth,boolean rentable,String description);
		
		boolean editSquareInfo(long id,int left, int top, int height,int width, String color,String type);
		
		boolean deleteUnit(long id, long levelId);
		
		Optional<Unit> getUnitById(long id);
		
		Optional<Square> getSquareById(long id);
		
		Set<Unit> getAllUnits();
		
		Set<Square> getAllSquares();
		
		Set<Unit> getUnitsByLevelId(long levelId);
		
		boolean deleteUnitsFromLevel(Set<Long> unitIds, long levelId);
	

}

