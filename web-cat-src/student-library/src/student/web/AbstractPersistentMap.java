package student.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.AbstractMap.SimpleEntry;
import student.web.internal.ApplicationSupportStrategy;
import student.web.internal.LocalityService;
import student.web.internal.ObjectFieldExtractor;
import student.web.internal.PersistentStorageManager;
import student.web.internal.ReadOnlySet;
import student.web.internal.converters.AliasService;


/**
 *
 * This is an abstract map representing all of the general functionality for
 * maps that create projections on a particular persistence layer. Note that
 * some of these functions return the results from the entire persistence layer
 * without respect to the generic type of this map. However, some of the
 * operations do respect the generic type of the implementing map. To implement
 * this map, the client only needs to implement a constructor that informs the
 * super class of the generic type (to prevent type erasure) and the directory
 * to treat as the base directory in the persistence store. The directory is non
 * absolute and is based on the configured datastore directory.
 *
 * @author mjw87
 *
 * @param <T> The type of objects stored in the map.
 */
public abstract class AbstractPersistentMap<T>
    implements PersistentMap<T>
{
//    /**
//     * Separator character for keywords within a persisted object id. This is
//     * used for conversion to a remote map.
//     */
//    protected static final String SEPARATOR = "-.-";

//    /**
//     * Timestamp of the last time the keyset of the persistence library was
//     * retrieved.
//     */
//    private long idSetTimestamp = 0L;

    /**
     * The class type this map is projecting onto the persistence store.
     */
    protected Class<T> typeAware;
    /**
     * The class loader to use when looking up classes of objects being loaded.
     */
    protected ClassLoader loader;
//    /**
//     * The latest obtained keyset from the persistence store.
//     */
//    protected HashSet<String> idSet = new HashSet<String>();
//    private ReadOnlyHashSet<String> snapshotIds;
//    private long snapshotTimestamp = 0L;

    /**
     * The cached context map for objects retrieved from the store. It is used
     * to reconstitute objects.
     */
    private Map<String, PersistentStorageManager.StoredObject> context;

    /**
     * An extractor object for extracting fields from objects.
     */
    private ObjectFieldExtractor extractor = new ObjectFieldExtractor(
        LocalityService.getSupportStrategy().getReflectionProvider());

    /**
     * The instance of the persistence store this Map is projecting on.
     */
    private PersistentStorageManager PSM;

    private ApplicationSupportStrategy support;

    /**
     * Get the cache ID for this map.
     * @return This map's cache ID.
     */
    protected abstract String getCacheId(String uniqueId);

    /**
     * Create a new map.
     * @param directoryName The directory name to use to back this map.
     * @param typeAware The type of objects stored in this map.
     */
    protected AbstractPersistentMap(String uniqueId, String directoryName, Class<T>  typeAware )
    {

        init( uniqueId, directoryName, typeAware );
        loader = typeAware.getClassLoader();
    }
    /**
     * Create a new map.
     * @param directoryName The directory name to use to back this map.
     * @param typeAware The type of objects stored in this map.
     * @param loader The class loader to use when looking up classes of
     *               loaded objects.
     */
    protected AbstractPersistentMap(String uniqueId, String directoryName, Class<T> typeAware, ClassLoader loader)
    {
        init(uniqueId, directoryName, typeAware);
        this.loader = loader;
    }

    private void init(String uniqueId, String directoryName, Class<T> typeAware )
    {
        PSM = PersistentStorageManager.getInstance( directoryName );
        support = LocalityService.getSupportStrategy();
        if ( support.getPersistentCache( getCacheId(uniqueId) ) != null )
        {
            context = support.getPersistentCache( getCacheId(uniqueId) );
        }
        else
        {
            context = support.initPersistentCache( getCacheId(uniqueId));
        }
        this.typeAware = typeAware;
        AliasService.addAliasClass( typeAware );
    }

    public T remove( Object key )
    {
        assert key != null : "An key cannot be null";
        assert key instanceof String : "Persistence maps only allows for keys of type String";
        String objectId = (String)key;
        assert objectId.length() > 0 : "An key cannot be an empty string";
        T previousValue = getPrevious( (String)key );
        removePersistentObject( objectId );
        return previousValue;

    }


    public T put( String key, T value )
    {
        assert key != null : "An objectId cannot be null";
        assert key.length() > 0 : "An objectId cannot be an empty string";
        assert !( value instanceof Class ) : "The object to store cannot "
            + "be a class; perhaps you wanted "
            + "to provide an instance of this class instead?";
        T previousValue = getPrevious( key );
        setPersistentObject( key, value );
        return previousValue;
    }


    private T getPrevious(String key)
    {
        PersistentStorageManager.StoredObject cached = context.get(key);
        T previousValue = null;
        if (cached != null)
        {
            try
            {
                @SuppressWarnings("unchecked")
                T pv = (T)cached.value();
                previousValue = pv;
            }
            catch (ClassCastException e)
            {
                // Can't cast!
            }
        }

        if (cached == null)
        {
            PersistentStorageManager.StoredObject previous = PSM.getPersistentObject( key, context,
                typeAware.getClassLoader() );
            if ( previous != null )
            {
//                if ( previous.value().getClass().equals( typeAware ) )
                if (typeAware.isInstance(previous.value()))
                {
                    @SuppressWarnings("unchecked")
                    T pv = (T)previous.value();
                    previousValue = pv;
                }
            }
        }
        return previousValue;
    }


    public void putAll( Map<? extends String, ? extends T> externalMap )
    {
        for ( Map.Entry<? extends String, ? extends T> entry : externalMap.entrySet() )
        {
            setPersistentObject( entry.getKey(), entry.getValue() );
        }
    }


    public T get( Object key )
    {
        assert key != null : "An objectId cannot be null";
        assert key instanceof String : "Persistence maps only allows for keys of type String";
        String objectId = (String)key;
        assert objectId.length() > 0 : "An objectId cannot be an empty string";
        T foundObject = getPersistentObject( objectId );
        if ( context.get( key ) != null
            && PSM.hasFieldSetChanged( (String)key, context.get( key )
                .timestamp() ) )
        {
            PSM.refreshPersistentObject( (String)key, context,
                context.get( key ),
                typeAware.getClassLoader() );
        }
        return foundObject;
    }


    //
    public boolean containsKey( Object key )
    {
        assert key instanceof String : "Persistence maps only allows for keys of type String";
        String objectId = (String)key;
        return PSM.hasFieldSetFor( objectId, null );
    }


    public int size()
    {
        return PSM.getAllIds().size();
    }


    public boolean isEmpty()
    {

        return PSM.getAllIds().isEmpty();

    }


    //
    public boolean containsValue( Object value )
    {
        for ( String id : PSM.getAllIds() )
        {
            T persistedObject = getPersistentObject( id );
            // Just incase the store shifted under us
            if ( persistedObject != null && persistedObject.equals( value ) )
            {
                return true;
            }
        }
        return false;

    }


    //
    public void clear()
    {

        context.clear();
        for ( String id : PSM.getAllIds() )
        {
            removePersistentObject( id );
        }
        PSM.flushCache();
    }


    public Set<String> keySet()
    {
        return PSM.getAllIds();

    }


    public Collection<T> values()
    {
        Set<T> valueSet = new HashSet<T>();
        for ( String key : PSM.getAllIds() )
        {
            T lookup = getPersistentObject( key );
            // Just incase the persistence store moved under us
            if ( lookup != null )
                valueSet.add( lookup );
        }
        return valueSet;

    }


    public Set<Entry<String, T>> entrySet()
    {

        HashSet<Entry<String, T>> valueSet = new HashSet<Entry<String, T>>();

        for ( String id : PSM.getAllIds() )
        {
            T lookup = getPersistentObject( id );
            // Just incase the persistence store moved under us
            if ( lookup != null )
            {
                valueSet.add( new SimpleEntry<String, T>( id, lookup ) );
            }
        }
        return new ReadOnlySet<Entry<String, T>>(valueSet);
    }


    /**
     * Look up the persistent object with the given ID.
     * @param objectId The object ID to look up.
     * @return The object associated with the given ID.
     */
    protected T getPersistentObject( String objectId )
    {
        T result = null;
        PersistentStorageManager.StoredObject latest = context.get( objectId );
        if ( latest != null
            && !PSM.hasFieldSetChanged( objectId, latest.timestamp() ) )
        {
//            if ( latest.value().getClass().equals( typeAware ) )
            if( typeAware.isInstance( latest.value() ))
            {
                result = returnAsType(typeAware, latest.value());
            }
        }
        else
        {
            if ( loader == null )
            {
                loader = this.getClass().getClassLoader();
            }
            latest = PSM.getPersistentObject( objectId, context, loader );
            if ( latest != null )
            {
                context.put( objectId, latest );
                result = returnAsType( typeAware, latest.value() );
                if ( result != latest.value() )
                {
                    latest.setValue( result );
                }
            }
        }
        return result;
    }


    private <V> V returnAsType( Class<V> t, Object value )
    {
        if ( value == null )
        {
            return null;
        }
        if ( value instanceof TreeMap && !TreeMap.class.isAssignableFrom( t ) )
        {
            @SuppressWarnings("unchecked")
            Map<String, Object> valueAsMap = (Map<String, Object>)value;
            value = extractor.fieldMapToObject(t, valueAsMap);
        }
        if ( t.isAssignableFrom( value.getClass() ) )
        {
            @SuppressWarnings("unchecked")
            V valueAsV = (V)value;
            return valueAsV;
        }

        return null;
    }


    /**
     * Set/store the persistent object for a given ID.
     * @param <ObjectType> The type of object to store.
     * @param objectId The ID to associate the object with.
     * @param object The object to store.
     */
    protected <ObjectType> void setPersistentObject(
        String objectId,
        ObjectType object)
    {
        try
        {
            PersistentStorageManager.StoredObject latest = context.get( objectId );
            if ( latest != null )
            {
                latest.setValue( object );
                if ( loader == null )
                    loader = this.getClass().getClassLoader();
                PSM.storePersistentObjectChanges( objectId, context, latest, loader );
                context.put( objectId, latest );
            }
            else
            {
                latest = PSM.storePersistentObject( objectId, context, object );
                context.put( objectId, latest );
            }
        }
        catch ( RuntimeException e )
        {
            PSM.removeFieldSet( objectId );
            throw e;
        }
    }


    /**
     * Remove a persistent object from the store.
     * @param objectId The ID of the object to remove.
     */
    protected void removePersistentObject( String objectId )
    {
        PSM.removeFieldSet( objectId );
    }

}
