package com.ntmo.attendance.scheduled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.ntmo.attendance.entity.Attendance;
import com.ntmo.attendance.entity.Employee;
import com.ntmo.attendance.entity.EmployeeAttendance;
import com.ntmo.attendance.entity.Manager;
import com.ntmo.attendance.repos.AttendanceRepository;
import com.ntmo.attendance.service.EmployeeService;
import com.ntmo.attendance.service.ManagerService;
import com.ntmo.attendance.util.SendEmailUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class AttendanceReport {
	
	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private ManagerService mgrService;
	
	@Autowired 
	private AttendanceRepository aRep;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private static String[] monthName = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	private static String[] days = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};

	@Scheduled(fixedRate = 3600000)
	public void scheduleFixedRateTask() {
	    
	    log.info("AttendanceReport: scheduleFixedRateTask - ");
	    // weeklyReport();
	}
	
	private List<Employee> getDailyAttendanceByManager(Manager m) {
		
		List<Employee> empList = empService.getEmployee();
		List<Employee> filteredList = empList
			.stream()
			.filter( emp -> emp.getManagerId() == m.getId())
			.collect(Collectors.toList());
		
		Date startDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
		Date endDate = Date.from(LocalDate.now().atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
		
		empList.clear();
		
		for (Employee emp : filteredList) {
			List<Attendance> aList = aRep.findByEmployeeIdAndPresenceDateBetween(emp.getId(), startDate, endDate);
			if(aList.size() > 0) {
				empList.add(emp);
			}
		}
		
		return empList;
	}
	
	private List<EmployeeAttendance> getAttendanceByManager(Manager m, int minusDays) {
		
		List<Employee> empList = empService.getEmployee();
		List<Employee> filteredList = empList
			.stream()
			.filter( emp -> emp.getManagerId() == m.getId())
			.collect(Collectors.toList());
		
		Date startDate = Date.from(LocalDate.now().minusDays(minusDays).atStartOfDay().toInstant(ZoneOffset.UTC));
		Date endDate = Date.from(LocalDate.now().atTime(23, 59, 59).toInstant(ZoneOffset.UTC));
		
		empList.clear();
		
		List<EmployeeAttendance> eaList = new ArrayList<EmployeeAttendance>();
		for (Employee emp : filteredList) {
			EmployeeAttendance ea = new EmployeeAttendance();
			ea.setEmployeeId(emp.getId());
			ea.setEmployeeName(emp.getEmployeeName());
			ea.setRemarks(emp.getRemarks());
			List<Attendance> aList = aRep.findByEmployeeIdAndPresenceDateBetween(emp.getId(), startDate, endDate);
			List<Date> ad = new ArrayList<Date>();
			if(aList.size() > 0) {
				for (Attendance a : aList) {
					ad.add(a.getPresenceDate());
				}
			}
			ea.setPresenceDate(ad);
			eaList.add(ea);
		}
		
		return eaList;
	}
	
	private List<Date> getListOfDate(Date start, int noOfDays) {
		
		List<Date> dates = new ArrayList<Date>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(start);

	    for(int i = 0; i < noOfDays; i++)
	    {
	        Date result = calendar.getTime();
	        dates.add(result);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
	
	@SuppressWarnings("deprecation")
	private boolean containsDate(List<Date> dateList, Date needle) {
		
		for(Date d : dateList) {
			if(d.getDate() == needle.getDate() && d.getMonth() == needle.getMonth() && d.getYear() == needle.getYear()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void setThinBorder(CellStyle style) {
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
	}
	
	@SuppressWarnings("deprecation")
	private String generateReport(Date sDate, Date eDate, Manager mgr, List<EmployeeAttendance> eaList) throws IOException {

		if(eaList.isEmpty()) {
			log.debug("No reportees for manager - " + mgr.getName());
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String reportName = "Attendance_" + mgr.getId() + "_" + df.format(eDate) + ".xlsx";
		log.debug("Generating report - " + reportName + " on " + eDate);
		
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet(monthName[sDate.getMonth()]);
			int rowCount = 0;
			
			XSSFFont _bold = workbook.createFont();
			_bold.setColor(IndexedColors.BLACK.getIndex());
			_bold.setBold(true);
			
			// Print Header
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			CreationHelper createHelper = workbook.getCreationHelper(); 
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm"));
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setFont(_bold);
			setThinBorder(headerStyle);
			
			CellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);
			setThinBorder(centerAlign);
			
			CellStyle leftAlign = workbook.createCellStyle();
			leftAlign.setAlignment(HorizontalAlignment.LEFT);
			setThinBorder(leftAlign);
			
			Cell cell = row.createCell(columnCount++);
			cell.setCellValue("Reportee Name");
			cell.setCellStyle(headerStyle);

			List<Date> lDate = getListOfDate(sDate, 14);
			for(Date d: lDate) {
				if(d.getDay() == 0 || d.getDay() == 6) {
					// Skip for Sunday-0 and Saturday-6
					continue;
				}
				cell = row.createCell(columnCount++);
				cell.setCellValue(d);
				cell.setCellStyle(headerStyle);
			}
			
			cell = row.createCell(columnCount++);
			cell.setCellValue("14 Day Total");
			cell.setCellStyle(headerStyle);
			
			cell = row.createCell(columnCount++);
			cell.setCellValue("Remarks");
			cell.setCellStyle(headerStyle);
			
			// Print row with days
			row = sheet.createRow(rowCount++);
			columnCount = 0;
			
			cell = row.createCell(columnCount++);
			cell.setBlank();
			cell.setCellStyle(leftAlign);
			
			for(Date d: lDate) {
				if(d.getDay() == 0 || d.getDay() == 6) {
					continue;
				}
				cell = row.createCell(columnCount++);
				cell.setCellValue(days[d.getDay()]);
				cell.setCellStyle(centerAlign);
			}
			
			cell = row.createCell(columnCount++);
			cell.setBlank();
			cell.setCellStyle(centerAlign);
			
			cell = row.createCell(columnCount++);
			cell.setBlank();
			cell.setCellStyle(leftAlign);
			
			// Print individual Employee's data
			for(EmployeeAttendance ea : eaList) {
				row = sheet.createRow(rowCount++);
				columnCount = 0;
				int _count = 0;
				
				cell = row.createCell(columnCount++);
				cell.setCellValue(ea.getEmployeeName()); 
				cell.setCellStyle(leftAlign);
				
				for(Date d: lDate) {
					if(d.getDay() == 0 || d.getDay() == 6) {
						continue;
					}
					cell = row.createCell(columnCount++);
					if(containsDate(ea.getPresenceDate(), d)) {
						cell.setCellValue(1);
						_count++;
					} else {
						cell.setBlank();
					}
					cell.setCellStyle(centerAlign);
				}
				
				cell = row.createCell(columnCount++);
				cell.setCellValue(_count);
				cell.setCellStyle(centerAlign);
				
				cell = row.createCell(columnCount++);
				cell.setCellValue(ea.getRemarks());
				cell.setCellStyle(leftAlign);
				
			}
			
			sheet.autoSizeColumn(0);	// Name
			sheet.autoSizeColumn(11);	// Total
			sheet.autoSizeColumn(12);	// Remarks
			
			try (FileOutputStream outputStream = new FileOutputStream(reportName)) {
				workbook.write(outputStream);
			}
		}
		
		return reportName;
	}
	
	// Report running daily at 10 am, 2 pm and 6 pm
	@Scheduled(cron = "0 0 10,14,18 * * MON-FRI")
	public void dailyReport() {
	 
	    log.info("AttendanceReport: dailyReport - ");
	    
	    List<Manager> managers = mgrService.get();
	    
	    for(Manager mgr : managers) {
	    	List<Employee> emps = getDailyAttendanceByManager(mgr);
	    		log.info("Manager [" + mgr.getName() + "]: " + emps.toString());
	    }
	}
	
	// Report running daily at 10 am, 2 pm and 6 pm
	@SuppressWarnings("deprecation")
	@Scheduled(cron = "0 0 19 * * FRI")
	public void weeklyReport() {
	 
	    Date today = Date.from(LocalDate.now().atTime(18, 29, 59).toInstant(ZoneOffset.UTC));	// 5h30m difference from UTC
	    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    int numOfDays = today.getDay() + 7;
	    Date startDate =  Date.from(LocalDate.now().minusDays(numOfDays).atStartOfDay().toInstant(ZoneOffset.UTC));
	    //Date endDate = Date.from(LocalDate.now().minusDays(today.getDay()).atStartOfDay().toInstant(ZoneOffset.UTC));
	    
	    log.info("AttendanceReport: weeklyReport - " + df.format(startDate) + " and " + df.format(today));

	    List<Manager> managers = mgrService.get();
	    
	    for(Manager mgr : managers) {
	    	if(mgr.isWeeklyReport()) {
		    	List<EmployeeAttendance> eaList = getAttendanceByManager(mgr, numOfDays);
		    		log.info("Manager [" + mgr.getName() + "]: " + eaList);
		    	try {
		    		String report = generateReport(startDate, today, mgr, eaList);
		    		SendEmailUtil mailUtil = new SendEmailUtil();
		    		mailUtil.setJavaMailSender(javaMailSender);
		    		mailUtil.setToAddress(mgr.getName());
		    		mailUtil.setFromAddress("TeamO");
		    		df = new SimpleDateFormat("MM/dd");
		    		mailUtil.setSubject("WFO report for week ending " + df.format(today));
		    		String message = "PFA WFO report for week ending " + df.format(today) + 
		    				".\r\n\r\nThanks,\r\nTeamO";
		    		mailUtil.setMessage(message);
		    		mailUtil.setAttachmentName(report);
		    		mailUtil.sendEmail();
		    		
		    		// Delete the generated excel report
		    		File file = new File(report);
		    		Files.deleteIfExists(file.toPath());
		    	} catch (IOException ioe) {
		    		log.error(ioe.toString());
		    		ioe.printStackTrace();
		    	} catch (Exception ex) {
		    		log.error(ex.toString());
		    		ex.printStackTrace();
		    	}
	    	} else {
	    		log.info("Manager " + mgr.getName() + " doesn't want weekly report");
	    	}
	    }
	}
	
}
