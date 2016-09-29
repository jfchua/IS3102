package application.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import application.domain.EventCreateForm;

import application.domain.validator.EventCreateFormValidator;

import application.service.user.EventService;

import application.service.user.UserService;

@Controller
public class EventController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private final EventService eventService;
	private final UserService userService;
	private final EventCreateFormValidator eventCreateFormValidator;

	@Autowired
	public EventController(EventService eventService, UserService userService, EventCreateFormValidator eventCreateFormValidator) {
		super();
		this.eventService = eventService;
		this.userService = userService;
		this.eventCreateFormValidator = eventCreateFormValidator;
	}
	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(eventCreateFormValidator);
	}
	@PreAuthorize("hasAuthority('EVENT')")
	@RequestMapping(value = "/event/create", method = RequestMethod.GET)
	public ModelAndView createEvent() {
		LOGGER.debug("Getting createEvent page");
		System.out.println("Now at event page...");
		//return "sendMessage";
		return new ModelAndView("event_create", "users", userService.getAllUsers());
	}

	@RequestMapping("/event/view")
	public ModelAndView getViewEventsPage() {
		LOGGER.debug("Getting view Events");
		return new ModelAndView("event_view", "events", eventService.getAllEvents());
	}

	
	@RequestMapping(value = "/event/create", method = RequestMethod.POST)
	public String handleCreateEventForm(@Valid @ModelAttribute("form") EventCreateForm form, BindingResult bindingResult) {
		/*messageService.sendMessage(form);
		return "redirect:/users";*/


		if (bindingResult.hasErrors()) {
			//System.out.println("This is the title: " +form.getEvent_title());
			System.out.println("There is error with the binding result");
			// failed validation
			return "home";
		}
		try {
			System.out.println("Trying to create Event... and save it...");
			eventService.createEvent(form);
			
		} catch (DataIntegrityViolationException e) {
			// probably email already exists - very rare case when multiple admins are adding same user
			// at the same time and form validation has passed for more than one of them.
			LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
			return "home";
		}
		// ok, redirect
		return "home";
	}
}
