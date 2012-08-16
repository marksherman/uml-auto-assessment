Download the BIRT runtime from its source on the web
(http://eclipse.org/birt/) and move the ReportEngine folder
into this directory.

Once this is done, the only components required to remain
inside ReportEngine are configuration and plugins.  The
contents of lib should be moved into the project's Libraries
folder.

Lastly, build the net.sf.webcat.oda.core plug-in from the
ODACorePlugin project and copy the corresponding JAR file
into ReportEngine/plugins.
