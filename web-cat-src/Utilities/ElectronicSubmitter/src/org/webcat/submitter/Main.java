/*==========================================================================*\
 |  $Id: Main.java,v 1.3 2010/08/31 16:06:43 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.webcat.submitter.targets.AssignmentTarget;
import org.webcat.submitter.targets.SubmissionTarget;

//--------------------------------------------------------------------------
/**
 * <p>
 * A simple main class that allows the submitter to be used from the command
 * line, if desired.
 * </p>
 * <h2>Usage</h2>
 * <pre>
 * Usage: org.webcat.submitter.Main [OPTION...] FILES...
 *
 * Options:
 *
 *   -t, --targets=URL            uses submission targets from URL
 *   -l, --list                   lists the submission targets loaded with -t
 *   -u, --user=NAME              user name for remote submission target
 *   -p, --pass=PASS              password for remote submission target
 *   -a, --assignment=PATH        the path to the assignment target
 * </pre>
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/08/31 16:06:43 $
 */
public class Main
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * The main entry point for the application.
     *
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        Main main = new Main();
        main.run(new ArrayList<String>(Arrays.asList(args)));
    }


    // ----------------------------------------------------------
    /**
     * The main logic for the application.
     *
     * @param args the command line arguments
     * @throws IOException
     */
    public void run(List<String> args) throws IOException
    {
        submittableFiles = new ArrayList<SubmittableFile>();

        while (!args.isEmpty())
        {
            String arg = args.remove(0);

            if ("-t".equals(arg))
            {
                targetsURL = new URL(nextArg(args));
            }
            else if (arg.startsWith("--targets"))
            {
                targetsURL = new URL(stripValue(arg));
            }
            else if ("-l".equals(arg) || "--list".equals(arg))
            {
                listTargetsOnly = true;
            }
            else if ("-u".equals(arg))
            {
                username = nextArg(args);
            }
            else if ("--user".equals(arg))
            {
                username = stripValue(arg);
            }
            else if ("-p".equals(arg))
            {
                password = nextArg(args);
            }
            else if ("--pass".equals(arg))
            {
                password = stripValue(arg);
            }
            else if ("-a".equals(arg))
            {
                assignmentPath = nextArg(args);
            }
            else if ("--assignment".equals(arg))
            {
                assignmentPath = stripValue(arg);
            }
            else
            {
                File file = new File(arg);
                SubmittableFile sf = new SubmittableFile(file);
                submittableFiles.add(sf);

                while (!args.isEmpty())
                {
                    arg = args.remove(0);
                    file = new File(arg);
                    sf = new SubmittableFile(file);
                    submittableFiles.add(sf);
                }
            }
        }

        if (!listTargetsOnly && submittableFiles.size() == 0)
        {
            printUsageAndExit();
        }

        performSubmission();
    }


    // ----------------------------------------------------------
    /**
     * Performs the submission.
     */
    private void performSubmission()
    {
        ISubmittableItem[] items =
            new ISubmittableItem[submittableFiles.size()];
        submittableFiles.toArray(items);

        Submitter submitter = new Submitter();

        try
        {
            submitter.readSubmissionTargets(targetsURL);
        }
        catch (TargetParseException e)
        {
            printTargetParseErrors(e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (listTargetsOnly)
        {
            try
            {
                listTargets("", submitter.getRoot());
            }
            catch (SubmissionTargetException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            SubmissionTarget target = null;

            try
            {
                target = submitter.getTarget(assignmentPath);
            }
            catch (TargetParseException e)
            {
                printTargetParseErrors(e);
            }
            catch (SubmissionTargetException e)
            {
                e.printStackTrace();
            }

            if (target == null)
            {
                System.out.println("There was no submission target with the " +
                        "name: \"" + assignmentPath + "\".");
                System.exit(1);
            }

            SubmissionManifest manifest = new SubmissionManifest();
            manifest.setUsername(username);
            manifest.setPassword(password);
            manifest.setSubmittableItems(items);
            manifest.setAssignment((AssignmentTarget) target);

            try
            {
                submitter.submit(manifest);

                if (submitter.hasResponse())
                {
                    System.out.println("The submission was successful, and " +
                            "generated the following response:");
                    System.out.println(submitter.getResponse());
                }
                else
                {
                    System.out.println("The submission was successful.");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Prints the errors contained inside a {@link TargetParseException} and
     * exits the program.
     *
     * @param e the exception to print
     */
    private void printTargetParseErrors(TargetParseException e)
    {
        System.out.println("The following errors occurred when parsing the " +
                "submission targets:");

        for (TargetParseError error : e.getErrors())
        {
            System.out.println(error.toString());
        }

        System.exit(1);
    }

    // ----------------------------------------------------------
    /**
     * Recursive helper method to list the available submission targets to the
     * console.
     *
     * @param path the path so far to the specified target
     * @param target the target that should be listed
     */
    private void listTargets(String path, SubmissionTarget target)
    throws SubmissionTargetException
    {
        for (SubmissionTarget child : target.getLogicalChildren())
        {
            String childPath = path + child.getName();
            System.out.println(childPath);
            listTargets(childPath + "/", child);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieves the next argument passed on the command line, or exits and
     * prints the usage message if there isn't one when it is expected.
     *
     * @param args the remaining command line arguments
     * @return the next command line argument
     */
    private String nextArg(List<String> args)
    {
        if (args.isEmpty())
        {
            printUsageAndExit();
            return null; // never reached
        }
        else
        {
            return args.remove(0);
        }
    }


    // ----------------------------------------------------------
    /**
     * Strips the value after the equal sign off a long-form command line
     * option.
     *
     * @param arg the argument string
     * @return the value of the option
     */
    private String stripValue(String arg)
    {
        int index = arg.indexOf('=');

        if (index == -1)
        {
            printUsageAndExit();
            return null; // never reached
        }
        else
        {
            return arg.substring(index + 1);
        }
    }


    // ----------------------------------------------------------
    /**
     * Prints the usage of this application and exits.
     */
    private void printUsageAndExit()
    {
        System.out.println("Usage: org.webcat.submitter.Main "
                + "[OPTION...] FILES...");

        System.out.println("Options:");

        System.out.println(
                "  -t, --targets=URL            "
                + "uses submission targets from URL");

        System.out.println(
                "  -l, --list                   "
                + "lists the submission targets loaded with -t");

        System.out.println(
                "  -u, --user=NAME              "
                + "user name for remote submission target");

        System.out.println(
                "  -p, --pass=PASS              "
                + "password for remote submission target");

        System.out.println(
                "  -a, --assignment=PATH        "
                + "the path to the assignment target");

        System.out.println();

        System.exit(1);
    }


    //~ Static/instance variables .............................................

    private URL targetsURL;
    private boolean listTargetsOnly;
    private String username;
    private String password;
    private String assignmentPath;
    private List<SubmittableFile> submittableFiles;
}
