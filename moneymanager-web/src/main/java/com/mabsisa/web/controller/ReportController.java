/**
 * 
 */
package com.mabsisa.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.util.CommonConstant;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.report.ReportService;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/dashboard")
	public String index(Model model) {
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/reportdashboard";
	}

	@GetMapping("/trackassignment")
	public String trackAssignment(Model model) {
		Map<String, Integer> records = new HashMap<String, Integer>();
		try {
			records = reportService.findAssignmentRecord();
			model.addAttribute(CommonConstant.KEY_TOTAL_CUSTOMER, records.get(CommonConstant.KEY_TOTAL_CUSTOMER));
			model.addAttribute(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER,
					records.get(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER));
			model.addAttribute(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER,
					records.get(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER));
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
			model.addAttribute(CommonConstant.KEY_TOTAL_CUSTOMER, 0);
			model.addAttribute(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER, 0);
			model.addAttribute(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER, 0);
		}
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/trackassignment";
	}

	@GetMapping("/collectionmismatchlocationtracking")
	public String collectionMismatchLocationTracking(Model model) {
		List<CustomerCollectionDetailAudit> customerCollectionDetailAudits = new ArrayList<CustomerCollectionDetailAudit>();
		try {
			customerCollectionDetailAudits = reportService.findAll();
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetailAudits", customerCollectionDetailAudits);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/collectionmismatchlocationtracking";
	}

}
