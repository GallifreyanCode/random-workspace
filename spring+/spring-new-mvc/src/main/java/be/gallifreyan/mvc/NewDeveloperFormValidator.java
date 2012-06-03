package be.gallifreyan.mvc;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import be.gallifreyan.persistence.entity.Developer;

@Component
public class NewDeveloperFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		/* Validate the instances */
		return Developer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Developer developer = (Developer) target;
		
		if (developer.getName().isEmpty()) {
			System.out.println("jsdfjsdjdddddddddddddddddddddddfsjdfjsdf");
			errors.rejectValue("name", "required.name");
		}
	}
}
