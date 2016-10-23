package application.service;

import application.entity.ClientOrganisation;


public interface UnitAttributeService {
	boolean createUnitAttributeTypeOnClientOrganisation(ClientOrganisation client, String unitAttributeType);
}
