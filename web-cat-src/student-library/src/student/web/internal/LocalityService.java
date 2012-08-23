package student.web.internal;

import student.testingsupport.SystemIOUtilities;


public abstract class LocalityService
{
    public static ApplicationSupportStrategy getSupportStrategy()
    {
        ApplicationSupportStrategy support = null;
        if ( SystemIOUtilities.isOnServer() )
        {
            try
            {
                Class<?> strategyClass = Class.forName( "student.web.internal.ServerApplicationSupportStrategy" );
                support = (ApplicationSupportStrategy)strategyClass.newInstance();
                return support;
            }
            catch ( Exception e )
            {
                
            }
        }
        try
        {
            Class<?> strategyClass = Class.forName( "cloudspace.ui.applet.AppletApplicationSupportStrategy" );
            support = (ApplicationSupportStrategy)strategyClass.newInstance();
            
        }
        catch(Exception e2)
        {
            if ( support == null )
            {
                support = new LocalApplicationSupportStrategy();
            }
            //do nothing, default to the local strat.
        }
        return support;
    }
}
