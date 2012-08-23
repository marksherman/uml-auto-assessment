/*==========================================================================*\
 |  $Id: WizardPageControl.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest integration package for Visual
 |	Studio.NET.
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
using System.Windows.Forms;

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	// --------------------------------------------------------------------
	/// <summary>
	/// The base class for pages in a <code>WizardForm</code>.
	/// </summary>
	public class WizardPageControl : UserControl
	{
		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether the Back button should be enabled
		/// on this page.
		/// </summary>
		public virtual bool IsBackEnabled
		{
			get
			{
				return true;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether the Next button should be enabled
		/// on this page.
		/// </summary>
		public virtual bool IsNextEnabled
		{
			get
			{
				return true;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether the Finish button should be
		/// enabled on this page.
		/// </summary>
		public virtual bool IsFinishEnabled
		{
			get
			{
				return true;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the <code>WizardForm</code> that owns this page.
		/// </summary>
		protected internal WizardForm Wizard
		{
			get
			{
				return wizard;
			}
			set
			{
				wizard = value;
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Called when this page is being left for the next one.
		/// </summary>
		/// <param name="nextPage">
		/// The next page that will be loaded.
		/// </param>
		public virtual void OnGoingToNextPage(WizardPageControl nextPage)
		{
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when this page is being left for the previous one.
		/// </summary>
		/// <param name="previousPage">
		/// The previous page that will be loaded.
		/// </param>
		public virtual void OnGoingToPreviousPage(
			WizardPageControl previousPage)
		{
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when this page is about to be loaded, having arrived from
		/// the previous one.
		/// </summary>
		/// <param name="previousPage">
		/// The previous page that was left.
		/// </param>
		public virtual void OnComingFromPreviousPage(
			WizardPageControl previousPage)
		{
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when this page is about to be loaded, having arrived from
		/// the next one.
		/// </summary>
		/// <param name="nextPage">
		/// The next page that was left.
		/// </param>
		public virtual void OnComingFromNextPage(WizardPageControl nextPage)
		{
		}


		//~ Instance variables ...............................................

		// The wizard that this page is currently in.
		private WizardForm wizard;
	}
}
