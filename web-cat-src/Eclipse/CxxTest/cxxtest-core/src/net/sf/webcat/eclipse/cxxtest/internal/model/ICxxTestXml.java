/*==========================================================================*\
 |  $Id: ICxxTestXml.java,v 1.1 2009/09/13 12:59:29 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.internal.model;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2009/09/13 12:59:29 $
 */
public interface ICxxTestXml
{
	static final String TAG_TRACE = "trace"; //$NON-NLS-1$
	static final String TAG_WARNING = "warning"; //$NON-NLS-1$
	static final String TAG_FAILED_TEST = "failed-test"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT = "failed-assert"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_EQ = "failed-assert-eq"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_SAME_DATA = "failed-assert-same-data"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_DELTA = "failed-assert-delta"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_NE = "failed-assert-ne"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_LT = "failed-assert-lt"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_LE = "failed-assert-le"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_RELATION = "failed-assert-relation"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_PREDICATE = "failed-assert-predicate"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_THROWS = "failed-assert-throws"; //$NON-NLS-1$
	static final String TAG_FAILED_ASSERT_NOTHROW = "failed-assert-nothrow"; //$NON-NLS-1$
	
	static final String ATTR_LINE = "line"; //$NON-NLS-1$
	static final String ATTR_MESSAGE = "message"; //$NON-NLS-1$
	static final String ATTR_EXPRESSION = "expression"; //$NON-NLS-1$
	static final String ATTR_LHS_DESC = "lhs-desc"; //$NON-NLS-1$
	static final String ATTR_RHS_DESC = "rhs-desc"; //$NON-NLS-1$
	static final String ATTR_LHS_VALUE = "lhs-value"; //$NON-NLS-1$
	static final String ATTR_RHS_VALUE = "rhs-value"; //$NON-NLS-1$
	static final String ATTR_SIZE_DESC = "size-desc"; //$NON-NLS-1$
	static final String ATTR_SIZE_VALUE = "size-value"; //$NON-NLS-1$
	static final String ATTR_DELTA_DESC = "delta-desc"; //$NON-NLS-1$
	static final String ATTR_DELTA_VALUE = "delta-value"; //$NON-NLS-1$
	static final String ATTR_VALUE = "value"; //$NON-NLS-1$
	static final String ATTR_RELATION = "relation"; //$NON-NLS-1$
	static final String ATTR_ARG_DESC = "arg-desc"; //$NON-NLS-1$
	static final String ATTR_ARG_VALUE = "arg-value"; //$NON-NLS-1$
	static final String ATTR_PREDICATE = "predicate"; //$NON-NLS-1$
	static final String ATTR_TYPE = "type"; //$NON-NLS-1$
	static final String ATTR_THREW = "threw"; //$NON-NLS-1$
}
