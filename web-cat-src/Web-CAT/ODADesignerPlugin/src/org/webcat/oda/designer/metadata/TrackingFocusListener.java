/*==========================================================================*\
 |  $Id: TrackingFocusListener.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.metadata;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

// ------------------------------------------------------------------------
/**
 * A focus listener that keeps track of the original text value of a control so
 * that dirty states can be properly managed only if the value changes.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: TrackingFocusListener.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class TrackingFocusListener implements FocusListener
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new tracking focus listener that sets the dirty flag on the
     * specified form page.
     *
     * @param formPage
     *            the form page to be made dirty when changes occur
     */
    public TrackingFocusListener(OverviewFormPage formPage)
    {
        this.formPage = formPage;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called when the control gains the user input focus. Subclasses must call
     * super.focusGained(e).
     */
    public void focusGained(FocusEvent e)
    {
        hasChanged = false;
        originalText = getWidgetText(e.widget);
    }


    // ----------------------------------------------------------
    /**
     * Called when the control loses the user input focus. Subclasses must call
     * super.focusLost(e).
     */
    public void focusLost(FocusEvent e)
    {
        String newText = getWidgetText(e.widget);

        if (originalText != null && !originalText.equals(newText))
        {
            hasChanged = true;
        }

        if (hasChanged)
        {
            textDidChange();
            formPage.markAsDirty();
        }
    }


    // ----------------------------------------------------------
    /**
     * Called if the text in the control has changed when the focus was lost.
     */
    protected void textDidChange()
    {
        // Default implementation does nothing. Subclasses can override to
        // provide their own functionality.
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether the control's text was changed by the
     * user during this edit session.
     *
     * @return true if the control's text changed in this edit session,
     *         otherwise false.
     */
    public boolean hasChanged()
    {
        return hasChanged;
    }


    // ----------------------------------------------------------
    /**
     * Utility method to get the text value of a widget without knowing its
     * exact type.
     *
     * @param widget
     *            the widget whose text should be retrieved
     */
    private String getWidgetText(Widget widget)
    {
        if (widget instanceof Text)
        {
            return ((Text) widget).getText();
        }
        else if (widget instanceof Combo)
        {
            return ((Combo) widget).getText();
        }
        else
        {
            return null;
        }
    }


    //~ Static/instance variables .............................................

    private OverviewFormPage formPage;
    private String originalText;
    private boolean hasChanged;
}
