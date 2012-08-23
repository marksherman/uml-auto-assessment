package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class IndexOutOfBoundsExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<IndexOutOfBoundsException> CLASS_TYPE = IndexOutOfBoundsException.class;
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
		// try to figure out what the index was... the message is of the form
		// "Index: 1, Size: 1"
		StringTokenizer tok = new StringTokenizer(exToWrap.getMessage(), ": ");
		// first we get "Index"
		tok.nextToken();
		// now the actual index... need to remove the comma at the end
		String index = tok.nextToken();
		index = index.substring(0, index.length() - 1);
		// now we get "Size"
		tok.nextToken();
		// finally, the actual size
		String size = tok.nextToken();

		String newMessage = "It appears that the code was trying to access an "
		    + "element at index " + index + ".  ";

		// show an error message if it's a negative index
		if (Integer.parseInt(index) < 0)
		{
			newMessage += "Remember, you cannot have a negative index for "
			    + "accessing an object.";
		}
		else
		{
			int biggest = Integer.parseInt(size) - 1;
			if (biggest >= 0)
			{
				newMessage += "However, the object only has a size of "
						+ size
						+ ", so the biggest index you can have is "
						+ biggest
						+ " (remember, the maximum index can only be one "
						+ "less than the size).";
			}
			else
			{
				newMessage += "However, the object only has a size of zero, "
				    + "so it doesn't have any elements in it yet.";
			}
		}
		return newMessage;
	}
}
