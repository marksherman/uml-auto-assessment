/*
 * ==========================================================================*\
 * | $Id: FlexibleXStream.java,v 1.8 2011/06/21 14:57:28 mwoodsvt Exp $
 * |*-------------------------------------------------------------------------*|
 * | Copyright (C) 2007-2010 Virginia Tech | | This file is part of the
 * Student-Library. | | The Student-Library is free software; you can
 * redistribute it and/or | modify it under the terms of the GNU Lesser General
 * Public License as | published by the Free Software Foundation; either version
 * 3 of the | License, or (at your option) any later version. | | The
 * Student-Library is distributed in the hope that it will be useful, | but
 * WITHOUT ANY WARRANTY; without even the implied warranty of | MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the | GNU Lesser General Public
 * License for more details. | | You should have received a copy of the GNU
 * Lesser General Public License | along with the Student-Library; if not, see
 * <http://www.gnu.org/licenses/>.
 * \*==========================================================================
 */

package student.web.internal;

import student.web.internal.converters.CachedClassConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.io.xml.XppDriver;


// -------------------------------------------------------------------------
/**
 * A custom subclass of {@link XStream} that allows for flexible/forgiving
 * reconstruction of objects. Missing classes don't cause errors--the
 * corresponding objects are reconstituted as null. Missing fields do not cause
 * errors, either--they are simply ignored.
 *
 * @author Stephen Edwards
 * @author Last changed by $Author: mwoodsvt $
 * @version $Revision: 1.8 $, $Date: 2011/06/21 14:57:28 $
 */
public class FlexibleXStream
    extends XStream
{
    private static class CachedClassPriorityConverterLookup
        extends DefaultConverterLookup
    {
        private CachedClassConverter ccc;
    
        public CachedClassPriorityConverterLookup( CachedClassConverter ccc )
        {
            this.ccc = ccc;
        }
        public Converter lookupConverterForType(Class type)
        {
            if(ccc.canConvert( type ))
                return ccc;
            return super.lookupConverterForType( type );
        }
    }
    // ----------------------------------------------------------
    public FlexibleXStream(ClassLoader loader, CachedClassConverter ccc)
    {
        super(
            LocalityService.getSupportStrategy().getReflectionProvider(), new XppDriver(),   
            loader, null, new CachedClassPriorityConverterLookup(ccc), 
            null);
        super.mapper = new FlexibleMapper(getMapper());
    }
}
