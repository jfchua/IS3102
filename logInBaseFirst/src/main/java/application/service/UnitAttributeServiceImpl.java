package application.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.ClientOrganisation;
import application.entity.Unit;
import application.entity.UnitAttributeType;
import application.entity.UnitAttributeValue;
import application.repository.ClientOrganisationRepository;
import application.repository.IconRepository;
import application.repository.LevelRepository;
import application.repository.SquareRepository;
import application.repository.UnitAttributeTypeRepository;
import application.repository.UnitAttributeValueRepository;
import application.repository.UnitRepository;
@Service
public class UnitAttributeServiceImpl implements UnitAttributeService {
	private final UnitRepository unitRepository;
	private final LevelRepository levelRepository;
	private final SquareRepository squareRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final UnitAttributeTypeRepository unitAttributeTypeRepository;
	private final UnitAttributeValueRepository unitAttributeValueRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(LevelServiceImpl.class);
	
	
	@Autowired
	public UnitAttributeServiceImpl(LevelRepository levelRepository,UnitRepository unitRepository, SquareRepository squareRepository,ClientOrganisationRepository clientOrganisationRepository,
			UnitAttributeTypeRepository unitAttributeTypeRepository,UnitAttributeValueRepository unitAttributeValueRepository) {
		this.levelRepository =levelRepository;
		this.unitRepository = unitRepository;
		this.squareRepository=squareRepository;
		this.clientOrganisationRepository=clientOrganisationRepository;
		this.unitAttributeTypeRepository=unitAttributeTypeRepository;
		this.unitAttributeValueRepository=unitAttributeValueRepository;
	}
	
	@Override
	public boolean createAttributeTypeOnClient(ClientOrganisation client, String unitAttributeType) {
		//unitAttributeType=unitAttributeType.toUpperCase();
		UnitAttributeType type= new UnitAttributeType();
		type.setAttributeType(unitAttributeType);
		unitAttributeTypeRepository.saveAndFlush(type);
		Set<UnitAttributeType> types=client.getUnitAttributeTypes();
		types.add(type);
		client.setUnitAttributeTypes(types);
		clientOrganisationRepository.saveAndFlush(client);
		return true;
	}
	
	@Override
	public boolean attributeTypeExists(ClientOrganisation client, String unitAttributeType) {
		unitAttributeType=unitAttributeType.toUpperCase();
		Set<UnitAttributeType> existingTypes=client.getUnitAttributeTypes();
		for(UnitAttributeType type:existingTypes){
			String existingTypeString=(type.getAttributeType()).toUpperCase();
			if(unitAttributeType.equals(existingTypeString)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean attributeTypeDefault(String unitAttributeType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean  createAttributeValueOnType(String unitAttributeValue, String unitAttributeType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean attributeValueExists(String unitAttributeValue) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public boolean createUnitWithAttributeValues(Set<String> attributeValueStrings, Set<String> attributeTypeStrings) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isDefaultAttribute(String unitAttributeType) {
		
		unitAttributeType=unitAttributeType.toUpperCase();
		
		if(unitAttributeType.equals("UNITNUMBER")||unitAttributeType.equals("LENGTH")
				||unitAttributeType.equals("WIDTH")||unitAttributeType.equals("RENT")
				||unitAttributeType.equals("DESCRIPTION")||unitAttributeType.equals("LEFT")
				||unitAttributeType.equals("TOP")||unitAttributeType.equals("LEVELNUM")
				||unitAttributeType.equals("BUILDINGNAME")
				)
			return true;
		else
			return false;
	}
	@Override
	public UnitAttributeType getAttributeTypeByString(ClientOrganisation client,String attributeType) {
		// TODO Auto-generated method stub
		attributeType=attributeType.toUpperCase();
		Set<UnitAttributeType> types=client.getUnitAttributeTypes();
		for(UnitAttributeType oneType:types){//LOOP THROUGH ALL TYPES OF CLIENT TO FIND THE TYPE BY STRING 
			String oneTypeString=(oneType.getAttributeType()).toUpperCase();
			if(attributeType.equals(oneTypeString)){
				return oneType;
			}
		}
		return null;
	}
	@Override
	public boolean attributeValueExistsOnType(UnitAttributeType unitAttributeType,UnitAttributeValue unitAttributeValue) {
		// TODO Auto-generated method stub
		Set<UnitAttributeValue> values=unitAttributeType.getUnitAttributeValues();
		if(values.contains(unitAttributeValue))
			return true;
		else
			return false;
	}
	@Override
	public boolean attributeValueExistsOnClient(ClientOrganisation client,String attributeValue) {
		// TODO Auto-generated method stub
		attributeValue=attributeValue.toUpperCase();
		Set<UnitAttributeType> types=client.getUnitAttributeTypes();
		for(UnitAttributeType type:types){
			Set<UnitAttributeValue> existingValues=type.getUnitAttributeValues();
			for(UnitAttributeValue value:existingValues){
				String existingValueString=(value.getAttributeValue()).toUpperCase();
				if(attributeValue.equals(existingValueString)){
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public UnitAttributeValue createAttributeValueOnTypeNUnit(String attributeValue, UnitAttributeType unitAttributeType,Unit unit) {
		// TODO Auto-generated method stub
		UnitAttributeValue value=new UnitAttributeValue();
		value.setAttributeValue(attributeValue);
		unitAttributeValueRepository.saveAndFlush(value);
		
		Set<UnitAttributeValue> valuesOfType=unitAttributeType.getUnitAttributeValues();
		valuesOfType.add(value);
		unitAttributeType.setUnitAttributeValues(valuesOfType);
		unitAttributeTypeRepository.saveAndFlush(unitAttributeType);
		
		Set<UnitAttributeValue> valuesOfUnit=unit.getUnitAttributeValues();
		valuesOfUnit.add(value);
		unit.setUnitAttributeValues(valuesOfUnit);
		unitRepository.saveAndFlush(unit);
		
		Set<Unit> units=new HashSet<Unit>();
		units.add(unit);
		value.setUnits(units);				
		value.setUnitAttributeType(unitAttributeType);
		unitAttributeValueRepository.saveAndFlush(value);
		return value;
	}
	@Override
	public UnitAttributeValue getAttributeValueByStringOnClient(ClientOrganisation client,String attributeValue) {
		// TODO Auto-generated method stub
		attributeValue=attributeValue.toUpperCase();
		Set<UnitAttributeType> types=client.getUnitAttributeTypes();
		for(UnitAttributeType type:types){
			Set<UnitAttributeValue> existingValues=type.getUnitAttributeValues();
			for(UnitAttributeValue value:existingValues){
				String existingValueString=(value.getAttributeValue()).toUpperCase();
				if(attributeValue.equals(existingValueString)){
					return value;
				}
			}
		}
		return null;
	}
	@Override
	public boolean setAttributeValueonTypeNUnit(UnitAttributeValue unitAttributeValue,UnitAttributeType unitAttributeType,Unit unit) {
		// TODO Auto-generated method stub	
		Set<UnitAttributeValue> valuesOfType=unitAttributeType.getUnitAttributeValues();
		valuesOfType.add(unitAttributeValue);
		unitAttributeType.setUnitAttributeValues(valuesOfType);
		unitAttributeTypeRepository.saveAndFlush(unitAttributeType);
		
		Set<UnitAttributeValue> valuesOfUnit=unit.getUnitAttributeValues();
		valuesOfUnit.add(unitAttributeValue);
		unit.setUnitAttributeValues(valuesOfUnit);
		unitRepository.saveAndFlush(unit);
		
		Set<Unit> units=unitAttributeValue.getUnits();
		units.add(unit);
		unitAttributeValue.setUnits(units);				
		unitAttributeValue.setUnitAttributeType(unitAttributeType);
		unitAttributeValueRepository.saveAndFlush(unitAttributeValue);
		return true;
		
	}
	@Override
	public boolean setAttributeValueonUnit(UnitAttributeValue unitAttributeValue,Unit unit) {
		// TODO Auto-generated method stub
	
		
		Set<UnitAttributeValue> valuesOfUnit=unit.getUnitAttributeValues();
		valuesOfUnit.add(unitAttributeValue);
		unit.setUnitAttributeValues(valuesOfUnit);
		unitRepository.saveAndFlush(unit);
		
		Set<Unit> units=unitAttributeValue.getUnits();
		units.add(unit);
		unitAttributeValue.setUnits(units);				
		unitAttributeValueRepository.saveAndFlush(unitAttributeValue);
		return true;
	}
}
