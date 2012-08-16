/*==========================================================================*\
 |  $Id: Behavior.java,v 1.1 2011/06/09 15:31:24 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package student.testingsupport.reflection;

//-------------------------------------------------------------------------
/**
 *  TODO: document.
 *
 *  @param <ConcreteFilterType> A parameter indicating the concrete subclass
 *  of this class, for use in providing more specialized return types on
 *  some methods.
 *  @param <FilteredObjectType> A parameter indicating the kind of object
 *  this filter accepts.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2011/06/09 15:31:24 $
 */
public abstract class Behavior<ConcreteFilterType, FilteredObjectType>
    extends NameFilter<ConcreteFilterType, FilteredObjectType>
{
    //~ Fields ................................................................



    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new Behavior object.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfConstraint A description of the constraint imposed
     * by this filter (just one step in the chain).
     */
    protected Behavior(
        Behavior<ConcreteFilterType, FilteredObjectType> previous,
        String descriptionOfConstraint)
    {
        super(previous, descriptionOfConstraint);
    }


    //~ Public Methods ........................................................



    //~ Protected Methods .....................................................

}
