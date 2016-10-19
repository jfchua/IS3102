package application.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import application.entity.ClientOrganisation;
import application.entity.Icon;
import application.enumeration.IconType;
import application.exception.IconNotFoundException;
import application.exception.InvalidIconException;

public interface IconService {
	boolean createIconOnClientOrganisation(ClientOrganisation client, String iconType, String iconPath) throws InvalidIconException;
	boolean editIcon(ClientOrganisation client,long iconId,String iconPath) throws IconNotFoundException, InvalidIconException;
	boolean deleteIconFromClientOrganisation(ClientOrganisation client, long iconId) throws IconNotFoundException;
	Set<Icon> getAllIconFromClientOrganisation(ClientOrganisation client);
}
