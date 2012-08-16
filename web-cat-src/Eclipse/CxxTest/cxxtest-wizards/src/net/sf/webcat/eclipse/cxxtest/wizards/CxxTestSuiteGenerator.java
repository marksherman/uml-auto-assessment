/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.webcat.eclipse.cxxtest.wizards;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.IFunctionDeclaration;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.core.model.IWorkingCopy;
import org.eclipse.cdt.internal.ui.wizards.classwizard.NewClassWizardMessages;
import org.eclipse.cdt.internal.ui.wizards.filewizard.NewSourceFileGenerator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * Generates a CxxTest suite header file based on information chose in the
 * wizard by the user.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
@SuppressWarnings("restriction")
public class CxxTestSuiteGenerator
{
	private String suiteName;
	private IPath suitePath;
	private IPath headerUnderTestPath;
	private String superClass;
	private boolean createSetUp;
	private boolean createTearDown;
	private IFunctionDeclaration[] functionStubs;
	private String[] stubNames;

	private IFile suiteFile;
	private ITranslationUnit createdSuite;

	public CxxTestSuiteGenerator(String suiteName, IPath suitePath,
			IPath headerUnderTestPath, String superClass,
			boolean createSetUp, boolean createTearDown,
			IFunctionDeclaration[] methodStubs)
	{
		this.suiteName = suiteName;
		this.suitePath = suitePath;
		this.headerUnderTestPath = headerUnderTestPath;
		this.superClass = superClass;
		this.createSetUp = createSetUp;
		this.createTearDown = createTearDown;
		this.functionStubs = methodStubs;
		
		populateStubNameMap();
	}
	
	private void populateStubNameMap()
	{
		int stubCount = functionStubs.length;

		stubNames = new String[stubCount];
		ICElement[] stubElements = new ICElement[stubCount];
		
		for(int i = 0; i < stubCount; i++)
		{
			stubNames[i] = functionStubs[i].getElementName();
			stubElements[i] = functionStubs[i];
		}

		boolean anotherPass, fixIthName;

		do
		{
			anotherPass = false;
			fixIthName = false;

			for(int i = 0; i < stubCount; i++)
			{
				for(int j = 0; j < stubCount; j++)
				{
					if(i != j && stubNames[i].equals(stubNames[j]))
					{
						// The names of the stubs are equal, so go up a step and
						// try to further qualify them.
						if(!(stubElements[j] instanceof ITranslationUnit))
						{
							stubElements[j] = stubElements[j].getParent();
							
							String jPrefix;
							
							if(!(stubElements[j] instanceof ITranslationUnit))
								jPrefix = stubElements[j].getElementName();
							else
								jPrefix = "_global"; //$NON-NLS-1$
							
							stubNames[j] = jPrefix + "_" + stubNames[j]; //$NON-NLS-1$
							
							anotherPass = true;
							fixIthName = true;
						}
					}
				}
				
				if(fixIthName)
				{
					if(!(stubElements[i] instanceof ITranslationUnit))
					{
						stubElements[i] = stubElements[i].getParent();
		
						String iPrefix;
						if(!(stubElements[i] instanceof ITranslationUnit))
							iPrefix = stubElements[i].getElementName();
						else
							iPrefix = "_global"; //$NON-NLS-1$
		
						stubNames[i] = iPrefix + "_" + stubNames[i]; //$NON-NLS-1$
					}
					
					fixIthName = false;
				}
			}
		} while(anotherPass);
	}

	public void generate(IProgressMonitor monitor) throws CoreException, InterruptedException
	{
        suiteFile = NewSourceFileGenerator.createHeaderFile(
        		suitePath, true, new SubProgressMonitor(monitor, 50));
        
        if(suiteFile != null)
            createdSuite = (ITranslationUnit) CoreModel.getDefault().create(suiteFile);

        // create a working copy with a new owner
        IWorkingCopy suiteWorkingCopy = createdSuite.getWorkingCopy();

        String suiteContent = constructHeaderFileContent(createdSuite,
        		"", //suiteWorkingCopy.getBuffer().getContents(), //$NON-NLS-1$
        		new SubProgressMonitor(monitor, 100));
        suiteWorkingCopy.getBuffer().setContents(suiteContent);

        if (monitor.isCanceled()) {
            throw new InterruptedException();
        }

        suiteWorkingCopy.reconcile();
        suiteWorkingCopy.commit(true, monitor);
        monitor.worked(50);
	}
	
	public IFile getCreatedSuiteFile()
	{
		return suiteFile;
	}

    private String constructHeaderFileContent(ITranslationUnit headerTU,
    		String oldContents, IProgressMonitor monitor)
    {
        monitor.beginTask(
        		NewClassWizardMessages.NewClassCodeGeneration_createType_task_header, 100); //$NON-NLS-1$
        
        if (oldContents != null && oldContents.length() == 0)
            oldContents = null;
        
        StringBuffer text = new StringBuffer();
        
        appendFilePrologue(text);
        appendClassPrologue(text);
        
        if(createSetUp)
        	appendSetUp(text);
        if(createTearDown)
        	appendTearDown(text);

        if(functionStubs != null)
        	appendFunctionStubs(text);

        appendClassEpilogue(text);
        appendFileEpilogue(text);

        String newContents = text.toString();
        monitor.done();
        return newContents;
    }
    
    private void appendFilePrologue(StringBuffer text)
    {
    	String guardSymbol = suiteName.toUpperCase() + "_H_"; //$NON-NLS-1$
    	text.append("#ifndef " + guardSymbol + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
    	text.append("#define " + guardSymbol + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
    	
    	text.append("#include <cxxtest/TestSuite.h>\n\n"); //$NON-NLS-1$
    	
    	if(headerUnderTestPath != null)
    	{
    		IPath headerAbsPath = headerUnderTestPath.makeAbsolute();
    		IPath suiteAbsPath = suitePath.makeAbsolute();
    		int matchingSegs = headerAbsPath.matchingFirstSegments(suiteAbsPath);
    		IPath includePath = headerAbsPath.removeFirstSegments(matchingSegs);

    		text.append("#include \"" + includePath.toOSString() + "\"\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    }

    private void appendClassPrologue(StringBuffer text)
    {
    	text.append("class " + suiteName + " : public " + superClass + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    	text.append("{\n"); //$NON-NLS-1$
    	text.append("public:\n"); //$NON-NLS-1$
    }

    private void appendSetUp(StringBuffer text)
    {
    	text.append("\tvoid setUp()\n"); //$NON-NLS-1$
    	text.append("\t{\n"); //$NON-NLS-1$
    	text.append("\t\t// TODO: Implement setUp() function.\n"); //$NON-NLS-1$
    	text.append("\t}\n\n"); //$NON-NLS-1$
    }

    private void appendTearDown(StringBuffer text)
    {
    	text.append("\tvoid tearDown()\n"); //$NON-NLS-1$
    	text.append("\t{\n"); //$NON-NLS-1$
    	text.append("\t\t// TODO: Implement tearDown() function.\n"); //$NON-NLS-1$
    	text.append("\t}\n\n"); //$NON-NLS-1$
    }

    private void appendFunctionStubs(StringBuffer text)
    {
    	for(int i = 0; i < functionStubs.length; i++)
    	{
    		String name = stubNames[i];

    		name = "test" + Character.toUpperCase(name.charAt(0)) + //$NON-NLS-1$
    			name.substring(1);
    		
        	text.append("\tvoid " + name + "()\n"); //$NON-NLS-1$ //$NON-NLS-2$
        	text.append("\t{\n"); //$NON-NLS-1$
        	text.append("\t\t// TODO: Implement " + name + "() function.\n"); //$NON-NLS-1$ //$NON-NLS-2$
        	text.append("\t}\n\n");    		 //$NON-NLS-1$
    	}
    }

    private void appendClassEpilogue(StringBuffer text)
    {
    	text.append("};\n\n"); //$NON-NLS-1$
    }

    private void appendFileEpilogue(StringBuffer text)
    {
    	String guardSymbol = suiteName.toUpperCase() + "_H_"; //$NON-NLS-1$
    	text.append("#endif /*" + guardSymbol + "*/\n"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
