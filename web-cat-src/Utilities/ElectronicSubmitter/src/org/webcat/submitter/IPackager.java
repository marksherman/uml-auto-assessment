/*==========================================================================*\
 |  $Id: IPackager.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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
import java.io.OutputStream;
import java.util.Map;

//--------------------------------------------------------------------------
/**
 * The packager interface implemented by classes that are registered as
 * packagers in the submission plug-in.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public interface IPackager
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Starts a new package that will be written to the specified stream.
     *
     * @param stream The stream to which the package will be written
     * @param parameters A dictionary of parameters that are specified in the
     *     target definition
     * @throws IOException if an I/O exception occurred
     */
    void startPackage(OutputStream stream, Map<String, String> parameters)
    throws IOException;


    // ----------------------------------------------------------
    /**
     * Adds a submittable item to the package that was started by a call
     * to {@link #startPackage}.
     *
     * @param item the item to add
     * @throws IOException if an I/O exception occurred
     */
    void addSubmittableItem(ISubmittableItem item) throws IOException;


    // ----------------------------------------------------------
    /**
     * Finalizes the package that was started by a call to
     * {@link #startPackage}. This method should flush the stream but
     * <b>not</b> close it.
     *
     * @throws IOException if an I/O exception occurred
     */
    void endPackage() throws IOException;
}
