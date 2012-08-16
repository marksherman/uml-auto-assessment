package org.webcat.outcomesmeasurement.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.webcat.core.Course;
import org.webcat.core.CourseOffering;
import org.webcat.core.Department;
import org.webcat.outcomesmeasurement.BasePage;
import org.webcat.outcomesmeasurement.Coursework;
import org.webcat.outcomesmeasurement.MeasureOfOffering;
import org.webcat.outcomesmeasurement.OutcomePair;
import org.webcat.outcomesmeasurement.OutcomePairStatistic;
import org.webcat.outcomesmeasurement.StudentAnswer;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

@SuppressWarnings("serial")
public class DeptReportsPage extends BaseReportsPage {
    public DeptReportsPage(WOContext context) {
        super(context);
        if (reportType == null) reportType = "outcomePairs";
    }

	public String reportType;
	public String newReportType;
	public String currDepartment;

    @Override
    public void appendToResponse(WOResponse response, WOContext context) {
    	NSArray<Department> depts = Department.allObjects(localContext());
    	if (depts.count() > 0){
    		outcomePairStats = new NSMutableArray<OutcomePairStatistic>();
    		Department dept = depts.get(0);
    		currDepartment = dept.name();
    		NSArray<Course> courses = dept.courses();
    		for (Course course : courses){
    			NSArray<CourseOffering> offerings = course.offerings();
    			NSArray<Coursework> allCoursework = Coursework.objectsMatchingQualifier(localContext(), Coursework.courseOffering.in(offerings).and(Coursework.outcomePair.isNotNull()));
        		if (reportType.equals("measures")){
        			NSArray<MeasureOfOffering> measures = MeasureOfOffering.objectsMatchingQualifier(localContext(), MeasureOfOffering.courseOffering.in(offerings));
        			outcomePairStats = getStatsForMeasures(measures);
        		} else if (reportType.equals("outcomePairs")){
        			outcomePairStats = getStatsForCoursework(allCoursework);
        		}
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

	private NSMutableArray<OutcomePairStatistic> getStatsForMeasures(
			NSArray<MeasureOfOffering> measures) {
		// TODO Auto-generated method stub
		return null;
	}
}