/*==========================================================================*\
 |  $Id: Constructor.java,v 1.2 2011/06/09 15:31:24 stedwar2 Exp $
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
 *  TODO: add annotation support.
 *
 *  @param <ClassType> If present, this is a constraint on the type of
 *  object this constructor creates.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/06/09 15:31:24 $
 */
public class Constructor<ClassType>
    extends Behavior<Constructor<ClassType>,
        java.lang.reflect.Constructor<ClassType>>
{
    //~ Fields ................................................................

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new Constructor object that represents a constructor filter.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfThisStage A description of this stage in the
     * filter chain.
     */
    protected Constructor(
        Constructor<ClassType> previous, String descriptionOfThisStage)
    {
        super(previous, descriptionOfThisStage);
        // TODO Auto-generated constructor stub
    }


    //~ Public Methods ........................................................


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    @Override
    protected Constructor<ClassType> createFreshFilter(
        Constructor<ClassType> previous,
        String descriptionOfThisStage)
    {
        return new Constructor<ClassType>(previous, descriptionOfThisStage);
    }


    // ----------------------------------------------------------
    @Override
    protected Constructor<ClassType> createFreshFilter(
        java.lang.reflect.Constructor<ClassType> object)
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    @Override
    protected String nameOf(java.lang.reflect.Constructor<ClassType> object)
    {
        return object.getName();
    }


    // ----------------------------------------------------------
    @Override
    protected int modifiersFor(java.lang.reflect.Constructor<ClassType> object)
    {
        return object.getModifiers();
    }


    // ----------------------------------------------------------
    @Override
    protected String filteredObjectDescription()
    {
        return "constructor";
    }
}
