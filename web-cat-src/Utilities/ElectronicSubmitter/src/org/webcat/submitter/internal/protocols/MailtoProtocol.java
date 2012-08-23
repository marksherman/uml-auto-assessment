/*==========================================================================*\
 |  $Id: MailtoProtocol.java,v 1.2 2010/09/14 18:13:30 aallowat Exp $
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

package org.webcat.submitter.internal.protocols;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.IProtocol;
import org.webcat.submitter.SubmissionManifest;
import org.webcat.submitter.targets.AssignmentTarget;

//--------------------------------------------------------------------------
/**
 * <p>
 * A protocol for the "mailto" URI scheme that supports sending the submitted
 * file as an e-mail attachment.
 * </p><p>
 * This protocol depends on the system properties <tt>mail.smtp.host</tt> and
 * <tt>mail.smtp.from</tt> to determine the SMTP server and return address to
 * use, respectively. For more information about the available properties, see
 * the <a href="http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html">
 * JavaMail API documentation</a>.
 * </p>
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/09/14 18:13:30 $
 */
public class MailtoProtocol implements IProtocol
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see IProtocol#submit(SubmissionManifest, ILongRunningTask)
     */
    public void submit(SubmissionManifest manifest, ILongRunningTask task)
    throws IOException
    {
        // Create the archive in a temp file.

        File zipFile = File.createTempFile("submitter_", ".zip");
        FileOutputStream outStream = new FileOutputStream(zipFile);

        manifest.packageContentsIntoStream(outStream, task, null);

        outStream.close();

        Properties props = new Properties(System.getProperties());

        AssignmentTarget asmt = manifest.getAssignment();

        String authString = asmt.getTransportParameters().get("auth");
        boolean auth = false;

        if (authString != null)
        {
            auth = Boolean.parseBoolean(authString);
        }

        Session session;
        Authenticator authenticator = null;

        if (auth)
        {
            props.put("mail.smtp.auth", "true");
            authenticator = new MailAuthenticator(manifest.getUsername(),
                    manifest.getPassword());
        }

        session = Session.getDefaultInstance(props, authenticator);

        try
        {
            Message message = new MimeMessage(session);

            if (props.containsKey("mail.smtp.from"))
            {
                message.setFrom(new InternetAddress(
                        props.getProperty("mail.smtp.from")));
            }
            else
            {
                // Use the default behavior, which uses the machine's
                // default InternetAddress.

                message.setFrom();
            }

            URI transport = manifest.getResolvedTransport(null);

            String to = manifest.resolveParameters(transport
                    .getSchemeSpecificPart(), null);
            String subject = manifest.resolveParameters(asmt
                    .getTransportParameters().get("subject"), null);

            message.setRecipients(Message.RecipientType.TO, InternetAddress
                    .parse(to));
            message.setSubject(subject);
            message.setSentDate(new Date());

            Multipart multiPart = new MimeMultipart();

            Set<Map.Entry<String, String>> transportParams = asmt
                    .getTransportParameters().entrySet();

            for (Iterator<Map.Entry<String, String>> it = transportParams
                    .iterator(); it.hasNext();)
            {
                Map.Entry<String, String> entry = it.next();
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                String convertedValue = manifest.resolveParameters(
                		paramValue, null);

                if (paramName.startsWith("$file."))
                {
                    MimeBodyPart filePart = new MimeBodyPart();
                    filePart.setFileName(convertedValue);
                    filePart.setDataHandler(new DataHandler(
                            new FileDataSource(zipFile)));
                    multiPart.addBodyPart(filePart);
                }
            }

            message.setContent(multiPart);
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            throw new IOException(e.getMessage());
        }

        zipFile.delete();
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#hasResponse()
     */
    public boolean hasResponse()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#getResponse()
     */
    public String getResponse()
    {
        return null;
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    /**
     * An authenticator subclass for a mail session that uses a username and
     * password provided by the user.
     */
    private static class MailAuthenticator extends Authenticator
    {
        //~ Constructors ......................................................

        // ----------------------------------------------------------
        /**
         * Initializes a new instance of the MailAuthenticator class.
         *
         * @param user the username
         * @param pass the password
         */
        public MailAuthenticator(String user, String pass)
        {
            username = user;
            password = pass;
        }


        //~ Methods ...........................................................

        // ----------------------------------------------------------
        /**
         * Gets a PasswordAuthentication instance corresponding to this
         * authenticator.
         *
         * @return the PasswordAuthentication instance
         */
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }


        //~ Static/instance variables .........................................

        /* The username to authenticate with. */
        private String username;

        /* The password to authenticate with. */
        private String password;
    }
}
