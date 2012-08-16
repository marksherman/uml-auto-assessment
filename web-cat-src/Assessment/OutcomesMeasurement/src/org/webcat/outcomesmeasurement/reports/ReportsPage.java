package org.webcat.outcomesmeasurement.reports;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.webcat.outcomesmeasurement.BasePage;
import org.webcat.outcomesmeasurement.OutcomePairStatistic;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

@SuppressWarnings("serial")
public class ReportsPage extends BasePage {
	public BigDecimal excellentCutoff;
	public BigDecimal moderateCutoff;
	
	public String newExcellentCutoff = null;
	public String newModerateCutoff = null;
	public String errorMsg = "";
	
	public String viewType = "Summary View";
	public String reportType = "default";
	public String newReportType;
	public boolean summaryView = true;
	public boolean detailView = false;
	
	public NSMutableArray<OutcomePairStatistic> outcomePairStats;
	public OutcomePairStatistic anOutcomeStat;
	
	public NSTimestamp endDate;
	public NSTimestamp startDate;
	
	public WOComponent updateCutoffs(){
		if (newReportType != null) System.out.println("nrt: " + newReportType);
		if (reportType.equals("default")) calculateOutcomes();
		if (reportType.equals("overTime")) viewOverTime();
		return null;
	}
	
	public WOComponent viewOverTime(){
		reportType = "overTime";
		outcomePairStats = OutcomeReports.generateOutcomeOverTimeReport(localContext(), excellentCutoff, moderateCutoff, anOutcomeStat.getOutcomePair());
		return null;
	}
	
	public WOComponent setViewToSummary(){
		viewType = "Summary View";
		summaryView = true;
		detailView = false;
		return null;
	}
	
	public WOComponent setViewToDetail(){
		viewType = "Detailed View";
		summaryView = false;
		detailView = true;
		return null;
	}
	
	public boolean isSummaryView(){ return summaryView; }
	public boolean isDetailView(){ return detailView; }
	
	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		if (outcomePairStats == null){
			excellentCutoff = new BigDecimal(85.0);
			moderateCutoff = new BigDecimal(60.0);
			calculateOutcomes();
		}
		super.appendToResponse(response, context);
	}
	
	public void calculateOutcomes() {
		errorMsg = "";
		outcomePairStats = new NSMutableArray<OutcomePairStatistic>();
		
		if (newExcellentCutoff != null){
			try {
				excellentCutoff = new BigDecimal(newExcellentCutoff);
			} catch (Exception e){
				errorMsg = "ERROR: You must enter a valid number for the cutoff values";
			}
		} else {
			newExcellentCutoff = String.valueOf(excellentCutoff);
		}
		
		if (newModerateCutoff != null){
			try {
				moderateCutoff = new BigDecimal(newModerateCutoff);
			} catch (Exception e){
				errorMsg = "ERROR: You must enter a valid number for the cutoff values";
			}
		} else {
			newModerateCutoff = String.valueOf(moderateCutoff);
		}
		if (reportType.equals("default")) outcomePairStats = OutcomeReports.generateDefaultReport(localContext(), excellentCutoff, moderateCutoff);
		
	}
	
	public ReportsPage(WOContext context) {
        super(context);
    }
    
    static Logger log = Logger.getLogger(ReportsPage.class);
}