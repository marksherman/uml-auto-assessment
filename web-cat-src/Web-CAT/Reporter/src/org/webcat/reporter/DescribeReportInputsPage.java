package org.webcat.reporter;

import org.webcat.core.objectquery.AbstractQueryAssistantModel;
import org.webcat.core.objectquery.ObjectQuerySurrogate;
import org.webcat.core.objectquery.QueryAssistantDescriptor;
import org.webcat.core.objectquery.QueryAssistantManager;
import org.webcat.ui.generators.JavascriptGenerator;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSMutableSet;

/**
 *
 * @author Tony Allevato
 * @version $Id: DescribeReportInputsPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class DescribeReportInputsPage extends ReporterComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public DescribeReportInputsPage(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    public NSArray<ReportDataSet> dataSets;
    public ReportDataSet dataSet;
    public int dataSetIndex;

    public NSDictionary<String, Object> parameter;

    public String reportDescription;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        dataSets = localReportTemplate().dataSets();

        if (dataSets != null)
        {
            objectQuerySurrogates = new NSMutableArray<ObjectQuerySurrogate>();

            for (int i = 0; i < dataSets.count(); i++)
            {
                objectQuerySurrogates.addObject(new ObjectQuerySurrogate(
                        dataSets.objectAtIndex(i).wcEntityName()));
            }
        }

        parameterValues = new NSMutableDictionary<String, Object>();

        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public NSArray<QueryAssistantDescriptor> queryAssistantsForDataSet()
    {
        String entityName = dataSet.wcEntityName();

        return QueryAssistantManager.getInstance().assistantsForEntity(
                entityName);
    }


    // ----------------------------------------------------------
    public ObjectQuerySurrogate objectQuerySurrogateForDataSet()
    {
        return objectQuerySurrogates.objectAtIndex(dataSetIndex);
    }


    // ----------------------------------------------------------
    public String titleForDataSetPageModule()
    {
        return "Data set: " + dataSet.name() + " (" +
            (dataSetIndex + 1) + " of " + dataSets.count() + ")";
    }


    // ----------------------------------------------------------
    public Object valueForParameter()
    {
        return parameterValues.objectForKey(parameter.objectForKey("name"));
    }


    // ----------------------------------------------------------
    public void setValueForParameter(Object value)
    {
        parameterValues.setObjectForKey(value, parameter.objectForKey("name"));
    }


    // ----------------------------------------------------------
    public WOActionResults changeReportTemplate()
    {
        clearLocalReportState();

        return pageWithName(PickTemplateToGeneratePage.class);
    }


    // ----------------------------------------------------------
    public WOActionResults generateReport()
    {
        String desc = reportDescription;
        if (desc == null)
        {
            desc = defaultDescription();
        }

        setLocalReportDescription(desc);

        commitReportGeneration(objectQuerySurrogates, dataSets);
        return pageWithName(GeneratedReportPage.class);
    }


    // ----------------------------------------------------------
    public String defaultDescription()
    {
        return localReportTemplate().name();
    }


    //~ Static/instance variables .............................................

    private NSMutableArray<ObjectQuerySurrogate> objectQuerySurrogates;
    private NSMutableDictionary<String, Object> parameterValues;
}
