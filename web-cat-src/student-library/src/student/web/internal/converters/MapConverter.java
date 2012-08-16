package student.web.internal.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import student.web.internal.Snapshot;


/**
 * Converts a java.util.Map to XML, specifying an 'entry' element with 'key' and
 * 'value' children.
 * <p>
 * Note: 'key' and 'value' is not the name of the generated tag. The children
 * are serialized as normal elements and the implementation expects them in the
 * order 'key'/'value'.
 * </p>
 * <p>
 * Supports java.util.HashMap, java.util.Hashtable and java.util.LinkedHashMap.
 * </p>
 *
 * @author Joe Walnes
 */
public class MapConverter
    extends AbstractCollectionConverter
{
    private TreeMapConverter mapConverter;

    // Snapshot newSnapshot;
    // Snapshot oldSnapshot;

    public MapConverter(Mapper mapper)
    {
        super(mapper);
        mapConverter = new TreeMapConverter(mapper);
    }


    // public void setupSnapshots(Snapshot newSnap,
    // Snapshot oldSnap) {
    // newSnapshot = newSnap;
    // oldSnapshot = oldSnap;
    // }

    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class type)
    {
        if (type == null)
        {
            return false;
        }
        return type.equals(HashMap.class) || type.equals(Hashtable.class)
            || type.getName().equals("java.util.LinkedHashMap")
            || type.getName().equals("sun.font.AttributeMap") // Used by
                                                              // java.awt.Font
                                                              // in JDK 6
        ;
    }


    public void marshal(
        Object source,
        HierarchicalStreamWriter writer,
        MarshallingContext context)
    {
        Map<?, ?> map = (Map<?, ?>)source;

        // UUID oldId = Snapshot.findId(map, newSnapshot, oldSnapshot);
        UUID id = Snapshot.lookupId(source, true);
        writer.addAttribute(XMLConstants.ID_ATTRIBUTE, id.toString());
        updateMap((Map<Object, Object>)map);
        for (Iterator<?> iterator = map.entrySet().iterator();
            iterator.hasNext(); )
        {
            Entry<?, ?> entry = (Entry<?, ?>)iterator.next();
            ExtendedHierarchicalStreamWriterHelper.startNode(
                writer,
                mapper().serializedClass(Map.Entry.class),
                Map.Entry.class);
            UUID keyId = Snapshot.lookupId(entry.getKey(), true);
            writer.addAttribute(XMLConstants.ID_ATTRIBUTE, keyId.toString());
            writeItem(entry.getKey(), context, writer);
            if (entry.getValue() instanceof NullableClass)
            {
                ((NullableClass)entry.getValue())
                    .writeHiddenClass(mapConverter, writer, context);
            }
            else
            {
                writeItem(entry.getValue(), context, writer);
            }

            writer.endNode();
        }
    }


    public static void updateMap(Map<Object, Object> source)
    {
        // Lookup the id for this map
        UUID mapId = Snapshot.lookupId(source, false);
        // Get the newest copy of this map
        Map newestMap = (Map)Snapshot.getNewest().findObject(mapId);
        // If there is something newer we should start the update process
        if (newestMap != null)
        {
            // Look at every key in the new map
            for (Object newObject : newestMap.keySet())
            {
                // Do we know about this object locally?
                UUID id = Snapshot.getNewest().findId(newObject);
                Object localLookup = Snapshot.getLocal().findObject(id);
                // If the id is null, we know that we have never seen this
                // item before. Push it into the list.
                if (localLookup == null)
                {
                    source.put(newObject, newestMap.get(newObject));
                }
                // else
                // If the id is not null we have seen this item before. The
                // current logic is that if we have seen it, we have either
                // removed it intentionally or it still exists in the map. If
                // it is still in the map then we should just let the flexible
                // field set converter deal with it.
            }
        }
    }


    public Object unmarshal(
        HierarchicalStreamReader reader,
        UnmarshallingContext context)
    {
        @SuppressWarnings("unchecked")
        Map<Object, Object> map = (Map<Object, Object>)createCollection(
            context.getRequiredType());
        UUID id = UUID.fromString(
            reader.getAttribute(XMLConstants.ID_ATTRIBUTE));
        populateMap(reader, context, map);
        Snapshot.getLocal().resolveObject(id, map, (Map<String, Object>)null);
        return map;
    }


    protected void populateMap(
        HierarchicalStreamReader reader,
        UnmarshallingContext context,
        Map<Object, Object> map)
    {
        while (reader.hasMoreChildren())
        {
            reader.moveDown();
            UUID id = UUID.fromString(
                reader.getAttribute(XMLConstants.ID_ATTRIBUTE));
            reader.moveDown();
            Object key = readItem(reader, context, map);
            reader.moveUp();

            reader.moveDown();
            Object value = readItem(reader, context, map);
            reader.moveUp();

            map.put(key, value);
            Snapshot.getLocal().resolveObject(
                id,
                key,
                (Map<String, Object>)null);
            reader.moveUp();
        }
    }

}
