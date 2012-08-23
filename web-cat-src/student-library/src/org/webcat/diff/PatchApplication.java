package org.webcat.diff;

import java.util.List;

public class PatchApplication<T>
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public PatchApplication(List<T> result, boolean[] appliedPatches)
    {
        this.result = result;
        this.appliedPatches = appliedPatches;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public List<T> getResult()
    {
        return result;
    }


    // ----------------------------------------------------------
    public boolean[] getAppliedPatches()
    {
        return appliedPatches;
    }


    //~ Static/instance variables .............................................

    private List<T> result;

    private boolean[] appliedPatches;
}
