/**
 * 
 */
package com.mabsisa.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.SearchParam;
import com.mabsisa.common.model.User;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/assignment")
public class AssignmentController {

	List<String> searchParams;
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void init() {
		if (null == searchParams) {
			searchParams = new ArrayList<String>();
			for(SearchParam searchParam : SearchParam.values()) {
				searchParams.add(searchParam.name());
			}
		}
	}
	
	@GetMapping("/search")
	public String search(Model model) {
		List<User> users = new ArrayList<User>();
		try {
			users = userService.findAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("users", users);
		model.addAttribute("searchParams", searchParams);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}
	
	@PostMapping("/search")
	public String seachResult(HttpServletRequest request, Model model) {
		List<User> users = new ArrayList<User>();
		try {
			long collectorId = ServletRequestUtils.getLongParameter(request, "collectorId");
			String searchParam = ServletRequestUtils.getStringParameter(request, "searchParam");
			users = userService.findAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("users", users);
		model.addAttribute("searchParams", searchParams);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}
	
}
