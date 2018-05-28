package at.fh.swenga.jpa.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.EmployeeDao;
import at.fh.swenga.jpa.model.EmployeeModel;

@Controller
public class EmployeeController {

	@Autowired
	EmployeeDao employeeDao;

	@RequestMapping(value = { "/", "list" })
	public String index(Model model) {

		List<EmployeeModel> employees = employeeDao.getEmployees();

		model.addAttribute("employees", employees);
		return "index";
	}

	@RequestMapping("/fillData")
	@Transactional
	public String fillData(Model model) {

		Date now = new Date();

		EmployeeModel p1 = new EmployeeModel("Johann", "Blauensteiner", now);
		employeeDao.persist(p1);

		EmployeeModel p2 = new EmployeeModel("Max", "Mustermann", now);
		employeeDao.persist(p2);

		EmployeeModel p3 = new EmployeeModel("Jane", "Doe", now);
		employeeDao.persist(p3);

		EmployeeModel p4 = new EmployeeModel("Franz", "Wachter", now);
		employeeDao.persist(p4);

		return "forward:list";
	}

	@RequestMapping("/searchEmployees")
	public String search(Model model, @RequestParam String searchString) {
		model.addAttribute("employees", employeeDao.searchEmployees(searchString));

		return "index";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/delete")
	public String deleteData(Model model, @RequestParam int id) {
		employeeDao.delete(id);

		return "forward:list";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String handleLogin() {
		return "login";
	}

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}
	
	// when 403 is returned(do something you are not allowed to do) -> go to login page
	/*
	 * @ExceptionHandler()
	 * 
	 * @ResponseStatus(code=HttpStatus.FORBIDDEN) public String handle403(Exception
	 * ex) {
	 * 
	 * return "login";
	 * 
	 * }
	 */
}