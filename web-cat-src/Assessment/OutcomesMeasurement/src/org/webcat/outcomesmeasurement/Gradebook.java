package org.webcat.outcomesmeasurement;

import com.webobjects.foundation.NSMutableArray;

public class Gradebook {	
	private NSMutableArray<String> crns;
	private NSMutableArray<String> headerRow;
	private NSMutableArray<String> mappingRow;
	private NSMutableArray<String> maxValueRow;
	private NSMutableArray<String> courseworkList;
	private NSMutableArray<StudentRow> dataRows;
	
	public Gradebook(){
		setDataRows(new NSMutableArray<StudentRow>());
		setCrns(new NSMutableArray<String>());
		setCourseworkList(new NSMutableArray<String>());
	}

	public void setHeaderRow(NSMutableArray<String> headerRow) { this.headerRow = headerRow; }
	public NSMutableArray<String> getHeaderRow() { return headerRow; }

	public void setMappingRow(NSMutableArray<String> mappingRow) { this.mappingRow = mappingRow; }
	public NSMutableArray<String> getMappingRow() { return mappingRow; }

	public void setDataRows(NSMutableArray<StudentRow> dataRows) { this.dataRows = dataRows; }
	public NSMutableArray<StudentRow> getDataRows() { return dataRows; }

	public void setMaxValueRow(NSMutableArray<String> maxValueRow) { this.maxValueRow = maxValueRow; }
	public NSMutableArray<String> getMaxValueRow() { return maxValueRow; }

	public void setCrns(NSMutableArray<String> crns) { this.crns = crns; }
	public NSMutableArray<String> getCrns() { return crns; }

	public void setCourseworkList(NSMutableArray<String> courseworkList) { this.courseworkList = courseworkList; }
	public NSMutableArray<String> getCourseworkList() { return courseworkList; }
	
	public void addCrn(String newCrn) {
		if (newCrn.indexOf(".")!= -1) newCrn = newCrn.substring(0, newCrn.indexOf("."));
		if (!crns.contains(newCrn)) crns.add(newCrn);
	}
}