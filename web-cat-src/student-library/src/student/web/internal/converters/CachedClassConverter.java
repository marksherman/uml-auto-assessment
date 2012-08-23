package student.web.internal.converters;

import java.util.Map;

import student.web.internal.ApplicationSupportStrategy;
import student.web.internal.LocalityService;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class CachedClassConverter implements Converter
{
    private FlexibleFieldSetConverter ffsc;
    private Map<String,Object> context;
    public CachedClassConverter(Map<String,Object> context)
    {
        this.context = context;
    }
    public void marshal(
        Object source,
        HierarchicalStreamWriter writer,
        MarshallingContext context )
    {
        ApplicationSupportStrategy support = LocalityService.getSupportStrategy();
        Object alias = support.getAlias( source );
        if ( alias != null && !( alias instanceof Alias ) )
            throw new IllegalArgumentException( "You have included a reference to a "
                + source.getClass().getName()
                + " this object MUST already be present in the Persistent Layer.  "
                + "This prevents you from accidently creating a new object when "
                + "you intended to \"alias\" an object already in the persistent store.  "
                + "To fix this problem, make sure that any references contained within the "
                + "object you are persisting are inserted into the persistent store first.  "
                + "For example, if object foo contains a reference to object bar, insert bar into the persistent "
                + "store first then insert foo.  This probably means that you have called new "
                + source.getClass().getName()
                + " somewhere you shouldn't have." );
        writer.startNode( Alias.class.getName() );
        ffsc.marshal( alias, writer, context );
        writer.endNode();
    }

    private boolean disable = true;


    public void init()
    {
        disable = true;
    }


    @SuppressWarnings("rawtypes")
    public boolean canConvert( Class type )
    {
        if ( disable )
        {
            disable = false;
            return false;
        }
        return type != null
            && AliasService.getSupportSet().contains( type.getName() );
    }


    public Object unmarshal(
        HierarchicalStreamReader reader,
        UnmarshallingContext context )
    {
        reader.moveDown();
        Object result = context.convertAnother( context.currentObject(),
            Alias.class );
        reader.moveUp();
        return result;
    }


    public void setFlexibleFieldSetConverter(
        FlexibleFieldSetConverter fConverter )
    {
        this.ffsc = fConverter;
    }
}
