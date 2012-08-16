package org.webcat.outcomesmeasurement;

import org.webcat.core.Semester;

public class OutcomePairStatistic {
	private boolean dirtyCache = false;
	private int excellentCount = 0;
	private int adequateCount = 0;
	private int unsatisfactoryCount = 0;
	private int totalNumber = 0;
	private float excellentAsPercent = 0;
	private float adequateAsPercent = 0;
	private float unsatisfactoryAsPercent = 0;
	private int excellentCutoff = 70;
	private int adequateCutoff = 40;

	public int getExcellentCutoff() {
		return excellentCutoff;
	}
	public void setExcellentCutoff(int excellentCutoff) {
		this.excellentCutoff = excellentCutoff;
	}
	public int getAdequateCutoff() {
		return adequateCutoff;
	}
	public void setAdequateCutoff(int adequateCutoff) {
		this.adequateCutoff = adequateCutoff;
	}
	private Semester semester;
	
	private OutcomePair outcomePair;
	
	public int getExcellentCount() {
		return excellentCount;
	}
	public void setExcellentCount(int excellentCount) {
		this.excellentCount = excellentCount;
	}
	public int getAdequateCount() {
		return adequateCount;
	}
	public void setAdequateCount(int moderateCount) {
		this.adequateCount = moderateCount;
	}
	public int getUnsatisfactoryCount() {
		return unsatisfactoryCount;
	}
	public void setUnsatisfactoryCount(int poorCount) {
		this.unsatisfactoryCount = poorCount;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public OutcomePair getOutcomePair() {
		return outcomePair;
	}
	public void setOutcomePair(OutcomePair outcomePair) {
		this.outcomePair = outcomePair;
	}
	
	public void incrementExcellent(){
		this.excellentCount++;
		this.totalNumber++;
		if (!dirtyCache) dirtyCache = true;
	}
	public void incrementAdequate(){ 
		this.adequateCount++;
		this.totalNumber++;
		if (!dirtyCache) dirtyCache = true;
	}
	public void incrementUnsatisfactory(){ 
		this.totalNumber++;
		this.unsatisfactoryCount++;
		if (!dirtyCache) dirtyCache = true;
	}
	
	private void resetPercents(){
		setExcellentAsPercent();
		setUnsatisfactoryAsPercent();
		setAdequateAsPercent();
	}
	
	private void setExcellentAsPercent(){
		this.excellentAsPercent = doDivision(this.excellentCount);
	}
	
	private void setAdequateAsPercent(){
		this.adequateAsPercent = doDivision(this.adequateCount);
	}
	
	private void setUnsatisfactoryAsPercent(){
		this.unsatisfactoryAsPercent = doDivision(this.unsatisfactoryCount);
	}
	
	public float getExcellentAsPercent(){ 
		if (dirtyCache) resetPercents();
		return this.excellentAsPercent; 
	}
	public float getAdequateAsPercent(){ 
		if (dirtyCache) resetPercents();
		return this.adequateAsPercent;
	}
	public float getUnsatisfactoryAsPercent(){ 
		if (dirtyCache) resetPercents();
		return this.unsatisfactoryAsPercent; 
	}
	
	private float doDivision(float dividend){
		if (this.totalNumber == 0 ) return 0;
		return dividend / this.totalNumber * 100;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	public Semester getSemester() {
		return semester;
	}
}
