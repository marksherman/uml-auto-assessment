//
//	This file is part of the Web-CAT Electronic Submission Package for Visual
//	Studio.NET.
//
//	Web-CAT is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	Web-CAT is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with Web-CAT; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration.Install;
using System.Diagnostics;
using Microsoft.Win32;
using System.Collections;

namespace WebCAT.Submitter.Installer
{
	/// <summary>
	/// A custom installer action that runs Visual Studio with the /setup
	/// command line option in order to refresh the command information from
	/// the newly installed package.
	/// </summary>
	[RunInstaller(true)]
	public partial class DevenvSetup : System.Configuration.Install.Installer
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new instance of the DevenvSetup class.
		/// </summary>
		public DevenvSetup()
		{
			InitializeComponent();
		}


		//  -------------------------------------------------------------------
		/// <summary>
		/// Performs the installation for this custom action.
		/// </summary>
		/// <param name="stateSaver">
		/// A dictionary that saves information for installing, committing, or
		/// rolling back the installation.
		/// </param>
		public override void Install(IDictionary stateSaver)
		{
			base.Install(stateSaver);

			RegistryKey setupKey = Registry.LocalMachine.OpenSubKey(
				@"SOFTWARE\Microsoft\VisualStudio\8.0\Setup\VS");

			// If we don't find 8.0 (2005), try 9.0 (2008), even though we
			// don't officially support it yet.

			if (setupKey == null)
			{
				setupKey = Registry.LocalMachine.OpenSubKey(
					@"SOFTWARE\Microsoft\VisualStudio\9.0\Setup\VS");
			}

			if (setupKey != null)
			{
				string devenv =
					setupKey.GetValue("EnvironmentPath").ToString();

				if (!string.IsNullOrEmpty(devenv))
				{
					Process.Start(devenv, "/setup").WaitForExit();
				}

				setupKey.Close();
			}
		}
	}
}