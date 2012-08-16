/*==========================================================================*\
 |  $Id: CodeElementVisitor.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using Microsoft.VisualStudio.VCCodeModel;
using EnvDTE;

namespace WebCAT.CxxTest.VisualStudio.Utility
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Implements the "visitor" pattern for code model elements. This class
	/// should be subclassed and the Visit() method overridden to provide
	/// logic that will be invoked on each element in the code model that is
	/// encountered.
	/// </summary>
	internal abstract class CodeElementVisitor
	{
		// ------------------------------------------------------
		/// <summary>
		/// Called when a code element in the model is encountered. Must be
		/// implemented.
		/// </summary>
		/// <param name="element">
		/// The element that was encountered.
		/// </param>
		/// <returns>
		/// True if the children of this element should be visited; otherwise,
		/// false.
		/// </returns>
		public abstract bool Visit(CodeElement element);


		// ------------------------------------------------------
		/// <summary>
		/// Called when a project item is encountered.
		/// </summary>
		/// <param name="projectItem">
		/// The project item that was encountered.
		/// </param>
		/// <returns>
		/// True if the children of this element should be visited; otherwise,
		/// false.
		/// </returns>
		public virtual bool Visit(ProjectItem projectItem)
		{
			return true;
		}


		// ------------------------------------------------------
		public void Process(Project project)
		{
			foreach (ProjectItem item in project.ProjectItems)
				Process(item);
		}


		// ------------------------------------------------------
		public void Process(ProjectItem projectItem)
		{
			if (Visit(projectItem))
			{
				try
				{
					if (projectItem.FileCodeModel != null)
						Process(projectItem.FileCodeModel);

					foreach (ProjectItem child in projectItem.ProjectItems)
						Process(child);
				}
				catch (Exception) { }
			}
		}


		// ------------------------------------------------------
		public void Process(CodeModel codeModel)
		{
			Process(codeModel.CodeElements);
		}


		// ------------------------------------------------------
		public void Process(FileCodeModel codeModel)
		{
			Process(codeModel.CodeElements);
		}


		// ------------------------------------------------------
		public void Process(CodeElement element)
		{
			bool visitChildren = Visit(element);

			if (visitChildren)
				Process(element.Children);
		}


		// ------------------------------------------------------
		public void Process(CodeElements elements)
		{
			foreach(CodeElement element in elements)
			{
				Process(element);
			}
		}
	}
}
