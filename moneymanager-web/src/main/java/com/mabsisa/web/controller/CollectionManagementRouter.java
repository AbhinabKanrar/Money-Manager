/**
 * 
 */
package com.mabsisa.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.customer.CustomerCollectionService;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/collection")
public class CollectionManagementRouter {

	@Autowired
	private CustomerCollectionService customerCollectionService;
	
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
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetails", customerCollectionDetails);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/listcustomercollectionDetail";
	}
	
}
