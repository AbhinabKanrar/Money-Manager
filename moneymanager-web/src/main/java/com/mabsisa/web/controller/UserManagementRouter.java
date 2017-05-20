/**
 * 
 */
package com.mabsisa.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.Role;
import com.mabsisa.common.model.User;
import com.mabsisa.common.model.UserStatus;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/user")
public class UserManagementRouter {

	@Autowired
	private UserService userService;
	
	private List<String> roles;
	private List<String> userStatuses;
	
	@PostConstruct
	public void init() {
		if (null == roles) {
			roles = new ArrayList<String>();
			for (Role role : Role.values()) {
				roles.add(role.name());
			}
		}
		if (null == userStatuses) {
			userStatuses = new ArrayList<String>();
			for (UserStatus userStatus : UserStatus.values()) {
				userStatuses.add(userStatus.name());
			}
		}
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("roles", roles);
		model.addAttribute("userStatuses", userStatuses);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "usermanagement/adduser";
	}
	
	@PostMapping(value = "/addupdate", params = "action=add")
	public String add(@ModelAttribute("user") User user, Model model) {
		if (!isValid(user, true)) {
			model.addAttribute("errMessage", "Invalid data detected");
			model.addAttribute("employee", user);
			model.addAttribute("roles", roles);
			model.addAttribute("userStatuses", userStatuses);
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "employeemanagement/addemployee";
		}
		try {
			user = userService.save(user);
			model.addAttribute("successMessage", "Record added successfully");
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Can't add employee at this moment");
		}
		model.addAttribute("employee", user);
		model.addAttribute("roles", roles);
		model.addAttribute("userStatuses", userStatuses);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "usermanagement/adduser";
	}

	@PostMapping(value = "/addupdate", params = "action=update")
	public String update(@ModelAttribute("user") User user, Model model) {
		if (!isValid(user, false)) {
			model.addAttribute("errMessage", "Invalid data detected");
			model.addAttribute("status", 1);
			model.addAttribute("employee", user);
			model.addAttribute("roles", roles);
			model.addAttribute("userStatuses", userStatuses);
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "employeemanagement/addemployee";
		}
		try {
			user = userService.update(user);
			model.addAttribute("successMessage", "Record updated successfully");
		} catch(Exception e) {
			model.addAttribute("errMessage", "Can't update employee at this moment");
		}
		model.addAttribute("status", 1);
		model.addAttribute("employee", user);
		model.addAttribute("roles", roles);
		model.addAttribute("userStatuses", userStatuses);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "usermanagement/adduser";
	}
	
	@GetMapping("/view")
	public String view(Model model) {
		List<User> users = new ArrayList<User>();
		try {
			users = userService.findAll();
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("users", users);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "usermanagement/listuser";
	}
	
	@GetMapping("/update/{userId}")
	public String update(@PathVariable("userId") String userId, Model model) {
		User user;
		try {
			user = userService.findUserByUserId(Long.valueOf(userId));
		} catch(Exception e) {
			model.addAttribute("errMessage", "No data found");
			user = new User();
		}
		model.addAttribute("roles", roles);
		model.addAttribute("userStatuses", userStatuses);
		model.addAttribute("status", 1);
		model.addAttribute("user", user);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "usermanagement/adduser";
	}	
	
	private boolean isValid(User user, boolean passwordEnabled) {
		if (user != null && 
				user.getUsername() != null &&
				user.getRole() != null) {
			if (passwordEnabled && user.getPassword() != null) {
				return true;
			}
			return true;
		}
		return false;
	}
	
}
