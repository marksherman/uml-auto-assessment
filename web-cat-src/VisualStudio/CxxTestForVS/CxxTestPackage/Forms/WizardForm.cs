/*==========================================================================*\
 |  $Id: WizardForm.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace WebCAT.CxxTest.VisualStudio.Forms
{
	// --------------------------------------------------------------------
	/// <summary>
	/// A form that supports the addition of one or more wizard pages and
	/// provides navigation between them.
	/// </summary>
	public partial class WizardForm : Form
	{
		//~ Constructor ......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a new wizard form.
		/// </summary>
		public WizardForm()
		{
			InitializeComponent();

			pages = new List<WizardPageControl>();

			currentPageIndex = 0;
		}


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the bold banner title at the top of the wizard.
		/// </summary>
		public string BannerTitle
		{
			get
			{
				return titleLabel.Text;
			}
			set
			{
				titleLabel.Text = value;
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the descriptive banner message at the top of the
		/// wizard.
		/// </summary>
		public string BannerMessage
		{
			get
			{
				return messageLabel.Text;
			}
			set
			{
				messageLabel.Text = value;
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		/// <summary>
		/// Adds a wizard page to the wizard.
		/// </summary>
		/// <param name="page">
		/// The page to be added to the wizard.
		/// </param>
		public void AddPage(WizardPageControl page)
		{
			Size size = pageContainer.Size;
			Size pageSize = page.Size;

			pages.Add(page);
			page.Wizard = this;

			if (pageSize.Width > size.Width)
				size.Width = pageSize.Width;

			if (pageSize.Height > size.Height)
				size.Height = pageSize.Height;

			this.Size = new Size(size.Width + 406 - 384,
				size.Height + 334 - 168);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when the form is loaded.
		/// </summary>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		protected override void OnLoad(EventArgs e)
		{
			base.OnLoad(e);

			RefreshButtons();
			LoadCurrentPage();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when the Finish button is clicked. Subclasses should
		/// override this to provide any logic necessary upon completion
		/// of the wizard.
		/// </summary>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		protected virtual void OnFinish(EventArgs e)
		{
		}


		// ------------------------------------------------------
		/// <summary>
		/// Removes the current page from the wizard controls hierarchy.
		/// </summary>
		private void RemoveCurrentPage()
		{
			if(pages.Count != 0)
				pageContainer.Controls.Remove(pages[currentPageIndex]);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Loads the current page into the wizard controls hierarchy.
		/// </summary>
		private void LoadCurrentPage()
		{
			if (pages.Count != 0)
				pageContainer.Controls.Add(pages[currentPageIndex]);
		}


		// ------------------------------------------------------
		/// <summary>
		/// Asks the current page for the states of the wizard buttons and
		/// updates their enablements appropriately.
		/// </summary>
		public void RefreshButtons()
		{
			if (pages.Count == 0)
				return;

			WizardPageControl currentPage = pages[currentPageIndex];

			if (currentPageIndex == 0)
				backButton.Enabled = false;
			else
				backButton.Enabled = currentPage.IsBackEnabled;

			if (currentPageIndex == pages.Count - 1)
				nextButton.Enabled = false;
			else
				nextButton.Enabled = currentPage.IsNextEnabled;

			finishButton.Enabled = currentPage.IsFinishEnabled;
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when the Back button is clicked.
		/// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		private void backButton_Click(object sender, EventArgs e)
		{
			RemoveCurrentPage();

			WizardPageControl currentPage = pages[currentPageIndex];
			currentPageIndex--;
			WizardPageControl previousPage = pages[currentPageIndex];

			currentPage.OnGoingToPreviousPage(previousPage);
			previousPage.OnComingFromNextPage(currentPage);

			LoadCurrentPage();
			RefreshButtons();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when the Next button is clicked.
		/// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		private void nextButton_Click(object sender, EventArgs e)
		{
			RemoveCurrentPage();

			WizardPageControl currentPage = pages[currentPageIndex];
			currentPageIndex++;
			WizardPageControl nextPage = pages[currentPageIndex];

			currentPage.OnGoingToNextPage(nextPage);
			nextPage.OnComingFromPreviousPage(currentPage);

			LoadCurrentPage();
			RefreshButtons();
		}


		// ------------------------------------------------------
		/// <summary>
		/// Called when the Finish button is clicked.
		/// </summary>
		/// <param name="sender">
		/// The sender of the event.
		/// </param>
		/// <param name="e">
		/// An <code>EventArgs</code> object.
		/// </param>
		private void finishButton_Click(object sender, EventArgs e)
		{
			OnFinish(e);
		}


		//~ Instance variables ...............................................

		// The list of pages in the wizard.
		private List<WizardPageControl> pages;

		// The index of the current page.
		private int currentPageIndex;
	}
}