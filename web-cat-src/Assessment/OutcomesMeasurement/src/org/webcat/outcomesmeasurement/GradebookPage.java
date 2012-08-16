package org.webcat.outcomesmeasurement;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;

@SuppressWarnings("serial")
public class GradebookPage extends BasePage {
    public GradebookPage(WOContext context) {
        super(context);
    }
 
    public int            index;

    public String         filePath;
    public NSData         data;

    public WOComponent uploadGradebook()
    {
        if (  filePath != null
           && !filePath.equals( "" )
           && data != null
           && data.length() > 0 )
        {
            UploadGradebookPage page = (UploadGradebookPage)pageWithName(
            		UploadGradebookPage.class.getName() );
            page.nextPage = this;
            page.filePath = filePath;
            page.data     = data;
            page.initializeData();
            return page;
        }
        else
        {
            error( "Please select a (non-empty) Excel file to upload." );
            return null;
        }
    }
}