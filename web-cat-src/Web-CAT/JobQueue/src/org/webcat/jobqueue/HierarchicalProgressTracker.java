/*==========================================================================*\
 |  $Id: HierarchicalProgressTracker.java,v 1.1 2010/05/11 14:51:44 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008-2009 Virginia Tech
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

package org.webcat.jobqueue;

import java.util.Enumeration;
import org.apache.log4j.Logger;
import com.webobjects.foundation.NSMutableArray;

//-------------------------------------------------------------------------
/**
 * A class that allows a job to track its progress using a hierarchy of tasks.
 * The top-level task partitions the entire progress range (0-100%) into
 * subranges; if nested tasks are created by further calls to
 * {@link #beginTask}, then the current subrange is partitioned further,
 * allowing for easy management of subtasks.
 *
 * @author Tony Allevato
 * @author Last changed by $Author: aallowat $
 * @version $Revision: 1.1 $, $Date: 2010/05/11 14:51:44 $
 */
public class HierarchicalProgressTracker
{
    //~ Constructor .......................................................

    // ----------------------------------------------------------
    public HierarchicalProgressTracker()
    {
        tasks = new NSMutableArray<Task>();
        isDone = false;
    }


    //~ Public Methods ....................................................

    // ----------------------------------------------------------
    public void beginTask(String description, int[] weights)
    {
        tasks.addObject(new Task(weights, weights.length, description));

        if (description == null)
        {
            logTaskStack("New task with " + weights.length + " units");
        }
        else
        {
            logTaskStack("New task (\"" + description + "\") with "
                    + weights.length + " units");
        }
    }


    // ----------------------------------------------------------
    public void beginTask(String description, int totalWork)
    {
        tasks.addObject(new Task(null, totalWork, description));

        if (description == null)
        {
            logTaskStack("New task with " + totalWork + " units");
        }
        else
        {
            logTaskStack("New task (\"" + description + "\") with " + totalWork
                    + " units");
        }
    }


    // ----------------------------------------------------------
    public void beginTask(int[] weights)
    {
        beginTask(null, weights);
    }


    // ----------------------------------------------------------
    public void beginTask(int totalWork)
    {
        beginTask(null, totalWork);
    }


    // ----------------------------------------------------------
    public void worked(int delta)
    {
        Task task = tasks.lastObject();
        task.step(delta);
    }


    // ----------------------------------------------------------
    public void completeCurrentTask()
    {
        tasks.removeLastObject();

        if (tasks.count() == 0)
        {
            setIsDone(true);
        }
        else
        {
            worked(1);
        }

        logTaskStack("Task " + tasks.count() + " complete");
    }


    // ----------------------------------------------------------
    public String descriptionOfCurrentTask()
    {
        int lastIndex = tasks.count() - 1;

        for (int i = lastIndex; i >= 0; i--)
        {
            Task task = tasks.objectAtIndex(i);

            if (task.description() != null)
            {
                return task.description();
            }
        }

        return null;
    }


    // ----------------------------------------------------------
    public double percentDone()
    {
        if (isDone)
        {
            return 1;
        }

        double workDone = 0, divisor = 1;

        for (Task task : tasks)
        {
            workDone += task.percentDone() * divisor;
            divisor *= task.nextWeightPercent();
        }

        return workDone;
    }


    // ----------------------------------------------------------
    public boolean isDone()
    {
        return isDone;
    }


    // ----------------------------------------------------------
    public void setIsDone(boolean done)
    {
        isDone = done;
    }


    //~ Private Methods/Classes ...........................................

    // ----------------------------------------------------------
    private void logTaskStack(String prefix)
    {
        if (!log.isDebugEnabled())
            return;

        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(": ");

        Enumeration<Task> e = tasks.objectEnumerator();
        while (e.hasMoreElements())
        {
            Task task = e.nextElement();
            buffer.append("(");
            buffer.append(task.stepsDoneSoFar);
            buffer.append(" of ");
            buffer.append(task.totalSteps);
            buffer.append(") ");
        }

        log.debug(buffer.toString());
    }


    // ----------------------------------------------------------
    private class Task
    {
        //~ Public Fields .....................................................

        public int weightSoFar;
        public int stepsDoneSoFar;
        public int[] weights;
        public int totalWeight;
        public int totalSteps;


        //~ Constructors ......................................................

        // ----------------------------------------------------------
        public Task(int[] weights, int total, String desc)
        {
            initialize(weights, total);
            description = desc;
        }


        // ----------------------------------------------------------
        private void initialize(int[] myWeights, int total)
        {
            weights = myWeights;
            totalWeight = 0;

            if (myWeights == null)
            {
                totalWeight = total;
            }
            else
            {
                for (int weight : myWeights)
                {
                    totalWeight += weight;
                }
            }

            weightSoFar = 0;
            stepsDoneSoFar = 0;
            totalSteps = total;
        }


        //~ Public Methods ....................................................

        // ----------------------------------------------------------
        public double nextWeightPercent()
        {
            if (weights == null)
            {
                return 1.0 / totalWeight;
            }
            else
            {
                return (double) weights[stepsDoneSoFar] / totalWeight;
            }
        }


        // ----------------------------------------------------------
        public void step(int delta)
        {
            int stop = Math.min(totalSteps, stepsDoneSoFar + delta);

            if (weights == null)
            {
                weightSoFar += delta;
            }
            else
            {
                for (int i = stepsDoneSoFar; i < stop; i++)
                {
                    weightSoFar += weights[i];
                }
            }

            stepsDoneSoFar += delta;

            if (stepsDoneSoFar > totalSteps)
            {
                stepsDoneSoFar = totalSteps;
                weightSoFar = totalWeight;
            }
        }


        // ----------------------------------------------------------
        public double percentDone()
        {
            return (double) weightSoFar / (double) totalWeight;
        }


        // ----------------------------------------------------------
        public String description()
        {
            if (description != null)
            {
                return description;
            }
            else
            {
                return null;
            }
        }


        //~ Instance/static variables (for HierarchicalProgressTracker.Task) ..

        private String description;
    }


    //~ Instance/static variables (for HierarchicalProgressTracker) ...........

    private boolean isDone;
    private NSMutableArray<Task> tasks;

    private static Logger log = Logger.getLogger(
            HierarchicalProgressTracker.class);
}
