package org.webcat.outcomesmeasurement;

import com.webobjects.foundation.NSMutableArray;

public class SpreadsheetRow {
	private String idCell;
	private NSMutableArray<String> dataCells;
	public String getIdCell() {
		return idCell;
	}
	public void setIdCell(String idCell) {
		this.idCell = idCell;
	}
	public NSMutableArray<String> getDataCells() {
		return dataCells;
	}
	public void setDataCells(NSMutableArray<String> dataCells) {
		this.dataCells = dataCells;
	}
}
