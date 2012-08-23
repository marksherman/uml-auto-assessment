/*==========================================================================*\
 |  $Id: IterableSequence.java,v 1.2 2010/02/23 17:06:35 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student;
import java.util.Iterator;


//-------------------------------------------------------------------------
/**
 *  This class is just an iterator wrapped inside an Iterable so that it
 *  can be used in for-each loops.
 *
 *  @param <T> The type of elements contained in this sequence
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/02/23 17:06:35 $
 */
public class IterableSequence<T>
    implements Iterable<T>
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new IterableSequence.
     * @param iterator The iterator to wrap
     */
    public IterableSequence(Iterator<T> iterator)
    {
        iter = iterator;
    }


    //~ Public methods ........................................................

    // ----------------------------------------------------------
    /**
     * Returns an iterator over a set of elements of type T.
     * @return an iterator
     */
    public Iterator<T> iterator()
    {
        return iter;
    }


    //~ Instance/static variables .............................................
    private Iterator<T> iter;
}
