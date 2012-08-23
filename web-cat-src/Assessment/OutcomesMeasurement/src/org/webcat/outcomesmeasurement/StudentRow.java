package org.webcat.outcomesmeasurement;

import com.webobjects.foundation.NSMutableArray;

public class StudentRow {
	private String studentId;
	private String crn;
	private NSMutableArray<String> dataCells;
	public StudentRow(){
		dataCells = new NSMutableArray<String>();
	}
	public void setCrn(String crn) {
		if (crn.indexOf(".") != -1){
			this.crn = crn.substring(0, crn.indexOf("."));
		} else {
			this.crn = crn;
		}
	}
	public String getCrn() {
		return crn;
	}
	public void setDataCells(NSMutableArray<String> dataCells) {
		this.dataCells = dataCells;
	}
	public NSMutableArray<String> getDataCells() {
		return dataCells;
	}
	
	public void addCell(String cell){
		this.dataCells.add(cell);
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentId() {
		return studentId;
	}
}
