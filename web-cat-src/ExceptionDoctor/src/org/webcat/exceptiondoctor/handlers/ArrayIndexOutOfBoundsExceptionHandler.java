package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;

public class ArrayIndexOutOfBoundsExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<ArrayIndexOutOfBoundsException> CLASS_TYPE = ArrayIndexOutOfBoundsException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	private String getArrayName(List<String> variables)
	{
		if (variables.size() == 1)
		{

			return variables.get(0);
		}
		return null;
	}

	public int getValue(String oldMessage)
	{
		return Integer.parseInt(oldMessage);
	}

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String line = super.findLine(exToWrap);
		String oldMessage = exToWrap.getMessage();
		// List<String> variables = getVariables(line, "[");
		// List<String> variables = getArrayVariables(line);
		int intValue = getValue(oldMessage);
		// String arrayIndex = getIndexValue(variables, line);
		// String newMessage = buildErrorMessage(oldMessage, intValue,
		// arrayIndex,
		// variables);
		String error = "";

		error += getArrayNameMessage(line, intValue);
		error += getIndexMessage(intValue);
		//error += getIndexValueMessage(line, intValue);

		return error;

	}

	private String getArrayNameMessage(String line, int value)
	{
		String error = "";
		/*List<String> variables = getArrayNames(line);
		if (variables.size() > 0)
		{
			error += "It seems that the code tried to use an illegal value as an index to an array.  ";
			if (variables.size() == 1)
			{
				error += "The code was trying to access an element at index "
						+ value + " of the array called \"" + variables.get(0)
						+ "\".  ";
			}
			else
			{
				error += "The code was trying to access an element at index "
						+ value + " at one of the arrays in the line: (";
				for (int i = 0; i < variables.size(); i++)
				{
					error += variables.get(i);
					if (i + 1 != variables.size())
					{
						error += ", ";
					}
				}
				error += ").  ";
			}
		}
		else
		{*/
			error += "It seems that the code tried to use an illegal value as an index to an object.  ";
			error += "The code was trying to access an element at index "
					+ value + " of an array (or other object) on that line.  ";
		//}
		return error;
	}

	private String getIndexMessage(int intValue)
	{
		String error = "";
		if (intValue < 0)
		{
			error += "Remember, you cannot have a negative index. Be sure that the index is always positive.  ";
		}
		else if (intValue == 0)
		{
			error += "The array does not have any elements in it yet.  Remember, creating an array does not automatically populate it.  ";
		}
		else
		{
			error += "The size of the array may be less than "
					+ intValue
					+ ".  Keep in mind that if the array size is N, the biggest index you can access is N-1.  ";
		}
		return error;
	}

	/*private String getIndexValueMessage(String line, int intVal)
	{
		String error = "";
		List<String> vars = getArrayVariables(line);
		error += "One of these expressions \"";
		for (int i = 0; i < vars.size(); i++)
		{
			error += vars.get(i);
			if (i + 1 != vars.size())
			{
				error += ", ";
			}
		}
		if(vars.size() == 0)
		{
			error+="COULD NOT FIND EXPRESSIONS";
		}
		error += "\" had the value " + intVal + " when the error occured.  ";

		return error;
	}*/

	/*public String getIndexValue(List<String> variables, String line)
	{
		String index = "";
		if (variables.size() == 1)
		{
			// if the index is a variable in the line of code, we need to
			// explain that
			int leftIndex = line.indexOf('[');
			int rightIndex = line.indexOf(']');
			if (leftIndex != -1 && rightIndex != -1)
			{
				index = line.substring(leftIndex + 1, rightIndex);

			}
		}
		return index;
	}*/

}
