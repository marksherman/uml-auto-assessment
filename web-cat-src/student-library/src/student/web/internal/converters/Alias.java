package student.web.internal.converters;

public class Alias
{

    private String key;
    private String contextMap;


    public String getContextMap()
    {
        return contextMap;
    }


    // Needed for persistence stuff DONT DELETE
    public Alias()
    {

    }


    public Alias( String key, String contextMap )
    {
        this.key = key;
        this.contextMap = contextMap;
    }


    public String getKey()
    {
        return key;
    }


    public Object _get_value_( String fieldName )
    {
        if ( fieldName.equals( "key" ) )
            return key;
        if (fieldName.equals( "contextMap" ))
            return contextMap;
        return null;
    }


    public void _write_field_( String fieldName, Object value )
    {
        if ( fieldName.equals( "key" ) )
            key = (String)value;
        if ( fieldName.equals( "contextMap" ))
            contextMap = (String)value;
    }
}
