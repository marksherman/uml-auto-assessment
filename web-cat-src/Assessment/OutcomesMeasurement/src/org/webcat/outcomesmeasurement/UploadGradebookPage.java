package org.webcat.outcomesmeasurement;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import org.webcat.core.AuthenticationDomain;
import org.webcat.core.CourseOffering;
import org.webcat.core.User;
import org.webcat.outcomesmeasurement.reports.ReportsPage;
import org.webcat.ui.generators.JavascriptGenerator;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;

@SuppressWarnings("serial")
public class UploadGradebookPage extends BasePage {
	private static final String QUESTION_REGEX = "[Qq]\\s?\\-?\\s?\\d";
	private static final String STUDENT_REGEX = "\\d{9}";
	private static final String STUDENT_REGEX_2 = "[A-Za-z0-9]{8}";
	private static final String OUTCOME_REGEX = "[A-Za-z]\\s?\\-?\\s?\\d";

	public static final NSArray<String> COLUMNS = new NSArray<String>(
			new String[]{
					"Unknown",
					"Student",
					"Assignment",
					"Question",
					"Totals",

			} );

	public UploadGradebookPage(WOContext context) {
		super(context);
	}

	//~ KVC Attributes (must be public) .......................................
	public String               filePath;
	public NSData               data;
	public String				cell;

	public CourseOffering		course;
	public String				crn;
	public int					crnIndex;
	public int 					studentIndex;	
	// --- For use in html/wod files ---
	public String				aColumn;
	public NSMutableArray<String>	aPreviewLine;
	public NSMutableArray<NSMutableArray<String>> previewLines;
	public StudentRow			aRow;
	public int					sheetIndex;
	public int					headerIndex;
	public int					mappingIndex;
	public int					maxValIndex;
	public int					colIndex;
	public int                  index;
	public int					cellIndex;
	// --- ----

	public NSMutableArray<Gradebook> allSheets;
	public Gradebook aSheet;

	public NSMutableArray<String> columns;
	public NSMutableArray<String> mappings;
	public NSMutableArray<String> maxValues;
	
	public NSMutableArray<NSMutableArray<String>> allColumns;
	public NSMutableArray<NSMutableArray<String>> allMappings;

	public AuthenticationDomain domain;

	public int sheetCount;
	private HSSFWorkbook wb;

	//~ Public Methods ........................................................
	// ----------------------------------------------------------

	public WOActionResults uploadGradebook()
	{
		clearAllMessages();
		return replace();
	}

	private WOActionResults replace(){
		JavascriptGenerator page = new JavascriptGenerator();
		page.refresh("fileInfo");
		return page;	
	}

	public WOComponent saveAllSheets(){
		for (Gradebook gBook : allSheets){
			log.debug("Coursework l: " + gBook.getCourseworkList().count());
			log.debug("Max Values l: " + gBook.getMaxValueRow().count());
			Map<String, Map<Integer, Coursework>> sectionAssignments = new HashMap<String, Map<Integer,Coursework>>();
			for (int i = 0; i < gBook.getCourseworkList().count(); i ++){
				String cwTitle = gBook.getCourseworkList().get(i);
				for (String localCrn : gBook.getCrns()){
					Coursework cw = Coursework.create(localContext(), 60.0, cwTitle, 80.0);
					Double max = null;
					if (gBook.getMaxValueRow().count() > 0 && i +2 < gBook.getMaxValueRow().count()) max = Double.valueOf(gBook.getMaxValueRow().get(i+2));
					cw.setMax_result(max);
					cw.setCourseOfferingRelationship(getCourse(localCrn));
					this.applyLocalChanges();
					mapOutcomes(gBook.getMappingRow().get(i), cw);
					if (sectionAssignments.containsKey(localCrn)){
						sectionAssignments.get(localCrn).put(i, cw);
					} else {
						Map<Integer, Coursework> courseworks = new HashMap<Integer, Coursework>();
						courseworks.put(i, cw);
						sectionAssignments.put(localCrn, courseworks);
					}
				}
			}
			
			
			
			for (StudentRow student : gBook.getDataRows()){
				User user = getUser(student.getStudentId());
				for (int dataIndex = 0; dataIndex < student.getDataCells().count(); dataIndex ++){
					Coursework cw = sectionAssignments.get(student.getCrn()).get(dataIndex);
					Double pointsEarned = Double.valueOf(student.getDataCells().get(dataIndex));
					Double percentEarned =  pointsEarned / cw.max_result();
					StudentAnswer.create(localContext(), percentEarned, pointsEarned, cw, user);
				}
			}
		}
		
//		CourseReportsPage page2 = (CourseReportsPage)pageWithName(CourseReportsPage.class.getName());
		
		ReportsPage page = (ReportsPage)pageWithName(ReportsPage.class.getName());
		page.nextPage = this;
		page.excellentCutoff = new BigDecimal(85.0);
		page.moderateCutoff = new BigDecimal(60.0);
		page.calculateOutcomes();
		return page;
	}

	private void mapOutcomes(String mappingItem, Coursework cw) {
		List<String> codes = new ArrayList<String>();
		StringBuilder letterCode = new StringBuilder();
		StringBuilder numberCode = new StringBuilder();
		for (int i = 0; i < mappingItem.length(); i++){
			char c = mappingItem.charAt(i);
			if (String.valueOf(c).matches("[A-Za-z]")) letterCode.append(c);
			if (String.valueOf(c).matches("\\d")) numberCode.append(c);
		}
		if (letterCode.length() > 0) codes.add(letterCode.toString());
		if (numberCode.length() > 0){
			String temp = "";
			if (numberCode.length() == 1)
				temp = "0";
			temp += numberCode.toString();
			codes.add(temp);
		}
		determineOutcome(codes, cw);
	}

	private void determineOutcome(List<String> mapCodes, Coursework cw) {
		ExternalOutcome eOut = null;
		ProgramOutcome pOut = null;
		for (String code : mapCodes){
			if (eOut == null){
				eOut = ExternalOutcome.uniqueObjectMatchingQualifier(localContext(), ExternalOutcome.microLabel.is(code));
				if (eOut == null && pOut == null){
					pOut = ProgramOutcome.uniqueObjectMatchingQualifier(localContext(), ProgramOutcome.microLabel.is(code));
				}
			} else if (pOut == null){
				pOut = ProgramOutcome.uniqueObjectMatchingQualifier(localContext(), ProgramOutcome.microLabel.is(code));
			}
			if (eOut != null && pOut != null)
				System.out.println("eOut: " + eOut.microLabel() + ", pOut: " + pOut.microLabel());
			if (eOut == null) System.out.println("no eOut for " + code);
			if (pOut == null) System.out.println("no pOut for " + code);
		}
		OutcomePair op = null;
		
		// this currently fails to handle single value mappings or multiple 
		// mappings of the same type (external or program) 

		if (eOut != null && pOut != null){
			op = OutcomePair.uniqueObjectMatchingQualifier(localContext(), OutcomePair.externalOutcome.is(eOut).and(OutcomePair.programOutcome.is(pOut)));
		}
		
		if (eOut != null && pOut != null && op == null){
			System.out.println("eh?");
			// should I create an OutcomePair here?
		}
		if (op != null){
			cw.setOutcomePairRelationship(op);
			log.debug("OP id: " + cw.outcomePair().id());
			localContext().saveChanges();
		}
	}

	public void initializeData(){
		allSheets = new NSMutableArray<Gradebook>();
		aPreviewLine = new NSMutableArray<String>();
		previewLines = new NSMutableArray<NSMutableArray<String>>();
		allColumns = new NSMutableArray<NSMutableArray<String>>();
		allMappings = new NSMutableArray<NSMutableArray<String>>();
		try {
			wb = new HSSFWorkbook(data.stream());
			sheetCount = wb.getNumberOfSheets();
			if (sheetCount == 0){
				// throw an error and return;
			}
			for (int i = 0; i < sheetCount; i++){
				getSheetValues(i);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getSheetValues(int currentSheetIndex) {
		Gradebook grades = new Gradebook();
		HSSFSheet sheet = wb.getSheetAt(currentSheetIndex);
		int rows = sheet.getPhysicalNumberOfRows();
		columns = new NSMutableArray<String>();
		mappings = new NSMutableArray<String>();
		maxValues = new NSMutableArray<String>();
		for (int k = 0; k < rows; k++){
			HSSFRow row = sheet.getRow(k);
			if (row == null){
				row = sheet.createRow(k);
			}
			RowTypes rowType = determineRowType(row);
			if (rowType == RowTypes.EMPTY) continue;
			ColumnTypes colType = null;
			int maxCell = row.getLastCellNum();
			StudentRow sRow = new StudentRow();
			NSMutableArray<String> courseworkCols = new NSMutableArray<String>();
			for (int cellCount = 0; cellCount < maxCell; cellCount++){
				Cell cell = row.getCell(cellCount);
				if (cell == null) cell = row.createCell(cellCount);

				if (rowType.equals(RowTypes.HEADER)){
					columns.add(getCellValue(cell));
					colType = determineColumnType(getCellValue(cell));
					if (colType.equals(ColumnTypes.COURSE)){
						crnIndex = cell.getColumnIndex();
					}
					if (colType.equals(ColumnTypes.ASSIGNMENT) ||
							colType.equals(ColumnTypes.QUESTION)) courseworkCols.add(getCellValue(cell));
					if (colType.equals(ColumnTypes.STUDENT)) studentIndex = cell.getColumnIndex();
				}

				if (rowType.equals(RowTypes.MAPPING)){
					String val = getCellValue(cell);
					if (val.indexOf(".") != -1) val = val.substring(0, val.indexOf("."));
					if (val.length() <= 3) mappings.add(val);
				}

				if (rowType.equals(RowTypes.MAX_POINTS)){
					maxValues.add(getCellValue(cell));
				}
				if (rowType.equals(RowTypes.STUDENT)){
					String val = getCellValue(cell);
					if (cell.getColumnIndex() == crnIndex){
						sRow.setCrn(val);
						grades.addCrn(val);
					}
					else if (cell.getColumnIndex() == studentIndex) sRow.setStudentId(val);
					else sRow.addCell(val);
				}
			}
			if (!courseworkCols.isEmpty()) grades.setCourseworkList(courseworkCols);
			if (!columns.isEmpty()){
				grades.setHeaderRow(columns);
			}
			if (!mappings.isEmpty()){
				grades.setMappingRow(mappings);
			}
			if (maxValues.count() > 0) grades.setMaxValueRow(maxValues);
			if (sRow.getDataCells().count() > 0) grades.getDataRows().add(sRow);
		}
		if (grades.getDataRows().count() > 0) allSheets.add(grades);
	}

	public WOActionResults refresh()
	{
		System.out.println("mapIndex: " + mappingIndex);
		System.out.println("sheetIndex: " + sheetIndex);
		System.out.println("cell: " + cell);
		System.out.println("aSheet.mapRow size: " + aSheet.getMappingRow().size());
		allSheets.get(sheetIndex).getMappingRow().set(mappingIndex, cell);
		log.debug("refresh()");
		clearAllMessages();
		JavascriptGenerator page = new JavascriptGenerator();
		page.refresh("content");
		return page;
	}

	//~ Private Methods .......................................................
	private static ColumnTypes determineColumnType(String value){
		value = value.toLowerCase();
		if (value.contains("student")
				|| value.contains("@")
				|| Pattern.matches(STUDENT_REGEX, value)
				|| Pattern.matches(STUDENT_REGEX_2, value)) return ColumnTypes.STUDENT;
		if (value.contains("question") ||
				Pattern.matches(QUESTION_REGEX, value)) return ColumnTypes.QUESTION;
		if (value.contains("hw") 
				|| value.contains("homework")
				|| value.contains("exam")
				|| value.contains("project")
				|| value.contains("final")) return ColumnTypes.ASSIGNMENT;
		if (value.contains("total")) return ColumnTypes.TOTAL;
		if (value.equalsIgnoreCase("course") || value.equalsIgnoreCase("crn") || value.equalsIgnoreCase("Section"))
			return ColumnTypes.COURSE;
		return ColumnTypes.UNKNOWN;
	}

	private static RowTypes determineRowType(HSSFRow row) {
		if (row.getPhysicalNumberOfCells() == 0){
			return RowTypes.EMPTY;
		}
		List<String> colHeaders = new ArrayList<String>();
		colHeaders.add("question");
		colHeaders.add("total");
		colHeaders.add("student");
		int curr = row.getFirstCellNum();
		int end = row.getLastCellNum();
		for (int i = curr; i <= end; i++){
			Cell cell;
			if (row.getCell(i) == null) cell = row.createCell(i); 
			cell = row.getCell(i);
			String cellValue = getCellValue(cell).toLowerCase();
			if (cellValue.contains("@") ||
					Pattern.matches(STUDENT_REGEX, cellValue)){
				return RowTypes.STUDENT;
			}
			if (cellValue.toLowerCase().contains("excellent")) return RowTypes.EXCELLENT_CUTOFF;
			if (cellValue.toLowerCase().contains("acceptable")) return RowTypes.ACCEPTABLE_CUTOFF;
			if (colHeaders.contains(cellValue)){
				return RowTypes.HEADER;
			}
			if (cellValue.contains("mapping") 
					|| Pattern.matches(OUTCOME_REGEX, cellValue)){
				return RowTypes.MAPPING;
			}
			if (cellValue.contains("max")){
				return RowTypes.MAX_POINTS;
			}
		}
		return RowTypes.UNKNOWN;
	}

	private static String getCellValue(Cell cell){
		int cellType = cell.getCellType();
		String cellValue;
		switch (cellType){
		case HSSFCell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		default:
			cellValue = "";
			break;
		}

		return cellValue;
	}

	private CourseOffering getCourse(String crn){
		EOEditingContext ec = localContext();
		course = CourseOffering.offeringForCrn(ec, crn);
		if (course == null){
			course = CourseOffering.create(ec);
			course.setCrn(crn);
			ec.saveChanges();
		}
		return course;
	}
	
	private User getUser(String id){
		EOEditingContext ec = localContext();
		User user = User.lookupUserByEmail(ec, id, user().authenticationDomain());
		if (user == null){
			user = User.createUser(id.substring(0, id.indexOf("@")), null, user().authenticationDomain(), Byte.valueOf("0"), ec);
		}
		return user;
	}
	
	static Logger log = Logger.getLogger(UploadGradebookPage.class );
}