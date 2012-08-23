package org.webcat.diff;

import java.util.LinkedList;
import java.util.List;

//-------------------------------------------------------------------------
/**
 * A linked list of {@link Patch} objects, providing additional utility methods
 * as well.
 *
 * @param <T> the type of item in the lists
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: mwoodsvt $
 * @version $Revision: 1.1 $, $Date: 2011/02/20 21:02:28 $
 */
public class PatchList<T> extends LinkedList<Patch<T>>
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Given an array of patches, return another array that is identical.
     *
     * @param patches
     *            Array of patch objects.
     * @return Array of patch objects.
     */
    public PatchList<T> clone()
    {
        PatchList<T> patchesCopy = new PatchList<T>();

        for (Patch<T> aPatch : this)
        {
            Patch<T> patchCopy = new Patch<T>();

            for (Diff<T> aDiff : aPatch.diffs)
            {
                Diff<T> diffCopy = new Diff<T>(aDiff.operation, aDiff.list);
                patchCopy.diffs.add(diffCopy);
            }

            patchCopy.start1 = aPatch.start1;
            patchCopy.start2 = aPatch.start2;
            patchCopy.length1 = aPatch.length1;
            patchCopy.length2 = aPatch.length2;
            patchesCopy.add(patchCopy);
        }

        return patchesCopy;
    }


    // ----------------------------------------------------------
    /**
     * Take a list of patches and return a textual representation.
     *
     * @param patches
     *            List of Patch objects.
     * @return Text representation of patches.
     */
    public String toString()
    {
        StringBuilder text = new StringBuilder();

        for (Patch<T> aPatch : this)
        {
            text.append(aPatch);
        }

        return text.toString();
    }


    //~ Static/instance variables .............................................

    private static final long serialVersionUID = 7915189621947019639L;
}
