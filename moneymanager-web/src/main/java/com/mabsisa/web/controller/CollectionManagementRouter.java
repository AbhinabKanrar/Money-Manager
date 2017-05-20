/**
 * 
 */
package com.mabsisa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.util.CommonUtils;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/collection")
public class CollectionManagementRouter {

	@GetMapping("/view")
	public String view(Model model) {
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/collectiondashboard";
	}
	
}
