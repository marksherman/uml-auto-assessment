package student.web.internal;

import java.util.Collection;
import java.util.HashSet;

// Maybe this class should be local inside AbstractPersistent Map, since
// that is the only place it is used.
public class ReadOnlySet<T> extends HashSet<T>
{
    /**
	 *
	 */
    private static final long serialVersionUID = 3888304419328745503L;


    /**
     * Constructs a new set containing the elements in the specified
     * collection.
     *
     * @param c The collection whose elements are to be placed into this set.
     * @throws NullPointerException If the specified collection is null.
     */
    public ReadOnlySet(Collection<? extends T> c)
    {
        super();
        for(T entry : c)
        {
            super.add( entry );
        }
    }


    /**
     * This set is read-only, so this method is unsupported.  It throws an
     * UnsupportedOperationException.
     * @param e The element to add.
     */
    public boolean add(T e)
    {
        throw new UnsupportedOperationException();
    }


    /**
     * This set is read-only, so this method is unsupported.  It throws an
     * UnsupportedOperationException.
     * @param o The element to remove.
     */
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();
    }


    /**
     * This set is read-only, so this method is unsupported.  It throws an
     * UnsupportedOperationException.
     */
    public void clear()
    {
        throw new UnsupportedOperationException();
    }


    public Object clone()
    {
        return new ReadOnlySet<T>(this);
    }


    /**
     * This set is read-only, so this method is unsupported.  It throws an
     * UnsupportedOperationException.
     * @param c The collection of elements to remove.
     */
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }


    /**
     * This set is read-only, so this method is unsupported.  It throws an
     * UnsupportedOperationException.
     * @param c The collection of elements to retain.
     */
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }


    /**
     * This set is read-only, so this method is unsupported.  It throws an
     * UnsupportedOperationException.
     * @param c The collection of elements to add.
     */
    public boolean addAll(Collection<? extends T> c)
    {
        throw new UnsupportedOperationException();
    }
}
