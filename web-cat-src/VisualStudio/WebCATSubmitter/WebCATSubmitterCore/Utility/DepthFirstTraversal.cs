/*==========================================================================*\
 |  $Id: DepthFirstTraversal.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT Electronic Submission engine for the
 |	.NET framework.
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

using System;
using System.Collections.Generic;
using System.Text;

namespace WebCAT.Submitter.Utility
{
	//  -----------------------------------------------------------------------
	/// <summary>
	/// Represents a method used by DepthFirstTraversal to access the children
	/// of an item in the traversal.
	/// </summary>
	/// <typeparam name="T">
	/// The type of the elements in the tree that is being traversed.
	/// </typeparam>
	/// <param name="element">
	/// The element whose children should be returned.
	/// </param>
	/// <returns>
	/// An enumerable list of children of the specified element.
	/// </returns>
	public delegate IEnumerable<T> DepthFirstChildAccessor<T>(T element);


	//  -----------------------------------------------------------------------
	/// <summary>
	/// A generalized depth-first traversal enumerator. Initialized with one or
	/// more root elements and a method that defines how to access their
	/// children, this allows a user to process every element in the tree with
	/// a simple foreach statement.
	/// </summary>
	/// <typeparam name="T">
	/// The type of the elements in the tree that is being traversed.
	/// </typeparam>
	public class DepthFirstTraversal<T> : IEnumerable<T>
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new depth first traversal enumerator with the specified
		/// root element and child accessor.
		/// </summary>
		/// <param name="root">
		/// The root element in the tree to be traversed.
		/// </param>
		/// <param name="children">
		/// The method that defines how to access the children of an element
		/// in the tree.
		/// </param>
		public DepthFirstTraversal(
			T root, DepthFirstChildAccessor<T> children) :
			this(new T[] { root }, children)
		{
		}


		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new depth first traversal enumerator with the specified
		/// root elements (a forest) and child accessor.
		/// </summary>
		/// <param name="roots">
		/// The root elements of the trees in the forest to be traversed.
		/// </param>
		/// <param name="children">
		/// The method that defines how to access the children of an element
		/// in the tree.
		/// </param>
		public DepthFirstTraversal(
			IEnumerable<T> roots, DepthFirstChildAccessor<T> children)
		{
			this.roots = roots;
			this.children = children;
		}

		
		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns an enumerator that iterates over the elements in this tree,
		/// in depth-first order.
		/// </summary>
		/// <returns>
		/// An enumerator that iterates over the elements in this tree, in
		/// depth-first order.
		/// </returns>
		public IEnumerator<T> GetEnumerator()
		{
			return RecursiveYielder(roots);
		}

		
		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns an enumerator that iterates over the elements in this tree,
		/// in depth-first order.
		/// </summary>
		/// <returns>
		/// An enumerator that iterates over the elements in this tree, in
		/// depth-first order.
		/// </returns>
		System.Collections.IEnumerator
			System.Collections.IEnumerable.GetEnumerator()
		{
			return (System.Collections.IEnumerator)
				((IEnumerable<T>)GetEnumerator());
		}

		
		//  -------------------------------------------------------------------
		/// <summary>
		/// Perfoms the recursive logic of the depth-first traversal.
		/// </summary>
		/// <remarks>
		/// This method should be optimized at some point to use an explicit
		/// stack instead of recursively creating new enumerators at each
		/// level of the traversal.
		/// </remarks>
		/// <returns>
		/// An enumerator that iterates over the elements in this tree, in
		/// depth-first order.
		/// </returns>
		private IEnumerator<T> RecursiveYielder(IEnumerable<T> level)
		{
			foreach (T element in level)
			{
				yield return element;

				foreach (T child in new DepthFirstTraversal<T>(
					children(element), children))
				{
					yield return child;
				}
			}
		}


		// ==== Fields ========================================================

		// The root elements of the forest being traversed.
		private IEnumerable<T> roots;

		// The method that defines how to access the children of an element in
		// the tree.
		private DepthFirstChildAccessor<T> children;
	}
}
