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
	 private final ClientOrganisationRepository clientOrganisationRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(IconServiceImpl.class);
	
	@Autowired
	public IconServiceImpl(IconRepository iconRepository, ClientOrganisationRepository clientOrganisationRepository) {
		//super();
		this.iconRepository = iconRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
		
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
		clientOrganisationRepository.saveAndFlush(client);
		return true;
	}catch(Exception e){
		return false;
	}
	
	}

	@Override
	public boolean editIcon(ClientOrganisation client,long iconId, String iconPath) {
		try{
			Set<Icon> icons=client.getIcons();
			Icon icon=iconRepository.findOne(iconId);
			if(icons.contains(icon)){
				icon.setIconPath(iconPath);
				iconRepository.saveAndFlush(icon);
				
				return true;
			}else{
			System.out.println("The icon is not from this client organisation");
			return false;
			}
		}catch(Exception e){
			System.out.println("IconService: Error at editing Icon"+iconId);
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
			iconRepository.delete(icon);
			System.out.println("icon deleted");
			iconRepository.flush();
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
