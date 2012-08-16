package student.web.internal;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.AbstractDualBidiMap;


/**
 * Implementation of <code>BidiMap</code> that uses two <code>HashMap</code>
 * instances.
 * <p>
 * Two <code>HashMap</code> instances are used in this class. This provides fast
 * lookups at the expense of storing two sets of map entries. Commons
 * Collections would welcome the addition of a direct hash-based implementation
 * of the <code>BidiMap</code> interface.
 * <p>
 * NOTE: From Commons Collections 3.1, all subclasses will use
 * <code>HashMap</code> and the flawed <code>createMap</code> method is ignored.
 * 
 * NOTE: Modified by Mike Woods to use an Identity Hash map for key->value
 * 
 * @since Commons Collections 3.0
 * @version $Id: DualBidiIdentityHashMap.java,v 1.1 2011/02/12 16:45:08 mwoodsvt
 *          Exp $
 * 
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 */
public class DualBidiIdentityHashMap<Key, Value> extends AbstractDualBidiMap
{
    /** Ensure serialization compatibility */
    private static final long serialVersionUID = 721969328361808L;


    /**
     * Creates an empty <code>HashBidiMap</code>.
     */
    public DualBidiIdentityHashMap()
    {
        super( new IdentityHashMap<Key, Value>(), new HashMap<Value, Key>() );
    }


    /**
     * Constructs a <code>HashBidiMap</code> and copies the mappings from
     * specified <code>Map</code>.
     * 
     * @param map
     *            the map whose mappings are to be placed in this map
     */
    public DualBidiIdentityHashMap( Map<Key, Value> map )
    {
        super( new IdentityHashMap<Key, Value>(), new HashMap<Value, Key>() );
        putAll( map );
    }


    /**
     * Constructs a <code>HashBidiMap</code> that decorates the specified maps.
     * 
     * @param normalMap
     *            the normal direction map
     * @param reverseMap
     *            the reverse direction map
     * @param inverseBidiMap
     *            the inverse BidiMap
     */
    protected DualBidiIdentityHashMap(
        Map<Key, Value> normalMap,
        Map<Key, Value> reverseMap,
        BidiMap inverseBidiMap )
    {
        super( normalMap, reverseMap, inverseBidiMap );
    }


    /**
     * Creates a new instance of this object.
     * 
     * @param normalMap
     *            the normal direction map
     * @param reverseMap
     *            the reverse direction map
     * @param inverseBidiMap
     *            the inverse BidiMap
     * @return new bidi map
     */
    protected BidiMap createBidiMap(
        Map normalMap,
        Map reverseMap,
        BidiMap inverseBidiMap )
    {
        return new DualBidiIdentityHashMap<Key, Value>( normalMap,
            reverseMap,
            inverseBidiMap );
    }
}
