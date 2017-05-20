/**
 * 
 */
package com.mabsisa.web.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.service.customer.CustomerService;

/**
 * @author abhinab
 *
 */
@Controller
@RequestMapping("/customer")
public class CustomerManagementRouter {

	private static final String fileExtentions = ".xlsx";
	
	@Autowired
	private CustomerService customerService;

	@GetMapping("/view")
	public String view(Model model) {
		List<Customer> customers = new ArrayList<Customer>();
		try {
			customers = customerService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customers", customers);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/listcustomers";
	}

	@GetMapping("/add")
	public String add(Model model) {
		Customer customer = new Customer(new BigDecimal("0.00"));
		model.addAttribute("customer", customer);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomer";
	}

	@PostMapping(value = "/addupdate", params = "action=add")
	public String add(@ModelAttribute("customer") Customer customer, Model model) {
		if (!isValid(customer)) {
			model.addAttribute("errMessage", "Invalid data detected");
			model.addAttribute("customer", customer);
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "customermanagement/addcustomer";
		}
		try {
			customer = customerService.save(customer);
			model.addAttribute("successMessage", "Record added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Can't add customer at this moment");
		}
		model.addAttribute("customer", customer);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomer";
	}

	@PostMapping(value = "/addupdate", params = "action=update")
	public String update(@ModelAttribute("customer") Customer customer, Model model) {
		if (!isValid(customer)) {
			model.addAttribute("errMessage", "Invalid data detected");
			model.addAttribute("status", 1);
			model.addAttribute("customer", customer);
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "customermanagement/addcustomer";
		}
		try {
			customer = customerService.update(customer);
			model.addAttribute("successMessage", "Record updated successfully");
		} catch (Exception e) {
			model.addAttribute("errMessage", "Can't update customer at this moment");
		}
		model.addAttribute("status", 1);
		model.addAttribute("customer", customer);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomer";
	}

	@PostMapping(value = "/addupdate", params = "action=delete")
	public String delete(@ModelAttribute("customer") Customer customer, Model model) {
		try {
			customer = customerService.delete(customer);
			model.addAttribute("successMessage", "Record deleted successfully");
		} catch (Exception e) {
			model.addAttribute("errMessage", "Can't delete customer at this moment");
		}
		model.addAttribute("status", 2);
		model.addAttribute("customer", customer);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomer";
	}

	@GetMapping("/find/{customerId}")
	public String find(@PathVariable("customerId") String customerId, Model model) {
		Customer customer;
		try {
			customer = customerService.findByCustomerId(Long.valueOf(customerId));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "No data found");
			customer = new Customer(new BigDecimal("0.00"));
		}
		model.addAttribute("status", 1);
		model.addAttribute("customer", customer);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomer";
	}

	@GetMapping("/add/excel")
	public String addExcel(Model model) {
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomerexcel";
	}
	
	@PostMapping("/customerexcelupload")
	public String customerexcelupload(@RequestParam MultipartFile excel, Model model) {
		if (!isValidExcel(excel)) {
			model.addAttribute("errMessage", "Invalid file format");
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "customermanagement/addcustomerexcel";
		}
		try {
			String destination = "/tmp/"+ excel.getOriginalFilename();
			File file = new File(destination);
			excel.transferTo(file);
			customerService.save(file);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to save the data at this moment");
		}
		model.addAttribute("successMessage", "Customer details uploaded successfully");
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "customermanagement/addcustomerexcel";
	}
	
	private boolean isValid(Customer customer) {
		if (customer != null && customer.getRegion() != null && customer.getAddress() != null
				&& Pattern.matches("[0-9]+(\\.){0,1}[0-9]*", String.valueOf(customer.getFee()))) {
			return true;
		}
		return false;
	}

	private boolean isValidExcel(MultipartFile excel) {
		if (excel != null) {
			String fileName = excel.getOriginalFilename();
			if (fileName != null && !fileName.isEmpty()) {
				int lastIndex = fileName.lastIndexOf('.');
				if (fileName.substring(lastIndex, fileName.length()).equalsIgnoreCase(fileExtentions)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
