package org.webcat.exceptiondoctor.handlers;

import java.io.File;
import java.lang.reflect.Constructor;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.xml.sax.SAXParseException;

public class SAXParseExceptionHandler
    extends AbstractHandler
    implements ExceptionHandlerInterface
{
    private static final Class<SAXParseException> CLASS_TYPE =
        SAXParseException.class;

    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		if(exToWrap.getMessage() == null)
			return null;
		SAXParseException exception = (SAXParseException) exToWrap;
		String message = exception.getMessage();
		if (exception.getMessage().contains(
				"XML document structures must start and end"))
		{
			String filePath = exception.getSystemId();
			int index = filePath.lastIndexOf(File.separator) + 1;
			message = "When the xml, or document structure, was read in "
					+ filePath.substring(index)
					+ ", Java found that one "
					+ "of your tags does not have a matching closing tag. "
					+ " Don't forget that every tag must have a matching "
					+ "closing tag.  This might also be thrown due to a "
					+ "simple typo.\nFor Example:\n<body>\n"
					+ "<\\body>\n This is not correct.";
		}
		// TODO: This should use an inherited method. It is done this way
		// because having no source code short circuits eDoc. This logic needs
		// to be changed
		return message;

	}

	private Throwable customRewireException(SAXParseException exToWrap,
			String newMessage, Class<?> exceptionType, StackTraceElement ste)
	{
		Throwable newException = constructNewException(newMessage,
				exceptionType, exToWrap);
		if (newException == null)
		{
			return null;
		}
		// StackTraceElement[] elements = { ste };
		// newException.setStackTrace(elements);
		//
		// newException.initCause(exToWrap);
		newException.setStackTrace(exToWrap.getStackTrace());
		return newException;
	}

	private Throwable constructNewException(String newMessage,
			Class<?> exceptionType, SAXParseException t)
	{
		Class<?>[] args = { String.class, String.class, String.class,
				int.class, int.class };
		Constructor<?> classConstructor;
		Throwable newException;

		try
		{
			classConstructor = exceptionType.getConstructor(args);
			newException = (Throwable) classConstructor.newInstance(newMessage,
					t.getPublicId(), t.getSystemId(), t.getLineNumber(), t
							.getColumnNumber());
		}
		catch (Throwable e)
		{
			return null;
		}
		return newException;
	}

}
