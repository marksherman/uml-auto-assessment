package student.web.internal.tests;

import student.web.*;
import java.net.*;
import java.util.*;
import student.web.TurboWebBot;

// -------------------------------------------------------------------------
/**
 *  This subclass of WebBot will collect data from a specified page.
 *
 *  @author  dboynton
 *  @version 2009.02.11
 */

public class SourceForgeScraper
    extends TurboWebBot
{
    //~ Instance/static variables .............................................

    //~ Constructor ...........................................................
    /**
     * Constructor with no page specified.  This will point to a default.
     */
	public SourceForgeScraper() {
		super("http://sourceforge.net/softwaremap/");
	}


    //~ Methods ...............................................................

	/**
	 * This method locates all html code within a specified div tag.
	 *
	 * @param divName The name of the html div tag that we are interested in.
	 * @return All html code between within the specified Div.  Null on error.
	 */
    public String getDivContents(String divName) {
		String pageHTML = getPageContent();
		String searchParam = "<div id=\"" + divName + "\">";
		int startPosition = 0;
		int endPosition = 0;

		// Find the division in question.  If found, set the starting marker
		// at the end of the tag, and the ending marker at the first div close.
		startPosition = pageHTML.indexOf(searchParam, 0);
		if (startPosition != -1) {
			startPosition += searchParam.length();
			endPosition = pageHTML.indexOf("</div>", startPosition);
		}

		// If the requested information was found, return the string.
		// Otherwise, return null.
		if (startPosition != -1 && endPosition != -1) {
			return pageHTML.substring(startPosition, endPosition);
		}
		else {
			return null;
		}
    }


	/**
	 * This method will return all aplications located within a specified
	 * block of HTML.  The block will be identified by its Division id.
	 *
	 * @param type A String identifying the type of application to look for.
	 * @returns An ArrayList containing all of the applications that were found.
	 */
    public ArrayList<String> getApplicationNames(String type) {
		String divContents = "";
		ArrayList<String> recoveredApps = new ArrayList<String>();

		returnToStartOfPage();
		divContents = getDivContents(type);
		jumpToThisHTML(divContents);

		advanceToNextLink();
		while (!isLookingAtEndOfPage()) {
			if (getLinkURI().toString().contains("projects")) {
				recoveredApps.add(getCurrentElementText());
			}
			advanceToNextLink();
		}

		returnToPreviousPage();
		return recoveredApps;
    }


	/**
	 * Prints the most active projects on SourceForge.
	 */
	public void printMostActive() {
		for (String application : getApplicationNames("most_active")) {
			out().println(application);
		}
	}

	/**
	 * Prints the projects that have the most downloads.
	 */
	public void printMostPopular() {
		for (String application : getApplicationNames("most_downloaded")) {
			out().println(application);
		}
	}

}
