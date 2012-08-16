package org.webcat.outcomesmeasurement.reports;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.webcat.core.Semester;
import org.webcat.outcomesmeasurement.Coursework;
import org.webcat.outcomesmeasurement.OutcomePair;
import org.webcat.outcomesmeasurement.OutcomePairStatistic;
import org.webcat.outcomesmeasurement.StudentAnswer;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;


public class OutcomeReports {
	
	/**
	 * Generates the default report.
	 * The default report pulls all outcome pairs found in the system and then pulls all grades for that
	 * outcome pair. It takes the results and categorizes the scores into three categories - poor, 
	 * moderate and excellent.
	 * @param context			The EOEdtingContext of the web app.
	 * @param excellentCutoff	The lowest percentage score that still qualifies as excellent. Also acts as the upper bound for the moderate scores.
	 * @param moderateCutoff	The lowest percentage score that still qualifies as moderate. Also acts as the upper bound for the poor scores.
	 * @return					Returns an array of OutcomePairStatistics for display on the reports page.
	 */
	public static NSMutableArray<OutcomePairStatistic> generateDefaultReport(EOEditingContext context, 
			BigDecimal excellentCutoff, 
			BigDecimal moderateCutoff){
		NSMutableArray<OutcomePairStatistic> returnList = new NSMutableArray<OutcomePairStatistic>();
		NSArray<OutcomePair> allOutcomePairs = OutcomePair.allObjects(context);
		for (OutcomePair pair : allOutcomePairs){
			OutcomePairStatistic opStat = new OutcomePairStatistic();
			opStat.setOutcomePair(pair);
			NSArray<Coursework> courseworks = pair.courseworks();
			for (Coursework cw : courseworks){
				NSArray<StudentAnswer> answers = cw.studentAnswers();
				for (StudentAnswer answer: answers){
					BigDecimal percent = new BigDecimal(answer.percentEarned());
					if (percent != null){
						percent = percent.multiply(new BigDecimal(100));
						if (percent.compareTo(excellentCutoff) >= 0){
							opStat.incrementExcellent();
						} else if (percent.compareTo(moderateCutoff) >= 0){
							opStat.incrementAdequate();
						}
						else { 
							opStat.incrementUnsatisfactory();
						}
					}
				}
			}
			if (opStat.getTotalNumber() > 0) returnList.add(opStat);
		}
		return returnList;
	}
	
	public static NSMutableArray<OutcomePairStatistic> generateOutcomeOverTimeReport(EOEditingContext context, 
			BigDecimal excellentCutoff, 
			BigDecimal moderateCutoff,
			OutcomePair outcomePair){
		NSMutableArray<OutcomePairStatistic> returnList = new NSMutableArray<OutcomePairStatistic>();
		Map<String, OutcomePairStatistic> semesterOutcomesMap = new HashMap<String, OutcomePairStatistic>();
		NSArray<Coursework> courseworks = outcomePair.courseworks();
		for (Coursework cw : courseworks){
			NSArray<StudentAnswer> answers = cw.studentAnswers();
			Semester sem = cw.courseOffering().semester();
			if (sem == null) continue;
			if (semesterOutcomesMap.containsKey(sem.toString())){
				System.out.println("Semester A");
			}else {
				OutcomePairStatistic opStat = new OutcomePairStatistic();
				opStat.setOutcomePair(outcomePair);
				opStat.setSemester(sem);
				semesterOutcomesMap.put(sem.toString(), opStat);
				System.out.println("Semester B");
			}
			for (StudentAnswer answer : answers){
				BigDecimal percent = new BigDecimal(answer.percentEarned());
				if (percent != null){
					percent = percent.multiply(new BigDecimal(100));
					if (percent.compareTo(excellentCutoff) >= 0){
						semesterOutcomesMap.get(sem.toString()).incrementExcellent();
					} else if (percent.compareTo(moderateCutoff) >= 0){
						semesterOutcomesMap.get(sem.toString()).incrementAdequate();
					}
					else { 
						semesterOutcomesMap.get(sem.toString()).incrementUnsatisfactory();
					}
				}
			}
		}
		Set<String> allSems = semesterOutcomesMap.keySet();
		System.out.println("Map Size: " + allSems.size());
		for (String entry : allSems){
			if (semesterOutcomesMap.get(entry).getTotalNumber() > 0) returnList.add(semesterOutcomesMap.get(entry));
		}
		return returnList;
	}
}