/*==========================================================================*\
 |  $Id: ReportUtilityEnvironment.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.reporter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlOps;
import ognl.OgnlRuntime;

//-------------------------------------------------------------------------
/**
 * <p>
 * A class that contains various handy utility functions that can be used from
 * OGNL expressions in a report. Many of these methods are functional
 * iteration-based algorithms, given the inability to easily do loops in OGNL. 
 * </p><p>
 * If you use the <tt>newOgnlContext()</tt> static method to create an OGNL
 * context, then an instance of this class is stored in the context as the
 * variable <tt>#ENV</tt>, so each of the public methods below can be called
 * by invoking them on that variable; for example,
 * <tt>#ENV.accumulate(...)</tt>.
 * </p><p>
 * Many of the methods in this class take OGNL lambda expressions as an
 * argument; to simplify the documentation below, these lambda expressions will
 * be categorized as one of the following:
 * <ul>
 * <li><b>UnaryOp:</b> an expression that takes a single argument, denoted by
 * <tt>#this</tt>. The return value is arbitrary.</li>
 * <li><b>BinaryOp:</b> an expression that takes two arguments, denoted by
 * <tt>#this.l</tt> and <tt>#this.r</tt> (that is, left-hand side and
 * right-hand side). The return type is arbitrary.</li>
 * <li><b>UnaryPredicate:</b> an expression that takes a single argument,
 * denoted by <tt>#this</tt>, and has a Boolean return type.</li>
 * <li><b>BinaryPredicate:</b> an expression that takes two arguments, denoted
 * by <tt>#this.l</tt> and <tt>#this.r</tt>, and has a Boolean return type.</li>
 * <li><b>Generator:</b> an expression that takes no arguments (<tt>#this</tt>
 * is undefined inside it) and returns an arbitrary value. 
 * </ul>
 * </p><p>
 * Due to the way OGNL parses expressions, a lambda expression cannot be
 * included directly as the argument to one of these methods; it must be stored
 * in a variable first. In other words,
 * <pre>
 *     #ENV.accumulate({1,2,3}, :[ #this.l + #this.r ])</pre>
 *     
 * is incorrect. The above expression must instead be written as
 * <pre>
 *     #f = :[ #this.l + #this.r ], #ENV.accumulate({1,2,3}, #f)</pre>
 * </p>
 * 
 * @author Tony Allevato
 * @version $Id: ReportUtilityEnvironment.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportUtilityEnvironment
{
    //~ Static methods ........................................................
    
    // ----------------------------------------------------------
    /**
     * Creates a new <tt>OgnlContext</tt> that is prepped with the utility
     * functions in the <tt>#ENV</tt> variable.
     * 
     * @return a new <tt>OgnlContext</tt>
     */
    public static OgnlContext newOgnlContext()
    {
        OgnlContext context = new OgnlContext();
        context.put("ENV", new ReportUtilityEnvironment(context));
        
        return context;
    }


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * <p>
     * Creates a new instance of the ReportUtilityEnvironment class.
     * </p><p>
     * This method takes as a parameter the OGNl context that will be used to
     * execute the expressions in the report. This is required by the methods
     * that take a lambda expression as an argument so that such expressions
     * can refer to variables and names outside the lambda.
     * </p>
     * 
     * @param globalContext the global OGNL context that will be used to
     *     execute the expressions in the report
     */
    public ReportUtilityEnvironment(OgnlContext globalContext)
    {
        this.context = globalContext;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * <p>
     * Computes the sum of all the objects in the specified list.
     * </p><p>
     * If the list contains elements of different types, then the type of the
     * result is determined by OGNL type conversions, after performing a
     * first-to-last addition of each item in the list.
     * </p>
     * 
     * @param list the list of items to add
     * @return the sum of the objects in the list
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public Object accumulate(List<Object> list) throws OgnlException
    {
        Object initial = null;
        
        if (list != null && !list.isEmpty())
        {
            initial = defaultValueForType(list.get(0).getClass());
        }
    
        return accumulate(list, initial,
                (Node) Ognl.parseExpression("#this.l + #this.r"));
    }


    // ----------------------------------------------------------
    /**
     * Computes the generalized sum of all the objects in the specified list
     * by using a lambda expression as the "add" operation.
     * 
     * @param list the list of items to "add"
     * @param initial the initial value from which to begin the accumulation
     * @param binaryOp a <b>BinaryOp</b> OGNL lambda expression used to sum the
     *     items in the list. <tt>#this.l</tt> is the currently accumulated
     *     "sum" so far, and <tt>#this.r</tt> is the next item to be "added".
     *     The result of this expression should be the "sum" of those two
     *     values.
     * @return the generalized sum of the objects in the list
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public Object accumulate(List<Object> list, Object initial, Node binaryOp)
    throws OgnlException
    {
        if (list == null || list.isEmpty())
        {
            return null;
        }
    
        HashMap<String, Object> args = new HashMap<String, Object>();
        Object oldThis = replaceThisInContext(args);
    
        for(Object item : list)
        {
            args.put("l", initial);
            args.put("r", item);
            
            initial = binaryOp.getValue(context, args);
        }
        
        replaceThisInContext(oldThis);
        
        return initial;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list of values where the (<i>i</i>+1)st element in the result
     * is the difference between the (<i>i</i>+1)st element and the <i>i</i>th
     * element in the specified list.
     * 
     * @param list the list of items from which to compute the
     *     adjacent differences
     * @return the list of adjacent differences
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public List<Object> adjacentDifference(List<Object> list)
    throws OgnlException
    {
        return adjacentDifference(list,
                (Node) Ognl.parseExpression("#this.r - #this.l"));
    }

    
    // ----------------------------------------------------------
    /**
     * <p>
     * Returns a list of values where the (<i>i</i>+1)st element in the result
     * is the difference between the (<i>i</i>+1)st element and the <i>i</i>th
     * element in the specified list. The 0th element in the resulting list is
     * equal to the 0th element in the specified list.
     * </p><p>
     * This version of the method is a generalized operation where the
     * specified expression is used to compute the "difference", rather than
     * standard subtraction.
     * </p>
     * 
     * @param list the list of items from which to compute the adjacent
     *     differences
     * @param binaryOp a <b>BinaryOp</b> OGNL lambda expression representing
     *     the operation used to "subtract" the items in the list;
     *     <tt>#this.l</tt> is the <i>i</i>th element, and <tt>#this.r</tt> is
     *     the (<i>i</i>+1)st element. The result of this expression should be
     *     the "difference" of <tt>#this.l</tt> from <tt>#this.r</tt>.
     * @return the list of generalized adjacent differences
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public List<Object> adjacentDifference(List<Object> list, Node binaryOp)
    throws OgnlException
    {
        if (list == null)
        {
            return null;
        }

        ArrayList<Object> newList = new ArrayList<Object>();

        if (!list.isEmpty())
        {
            HashMap<String, Object> args = new HashMap<String, Object>();
            Object oldThis = replaceThisInContext(args);

            Object value = list.get(0);
            newList.add(value);
            
            for (int i = 0; i < list.size() - 1; i++)
            {
                args.put("l", list.get(i));
                args.put("r", list.get(i + 1));

                value = binaryOp.getValue(context, args);
                newList.add(value);
            }
            
            replaceThisInContext(oldThis);
        }
        
        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Returns the number of items in the specified list that satisfy a
     * predicate. This is essentially the same as the OGNL expression
     * <tt>list.{? predicate }.size</tt>, but this method does not require that
     * the sub-list be computed in memory before getting its size.
     * 
     * @param list the list containing the items to count
     * @param predicate a <b>UnaryPredicate</b> OGNL lambda expression that is
     *     called to determine if an element in the list should be counted.
     *     <tt>#this</tt> refers to the current item in the list.
     * @return the number of items in the list that satisfy the predicate
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public int countIf(List<Object> list, Node predicate)
    throws OgnlException
    {
        if (list == null || list.isEmpty())
        {
            return 0;
        }
    
        int count = 0;
    
        for (Object item : list)
        {
            Object oldThis = replaceThisInContext(item);
            
            if (predicate.getValue(context, item).equals(Boolean.TRUE))
                count++;
            
            replaceThisInContext(oldThis);
        }
        
        return count;
    }


    // ----------------------------------------------------------
    /**
     * Evaluates an OGNL lambda expression for each element in the specified
     * list.
     * 
     * @param list the list of items to iterate over
     * @param unaryOp a <b>UnaryOp</b> OGNL lambda expression that will be
     *     called for each item in the list. Inside this expression,
     *     <tt>#this</tt> refers to the current item in the list; the result of
     *     the expression is ignored.
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public void forEach(List<Object> list, Node unaryOp) throws OgnlException
    {
        for (Object item : list)
        {
            Object oldThis = replaceThisInContext(item);
            unaryOp.getValue(context, item);
            replaceThisInContext(oldThis);
        }
    }


    // ----------------------------------------------------------
    /**
     * Returns the inner product of the two lists. The lists are assumed to be
     * numeric.
     * 
     * @param list1 the first list of values
     * @param list2 the second list of values
     * @return the scalar inner product of the two lists
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public Object innerProduct(List<Object> list1, List<Object> list2)
    throws OgnlException
    {
        return innerProduct(list1, list2, Integer.valueOf(0));
    }


    // ----------------------------------------------------------
    /**
     * Returns the inner product of the two lists, using the specified initial
     * sum. The lists are assumed to be numeric.
     * 
     * @param list1 the first list of values
     * @param list2 the second list of values
     * @param initial the initial value to which the pairwise products from the
     *     lists are added
     * @return the scalar inner product of the two lists
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public Object innerProduct(List<Object> list1, List<Object> list2,
            Object initial)
    throws OgnlException
    {
        return innerProduct(list1, list2, initial,
                (Node) Ognl.parseExpression("#this.l + #this.r"),
                (Node) Ognl.parseExpression("#this.l * #this.r"));
    }

    
    // ----------------------------------------------------------
    /**
     * Returns the generalized inner product of the two lists, using the
     * specified initial sum, additive operation, and multiplicative operation.
     * 
     * @param list1 the first list of values
     * @param list2 the second list of values
     * @param initial the initial value to which the pairwise products from the
     *     lists are added
     * @param addBinaryOp a <b>BinaryOp</b> OGNL lambda expression used to
     *     accumulate the pairwise products. <tt>#this.l</tt> is the
     *     accumulated value thus far, and <tt>#this.r</tt> is the next item to
     *     accumulate. The result of this expression should be the "sum" of
     *     those two values.
     * @param multBinaryOp a <b>BinaryOp</b> OGNL lambda expression used to
     *     calculate the pairwise products of values in the lists.
     *     <tt>#this.l</tt> is the <i>i</i>th value in <tt>list1</tt>, and
     *     <tt>#this.r</tt> is the <i>i</i>th item in <tt>list2</tt>. The
     *     result of this expression should be the "product" of those two
     *     values.
     * @return the generalized inner product of the two lists
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public Object innerProduct(List<Object> list1, List<Object> list2,
            Object initial, Node addBinaryOp, Node multBinaryOp)
    throws OgnlException
    {
        if (list1 == null || list2 == null)
        {
            return null;
        }

        if (!list1.isEmpty() && !list2.isEmpty())
        {
            HashMap<String, Object> args = new HashMap<String, Object>();
            Object oldThis = replaceThisInContext(args);

            for (int i = 0; i < Math.min(list1.size(), list2.size()); i++)
            {
                args.put("l", list1.get(i));
                args.put("r", list2.get(i));
                Object value = multBinaryOp.getValue(context, args);
                
                args.put("l", initial);
                args.put("r", value);
                initial = addBinaryOp.getValue(context, args);
            }
            
            replaceThisInContext(oldThis);
        }
        
        return initial;
    }


    // ----------------------------------------------------------
    /**
     * Returns a new list that contains the elements in the first list followed
     * by the elements in the second.
     * 
     * @param list1 the first list
     * @param list2 the second list
     * @return a list containing the elements in the first list followed by the
     *     elements in the second
     */
    public List<Object> joinLists(List<Object> list1, List<Object> list2)
    {
        ArrayList<Object> newList = new ArrayList<Object>();
    
        newList.addAll(list1);
        newList.addAll(list2);
        
        return newList;
    }


    // ----------------------------------------------------------
    /**
     * <p>
     * Returns a new list that contains the elements in each of the lists
     * passed to the function. These lists are themselves passed in as a list
     * of lists, permitting any number of lists to be joined.
     * </p><p>
     * As an example, <tt>#ENV.joinLists( {{1,2,3}, {4,5,6}, {7,8,9}} )</tt>
     * would return the list <tt>{1,2,3,4,5,6,7,8,9}</tt>.
     * </p>
     * 
     * @param lists the list containing the lists that should be joined
     * @return a list containing the elements in each of the specified lists
     */
    public List<Object> joinLists(List<List<Object>> lists)
    {
        ArrayList<Object> newList = new ArrayList<Object>();
    
        for (List<Object> list : lists)
        {
            newList.addAll(list);
        }
        
        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Returns a new map formed by merging the keys and values from the two
     * specified maps. In the event that a key is found in both maps, the value
     * in map2 takes precedence over that in map1.
     * 
     * @param map1 the first map to merge
     * @param map2 the second map to merge
     * @return a map containing the keys and values from map1 and map2
     */
    public Map<Object, Object> joinMaps(Map<Object, Object> map1,
            Map<Object, Object> map2)
    {
        Map<Object, Object> newMap = new LinkedHashMap<Object, Object>();
    
        newMap.putAll(map1);
        newMap.putAll(map2);
    
        return newMap;
    }


    // ----------------------------------------------------------
    /**
     * Returns a new map formed by merging the keys and values from the
     * specified maps. In the event that a key is found in multiple maps, the
     * value in the later map takes precedence over that in the earlier one.
     * 
     * @param maps the list of maps to merge
     * @return a map containing the keys and values from the specified maps
     */
    public Map<Object, Object> joinMaps(List<Map<Object, Object>> maps)
    {
        Map<Object, Object> newMap = new LinkedHashMap<Object, Object>();
    
        for (Map<Object, Object> map : maps)
        {
            newMap.putAll(map);
        }
    
        return newMap;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list of partial sums, where the <i>i</i>th element in the
     * result is the sum of the 0th through <i>i</i>th elements in the
     * specified list.
     * 
     * @param list the list of items from which to compute the partial sums
     * @return the list of partial sums
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public List<Object> partialSum(List<Object> list)
    throws OgnlException
    {
        return partialSum(list,
                (Node) Ognl.parseExpression("#this.l + #this.r"));
    }


    // ----------------------------------------------------------
    /**
     * <p>
     * Returns a list of partial sums, where the <i>i</i>th element in the
     * result is the sum of the 0th through <i>i</i>th elements in the
     * specified list.
     * </p><p>
     * This version of the method is a generalized operation where the
     * specified expression is used to accumulate the "sums", rather than
     * standard addition.
     * </p>
     * 
     * @param list the list of items from which to compute the partial sums
     * @param binaryOp a <b>BinaryOp</b> OGNL lambda expression, where
     *     <tt>#this.l</tt> is the currently accumulated "sum" so far, and
     *     <tt>#this.r</tt> is the next item to be "added". The result of this
     *     expression should be the "sum" of those two values.
     * @return the list of generalized partial sums
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public List<Object> partialSum(List<Object> list, Node binaryOp)
    throws OgnlException
    {
        if (list == null)
        {
            return null;
        }
    
        ArrayList<Object> newList = new ArrayList<Object>();
    
        if (!list.isEmpty())
        {
            HashMap<String, Object> args = new HashMap<String, Object>();
            Object oldThis = replaceThisInContext(args);
    
            Object value = list.get(0);
            newList.add(value);
            
            for (int i = 1; i < list.size(); i++)
            {
                args.put("l", value);
                args.put("r", list.get(i));
    
                value = binaryOp.getValue(context, args);
                newList.add(value);
            }
            
            replaceThisInContext(oldThis);
        }
        
        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list containing <tt>sampleSize</tt> items randomly sampled
     * from the specified list.
     * 
     * @param list the list of items to sample from
     * @param sampleSize the number of items to randomly sample from the list
     * @return a list containing <tt>sampleSize</tt> items randomly sampled
     *     from the list 
     */
    public List<Object> randomSample(List<Object> list, int sampleSize)
    {
        ArrayList<Object> newList = new ArrayList<Object>();
    
        int k = 0;
        
        for (Object item : list)
        {
            if (k < sampleSize)
            {
                newList.add(item);
            }
            else
            {
                int j = (int) (Math.random() * k);
                
                if (j < sampleSize)
                    newList.set(j, list.get(k));
            }
    
            k++;
        }
    
        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list that contains the elements of the specified list after
     * being randomly shuffled.
     * 
     * @param list the list containing the items to shuffle
     * @return a list containing the items in random order
     */
    public List<Object> randomShuffle(List<Object> list)
    {
        ArrayList<Object> newList = new ArrayList<Object>(list);

        for (int i = newList.size() - 1; i >= 1; i--)
        {
            int k = (int) (Math.random() * i);

            Object temp = newList.get(i);
            newList.set(i, newList.get(k));
            newList.set(k, temp);
        }

        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list containing the integers from <tt>start</tt> to
     * <tt>end</tt>, inclusive.
     * 
     * @param start the first integer in the range
     * @param end the last integer in the range
     * @return a list containing the integers from <tt>start</tt> to
     *     <tt>end</tt>, inclusive
     */
    public List<Integer> range(int start, int end)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for (int i = start; i <= end; i++)
        {
            list.add(i);
        }
        
        return list;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list that contains the elements of the specified list in
     * reverse order.
     * 
     * @param list the list containing the items to reverse
     * @return a list containing the items in reverse order
     */
    public List<Object> reverse(List<Object> list)
    {
        ArrayList<Object> newList = new ArrayList<Object>();
    
        for (int i = list.size() - 1; i >= 0; i--)
            newList.add(list.get(i));
    
        return newList;
    }


        // ----------------------------------------------------------
        /**
         * Given a list, this method returns a copy in which consecutive duplicate
         * elements have been removed, leaving only the first copy.
         * 
         * @param list the list containing the items to process
         * @return a new list with consecutive duplicate items removed
         * 
         * @throws OgnlException if a nested OGNL expression has an error
         */
        public List<Object> unique(List<Object> list) throws OgnlException
        {
            return unique(list,
                    (Node) Ognl.parseExpression("#this.l == #this.r"));
        }


    // ----------------------------------------------------------
    /**
     * Given a list, this method returns a copy in which consecutive duplicate
     * elements have been removed, leaving only the first copy. The specified
     * predicate is used to determine if two items are equal for the purposes
     * of this method.
     * 
     * @param list the list containing the items to process
     * @param predicate a <b>BinaryPredicate</b> OGNL lambda expression that
     *     will be used to compare consecutive items in the list.
     *     <tt>#this.l</tt> and <tt>#this.r</tt> are elements in the list. The
     *     result of this expression must be a Boolean indicating whether the
     *     items are considered to be equal.
     * @return a new list with consecutive duplicate items removed
     * 
     * @throws OgnlException if a nested OGNL expression has an error
     */
    public List<Object> unique(List<Object> list, Node predicate)
    throws OgnlException
    {
        ArrayList<Object> newList = new ArrayList<Object>();
    
        Iterator<Object> first = list.iterator();
        Object value = first.next();
        newList.add(value);
    
        HashMap<String, Object> args = new HashMap<String, Object>();
        Object oldThis = replaceThisInContext(args);
    
        while (first.hasNext())
        {
            Object next = first.next();
    
            args.put("l", value);
            args.put("r", next);
    
            boolean equiv = (Boolean) predicate.getValue(context, args);
            if (!equiv)
            {
                value = next;
                newList.add(value);
            }
        }
    
        replaceThisInContext(oldThis);
    
        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Replaces the <tt>#this</tt> reference in the current OGNL context with
     * the specified object, returning its old value.
     * 
     * @param newThis the new <tt>#this</tt> object for the context
     * @return the previous value of <tt>#this</tt>
     */
    private Object replaceThisInContext(Object newThis)
    {
        Object oldThis = context.getCurrentObject();
        context.setCurrentObject(newThis);
        return oldThis;
    }
    

    // ----------------------------------------------------------
    /**
     * Gets the default value for the specified numeric or string type. This
     * method is used to determine the initial value for the default overload
     * of the <tt>accumulate</tt> function.
     */
    private static Object defaultValueForType(Class<?> klazz)
    {
        if (klazz == Byte.class || klazz == Byte.TYPE)
        {
            return Byte.valueOf((byte) 0);
        }
        else if (klazz == Short.class || klazz == Short.TYPE)
        {
            return Short.valueOf((short) 0);
        }
        else if (klazz == Integer.class || klazz == Integer.TYPE)
        {
            return Integer.valueOf(0);
        }
        else if (klazz == Long.class || klazz == Long.TYPE)
        {
            return Long.valueOf(0);
        }
        else if (klazz == Float.class || klazz == Float.TYPE)
        {
            return Float.valueOf(0);
        }
        else if (klazz == BigInteger.class)
        {
            return BigInteger.ZERO;
        }
        else if (klazz == BigDecimal.class)
        {
            return BigDecimal.ZERO;
        }
        else if (klazz == Double.class || klazz == Double.TYPE)
        {
            return Double.valueOf(0);
        }
        else if (klazz == String.class || klazz == Character.class ||
                klazz == Character.TYPE)
        {
            return "";
        }
        else
        {
            return null;
        }
    }


    //~ Static/instance variables .............................................

    /** The OGNL context used during the execution of report expressions. */
    private OgnlContext context;
}
