Web-CAT Electronic Submitter
============================

Synopsis
--------

The Web-CAT Electronic Submitter is a self-contained library that can be
integrated into Java desktop and web applications to give students the ability
to electronically submit projects to online services, including grading
systems. This library is a standalone version of the functionality that we
already provide via Eclipse and Visual Studio extensions.

Web-CAT, the Web-based Center for Automated Testing, is one online grading
system that can be used with this library, and it has support for automatically
generating the XML-formatted submission target definitions that this library
uses to determine which assignments are available. We emphasize, however, that
this library does not require that you be using Web-CAT; it supports submitting
files to any system that uses a supported protocol.

The key features of this library include:

  * Built-in support for http, https, ftp, mailto, and file protocols
  * Built-in support for ZIP and JAR archiving
  * Additional protocols/packagers can be added at runtime when instantiating
    the submitter
  * Hierarchical tree of submission targets configured by an easy-to-understand
    XML file
  * File-pattern-based inclusion and exclusion of files in submissions
  * File-pattern-based specification of required files in submissions


Requirements
------------

If you are making submissions via http, https, ftp, or file protocols, the
only requirement is that you are running Java J2SE 5.0 or higher.

To make submissions via the mailto protocol, or to build the library from
source, you will also need the JavaBeans Activation Framework (activation.jar)
and JavaMail (mail.jar) on your classpath.


Using the Submitter in Your Java Application
--------------------------------------------

The Web-CAT Electronic Submitter can be integrated into your Java application
with a few easy steps:

1. Collect the files that you want to submit. To submit files directly from
   the file system, create one or more SubmittableFile objects that represent
   the files or folders that you want to submit.

        java.io.File folder = new java.io.File("path/to/file");
        SubmittableFile itemToSubmit = new SubmittableFile(folder);

   The submitter can submit any object that implements the `ISubmittableItem`
   interface. `SubmittableFile` will likely be sufficient for most purposes,
   but users can implement the `ISubmittableItem` interface to provide access
   to other resources. (For example, the Eclipse plug-ins that use this
   library implement `ISubmittableItem` to represent the structure of an
   Eclipse project instead of accessing the file system directly.)


2. Instantiate the submitter and load the submission targets.

        Submitter submitter = new Submitter();
        URL targetsURL = new URL("http://yoursite.com/targets.xml");
        submitter.readSubmissionTargets(targetsURL);

   Submission targets can be read from a URL or an input stream.


3. Provide the user with a way of selecting the `AssignmentTarget` that they
   wish to submit to. This can be provided through a tree-based user
   interface that starts at the root of the submission target tree and
   retrieves each node's children by calling
   `SubmissionTarget.getLogicalChildren()`.

        RootTarget root = submitter.getRoot();
        AssignmentTarget assignment = /* user-selected target */;


4. Construct a `SubmissionManifest` that specifies the items to submit, the
   assignment to submit to, and the user's username and password.

        SubmissionManifest manifest = new SubmissionManifest();
        manifest.setSubmittableItems(itemToSubmit);
        manifest.setAssignment(assignment);
        manifest.setUsername("username");
        manifest.setPassword("password");


5. Call the submitter's `submit()` method.

        submitter.submit(manifest);


6. Check whether the submission generated a response and display it to the
   user. The response is determined by the protocol used to submit the
   assignment; some protocols, such as `file:`, do not generate a response.
   Others, like `http:` and `https:`, return the HTTP response from the
   transport URL.

        if (submitter.hasResponse())
        {
            String response = submitter.getResponse();
            /* display response to the user */
        }


Command-Line Usage
------------------

While this library is primarily intended to be called from other Java code, a
simple main class is provided so that it can be used from the command line
(for testing purposes, for example).

        java -jar webcat-submitter.jar [OPTION...] FILES...

Options:

        -t, --targets=URL            uses submission targets from URL
        -l, --list                   lists the submission targets loaded with -t
        -u, --user=NAME              user name for remote submission target
        -p, --pass=PASS              password for remote submission target
        -a, --assignment=PATH        the path to the assignment target


The `-t/--targets` option specifies a URL from which the submission targets
should be loaded. This URL can use any protocol that the Java runtime can open
a connection to and read from; file and http are the two most likely choices.
This URL points to an XML file describing which assignments can be submitted
to. The format of this URL can be found at:

<http://web-cat.cs.vt.edu/WCWiki/EclipsePlugins/SubmissionPlugin/AssignmentDefinitions>

If the `-l/--list` option is provided, the submitter will list the paths of
all of the submission targets that are read from the target URL. The targets
that represent assignments (as opposed to targets that act as grouping
containers) are marked with an asterisk (*).

The `-u/--user` and `-p/-pass` options specify the username and password to
use for authentication to any of the `<transport>` URIs specified in the
submission target definitions. These values are NOT used to authenticate to
read the target URL specified by `-t/--target`.

The `-a/--assignment` option specifies the slash-delimited path to the
assignment that you wish to submit in the submission target definitions. The
path is determined by the names of the assignment groups and assignments
leading down to the target assignment. For example, in the following tree

    <submission-targets>
        <assignment-group name="Group A">
            <assignment-group name="Group B">
                <assignment name="Assignment">

the path to the assignment is `"Group A/Group B/Assignment"`.

After the options, you must specify one or more files or folders that will be
packaged up and submitted. If any of the items specified are folders, their
contents (and the contents of any of their child folders, recursively) will be
included in the package.
