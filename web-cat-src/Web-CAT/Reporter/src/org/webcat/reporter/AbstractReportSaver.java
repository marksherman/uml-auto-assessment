/*==========================================================================*\
 |  $Id: AbstractReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
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

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.webcat.birtruntime.BIRTRuntime;
import org.webcat.core.DeliverFile;

//------------------------------------------------------------------------
/**
 * Provides common base functionality for each of the report saver types. 
 *
 * @author Tony Allevato
 * @version $Id: AbstractReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public abstract class AbstractReportSaver
{
    //~ Constructors ..........................................................
    
    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the abstract report saver.
     * 
     * @param report the GeneratedReport object to be saved
     */
    public AbstractReportSaver(GeneratedReport report)
    {
        this.report = report;
        document = report.openReportDocument();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Closes the report document associated with this saver.
     */
    public void close()
    {
        document.close();
    }
    

    // ----------------------------------------------------------
    /**
     * Gets the report engine instance.
     * 
     * @return the report engine instance
     */
    protected IReportEngine reportEngine()
    {
        return BIRTRuntime.getInstance().getReportEngine();
    }


    // ----------------------------------------------------------
    /**
     * Gets the generated report associated with this saver.
     * 
     * @return the GeneratedReport object associated with this saver
     */
    protected GeneratedReport generatedReport()
    {
        return report;
    }
    
    
    // ----------------------------------------------------------
    /**
     * Gets the BIRT report document handle for the report being saved. After
     * using this handle, make sure to call the close method.
     * 
     * @return the IReportDocument handle for the report
     */
    protected IReportDocument reportDocument()
    {
        return document;
    }


    // ----------------------------------------------------------
    /**
     * Saves the contents of the report to the specified DeliverFile component,
     * which the caller should return to the user for download. Subclasses must
     * override this to provide the actual saving functionality for the format
     * that they implement.
     * 
     * @param file the DeliverFile component to store the saved data in
     * @return a Throwable that indicates an error that occurred (which the
     *     caller should display), or null if successful.
     */
    public abstract Throwable deliverTo(DeliverFile file);


    //~ Static/instance variables .............................................

    private GeneratedReport report;
    private IReportDocument document;
}
