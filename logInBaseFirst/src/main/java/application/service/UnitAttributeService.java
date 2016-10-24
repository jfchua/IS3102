package application.service;

import java.util.Set;

import application.entity.ClientOrganisation;
import application.entity.Unit;
import application.entity.UnitAttributeType;
import application.entity.UnitAttributeValue;


public interface UnitAttributeService {
	boolean createAttributeTypeOnClient(ClientOrganisation client, String unitAttributeType);
	boolean attributeTypeExists(ClientOrganisation client, String unitAttributeType);
	boolean attributeTypeDefault(String unitAttributeType);
	boolean createAttributeValueOnType(String unitAttributeValue, String unitAttributeType);
	boolean attributeValueExists(String unitAttributeValue);
	boolean createUnitWithAttributeValues(Set<String> attributeValueStrings, Set<String> attributeTypeStrings);
	boolean isDefaultAttribute(String unitAttributeType);
	UnitAttributeType getAttributeTypeByString(ClientOrganisation client,String attributeType);
	boolean attributeValueExistsOnType(UnitAttributeType unitAttributeType,UnitAttributeValue unitAttributeValue);
	boolean attributeValueExistsOnClient(ClientOrganisation client,String attributeValue);
	UnitAttributeValue createAttributeValueOnTypeNUnit(String attributeValue, UnitAttributeType unitAttributeType,Unit unit);
	UnitAttributeValue getAttributeValueByStringOnClient(ClientOrganisation client,String attributeValue);
	boolean setAttributeValueonTypeNUnit(UnitAttributeValue unitAttributeValue,UnitAttributeType unitAttributeType,Unit unit);
	boolean setAttributeValueonUnit(UnitAttributeValue unitAttributeValue,Unit unit);
}
