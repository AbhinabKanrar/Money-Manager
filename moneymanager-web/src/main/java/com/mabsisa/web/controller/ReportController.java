/**
 * 
 */
package com.mabsisa.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.CollectorCollection;
import com.mabsisa.common.model.CustomerAssignmentCollector;
import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.model.CustomerPerRegion;
import com.mabsisa.common.model.RevenueByRegion;
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
			model.addAttribute(CommonConstant.KEY_TOTAL_PAID_CUSTOMER,
					records.get(CommonConstant.KEY_TOTAL_PAID_CUSTOMER));
			model.addAttribute(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER,
					records.get(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
			model.addAttribute(CommonConstant.KEY_TOTAL_CUSTOMER, 0);
			model.addAttribute(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER, 0);
			model.addAttribute(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER, 0);
			model.addAttribute(CommonConstant.KEY_TOTAL_PAID_CUSTOMER, 0);
			model.addAttribute(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER, 0);
		}
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/trackassignment";
	}

	@GetMapping("/trackassignment/collector")
	public String trackAssignmentByCollector(Model model) {
		List<CustomerAssignmentCollector> customerAssignmentCollectors = new ArrayList<CustomerAssignmentCollector>();
		try {
			customerAssignmentCollectors = reportService.findAllAssignmentByCollector();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerAssignmentCollectors", customerAssignmentCollectors);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/trackAssignmentbycollector";
	}

	@GetMapping("/collectionmismatchlocationtracking")
	public String collectionMismatchLocationTracking(Model model) {
		List<CustomerCollectionDetailAudit> customerCollectionDetailAudits = new ArrayList<CustomerCollectionDetailAudit>();
		try {
			customerCollectionDetailAudits = reportService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetailAudits", customerCollectionDetailAudits);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/collectionmismatchlocationtracking";
	}

	@GetMapping("/search/numofcustomerperregion")
	public String searchNumOfCustomerPerRegion(Model model) {
		List<CustomerPerRegion> customerPerRegions = new ArrayList<CustomerPerRegion>();
		try {
			customerPerRegions = reportService.findAllCustomerPerRegion();
			if (customerPerRegions != null && !customerPerRegions.isEmpty()) {
				model.addAttribute("total", customerPerRegions.get(0).getTotal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
			model.addAttribute("total", 0);
		}
		model.addAttribute("customerPerRegions", customerPerRegions);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/numofcustomerperregion";
	}

	@GetMapping("/search/collecton/collector")
	public String searchCollectionByCollector(Model model) {
		List<CollectorCollection> collectorCollections = new ArrayList<CollectorCollection>();
		try {
			collectorCollections = reportService.findAllCollectionByCollector();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("collectorCollections", collectorCollections);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/collectionbycollector";
	}
	
	@GetMapping("/search/collectionoftoday")
	public String searchCollectionOfToday(Model model) {
		List<CollectorCollection> collectorCollections = new ArrayList<CollectorCollection>();
		try {
			collectorCollections = reportService.findAllCollectionOfToday();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("collectorCollections", collectorCollections);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/collectionoftoday";
	}
	
	@GetMapping("/search/revenuereceived")
	public String revenueReceived(Model model) {
		List<RevenueByRegion> revenueByRegions = new ArrayList<RevenueByRegion>();
		BigDecimal janTotal = new BigDecimal("0");
		BigDecimal febTotal = new BigDecimal("0");
		BigDecimal marTotal = new BigDecimal("0");
		BigDecimal aprTotal = new BigDecimal("0");
		BigDecimal mayTotal = new BigDecimal("0");
		BigDecimal junTotal = new BigDecimal("0");
		BigDecimal julTotal = new BigDecimal("0");
		BigDecimal augTotal = new BigDecimal("0");
		BigDecimal sepTotal = new BigDecimal("0");
		BigDecimal octTotal = new BigDecimal("0");
		BigDecimal novTotal = new BigDecimal("0");
		BigDecimal decTotal = new BigDecimal("0");
		try {
			revenueByRegions = reportService.findAllRevenueByRegion();
			if (revenueByRegions != null && !revenueByRegions.isEmpty()) {
				for (RevenueByRegion revenueByRegion : revenueByRegions) {
					janTotal = janTotal.add(revenueByRegion.getJanRevenue());
					febTotal = febTotal.add(revenueByRegion.getFebRevenue());
					marTotal = marTotal.add(revenueByRegion.getMarRevenue());
					aprTotal = aprTotal.add(revenueByRegion.getAprRevenue());
					mayTotal = mayTotal.add(revenueByRegion.getMayRevenue());
					junTotal = junTotal.add(revenueByRegion.getJunRevenue());
					julTotal = julTotal.add(revenueByRegion.getJulRevenue());
					augTotal = augTotal.add(revenueByRegion.getAugRevenue());
					sepTotal = sepTotal.add(revenueByRegion.getSepRevenue());
					octTotal = octTotal.add(revenueByRegion.getOctRevenue());
					novTotal = novTotal.add(revenueByRegion.getNovRevenue());
					decTotal = decTotal.add(revenueByRegion.getDecRevenue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("janTotal", janTotal);
		model.addAttribute("febTotal", febTotal);
		model.addAttribute("marTotal", marTotal);
		model.addAttribute("aprTotal", aprTotal);
		model.addAttribute("mayTotal", mayTotal);
		model.addAttribute("junTotal", junTotal);
		model.addAttribute("julTotal", julTotal);
		model.addAttribute("augTotal", augTotal);
		model.addAttribute("sepTotal", sepTotal);
		model.addAttribute("octTotal", octTotal);
		model.addAttribute("novTotal", novTotal);
		model.addAttribute("decTotal", decTotal);

		model.addAttribute("revenueByRegions", revenueByRegions);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/revenuebyregion";
	}

	@GetMapping("/search/paidunpaid")
	public String paidUnpaid(Model model) {
		List<CustomerPerRegion> customerPerRegions = new ArrayList<CustomerPerRegion>();
		try {
			customerPerRegions = reportService.findAllCustomerPerRegion();
			if (customerPerRegions != null && !customerPerRegions.isEmpty()) {
				model.addAttribute("total", customerPerRegions.get(0).getTotal());
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
			model.addAttribute("total", 0);
		}
		model.addAttribute("customerPerRegions", customerPerRegions);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "report/paidunpaid";
	}

}
