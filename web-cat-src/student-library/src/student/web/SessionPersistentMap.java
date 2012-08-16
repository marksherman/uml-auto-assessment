package student.web;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import student.web.internal.ApplicationSupportStrategy;
import student.web.internal.LocalityService;

// TODO: add equals()/hashCode()/toString() to all persistent maps
/**
 * The Session persistence layer represents all of the persisted items
 * associated with the current web session. In a desktop development environment
 * a session is equivalent to a single execution of the JVM. There is a single
 * map in the system for all session objects this means that many of the methods
 * do not respect the generic type argument of this class.
 *
 * @author mjw87
 *
 * @param <T>
 */
public class SessionPersistentMap<T> implements PersistentMap<T>
{
    private ApplicationSupportStrategy support;

    private Class<T> typeAware;

    private Map<String, Object> self;


    @SuppressWarnings("unchecked")
    public SessionPersistentMap( Class<T> type )
    {
        support = LocalityService.getSupportStrategy();
        this.typeAware = type;

        this.self = support.getSessionPersistentMap();

    }


    public int size()
    {
        return self.size();
    }


    public boolean isEmpty()
    {
        return self.isEmpty();
    }


    public boolean containsKey( Object key )
    {
        assert key instanceof String : "Persistence maps only allows for keys of type String";
        return self.containsKey( key );
    }


    public boolean containsValue( Object value )
    {
        if ( value == null || !typeAware.isInstance( value ) )
            return false;
        return self.containsValue( value );
    }


    @SuppressWarnings("unchecked")
    public T get( Object key )
    {
        assert key instanceof String : "Persistence maps only allows for keys of type String";
        Object o = self.get( key );
        Object unAlias = support.resolveAlias( o );
        if(unAlias != null)
            o = unAlias;
        if ( o != null && typeAware.isInstance( o ) )
            return (T)o;
        return null;
    }


    @SuppressWarnings("unchecked")
    public T put( String key, T value )
    {
        assert key != null : "An objectId cannot be null";
        assert key.length() > 0 : "An objectId cannot be an empty string";
        assert !( value instanceof Class ) : "The object to store cannot be a class; perhaps you wanted "
            + "to provide an instance of this class instead?";
        Object o = get( key );
        Object aliasValue = support.getAlias(value);
        if ( o == null || !typeAware.isInstance( o ) )
            o = null;
        self.put( key, aliasValue );
        return (T)o;
    }


    @SuppressWarnings("unchecked")
    public T remove( Object key )
    {
        assert key instanceof String : "Persistence maps only allows for keys of type String";
        String objectId = (String)key;
        assert objectId != null : "An key cannot be null";
        Object o = self.get( objectId );
        if ( o == null || !typeAware.isInstance( o ) )
            o = null;
        self.remove( objectId );
        return (T)o;
    }


    public void putAll( Map<? extends String, ? extends T> m )
    {
        self.putAll( m );
    }


    public void clear()
    {
        self.clear();
    }


    public Set<String> keySet()
    {
        return self.keySet();
    }


    @SuppressWarnings("unchecked")
    public Collection<T> values()
    {
        Set<String> keys = keySet();
        Set<T> values = new HashSet<T>();
        for ( String key : keys )
        {
            Object o = self.get( key );
            if ( typeAware.isInstance( o ) )
            {
                values.add( (T)o );
            }
            else
            {
                values.add( null );
            }
        }
        return values;
    }


    @SuppressWarnings("unchecked")
    public Set<java.util.Map.Entry<String, T>> entrySet()
    {
        Set<java.util.Map.Entry<String, Object>> entrySet = self.entrySet();
        Set<java.util.Map.Entry<String, T>> projectedEntrySet = new HashSet<java.util.Map.Entry<String, T>>();
        for ( java.util.Map.Entry<String, Object> entry : entrySet )
        {
            String key = entry.getKey();
            T value;
            if ( typeAware.isInstance( self.get( key ) ) )
            {
                value = (T)self.get(key);
            }
            else
            {
                value = null;
            }
            Entry<String, T> newEntry = new SimpleEntry<String, T>( key, value );
            projectedEntrySet.add( newEntry );
        }

        return projectedEntrySet;
    }
}
