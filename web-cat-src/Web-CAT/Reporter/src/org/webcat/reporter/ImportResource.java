/*==========================================================================*\
 |  $Id: ImportResource.java,v 1.2 2011/05/27 15:36:46 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
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

import com.webobjects.appserver.WOAssociation;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODynamicElement;
import com.webobjects.appserver.WOElement;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import er.ajax.AjaxUtils;

//-------------------------------------------------------------------------
/**
 * A dynamic element that lets a page or nested component request that script
 * or stylesheet resources be included in the &lt;head&gt; of a page, regardless
 * of where in the component content this element is used.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/05/27 15:36:46 $
 */
public class ImportResource
    extends WODynamicElement
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create an element.
     * @param aName        the component's name
     * @param associations the bindings for this instance of the component
     * @param template
     */
    public ImportResource(
        String aName,
        NSDictionary<String, WOAssociation> associations,
        WOElement template)
    {
        super(aName, associations, template);

        aType = associations.objectForKey("type");
        aFramework = associations.objectForKey("framework");
        aFilename = associations.objectForKey("filename");
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        WOComponent component = context.component();

        String type = aType != null
            ? (String)aType.valueInComponent(component)
            : null;
        String framework = aFramework != null
            ? (String)aFramework.valueInComponent(component)
            : null;
        String filename = aFilename != null
            ? (String)aFilename.valueInComponent(component)
            : null;

        // If no framework is specified, get the one from the calling
        // component.
        if (framework == null)
        {
            NSBundle bundle = NSBundle.bundleForClass(
                context.component().getClass());
            framework = bundle.name();
        }

        if (type.equalsIgnoreCase("script"))
        {
            AjaxUtils.addScriptResourceInHead(
                context, response, framework, filename);
        }
        else if (type.equalsIgnoreCase("stylesheet"))
        {
            AjaxUtils.addStylesheetResourceInHead(
                context, response, framework, filename);
        }
    }


    //~ Instance/static variables .............................................

    private WOAssociation aType;
    private WOAssociation aFramework;
    private WOAssociation aFilename;
}
