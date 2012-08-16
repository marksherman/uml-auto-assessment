package org.webcat.outcomesmeasurement.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import org.webcat.outcomesmeasurement.Coursework;
import org.webcat.outcomesmeasurement.OutcomePair;
import org.webcat.outcomesmeasurement.OutcomePairStatistic;
import org.webcat.outcomesmeasurement.Program;
import org.webcat.outcomesmeasurement.ProgramOutcome;
import org.webcat.outcomesmeasurement.StudentAnswer;

@SuppressWarnings("serial")
public class OutcomeReportsPage extends BaseReportsPage {
	public OutcomeReportsPage(WOContext context) {
		super(context);
	}
	public String errorMessage = null;

	public String reportType;
	public String newReportType;

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		errorMessage = null;
		NSArray<Program> programs = Program.allObjects(localContext());
		if (programs.count() == 0) { errorMessage = "No eligible programs found."; }
		else {
			Program prog = programs.get(0);
			NSArray<ProgramOutcome> programOutcomes = ProgramOutcome.objectsMatchingQualifier(localContext(), ProgramOutcome.program.eq(prog));
			NSArray<OutcomePair> programOutcomePairs = OutcomePair.objectsMatchingQualifier(localContext(), OutcomePair.programOutcome.in(programOutcomes));
			outcomePairStats = getStatsForOutcomePairs(programOutcomePairs);
		}
		super.appendToResponse(response, context);
	}

	private NSMutableArray<OutcomePairStatistic> getStatsForOutcomePairs(
			NSArray<OutcomePair> abetOutcomePairs) {
		Map<OutcomePair, OutcomePairStatistic> opMap = new HashMap<OutcomePair, OutcomePairStatistic>();
		NSMutableArray<OutcomePairStatistic> retVal = new NSMutableArray<OutcomePairStatistic>();

		for (OutcomePair pair : abetOutcomePairs){
			if (!opMap.containsKey(pair)){
				OutcomePairStatistic tempStat = new OutcomePairStatistic();
				tempStat.setOutcomePair(pair);
				opMap.put(pair, tempStat);
			}
			OutcomePairStatistic opStat = opMap.get(pair);
			NSArray<Coursework> allCw = pair.courseworks();
			for (Coursework cw : allCw){
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