package student.web.internal.converters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import org.webcat.diff.DiffList;
import org.webcat.diff.DiffPatcher;
import org.webcat.diff.Differ;
import org.webcat.diff.PatchApplication;
import student.web.internal.PersistentStorageManager.FakePrintWriter;
import student.web.internal.Snapshot;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;


/**
 * Converts most common Collections (Lists and Sets) to XML, specifying a nested
 * element for each item.
 * <p/>
 * <p>
 * Supports java.util.ArrayList, java.util.HashSet, java.util.LinkedList,
 * java.util.Vector and java.util.LinkedHashSet.
 * </p>
 *
 * @author Joe Walnes
 */
public class CollectionConverter extends AbstractCollectionConverter
{
    // private ReflectionProvider rp;
    private TreeMapConverter mapConverter;


    /**
     * Create a new CollectionConverter.
     *
     * @param mapper
     *            The mapper to use.
     */
    public CollectionConverter( Mapper mapper )
    {
        super( mapper );
        mapConverter = new TreeMapConverter( mapper );
        // this.rp = rp;
    }


    @SuppressWarnings("rawtypes")
    public boolean canConvert( Class type )
    {
        if ( type == null )
            return false;
        return type.equals( ArrayList.class )
            || type.equals( HashSet.class )
            || type.equals( LinkedList.class )
            || type.equals( Vector.class )
            || ( JVM.is14() && type.getName()
                .equals( "java.util.LinkedHashSet" ) );
    }


    private class ObjectIdComparator implements Comparator<Object>
    {

        public int compare( Object o1, Object o2 )
        {
            if ( o1 == null || o2 == null )
                return -1;
            if ( !o1.getClass().equals( o2.getClass() ) )
                return -1;
            UUID id1 = Snapshot.lookupId( o1, false );
            UUID id2 = Snapshot.lookupId( o2, false );
            if ( id1 != null && id1.equals( id2 ) )
                return 0;
            return -1;
        }
    }


    public void marshal(
        Object source,
        HierarchicalStreamWriter writer,
        MarshallingContext context )
    {
        UUID collectionId = Snapshot.lookupId( source, false );
        // Convert the source into a list for processing
        List<Object> localCollection = convertToList( source );
        marshal0( collectionId, source, writer, context, localCollection );
        // If we are writing this list for real, then update the baseline
        // because all future changes are going to be against whatever we put in
        // there.
        if ( !( writer instanceof FakePrintWriter ) )
        {
            Collection<Object> baseLine = new ArrayList<Object>();
            baseLine.addAll( localCollection );
            Snapshot.getLocal().resolveObject( collectionId,
                localCollection,
                baseLine );
        }
    }


    protected void marshal0(
        UUID collectionId,
        Object source,
        HierarchicalStreamWriter writer,
        MarshallingContext context,
        List<Object> localCollection )
    {
        // The Result of processing if any is required.
        List<Object> patchedSnapshot = new ArrayList<Object>();
        // Check if this is a new list. If so, merge with existing lists.
        if ( collectionId != null )
        {
            // Grab what we saw from the persistence layer the very first time
            // we saw it.
            Object baseCollection = Snapshot.getLocal()
                .getBaseObject( collectionId );
            // Convert the collection into a list.
            List<Object> baseList = convertToList( baseCollection );
            // If the base list is null then we know we have never seen this
            // list before.
            if ( baseList == null )
                baseList = Collections.<Object> emptyList();
            // Get the newest version of this collection we have ever seen.
            Object newestCollection = Snapshot.getNewest()
                .findObject( collectionId );
            // Convert the newest version of this collection to a list.
            List<Object> newestList = convertToList( newestCollection );
            // If there is no newest list set it to the base list.
            if ( newestList == null )
                newestList = baseList;
            // See what has changed in the list locally.
            Differ<Object> localDiff = new Differ<Object>( baseList,
                localCollection,
                new ObjectIdComparator() );
            // Compute Differences
            DiffList<Object> diffList = localDiff.getDifferences();
            // Prepare a patcher to patch the new local items onto the most
            // current list from the store.
            DiffPatcher<Object> patcher = new DiffPatcher<Object>( baseList,
                diffList,
                new ObjectIdComparator() );
            // Patching app result
            PatchApplication<Object> patchResult = patcher.apply( newestList );
            // The actual result list!
            patchedSnapshot = patchResult.getResult();
            // Back to the marshaling stuff. Here I write the id as an attribute
        }
        else
        {
            collectionId = Snapshot.lookupId( source, true );
            if ( localCollection != null )
                patchedSnapshot.addAll( localCollection );
        }
        writer.addAttribute( XMLConstants.ID_ATTRIBUTE, collectionId.toString() );
        // Clear out the old collection. I am going to load it up with the new
        // values
        localCollection.clear();
        if ( source instanceof Collection )
            ( (Collection<?>)source ).clear();
        for ( Iterator<Object> iterator = patchedSnapshot.iterator(); iterator.hasNext();
        /* no increment needed */)
        {
            Object item = iterator.next();
            UUID objId = Snapshot.lookupId( item, false );
            if ( objId != null )
            {
                Object localVersion = Snapshot.getLocal().findObject( objId );
                if ( localVersion != null )
                    item = localVersion;
            }
            localCollection.add( item );
            if ( source instanceof Collection )
            {
                @SuppressWarnings("unchecked")
                Collection<Object> sourceCollection =
                    (Collection<Object>)source;
                sourceCollection.add( item );

            }
            objId = Snapshot.lookupId( item, true );
            ExtendedHierarchicalStreamWriterHelper.startNode( writer,
                "_item",
                null );
            writer.addAttribute( XMLConstants.ID_ATTRIBUTE, objId.toString() );
            if ( item instanceof NullableClass )
                ( (NullableClass)item ).writeHiddenClass( mapConverter,
                    writer,
                    context );
            else
                writeItem( item, context, writer );
            writer.endNode();

        }

    }


    private List<Object> convertToList( Object source )
    {
        if ( source == null )
        {
            return null;
        }
        if (source instanceof Collection)
        {
            @SuppressWarnings("unchecked")
            Collection<Object> col = (Collection<Object>)source;
            List<Object> result = new ArrayList<Object>();
            result.addAll( col );
            return result;
        }
        if ( source.getClass().isArray() )
        {
            List<Object> computed = new ArrayList<Object>();
            for ( int i = 0; i < Array.getLength( source ); i++ )
            {
                Object item = Array.get( source, i );
                computed.add( item );
            }
            return computed;
        }
        return null;
    }


    public Object unmarshal(
        HierarchicalStreamReader reader,
        UnmarshallingContext context )
    {
        // Snapshot local = Snapshot.getLocal();
        Class<?> type = context.getRequiredType();
        UUID id = UUID.fromString( reader.getAttribute( XMLConstants.ID_ATTRIBUTE ) );
        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>)createCollection( type );
        Collection<Object> baseCollection = new ArrayList<Object>();
        populateCollection( reader, context, collection, baseCollection );
        Snapshot.getLocal().resolveObject( id, collection, baseCollection );
        return collection;
    }


    // protected void populateArray(HierarchicalStreamReader reader,
    // UnmarshallingContext context, Object collection) {
    // int i =0;
    // while (reader.hasMoreChildren()) {
    // Object item = readItem(reader, context, collection);
    // Array.set(collection, i, item);
    // resetLevel(reader);
    // i++;
    // }
    // }
    protected void populateCollection(
        HierarchicalStreamReader reader,
        UnmarshallingContext context,
        Collection<Object> collection,
        Collection<Object> baseCollection )
    {
        while ( reader.hasMoreChildren() )
        {
            Object item = readItemInCollection( reader, context, collection );
            collection.add( item );
            if ( baseCollection != null )
                baseCollection.add( item );
            resetLevel( reader );
        }
    }


    private void resetLevel( HierarchicalStreamReader reader )
    {
        reader.moveUp();
        reader.moveUp();
    }


    private Object readItemInCollection(
        HierarchicalStreamReader reader,
        UnmarshallingContext context,
        Collection<Object> collection )
    {

        reader.moveDown();
        String idAttr = reader.getAttribute( XMLConstants.ID_ATTRIBUTE );
        UUID id = null;
        id = UUID.fromString( idAttr );
        Object item = null;
        if ( reader.hasMoreChildren() )
        {
            reader.moveDown();
            item = readItem( reader, context, collection );
            // If after the read, the item isnt id'd (AKA it is a primitive)
            // then grab the id stored in the _item tag
            UUID readGenId = Snapshot.lookupId( item, false );
            if ( readGenId == null )
                Snapshot.getLocal().resolveObject( id,
                    item,
                    (Map<String, Object>)null );
        }
        return item;
    }
}
