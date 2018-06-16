package at.fh.swenga.jpa.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.fh.swenga.jpa.dao.EmployeeDao;
import at.fh.swenga.jpa.model.EmployeeModel;

@Controller
public class EmployeeController {

	@Autowired
	EmployeeDao employeeDao;

	

	

	@RequestMapping("/searchEmployees")
	public String search(Model model, @RequestParam String searchString) {
		model.addAttribute("employees", employeeDao.searchEmployees(searchString));

		return "index";
	}

	

	

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}
	
	// when 403 is returned(do something you are not allowed to do) -> go to login page
	 
	
}