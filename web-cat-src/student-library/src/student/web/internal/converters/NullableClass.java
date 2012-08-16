package student.web.internal.converters;

import java.util.UUID;

import student.web.internal.Snapshot;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class NullableClass
{
    private UUID id;

    private String nodeName;


    public NullableClass( String nodeName, UUID id )
    {
        this.id = id;
        this.nodeName = nodeName;
    }


    public UUID getID()
    {
        return id;
    }


    public String getNodeName()
    {
        return nodeName;
    }


    public void writeHiddenClass(
        TreeMapConverter mapConverter,
        HierarchicalStreamWriter writer,
        MarshallingContext context )
    {
        if ( Snapshot.getLocal().getFieldSetFromId( id ) != null )
        {
            writer.startNode( nodeName );
            writer.addAttribute( XMLConstants.ID_ATTRIBUTE, id.toString() );
            writer.addAttribute( XMLConstants.FIELDSET_ATTRIBUTE, "true" );
            mapConverter.marshal( Snapshot.getLocal().getFieldSetFromId( id ),
                writer,
                context );
            writer.endNode();
        }
    }
}
