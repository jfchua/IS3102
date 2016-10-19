package application.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import application.service.UserService;

@Component
public class EventCreateFormValidator implements Validator {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventCreateFormValidator.class);
	public boolean supports(Class<?> classz) {
		// TODO Auto-generated method stub
		return classz.equals(EventCreateForm.class);
	}	
	//blank message error
	public void validate(Object target, Errors errors) {
		EventCreateForm form = (EventCreateForm)target;
		if (form.getEvent_description().trim().length() == 0 ) {
			errors.reject("Event description cannot be blank");
		}
	}
}