package student.web.internal.converters;

import java.util.HashSet;
import java.util.Set;

import student.web.internal.ApplicationSupportStrategy;
import student.web.internal.LocalityService;


public class AliasService
{
    private static final String ALIAS_SUPPORT_SET = "alias_support_set";


    public static void addAliasClass( Class<?> clazz )
    {
        if ( !clazz.getName().startsWith( "java.lang" ) )
        {
            Set<String> supportSet = getSupportSet();
            supportSet.add( clazz.getName() );
        }
    }


    public static Set<String> getSupportSet()
    {
        ApplicationSupportStrategy support = LocalityService.getSupportStrategy();
        Object supportSetRaw = support.getSessionParameter( ALIAS_SUPPORT_SET );
        if ( supportSetRaw == null )
        {
            supportSetRaw = new HashSet<String>();
            support.setSessionParameter( ALIAS_SUPPORT_SET, supportSetRaw );
        }
        Set<String> supportSet = (Set<String>)supportSetRaw;
        return supportSet;
    }
}
