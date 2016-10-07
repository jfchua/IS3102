package application.service.user;

import java.util.Set;

import org.springframework.stereotype.Service;

import application.domain.ClientOrganisation;
import application.domain.Icon;
import enumeration.IconType;

public interface IconService {
	boolean createIconOnClientOrganisation(ClientOrganisation client, String iconType, String iconPath);
	boolean editIcon(long iconId,String iconType,String iconPath);
	boolean deleteIconFromClientOrganisation(ClientOrganisation client, long iconId);
	Set<Icon> getAllIconFromClientOrganisation(ClientOrganisation client);
}
