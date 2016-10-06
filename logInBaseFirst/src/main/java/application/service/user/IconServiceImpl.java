package application.service.user;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.Icon;
import application.repository.BuildingRepository;
import application.repository.ClientOrganisationRepository;
import application.repository.IconRepository;
import enumeration.IconType;
@Service
public class IconServiceImpl implements IconService {
	private final IconRepository iconRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(IconServiceImpl.class);
	
	@Autowired
	public IconServiceImpl(IconRepository iconRepository) {
		//super();
		this.iconRepository = iconRepository;
		
	}
	
	@Override
	public boolean createIconOnClientOrganisation(ClientOrganisation client, String iconType, String iconPath) {
		try{		
		Icon icon = new Icon();
		icon.setIconType(IconType.valueOf(iconType));
		icon.setIconPath(iconPath);
		iconRepository.saveAndFlush(icon);
		Set<Icon> icons=client.getIcons();
		icons.add(icon);
		client.setIcons(icons);
		return true;
	}catch(Exception e){
		return false;
	}
	
	}

	@Override
	public boolean editIcon(long iconId, String iconType, String iconPath) {
		try{
			Icon icon=iconRepository.findOne(iconId);
			icon.setIconPath(iconPath);
			icon.setIconType(IconType.valueOf(iconType));
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

	@Override
	public boolean deleteIconFromClientOrganisation(ClientOrganisation client, long iconId) {
		try{
			Icon icon=iconRepository.findOne(iconId);
			Set<Icon> icons=client.getIcons();
			icons.remove(icon);
			client.setIcons(icons);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Set<Icon> getAllIconFromClientOrganisation(ClientOrganisation client) {
		Set<Icon> icons=client.getIcons();
		return icons;
	}

}
