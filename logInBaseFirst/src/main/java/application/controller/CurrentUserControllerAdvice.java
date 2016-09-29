package application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import application.domain.CurrentUser;

@ControllerAdvice
public class CurrentUserControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserControllerAdvice.class);

	@ModelAttribute("CurrentUser")
	public CurrentUser getCurrentUser(Authentication authentication) {
/*		if ( authentication != null)
			System.out.println("Getting current user with name: " +authentication.getName() + "at currentCtrlAdvice");
		else
			System.out.println("Null authentication at currentusercontrolelradvice");
		return (authentication == null) ? null : (CurrentUser) authentication.getPrincipal();
	}*/
		 return (authentication == null) ? null : (CurrentUser) authentication.getPrincipal();
	}
}
