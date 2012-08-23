package student.web.internal.converters;

import student.web.internal.ApplicationSupportStrategy;
import student.web.internal.LocalityService;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class AliasConverter implements Converter
{

    private FlexibleFieldSetConverter ffsc;


    public AliasConverter( FlexibleFieldSetConverter ffsc )
    {
        this.ffsc = ffsc;
    }


    @SuppressWarnings("rawtypes")
    public boolean canConvert( Class type )
    {
        return type != null && type.equals( Alias.class );
    }


    public Object unmarshal(
        HierarchicalStreamReader reader,
        UnmarshallingContext context )
    {
        ApplicationSupportStrategy support = LocalityService.getSupportStrategy();
        if ( !reader.getNodeName().equals( Alias.class.getName() ) )
            throw new IllegalArgumentException( "The object of type "
                + reader.getNodeName()
                + " should be aliased within this class.  Because it is "
                + "not, this profile is considered corrupt and cannot be "
                + "loaded.  Please delete this object from the store and create a new one." );
        Object alias = ffsc.unmarshal( reader, context );
        Object lookup = support.resolveAlias( alias );
        
        return lookup;
    }


    public void marshal(
        Object source,
        HierarchicalStreamWriter writer,
        MarshallingContext context )
    {
        ffsc.marshal( source, writer, context );
    }

}
