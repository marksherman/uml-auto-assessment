package org.webcat.diff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//-------------------------------------------------------------------------
/**
 * A linked list of {@link Diff} objects that provides additional methods for
 * operating on the list or reconstituting the original lists.
 *
 * @param <T> the type of the item in the list
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/06/09 15:29:09 $
 */
public class DiffList<T> extends LinkedList<Diff<T>>
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Compute and return the source text (all equalities and deletions).
     * @return Source text.
     */
    public List<T> computeFirstList()
    {
        ArrayList<T> list = new ArrayList<T>();

        for (Diff<T> aDiff : this)
        {
            if (aDiff.operation != Diff.Operation.INSERT)
            {
                list.addAll(aDiff.list);
            }
        }

        return list;
    }


    // ----------------------------------------------------------
    /**
     * Compute and return the destination text (all equalities and insertions).
     * @return Destination text.
     */
    public List<T> computeSecondList()
    {
        ArrayList<T> list = new ArrayList<T>();

        for (Diff<T> aDiff : this)
        {
            if (aDiff.operation != Diff.Operation.DELETE)
            {
                list.addAll(aDiff.list);
            }
        }

        return list;
    }


    // ----------------------------------------------------------
    /**
     * Compute the Levenshtein distance; the number of inserted, deleted or
     * substituted characters.
     * @return Number of changes.
     */
    public int getLevenshteinDistance()
    {
        int levenshtein = 0;
        int insertions = 0;
        int deletions = 0;

        for (Diff<T> aDiff : this)
        {
            switch (aDiff.operation)
            {
                case INSERT:
                    insertions += aDiff.list.size();
                    break;

                case DELETE:
                    deletions += aDiff.list.size();
                    break;

                case EQUAL:
                    // A deletion and an insertion is one substitution.
                    levenshtein += Math.max(insertions, deletions);
                    insertions = 0;
                    deletions = 0;
                    break;
            }
        }

        levenshtein += Math.max(insertions, deletions);
        return levenshtein;
    }


    // ----------------------------------------------------------
    /**
     * loc is a location in text1, compute and return the equivalent location
     * in text2. e.g. "The cat" vs "The big cat", 1->1, 5->8
     *
     * @param loc
     *            Location within text1.
     * @return Location within text2.
     */
    public int translateIndex(int loc)
    {
        int chars1 = 0;
        int chars2 = 0;
        int last_chars1 = 0;
        int last_chars2 = 0;

        Diff<T> lastDiff = null;
        for (Diff<T> aDiff : this)
        {
            if (aDiff.operation != Diff.Operation.INSERT)
            {
                // Equality or deletion.
                chars1 += aDiff.list.size();
            }

            if (aDiff.operation != Diff.Operation.DELETE)
            {
                // Equality or insertion.
                chars2 += aDiff.list.size();
            }

            if (chars1 > loc)
            {
                // Overshot the location.
                lastDiff = aDiff;
                break;
            }

            last_chars1 = chars1;
            last_chars2 = chars2;
        }

        if (lastDiff != null && lastDiff.operation == Diff.Operation.DELETE)
        {
            // The location was deleted.
            return last_chars2;
        }

        // Add the remaining character length.
        return last_chars2 + (loc - last_chars1);
    }


    //~ Static/instance variables .............................................

    private static final long serialVersionUID = 9136119555777279980L;
}
