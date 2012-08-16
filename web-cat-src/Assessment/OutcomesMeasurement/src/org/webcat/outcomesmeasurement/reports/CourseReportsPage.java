package org.webcat.outcomesmeasurement.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.webcat.core.Course;
import org.webcat.core.CourseOffering;
import org.webcat.outcomesmeasurement.Coursework;
import org.webcat.outcomesmeasurement.OutcomePair;
import org.webcat.outcomesmeasurement.OutcomePairStatistic;
import org.webcat.outcomesmeasurement.StudentAnswer;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

@SuppressWarnings("serial")
public class CourseReportsPage extends BaseReportsPage {
	public CourseReportsPage(WOContext context) {
		super(context);
		allCourses = Course.allObjects(localContext());
		if (allCourses.count() > 0) currCourse = allCourses.get(0);
    }
    
	public String reportType;
	public String newReportType;

	public Course aCourse;
	public NSArray<Course> allCourses;
	public Course currCourse = null;
	
    
    public WOComponent switchCourse(){
		currCourse = aCourse;
		outcomePairStats = getOutcomePairStats(currCourse);
		return null;
	}
	
	@Override
    public void appendToResponse(WOResponse response, WOContext context) {
    	outcomePairStats = null;
    	if (currCourse != null)
    		outcomePairStats = getOutcomePairStats(currCourse);
    	super.appendToResponse(response, context);
    }
    
    private NSMutableArray<OutcomePairStatistic> getOutcomePairStats(Course course){
    	Map<OutcomePair, OutcomePairStatistic> opMap = new HashMap<OutcomePair, OutcomePairStatistic>();
		NSMutableArray<OutcomePairStatistic> retVal = new NSMutableArray<OutcomePairStatistic>();
		
    	NSArray<CourseOffering> offerings = course.offerings();
    	NSArray<Coursework> allCoursework = Coursework.objectsMatchingQualifier(localContext(), Coursework.courseOffering.in(offerings).and(Coursework.outcomePair.isNotNull()));
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
}