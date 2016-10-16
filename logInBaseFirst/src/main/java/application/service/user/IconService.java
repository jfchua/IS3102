package application.service.user;

import java.util.Set;

import org.springframework.stereotype.Service;

import application.domain.ClientOrganisation;
import application.domain.Icon;
import application.exception.IconNotFoundException;
import application.exception.InvalidIconException;
import enumeration.IconType;

public interface IconService {
	boolean createIconOnClientOrganisation(ClientOrganisation client, String iconType, String iconPath) throws InvalidIconException;
	boolean editIcon(ClientOrganisation client,long iconId,String iconPath) throws IconNotFoundException, InvalidIconException;
	boolean deleteIconFromClientOrganisation(ClientOrganisation client, long iconId) throws IconNotFoundException;
	Set<Icon> getAllIconFromClientOrganisation(ClientOrganisation client);
}
