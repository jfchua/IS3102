package application.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import application.service.UserService;

@Component
public class SendMessageFormValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageFormValidator.class);

	@Override
	public boolean supports(Class<?> classz) {
		// TODO Auto-generated method stub
		return classz.equals(SendMessageForm.class);
	}	

	@Override
	public void validate(Object target, Errors errors) {
		SendMessageForm form = (SendMessageForm)target;
		if (form.getMessage().trim().length() == 0 ) {
			errors.reject("Message contains only blanks");
		}

	}

}