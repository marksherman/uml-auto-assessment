/*==========================================================================*\
 |  $Id: IProtocol.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

package org.webcat.submitter;

import java.io.IOException;

//--------------------------------------------------------------------------
/**
 * Defines the interface used by the submitter to electronically submit a
 * project. Submission protocols should implement this interface and put their
 * custom submission functionality in the submit method.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public interface IProtocol
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Submits a set of submittable items with this protocol.
     *
     * @param params a SubmissionParameters object that contains information
     *     about the project to be submitted
     * @param task the task that will maintain the progress of the submission
     * @throws IOException if there is an I/O error
     */
    void submit(SubmissionManifest params, ILongRunningTask task)
    throws IOException;


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether the protocol sends back a response from
     * the submission.
     *
     * @return true if a meaningful response is returned; otherwise, false
     */
    boolean hasResponse();


    // ----------------------------------------------------------
    /**
     * Gets the response to the submission.
     *
     * @return a String containing a protocol-specific response
     */
    String getResponse();
}
