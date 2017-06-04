/**
 * 
 */
package com.mabsisa.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.model.Role;
import com.mabsisa.common.model.SearchParam;
import com.mabsisa.common.model.User;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.customer.AssignmentService;
import com.mabsisa.service.customer.CustomerService;
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

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AssignmentService assignmentService;

	@PostConstruct
	public void init() {
		if (null == searchParams) {
			searchParams = new ArrayList<String>();
			for (SearchParam searchParam : SearchParam.values()) {
				searchParams.add(searchParam.name());
			}
		}
	}

	@GetMapping("/search")
	public String search(Model model) {
		List<User> users = new ArrayList<User>();
		try {
			users = userService.findAll().stream().filter(user -> user.getRole() == Role.COLLECTOR).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("users", users);
		model.addAttribute("searchParams", searchParams);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}

	@PostMapping("/search")
	public String seachResult(HttpServletRequest request, Model model) {
		long collectorId = 0;
		List<Customer> customers = new ArrayList<Customer>();
		List<User> users = new ArrayList<User>();
		User collector = new User();
		try {
			collectorId = ServletRequestUtils.getLongParameter(request, "collectorId");
			collector = userService.findUserByUserId(collectorId);
			String searchParam = ServletRequestUtils.getStringParameter(request, "searchParam");
			SearchParam param = SearchParam.valueOf(searchParam);
			customers = customerService.findAll();
			if (customers != null && !customers.isEmpty()) {
				if (param == SearchParam.REGION) {
					model.addAttribute("status", 1);
					Set<String> regions = new HashSet<String>();
					for (Customer customer : customers) {
						regions.add(customer.getRegion());
					}
					model.addAttribute("regions", regions);
				} else if (param == SearchParam.BUILDING) {
					model.addAttribute("status", 2);
					Map<String, String> buildingRegions = new HashMap<String, String>();
					Set<String> buildings = new HashSet<String>();
					for (Customer customer : customers) {
						buildings.add(customer.getBuilding());
					}
					for(String building : buildings) {
						for (Customer customer : customers) {
							if (customer.getBuilding().equalsIgnoreCase(building)) {
								buildingRegions.put(building + "," +customer.getAddress(), customer.getRegion());
							}
						}
					}
					model.addAttribute("buildingRegions", buildingRegions);
				} else {
					model.addAttribute("customers", customers);
					model.addAttribute("status", 3);
				}
			} else {
				model.addAttribute("errMessage", "No customer data found");
			}
			users = userService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("users", users);
		model.addAttribute("collector", collector);
		model.addAttribute("collectorId", collectorId);
		model.addAttribute("searchParams", searchParams);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}

	@PostMapping("/assign/region")
	public String assignRegion(Model model, HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String[] regionsArray = parameterMap.get("regions");
		if (regionsArray != null && regionsArray.length > 0) {
			try {
				boolean flag = assignmentService.assignCustomer(regionsArray, ServletRequestUtils.getLongParameter(request, "collectorId"), SearchParam.REGION);
				if (!flag) {
					throw new Exception();
				}
			} catch(Exception e) {
				model.addAttribute("error", 1);
			}
		} else {
			model.addAttribute("error", 1);
		}
		model.addAttribute("confirmation", 1);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}
	
	@PostMapping("/assign/building")
	public String assignBuilding(Model model, HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String[] buildingRegionArray = parameterMap.get("buildingRegions");
		if (buildingRegionArray != null && buildingRegionArray.length > 0) {
			try {
				boolean flag = assignmentService.assignCustomer(buildingRegionArray, ServletRequestUtils.getLongParameter(request, "collectorId"), SearchParam.BUILDING);
				if (!flag) {
					throw new Exception();
				}
			} catch(Exception e) {
				model.addAttribute("error", 1);
			}
		} else {
			model.addAttribute("error", 1);
		}
		model.addAttribute("confirmation", 1);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}

	@PostMapping("/assign/customer")
	public String assignCustomer(Model model, HttpServletRequest request) throws ServletRequestBindingException {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String[] customerArray = parameterMap.get("customers");
		if (customerArray != null && customerArray.length > 0) {
			try {
				boolean flag = assignmentService.assignCustomer(customerArray, ServletRequestUtils.getLongParameter(request, "collectorId"), SearchParam.MANUAL);
				if (!flag) {
					throw new Exception();
				}
			} catch(Exception e) {
				model.addAttribute("error", 1);
			}
		} else {
			model.addAttribute("error", 1);
		}
		model.addAttribute("confirmation", 1);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "assignment/addupdateassignment";
	}

	
}
