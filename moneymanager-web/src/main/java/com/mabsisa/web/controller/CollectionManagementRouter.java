/**
 * 
 */
package com.mabsisa.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.User;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.customer.CustomerCollectionService;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/collection")
public class CollectionManagementRouter {

	@Autowired
	private CustomerCollectionService customerCollectionService;

	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/collectiondashboard";
	}

	@GetMapping("/view")
	public String view(Model model) {
		List<CustomerCollectionDetail> customerCollectionDetails = new ArrayList<CustomerCollectionDetail>();
		try {
			customerCollectionDetails = customerCollectionService.findAll();
			System.out.println(customerCollectionDetails.get(0).getJanFee());
			System.out.println(customerCollectionDetails.get(0).getFebFee());
			System.out.println(customerCollectionDetails.get(0).getMarFee());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetails", customerCollectionDetails);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/listcustomercollectionDetail";
	}

	@GetMapping("/view/{collectionId}")
	public String view(@PathVariable("collectionId") String collectionId, Model model) {
		CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();
		List<User> users = new ArrayList<User>();
		try {
			customerCollectionDetail = customerCollectionService.findByCollectionId(Long.valueOf(collectionId));
			users = userService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetail", customerCollectionDetail);
		model.addAttribute("users", users);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/addupdatecustomercollectionDetail";
	}
	
	@GetMapping("/view/assignment")
	public String view(Principal principal, Model model) {
		List<CustomerCollectionDetail> customerCollectionDetails = new ArrayList<CustomerCollectionDetail>();
		try {
			customerCollectionDetails = customerCollectionService.findByCollectorName(principal.getName());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetails", customerCollectionDetails);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/listassignment";
	}
	
	@GetMapping("/view/assignment/{collectionId}")
	public String viewCollection(@PathVariable("collectionId") String collectionId, Model model) {
		CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();
		try {
			customerCollectionDetail = customerCollectionService.findCollectionByCollectionId(Long.valueOf(collectionId));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetail", customerCollectionDetail);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/addupdatecollection";
	}

	@PostMapping(value = "/addupdate", params = "action=update")
	public String update(@ModelAttribute("customerCollectionDetail") CustomerCollectionDetail customerCollectionDetail,
			Model model) {
		List<User> users = new ArrayList<User>();
		if (!isValid(customerCollectionDetail)) {
			model.addAttribute("errMessage", "Invalid data detected");
			users = userService.findAll();
			model.addAttribute("users", users);
			model.addAttribute("customerCollectionDetail", customerCollectionDetail);
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "collectionmanagement/addupdatecustomercollectionDetail";
		}
		try {
			customerCollectionDetail = customerCollectionService.update(customerCollectionDetail);
			users = userService.findAll();
			model.addAttribute("successMessage", "Record updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Can't update customer at this moment");
		}
		model.addAttribute("users", users);
		model.addAttribute("customerCollectionDetail", customerCollectionDetail);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/addupdatecustomercollectionDetail";
	}

	private boolean isValid(CustomerCollectionDetail customerCollectionDetail) {
		if (customerCollectionDetail != null && customerCollectionDetail.getCollectionId() != null
				&& customerCollectionDetail.getCollectorId() != 0) {
			return true;
		}
		return false;
	}

}
