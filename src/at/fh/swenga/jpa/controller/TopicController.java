package at.fh.swenga.jpa.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class TopicController {
	
	@Autowired
	TopicDao topicDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	@Secured("ROLE_ADMIN")
	@RequestMapping("/deleteTopic")
	public String deleteData(Model model, @RequestParam int id) {
		topicDao.delete(id);

		return "forward:index";
	}
	

}
