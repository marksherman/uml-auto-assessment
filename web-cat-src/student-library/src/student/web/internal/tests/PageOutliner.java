package student.web.internal.tests;

import student.web.WebBotTask;
import student.web.WebBot;
import student.*;
import student.web.*;

// -------------------------------------------------------------------------
/**
 *  This robot is designed to search for heading tags.
 *
 *  @author  Catalina Astengo (castengo), CJ Norris (cjn)
 *  @version (2009.01.30)
 */
public class PageOutliner
    implements WebBotTask
{
    //~ Instance/static variables .............................................

    // Change the type of this variable if you write your own custom
    // robot class.
    WebBot bot = null;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * This task outputs header files.
     */
    public void task()
    {

        bot = new WebBot(
                    "http://courses.cs.vt.edu/~cs1705/practice/cs-quotes.html");

        //move to the next heading and show the text
        bot.advanceToNextHeading();

        bot.echoCurrentElementText();

        bot.out().println();

        bot.advanceToNextHeading();

        bot.echoCurrentElementText();

        bot.out().println();

        bot.advanceToNextHeading();

        bot.echoCurrentElementText();

        bot.out().println();

        bot.advanceToNextHeading();

        bot.echoCurrentElementText();

        bot.out().println();

        bot.advanceToNextHeading();

        bot.echoCurrentElementText();

        bot.out().println();
    }


    // ----------------------------------------------------------
    /**
     * This method provides access to the robot that carries out this
     * task.  Change the return type of this method if you write
     * your own custom robot class.
     * @return The robot used by this task.
     */
    public WebBot getRobot()
    {
        return bot;
    }


    // ----------------------------------------------------------
    public static void main(String[] args)
    {
        PageOutliner outliner = new PageOutliner();
        outliner.task();
    }
}
