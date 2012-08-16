/*==========================================================================*\
 |  $Id: HierarchicalProgressTracker.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter.internal.utility;

import java.util.Stack;

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
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class HierarchicalProgressTracker
{
    //~ Constructors ................,,,.......................................

    // ----------------------------------------------------------
    /**
     * Initializes a new hierarchical progress tracker.
     */
    public HierarchicalProgressTracker()
    {
        tasks = new Stack<Task>();
        isDone = false;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Subdivide the next unit of work into a subtask where the steps of the
     * subtask have the specified weights.
     *
     * @param description a description of the subtask
     * @param weights an array of integers describing how the steps of the
     *     subtask are weighted
     */
    public void beginTask(String description, int[] weights)
    {
        tasks.push(new Task(weights, weights.length, description));
    }


    // ----------------------------------------------------------
    /**
     * Subdivide the next unit of work into a subtask where the steps of the
     * subtask have equal weight.
     *
     * @param description a description of the subtask
     * @param totalWork the number of units of work that make up the subtask
     */
    public void beginTask(String description, int totalWork)
    {
        tasks.push(new Task(null, totalWork, description));
    }


    // ----------------------------------------------------------
    /**
     * Subdivide the next unit of work into a subtask where the steps of the
     * subtask have the specified weights.
     *
     * @param weights an array of integers describing how the steps of the
     *     subtask are weighted
     */
    public void beginTask(int[] weights)
    {
        beginTask(null, weights);
    }


    // ----------------------------------------------------------
    /**
     * Subdivide the next unit of work into a subtask where the steps of the
     * subtask have equal weight.
     *
     * @param totalWork the number of units of work that make up the subtask
     */
    public void beginTask(int totalWork)
    {
        beginTask(null, totalWork);
    }


    // ----------------------------------------------------------
    /**
     * Performs the specified number of units of work on the current subtask.
     * Note that if the accumulated number of units would meet or exceed the
     * total work for the task, the task must still be explicitly completed by
     * calling {@link #completeCurrentTask()}; this method does not
     * automatically complete such tasks.
     *
     * @param delta the number of units of work to perform
     */
    public void worked(int delta)
    {
        Task task = tasks.peek();
        task.step(delta);
    }


    // ----------------------------------------------------------
    /**
     * Completes the current subtask.
     */
    public void completeCurrentTask()
    {
        tasks.pop();

        if (tasks.empty())
        {
            setDone();
        }
        else
        {
            worked(1);
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the description of the current subtask.
     *
     * @return the description of the current subtask, or null if there is none
     */
    public String descriptionOfCurrentTask()
    {
        int lastIndex = tasks.size() - 1;

        for (int i = lastIndex; i >= 0; i--)
        {
            Task task = tasks.get(i);

            if (task.description() != null)
            {
                return task.description();
            }
        }

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Gets the percentage of total work done across all subtasks so far.
     *
     * @return the percentage of total work done, a floating-point value
     *     between 0 and 1
     */
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
    /**
     * Gets a value indicating whether the progress tracker is done.
     *
     * @return true if the tracker is done, otherwise false
     */
    public boolean isDone()
    {
        return isDone;
    }


    // ----------------------------------------------------------
    /**
     * Explicitly sets all work on the progress tracker to be done.
     */
    public void setDone()
    {
        isDone = true;
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    /**
     * Represents a subtask in the progress tracking stack.
     */
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

    private Stack<Task> tasks;
}
