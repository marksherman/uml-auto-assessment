package org.webcat.exceptiondoctor.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class FileNotFoundExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    public enum EType
    {
        NOFILE, PERMIS
    };
    private static final Class<FileNotFoundException> CLASS_TYPE = FileNotFoundException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	private EType getBadFileType(String oldMessage)
	{
		if (oldMessage.contains("No such file or directory"))
		{
			return EType.NOFILE;
		}
		else if (oldMessage.contains("Permission denied"))
		{
			return EType.PERMIS;
		}
		return null;
	}

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		// figure out the name of the file
		String oldMessage = exToWrap.getMessage();
		StringTokenizer tok = new StringTokenizer(oldMessage);
		String file = tok.nextToken();
		String newMessage = "It appears that the code was trying to operate on a file called "
				+ file + ".  ";
		EType eType = getBadFileType(oldMessage);
		if (eType == EType.NOFILE)
		{
			String validPath = testDirectory(exToWrap);
			newMessage += "However, it seems that this file may not exist.  ";
			newMessage += "Check that the filename is spelled correctly.  ";
			if (validPath != null)
			{
				newMessage += "Analysis shows that " + validPath
						+ " is a valid path.  The "
						+ "remainder of the file is invalid.  ";
			}
		}
		else if (eType == EType.PERMIS)
		{
			newMessage += "However, it seems that you may not have permission to read or write it.  ";
			newMessage += "Check the permissions of the file and make sure you can read/write it.  ";
		}
		else
		{
			// not sure what the actual error is
			newMessage += "For some reason, you are not able to use this file.  ";
		}
		return newMessage;
	}

	private String testDirectory(Throwable exToWrap)
	{
		String message = exToWrap.getMessage();
		StringTokenizer tok = new StringTokenizer(message, ":");
		String directory = tok.nextToken();
		directory = directory.substring(0, directory.indexOf(" "));
		String div = "/";
		int indexOfDir = directory.indexOf(div);

		if (indexOfDir == -1)
		{
			div = "\\";
			indexOfDir = directory.indexOf(div);

		}
		if (indexOfDir == -1)
		{
			return null;
		}
		// directory = directory.substring(0, indexOfDir);
		// directoryParts = new StringTokenizer(directory, div);

		String path = directory.substring(0, indexOfDir) + "/";
		File testFile = new File(path);
		int indexOfNextDiv = 1;
		String prevPath = "";
		while (indexOfNextDiv != -1 && testFile.isDirectory())
		{
			String partialDir = directory.substring(indexOfDir + 1);
			indexOfNextDiv = partialDir.indexOf(div);
			indexOfDir = indexOfDir + indexOfNextDiv + 1;
			prevPath = path;
			path = directory.substring(0, indexOfDir) + "/";
			testFile = new File(path);

		}
		if (indexOfNextDiv != -1)
		{
			return prevPath;
		}
		return path;
	}

}
