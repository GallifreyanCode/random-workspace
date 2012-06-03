package be.gallifreyan.mvc;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import be.gallifreyan.persistence.entity.Developer;
import be.gallifreyan.persistence.service.DeveloperService;


@Controller
@RequestMapping("/new")
public class NewDeveloperFormController {
	
	@Inject
	NewDeveloperFormValidator newDeveloperFormValidator;
	
	@Inject
	DeveloperService developerService;
	
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(
		@ModelAttribute("newDeveloperCommand") Developer newDeveloper,
		BindingResult result, SessionStatus status) {
 
		newDeveloperFormValidator.validate(newDeveloper, result);
 
		if (result.hasErrors()) {
			//if validator failed
			return "NewDeveloper";
		} else {
			
			Developer developerSave = new Developer();
			
			//personToSave.setFirstname(newDog.getFirstName());
			developerSave.setName(newDeveloper.getName());
			developerService.save(developerSave);
			
			status.setComplete();
			//form success
			return "NewDeveloper";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model){
 
		Developer newDeveloper = new Developer();
		
		//command object
		model.addAttribute("newDeveloperCommand", newDeveloper);
 
		//return form view
		return "NewDeveloper";
	}

}
