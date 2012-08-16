/*==========================================================================*\
 |  $Id: OdaResultSet.java,v 1.3 2012/05/09 14:34:43 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2012 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.reporter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Enumeration;
import org.webcat.oda.commons.IWebCATResultSet;
import org.webcat.oda.commons.WebCATDataException;
import org.webcat.woextensions.ReadOnlyEditingContext;
import org.webcat.woextensions.WCFetchSpecification;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.enhance.ExpressionAccessor;
import org.apache.log4j.Logger;
import org.webcat.core.EOBase;
import org.webcat.core.ObjectQuery;
import org.webcat.core.QualifierUtils;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;

//-------------------------------------------------------------------------
/**
 * A result set for a report.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2012/05/09 14:34:43 $
 */
public class OdaResultSet
    implements IWebCATResultSet
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a result set.
     *
     * @param dataSetId
     *      The ID of the ReportDataSet for which this result set is being
     *      generated
     * @param job
     *      The ManagedReportGenerationJob that is generating the report that
     *      will contain this data
     * @param query
     *      The query defining this result set
     */
    public OdaResultSet(int dataSetId, ManagedReportGenerationJob job,
            ObjectQuery query)
    {
        this.dataSetId = dataSetId;
        this.job = job;
        this.query = query;

        currentRow = 0;
        rawCurrentRow = 0;
        lastThrottleCheck = 0;
        currentBatchSize = 100;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    public void close()
    {
        editingContext.dispose();

//        ReportGenerationTracker.getInstance().completeDataSetForJobId(jobId);
        job.completeCurrentTask();
    }


    // ----------------------------------------------------------
    public int currentRow()
    {
        return currentRow;
    }


    // ----------------------------------------------------------
    public void execute()
    {
        recycleEditingContext();

        EOQualifier qualifier = query.qualifier(editingContext);

        EOQualifier[] quals = QualifierUtils.partitionQualifier(
            qualifier, query.objectType());
        fetchQualifier = quals[0];
        inMemoryQualifier = EOBase.accessibleBy(job.user()).and(quals[1]);

        WCFetchSpecification<?> fetch =
            new WCFetchSpecification<EOEnterpriseObject>(
                query.objectType(), fetchQualifier, null);
        iterator =
            new ERXFetchSpecificationBatchIterator(fetch, editingContext);
        iterator.setBatchSize(currentBatchSize);

//        ReportGenerationTracker.getInstance().startNextDataSetForJobId(jobId,
//                iterator.count());
        job.beginTask(null, iterator.count());
    }


    // ----------------------------------------------------------
    public void prepare(String entityType, String[] myExpressions)
        throws WebCATDataException
    {
        this.expressions = myExpressions;
        defaultContext = prepareOgnlContext();
        accessors = new ExpressionAccessor[myExpressions.length];

        int i = 0;
        for (String expression : myExpressions)
        {
            try
            {
                accessors[i] = Ognl.compileExpression(
                    defaultContext, null, expression).getAccessor();
            }
            catch (Exception e)
            {
                throw new WebCATDataException(e);
            }

            i++;
        }
    }


    // ----------------------------------------------------------
    private OgnlContext prepareOgnlContext()
    {
        OgnlContext ctx = ReportUtilityEnvironment.newOgnlContext();
        return ctx;
    }


    // ----------------------------------------------------------
    public boolean moveToNextRow()
    {
        throttleIfNecessary();
        boolean hasNext = true;
        rawCurrentRow++;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastProgressUpdateTime >=
            TIME_BETWEEN_PROGRESS_UPDATES)
        {
//            ReportGenerationTracker tracker =
//                ReportGenerationTracker.getInstance();

            // Check to see if the report has been canceled. If it has, we
            // just return no more rows here.
            if (job.isCancelled())
            {
                return false;
            }
/*            if (!tracker.doesJobExistWithId(jobId))
            {
                return false;
            }*/

            job.worked(rawCurrentRow - rowCountAtLastProgressUpdate);
//            tracker.doWorkForJobId(jobId,
//                    rawCurrentRow - rowCountAtLastProgressUpdate);

            lastProgressUpdateTime = currentTime;
            rowCountAtLastProgressUpdate = rawCurrentRow;
        }

        if (currentBatchEnum == null || !currentBatchEnum.hasMoreElements())
        {
            hasNext = getNextBatch();
        }

        if (hasNext)
        {
            currentObject = currentBatchEnum.nextElement();
            currentRow++;
        }

        if (log.isDebugEnabled())
        {
            // CAUTION: Since this row logging occurs on every row AND it is
            // possible that the toString() method for an object might
            // indirectly fetch other objects in order to display a human-
            // readable representation of itself, DEBUG level logging on this
            // class should only be enabled when absolutely necessary.

            String msg = "Row " + rawCurrentRow + ": "
                + currentObject.toString();
            log.debug(msg);
        }

        return hasNext;
    }


    // ----------------------------------------------------------
    public boolean booleanValueAtIndex(int column)
        throws WebCATDataException
    {
        Boolean value = evaluate(column, Boolean.class);
        return (value == null)? false : value;
    }


    // ----------------------------------------------------------
    public BigDecimal decimalValueAtIndex(int column)
        throws WebCATDataException
    {
        return evaluate(column, BigDecimal.class);
    }


    // ----------------------------------------------------------
    public double doubleValueAtIndex(int column)
        throws WebCATDataException
    {
        Double value = evaluate(column, Double.class);
        if (value == null)
        {
            return 0.0;
        }
        else
        {
            return value;
        }
    }


    // ----------------------------------------------------------
    public int intValueAtIndex(int column)
        throws WebCATDataException
    {
        Integer value = evaluate(column, Integer.class);
        return (value == null)? 0 : value;
    }


    // ----------------------------------------------------------
    public String stringValueAtIndex(int column)
        throws WebCATDataException
    {
        return evaluate(column, String.class);
    }


    // ----------------------------------------------------------
    public Timestamp timestampValueAtIndex(int column)
        throws WebCATDataException
    {
        return evaluate(column, NSTimestamp.class);
    }


    // ----------------------------------------------------------
    public boolean wasValueNull()
    {
        return wasNull;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    private void recycleEditingContext()
    {
        boolean suppressLog = false;

        if (editingContext != null)
        {
            suppressLog = editingContext.isLoggingSuppressed();
            editingContext.dispose();
        }

        editingContext = ReadOnlyEditingContext.newEditingContext();
        editingContext.setSuppressesLogAfterFirstAttempt(true);
        editingContext.setLoggingSuppressed(suppressLog);

        if (iterator != null)
        {
            iterator.setEditingContext(editingContext);
        }
    }


    // ----------------------------------------------------------
    private void updateMovingAverageAndRest()
    {
        batchTimeEnd = batchTimeStart;
        batchTimeStart = System.currentTimeMillis();

        if (batchTimeEnd == 0)
        {
            // Bail out if this is the first time through.  The batch size is
            // already set to a default value that we'll use to begin the
            // calculations.

            return;
        }

        // Note that this subtraction is "backwards" because of the way I've
        // reset the variables above.
        double avgTimePerRow =
            ((double) (batchTimeStart - batchTimeEnd)) / currentBatchSize;

        // Compute moving average.
        if (currentMovingAverage == 0)
        {
            currentMovingAverage = avgTimePerRow;
        }
        else
        {
            currentMovingAverage =
                ((MOVING_AVERAGE_WINDOW_SIZE - 1) * currentMovingAverage +
                avgTimePerRow) / MOVING_AVERAGE_WINDOW_SIZE;
        }

        //log.debug("Last_batch_size," + currentBatchSize + ",Last_batch_time,"
        //  + (batchTimeStart - batchTimeEnd) + ",Avg_time_per_row,"
        //  + avgTimePerRow + ",Current_moving_avg," + currentMovingAverage);

        // Compute the new batch size.
        long workTime = (long) (BATCH_TIME_SLICE * BATCH_LOAD_FACTOR);
        currentBatchSize = (int) (workTime / currentMovingAverage);

        if (currentBatchSize < BATCH_SIZE_MIN)
        {
            currentBatchSize = BATCH_SIZE_MIN;
        }
        else if (currentBatchSize > BATCH_SIZE_MAX)
        {
            currentBatchSize = BATCH_SIZE_MAX;
        }

        iterator.setBatchSize(currentBatchSize);

        try
        {
            long sleepTime =
                (long) (BATCH_TIME_SLICE * (1 - BATCH_LOAD_FACTOR));
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e)
        {
            // Do nothing.
        }
    }


    // ----------------------------------------------------------
    private boolean getNextBatch()
    {
        updateMovingAverageAndRest();

        if (iterator.hasNextBatch())
        {
            boolean getBatch = true;

            while (getBatch)
            {
                recycleEditingContext();
                @SuppressWarnings("unchecked")
                NSArray<Object> nextBatch = iterator.nextBatch();
                currentBatch = nextBatch;

                if (inMemoryQualifier != null)
                {
                    currentBatch = EOQualifier.filteredArrayWithQualifier(
                        currentBatch, inMemoryQualifier);
                }

                if (currentBatch.isEmpty())
                {
                    getBatch = iterator.hasNextBatch();
                }
                else
                {
                    currentBatchEnum = currentBatch.objectEnumerator();
                    return true;
                }
            }
        }

        return false;
    }


    // ----------------------------------------------------------
    private void throttleIfNecessary()
    {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastThrottleCheck > MILLIS_BETWEEN_THROTTLE_CHECK)
        {
            while (Reporter.getInstance().refreshThrottleStatus())
            {
                try
                {
                    Thread.sleep(MILLIS_TO_THROTTLE);
                }
                catch (InterruptedException e)
                {
                    // Nothing to do
                }
            }
            lastThrottleCheck = System.currentTimeMillis();
        }
    }


    // ----------------------------------------------------------
    private <T> T evaluate(int column, Class<T> destType)
        throws WebCATDataException
    {
        ExpressionAccessor accessor = accessors[column];
        Object result = null;
        defaultContext.setRoot(currentObject);

        try
        {
            result = accessor.get(defaultContext, currentObject);
        }
        catch (NullPointerException e)
        {
            // If any property along the key path evaluated to null, we don't
            // want to bail out; we'll just set the column value to null and
            // keep going.

            result = null;
        }
        catch (NSKeyValueCoding.UnknownKeyException e)
        {
            // Translate the expression into something a little easier for the
            // user to read.

            String msg = String.format(
                "In the expression above, the property \"%s\" is not " +
                "recognized by the source object (which is of type \"%s\")",
                e.key(), e.object().getClass().getName());

            // FIXME fix this
//            ReportGenerationTracker rgt =
//                ReportGenerationTracker.getInstance();
//            ReportDataSet dataSet =
//                ReportDataSet.forId(editingContext, dataSetId);
//            rgt.setLastErrorInfoForJobId(jobId, dataSet.name(), column, null,
//                    expressions[column], msg);

            // Rethrow modified version of original exception with new msg
            NSKeyValueCoding.UnknownKeyException replacement =
                new NSKeyValueCoding.UnknownKeyException(
                    msg, e.object(), e.key());
            replacement.setStackTrace(e.getStackTrace());
            throw new WebCATDataException(replacement);
        }
        catch (Exception e)
        {
            if (e instanceof OgnlException)
            {
                result = null;
            }
            else
            {
                // Before rethrowing the exception, pass the extra information
                // about where the error occurred to the report generation
                // tracker so that the queue processor can pass it along to
                // the user. This gets us better feedback than the standard
                // BIRT error message, which is something like "cannot get
                // value from column: N" with no other information.

                // FIXME fix this
//                ReportGenerationTracker rgt =
//                    ReportGenerationTracker.getInstance();

//                ReportDataSet dataSet = ReportDataSet.forId(
//                        editingContext, dataSetId);

//                rgt.setLastErrorInfoForJobId(jobId, dataSet.name(), column,
//                        null, expressions[column], e.getMessage());

                throw new WebCATDataException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("   Column " + column + " = " + result + ": "
                    + expressions[column]);
        }

        if (result == null)
        {
            wasNull = true;
            return null;
        }
        else
        {
            wasNull = false;

            if (destType.isInstance(result))
            {
                return destType.cast(result);
            }
            else
            {
                try
                {
                    return destType.cast(defaultContext.getTypeConverter()
                        .convertValue(
                            null, null, null, null, result, destType));
                }
                catch (Exception e)
                {
                    return tryFallbackConversions(column, result, destType);
                }
            }
        }
    }


    // ----------------------------------------------------------
    private <T> T tryFallbackConversions(int column, Object result,
            Class<T> destType)
    {
        if (destType == Integer.class)
        {
            // Conversion may have failed if we have a string that is a
            // floating point value and we want to convert to an integer.

            try
            {
                return destType.cast(Integer.valueOf(
                        (int) Double.parseDouble(result.toString())));
            }
            catch (NumberFormatException e)
            {
                // Fall through to the code below.
            }
        }
        else if (destType == NSTimestamp.class)
        {
            try
            {
                return destType.cast(
                    new NSTimestamp(Long.parseLong(result.toString())));
            }
            catch (NumberFormatException e)
            {
                // Fall through to the code below.
            }
        }

        String destinationType = destType.getSimpleName();
        if (destType == BigDecimal.class)
            destinationType = "Decimal";
        else if (destType == Double.class)
            destinationType = "Float";
        else if (destType == NSTimestamp.class)
            destinationType = "Timestamp";

        throw new IllegalArgumentException("The result (\"" +
                result.toString() + "\") of the expression [ " +
                expressions[column] + " ] could not be converted to type " +
                destinationType);
    }


    //~ Instance/static variables .............................................

    @SuppressWarnings("unused")
    private int dataSetId;

    private ManagedReportGenerationJob job;
    private ObjectQuery query;
    private ReadOnlyEditingContext editingContext;
    private EOQualifier fetchQualifier;
    private EOQualifier inMemoryQualifier;
    private ERXFetchSpecificationBatchIterator iterator;
    private int currentRow;
    private int rawCurrentRow;
    private String[] expressions;
    private ExpressionAccessor[] accessors;
    private OgnlContext defaultContext;
    private NSArray<Object> currentBatch;
    private Enumeration<Object> currentBatchEnum;
    private Object currentObject;
    private boolean wasNull;
    private long lastThrottleCheck;
    private long batchTimeStart;
    private long batchTimeEnd;
    private int currentBatchSize;
    private double currentMovingAverage;
    private long lastProgressUpdateTime;
    private int rowCountAtLastProgressUpdate;

    private static final long TIME_BETWEEN_PROGRESS_UPDATES = 4000;

    private static final long MILLIS_BETWEEN_THROTTLE_CHECK = 3000;
    private static final long MILLIS_TO_THROTTLE = 5000;

    // TODO: Add these as Reporter subsystem configuration options; cache
    // their values when this object is created
    /* Ideal amount of time to spend on each batch */
    private static final int BATCH_TIME_SLICE = 600;
    /* Fraction of the time slice to spend working (instead of resting) */
    private static final float BATCH_LOAD_FACTOR = 0.75f;
    private static final int BATCH_SIZE_MIN = 25;
    private static final int BATCH_SIZE_MAX = 250;

    private static final int MOVING_AVERAGE_WINDOW_SIZE = 10;

    private static final Logger log = Logger.getLogger(OdaResultSet.class);
}
