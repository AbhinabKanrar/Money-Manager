/**
 * 
 */
package com.mabsisa.common.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.model.CustomerCollectionDetail;

/**
 * @author abhinab
 *
 */
public class CommonUtils {

	public static void main(String[] args) {
		BigDecimal jan = new BigDecimal("100");
		BigDecimal feb = new BigDecimal("200");
		System.out.println(jan.subtract(feb));
	}
	
	private static final DateFormat dateFormat = new SimpleDateFormat("MMyyyy");
	
	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

	public static String getCurrentMMYYYY() {
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String getFutureMMYYYY(int monthCount) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, monthCount);
		return dateFormat.format(cal.getTime());
	}
	
	public static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}
	
	public static String getLoggedInUserAccess() {
		Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		String role = null;
		for (GrantedAuthority grantedAuthority : roles) {
			role = grantedAuthority.getAuthority();
		}
		return role;
	}
	
	private static String getStringFromExcel(Cell cell) {
		try {
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue().toUpperCase();
		} catch(Exception e) {
			return "";
		}
	}
	
	@SuppressWarnings("resource")
	public static List<CustomerCollectionDetail> generateCollectionDetails(File excel) throws IOException, InvalidFormatException {
	    Workbook workbook = new XSSFWorkbook(excel);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        List<Customer> customerDetails = new ArrayList<Customer>();
        
        List<CustomerCollectionDetail> customerCollectionDetails = new ArrayList<CustomerCollectionDetail>();
        
        Customer customer;
        CustomerCollectionDetail customerCollectionDetail;
        
        while (iterator.hasNext()) {
			Row currentRow = (Row) iterator.next();
			if (currentRow.getRowNum() != 0) {
				Iterator<Cell> cellIterator = currentRow.iterator();
				customer = new Customer();
				customerCollectionDetail = new CustomerCollectionDetail();
				
	            int count  = 0;
	            while (cellIterator.hasNext()) {
					Cell currentCell = (Cell) cellIterator.next();
					if (count == 0) {
						customer.setRegion(getStringFromExcel(currentCell));
						customerCollectionDetail.setRegion(getStringFromExcel(currentCell));
					} else if (count == 1) {
						customer.setBuilding(getStringFromExcel(currentCell));
						customerCollectionDetail.setBuilding(getStringFromExcel(currentCell));
					} else if (count == 2) {
						customer.setAddress(getStringFromExcel(currentCell));
						customerCollectionDetail.setAddress(getStringFromExcel(currentCell));
					} else if (count == 3) {
						customer.setClient(getStringFromExcel(currentCell));
						customerCollectionDetail.setClient(getStringFromExcel(currentCell));
					} else if (count == 4) {
						customer.setName(getStringFromExcel(currentCell));
						customerCollectionDetail.setName(getStringFromExcel(currentCell));
					} else if(count == 5) {
						customer.setFloor(getStringFromExcel(currentCell));
						customerCollectionDetail.setFloor(getStringFromExcel(currentCell));
					} else if(count == 6) {
						try{
							CellType cellType = currentCell.getCellTypeEnum();
							if (cellType == CellType.NUMERIC) {
								customer.setFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
								customerCollectionDetail.setFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							} else if(cellType == CellType.FORMULA) {
						        CellType currentCellType = currentCell.getCachedFormulaResultTypeEnum();
						        if (currentCellType == CellType.NUMERIC) {
						        	customer.setFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						        	customerCollectionDetail.setFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
								} else {
									customer.setFee(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
									customerCollectionDetail.setFee(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
								}
							} else {
								customer.setFee(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
								customerCollectionDetail.setFee(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							}
						} catch (Exception e) {
							customer.setFee(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							customerCollectionDetail.setFee(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}
					} else if(count == 7) {
						customer.setMahal(getStringFromExcel(currentCell));
						customerCollectionDetail.setMahal(getStringFromExcel(currentCell));
					} else if(count == 8) {
						customer.setTelephone(getStringFromExcel(currentCell));
						customerCollectionDetail.setTelephone(getStringFromExcel(currentCell));
					} else if(count == 9) {
						customer.setLeftTravel(getStringFromExcel(currentCell));
						customerCollectionDetail.setLeftTravel(getStringFromExcel(currentCell));
					} else if(count == 10) {
						customer.setNote(getStringFromExcel(currentCell));
						customerCollectionDetail.setNote(getStringFromExcel(currentCell));
					} else if(count == 11) {
						customerCollectionDetail.setJanFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 12) {
						customerCollectionDetail.setFebFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 13) {
						customerCollectionDetail.setMarFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 14) {
						customerCollectionDetail.setAprFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 15) {
						customerCollectionDetail.setMayFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 16) {
						customerCollectionDetail.setJunFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 17) {
						customerCollectionDetail.setJulFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 18) {
						customerCollectionDetail.setAugFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 19) {
						customerCollectionDetail.setSepFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 20) {
						customerCollectionDetail.setOctFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 21) {
						customerCollectionDetail.setNovFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else if(count == 22) {
						customerCollectionDetail.setDecFee(BigDecimal.valueOf(currentCell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}
					
	                count++;
				}
	            customerDetails.add(customer);
	            customerCollectionDetails.add(customerCollectionDetail);
			}
		}
//		return customerDetails;
        return customerCollectionDetails;
	}
	
}
