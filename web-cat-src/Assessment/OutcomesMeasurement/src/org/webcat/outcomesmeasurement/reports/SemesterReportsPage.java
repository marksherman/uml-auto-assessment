package org.webcat.outcomesmeasurement.reports;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import org.webcat.core.CourseOffering;
import org.webcat.core.Semester;
import org.webcat.outcomesmeasurement.Coursework;
import org.webcat.outcomesmeasurement.MeasureOfOffering;
import org.webcat.outcomesmeasurement.OutcomePair;
import org.webcat.outcomesmeasurement.OutcomePairStatistic;
import org.webcat.outcomesmeasurement.StudentAnswer;

@SuppressWarnings("serial")
public class SemesterReportsPage extends BaseReportsPage {	
	public String reportType;
	public String newReportType;
	
	public Semester currSemester;
	public NSArray<Semester> allSems;
	public Semester aSem;
	
	public SemesterReportsPage(WOContext context) {
        super(context);
        if (reportType == null) reportType = "outcomePairs";
        allSems = Semester.allObjects(localContext());
        currSemester = findMostRecentCompleted(allSems);
    }
	
	public WOComponent switchSemester(){
		currSemester = aSem;
		return null;
	}
	
    @Override
    public void appendToResponse(WOResponse response, WOContext context) {
    	if (allSems.count() > 0){
    		NSArray<CourseOffering> offerings = CourseOffering.objectsMatchingQualifier(localContext(), CourseOffering.semester.eq(currSemester));
    		outcomePairStats = new NSMutableArray<OutcomePairStatistic>();
    		NSArray<Coursework> allCoursework = Coursework.objectsMatchingQualifier(localContext(), Coursework.courseOffering.in(offerings).and(Coursework.outcomePair.isNotNull()));
    		if (reportType.equals("measures")){
    			NSArray<MeasureOfOffering> measures = MeasureOfOffering.objectsMatchingQualifier(localContext(), MeasureOfOffering.courseOffering.in(offerings));
    			outcomePairStats = getStatsForMeasures(measures);
    		} else if (reportType.equals("outcomePairs")){
    			outcomePairStats = getStatsForCoursework(allCoursework);
    		}
    		
    	}
    	super.appendToResponse(response, context);
    }
    
    private NSMutableArray<OutcomePairStatistic> getStatsForCoursework(
			NSArray<Coursework> allCoursework) {
    	Map<OutcomePair, OutcomePairStatistic> opMap = new HashMap<OutcomePair, OutcomePairStatistic>();
    	NSMutableArray<OutcomePairStatistic> retVal = new NSMutableArray<OutcomePairStatistic>();
    	for (Coursework cw : allCoursework){
    		OutcomePair pair = cw.outcomePair();
    		if (!opMap.containsKey(pair)){
    			OutcomePairStatistic tempStat = new OutcomePairStatistic();
    			tempStat.setOutcomePair(pair);
    			opMap.put(pair, tempStat);
    		}
    		OutcomePairStatistic opStat = opMap.get(pair);
    		NSArray<StudentAnswer> answers = cw.studentAnswers();
			for (StudentAnswer answer: answers){
				Double percent = answer.percentEarned();
				if (percent != null){
					percent = percent * 100;
					if (percent.compareTo(Double.valueOf(opStat.getExcellentCutoff())) >= 0){
						opStat.incrementExcellent();
					} else if (percent.compareTo(Double.valueOf(opStat.getAdequateCutoff())) >= 0){
						opStat.incrementAdequate();
					} else { 
						opStat.incrementUnsatisfactory();
					}
				}
			}
			opMap.put(pair, opStat);
    	}
    	Set<OutcomePair> keySet = opMap.keySet();
    	for (OutcomePair op : keySet){
    		OutcomePairStatistic ops = opMap.get(op);
    		if (ops.getTotalNumber() > 0) retVal.add(ops);
    	}
		return retVal;
	}

	private NSMutableArray<OutcomePairStatistic> getStatsForMeasures(NSArray<MeasureOfOffering> measures){
    	NSMutableArray<OutcomePairStatistic> retVal = new NSMutableArray<OutcomePairStatistic>();
//    	for (MeasureOfOffering off : measures){
//    		Integer excellent = off.excellent();
//    		Integer adequate = off.adequate();
//    		Integer unsatisfactory = off.unsatisfactory();
//    		off.ex
//    		NSArray<OutcomePair> pairs = off.measure().outcomePairs();
//    		
//    	}
    	return retVal;
    }
    
	private Semester findMostRecentCompleted(NSArray<Semester> semesters){
		Semester retVal = null;
		for (Semester sem : semesters){
			if (sem.semesterEndDate().compareTo(Calendar.getInstance().getTime()) > 0) continue;
			if (retVal == null) retVal = sem;
			else {
				if (sem.semesterEndDate().compare(retVal.semesterEndDate()) > 0) retVal = sem;
			}
		}
		return retVal;
	}
}