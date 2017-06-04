/**
 * 
 */
package com.mabsisa.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.PaymentReceipt;
import com.mabsisa.common.model.Role;
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
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetails", customerCollectionDetails);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/listcustomercollectionDetail";
	}

	@GetMapping("/view/unpaid/{month}")
	public String viewUnPaid(@PathVariable("month") String month, Model model) {
		List<CustomerCollectionDetail> customerCollectionDetails = new ArrayList<CustomerCollectionDetail>();
		try {
			customerCollectionDetails = customerCollectionService.findByMonth(Integer.valueOf(month));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetails", customerCollectionDetails);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/listcustomercollectionDetail";
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/download")
	public void download(HttpServletResponse response) throws IOException {
		String FILE_NAME = "/tmp/Customer Info v3.xlsx";
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");
        
        int rowNum = 0;

        List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionService.findAll();
        if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
        	Row row = sheet.createRow(rowNum++);
        	CellStyle style = workbook.createCellStyle();
        	style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        	Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setFillForegroundColor(HSSFColor.BLUE.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        	row.setRowStyle(style);
        	int colNum = 0;
        	Cell cell = row.createCell(colNum++);
        	cell.setCellValue("Customer ID");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Region");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Building");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Address");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Client");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Name");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Floor");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Mahal");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Telephone");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Left/Travel");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Note");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Collector ID");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Jan Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Feb Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Mar Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Apr Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("May Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Jun Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Jul Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Aug Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Sep Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Oct Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Nov Fee");
        	cell = row.createCell(colNum++);
        	cell.setCellValue("Dec Fee");
        	
			for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
				colNum = 0;
				row = sheet.createRow(rowNum++);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getCustomerId());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getRegion());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getBuilding());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getAddress());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getClient());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getName());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getFloor());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getFee() != null ? customerCollectionDetail.getFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getMahal());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getTelephone());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getLeftTravel());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getNote());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getCollectionId());
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getJanFee() != null ? customerCollectionDetail.getJanFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getFebFee() != null ? customerCollectionDetail.getFebFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getMarFee() != null ? customerCollectionDetail.getMarFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getAprFee() != null ? customerCollectionDetail.getAprFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getMayFee() != null ? customerCollectionDetail.getMayFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getJunFee() != null ? customerCollectionDetail.getJunFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getJulFee() != null ? customerCollectionDetail.getJulFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getAugFee() != null ? customerCollectionDetail.getAugFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getSepFee() != null ? customerCollectionDetail.getSepFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getOctFee() != null ? customerCollectionDetail.getOctFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getNovFee() != null ? customerCollectionDetail.getNovFee().doubleValue() : 0);
				cell = row.createCell(colNum++);
				cell.setCellValue(customerCollectionDetail.getDecFee() != null ? customerCollectionDetail.getDecFee().doubleValue() : 0);
			}
		}
        File file = new File(FILE_NAME);
        file.createNewFile();
        InputStream outputStream = new FileInputStream(FILE_NAME);
        
        response.addHeader("Content-disposition", "attachment;filename=Customer Info v3.xlsx");
    	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    	IOUtils.copy(outputStream, response.getOutputStream());
    	workbook.write(response.getOutputStream());
        workbook.close();
    	response.flushBuffer();
	}

	@GetMapping("/view/{collectionId}")
	public String view(@PathVariable("collectionId") String collectionId, Model model) {
		CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();
		List<User> users = new ArrayList<User>();
		try {
			customerCollectionDetail = customerCollectionService.findByCollectionId(Long.valueOf(collectionId));
			users = userService.findAll().stream().filter(user -> user.getRole() == Role.COLLECTOR).collect(Collectors.toList());
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
			model.addAttribute("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("customerCollectionDetail", customerCollectionDetail);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/addupdatecollection";
	}

	@GetMapping("/print/{collectionId}")
	public String print(@PathVariable("collectionId") String collectionId, Model model) {
		PaymentReceipt paymentReceipt = new PaymentReceipt();
		try {
			paymentReceipt = customerCollectionService.generatePayementReceipt(Long.valueOf(collectionId));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Unable to fetch the data at this moment");
		}
		model.addAttribute("paymentReceipt", paymentReceipt);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/receipt";
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
	
	@PostMapping(value = "/addupdate", params = "action=collect")
	public String collect(@ModelAttribute("customerCollectionDetail") CustomerCollectionDetail customerCollectionDetail,
			Model model) {
		if (!isValid(customerCollectionDetail)) {
			model.addAttribute("errMessage", "Invalid data detected");
			model.addAttribute("customerCollectionDetail", customerCollectionDetail);
			model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
			return "collectionmanagement/addupdatecollection";
		}
		try {
			customerCollectionDetail = customerCollectionService.collect(customerCollectionDetail);
			model.addAttribute("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errMessage", "Can't update customer at this moment");
		}
		model.addAttribute("customerCollectionDetail", customerCollectionDetail);
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "collectionmanagement/addupdatecollection";
	}

	@PostMapping("/remove/todayhistory")
	public void removeTodayHistory(HttpServletResponse response) throws IOException {
		try {
			customerCollectionService.refresh();
		} catch(Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/report/search/collectionoftoday");
	}
	
	private boolean isValid(CustomerCollectionDetail customerCollectionDetail) {
		if (customerCollectionDetail != null && customerCollectionDetail.getCollectionId() != null) {
			return true;
		}
		return false;
	}

}
