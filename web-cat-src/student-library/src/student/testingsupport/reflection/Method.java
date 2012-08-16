/*==========================================================================*\
 |  $Id: Method.java,v 1.2 2011/06/09 15:31:24 stedwar2 Exp $
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
 *  @param <ReturnType> If present, this is a constraint on the return type
 *  of the method that this object represents.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/06/09 15:31:24 $
 */
public class Method<ReturnType>
    extends Behavior<Method<ReturnType>, java.lang.reflect.Method>
{
    //~ Fields ................................................................

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new Method object that represents a method filter.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfThisStage A description of this stage in the
     * filter chain.
     */
    protected Method(Method<ReturnType> previous, String descriptionOfThisStage)
    {
        super(previous, descriptionOfThisStage);
    }


    //~ Public Methods ........................................................


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    @Override
    protected Method<ReturnType> createFreshFilter(
        Method<ReturnType>previous,
        String descriptionOfThisStage)
    {
        return new Method<ReturnType>(previous, descriptionOfThisStage);
    }


    // ----------------------------------------------------------
    @Override
    protected Method<ReturnType> createFreshFilter(
        java.lang.reflect.Method object)
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    @Override
    protected String nameOf(java.lang.reflect.Method object)
    {
        return object.getName();
    }


    // ----------------------------------------------------------
    @Override
    protected int modifiersFor(java.lang.reflect.Method object)
    {
        return object.getModifiers();
    }


    // ----------------------------------------------------------
    @Override
    protected String filteredObjectDescription()
    {
        return "method";
    }
}
