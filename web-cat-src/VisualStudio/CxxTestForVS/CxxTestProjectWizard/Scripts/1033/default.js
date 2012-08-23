/*==========================================================================*\
 |  $Id: default.js,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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

function OnFinish(selProj, selObj)
{
	try
	{
		var strProjectPath = wizard.FindSymbol('PROJECT_PATH');
		var strProjectName = wizard.FindSymbol('PROJECT_NAME');

		selProj = CreateCustomProject(strProjectName, strProjectPath);
		AddConfig(selProj, strProjectName);
		AddFilters(selProj);

		var InfFile = CreateCustomInfFile();
		AddFilesToCustomProj(selProj, strProjectName, strProjectPath, InfFile);
		InfFile.Delete();

		selProj.Object.Save();
	}
	catch(e)
	{
		if (e.description.length != 0)
			SetErrorInfo(e);
		return e.number
	}
}

function CreateCustomProject(strProjectName, strProjectPath)
{
	try
	{
		var strProjTemplatePath = wizard.FindSymbol('PROJECT_TEMPLATE_PATH');
		var strProjTemplate = '';
		strProjTemplate = strProjTemplatePath + '\\default.vcproj';

		var Solution = dte.Solution;
		var strSolutionName = "";
		if (wizard.FindSymbol("CLOSE_SOLUTION"))
		{
			Solution.Close();
			strSolutionName = wizard.FindSymbol("VS_SOLUTION_NAME");
			if (strSolutionName.length)
			{
				var strSolutionPath = strProjectPath.substr(0, strProjectPath.length - strProjectName.length);
				Solution.Create(strSolutionPath, strSolutionName);
			}
		}

		var strProjectNameWithExt = '';
		strProjectNameWithExt = strProjectName + '.vcproj';

		var oTarget = wizard.FindSymbol("TARGET");
		var prj;
		if (wizard.FindSymbol("WIZARD_TYPE") == vsWizardAddSubProject)  // vsWizardAddSubProject
		{
			var prjItem = oTarget.AddFromTemplate(strProjTemplate, strProjectNameWithExt);
			prj = prjItem.SubProject;
		}
		else
		{
			prj = oTarget.AddFromTemplate(strProjTemplate, strProjectPath, strProjectNameWithExt);
		}
		return prj;
	}
	catch(e)
	{
		throw e;
	}
}

function AddFilters(proj)
{
	try
	{
	    var group;

		// Add the folders to your project
		group = proj.Object.AddFilter('Header Files');
		group.Filter = 'h;hpp;hxx;hm;inl;inc;xsd';
		group.UniqueIdentifier = '{93995380-89BD-4b04-88EB-625FBE52EBFB}';

		group = proj.Object.AddFilter('Source Files');
		group.Filter = 'cpp;c;cc;cxx;def;odl;idl;hpj;bat;asm;asmx';
		group.UniqueIdentifier = '{4FC737F1-C7A5-4376-A066-2A32D752A2FF}';

		proj.Object.AddFilter('Test Suites');
	}
	catch(e)
	{
		throw e;
	}
}

function AddConfig(proj, strProjectName)
{
	try
	{
	    //
	    // Debug configuration options
	    //
		var config = proj.Object.Configurations('Debug');
		config.IntermediateDirectory = '$(ConfigurationName)';
		config.OutputDirectory = '$(ConfigurationName)';

		var CLTool = config.Tools('VCCLCompilerTool');
		CLTool.DebugInformationFormat = debugOption.debugEnabled;
		CLTool.Optimization = optimizeOption.optimizeDisabled;
		CLTool.PreprocessorDefinitions = 'DEBUG;_DEBUG';
		CLTool.ExceptionHandling = cppExceptionHandling.cppExceptionHandlingYesWithSEH;
		CLTool.RuntimeLibrary = runtimeLibraryOption.rtMultiThreadedDebugDLL;
		CLTool.BufferSecurityCheck = false;

		var LinkTool = config.Tools('VCLinkerTool');
		LinkTool.GenerateDebugInformation = true;
		LinkTool.AdditionalDependencies = 'CxxTestDerefereeSupportMTDDLL.lib';

        //
        // Release configuation options
        //
		config = proj.Object.Configurations('Release');
		config.IntermediateDirectory = '$(ConfigurationName)';
		config.OutputDirectory = '$(ConfigurationName)';

		var CLTool = config.Tools('VCCLCompilerTool');
		CLTool.PreprocessorDefinitions = 'NDEBUG';
		CLTool.ExceptionHandling = cppExceptionHandling.cppExceptionHandlingYesWithSEH;
		CLTool.RuntimeLibrary = runtimeLibraryOption.rtMultiThreadedDLL;

		var LinkTool = config.Tools('VCLinkerTool');
		LinkTool.AdditionalDependencies = 'CxxTestDerefereeSupportMTDLL.lib';
	}
	catch(e)
	{
		throw e;
	}
}

function DelFile(fso, strWizTempFile)
{
	try
	{
		if (fso.FileExists(strWizTempFile))
		{
			var tmpFile = fso.GetFile(strWizTempFile);
			tmpFile.Delete();
		}
	}
	catch(e)
	{
		throw e;
	}
}

function CreateCustomInfFile()
{
	try
	{
		var fso, TemplatesFolder, TemplateFiles, strTemplate;
		fso = new ActiveXObject('Scripting.FileSystemObject');

		var TemporaryFolder = 2;
		var tfolder = fso.GetSpecialFolder(TemporaryFolder);
		var strTempFolder = tfolder.Drive + '\\' + tfolder.Name;

		var strWizTempFile = strTempFolder + "\\" + fso.GetTempName();

		var strTemplatePath = wizard.FindSymbol('TEMPLATES_PATH');
		var strInfFile = strTemplatePath + '\\Templates.inf';
		wizard.RenderTemplate(strInfFile, strWizTempFile);

		var WizTempFile = fso.GetFile(strWizTempFile);
		return WizTempFile;
	}
	catch(e)
	{
		throw e;
	}
}

function GetTargetName(strName, strProjectName)
{
	try
	{
		// TODO: set the name of the rendered file based on the template filename
		// var strTarget = strName;

        // Example:
		// if (strName == 'readme.txt')
		//	strTarget = 'ReadMe.txt';

		return strTarget; 
	}
	catch(e)
	{
		throw e;
	}
}

function AddFilesToCustomProj(proj, strProjectName, strProjectPath, InfFile)
{
	try
	{
		var projItems = proj.ProjectItems

		var strTemplatePath = wizard.FindSymbol('TEMPLATES_PATH');

		var strTpl = '';
		var strName = '';

		var strTextStream = InfFile.OpenAsTextStream(1, -2);
		while (!strTextStream.AtEndOfStream)
		{
			strTpl = strTextStream.ReadLine();
			if (strTpl != '')
			{
				strName = strTpl;
				var strTarget = GetTargetName(strName, strProjectName);
				var strTemplate = strTemplatePath + '\\' + strTpl;
				var strFile = strProjectPath + '\\' + strTarget;

				var bCopyOnly = false;  //"true" will only copy the file from strTemplate to strTarget without rendering/adding to the project
				var strExt = strName.substr(strName.lastIndexOf("."));
				if(strExt==".bmp" || strExt==".ico" || strExt==".gif" || strExt==".rtf" || strExt==".css")
					bCopyOnly = true;
				wizard.RenderTemplate(strTemplate, strFile, bCopyOnly);
				proj.Object.AddFile(strFile);
			}
		}
		strTextStream.Close();
	}
	catch(e)
	{
		throw e;
	}
}
