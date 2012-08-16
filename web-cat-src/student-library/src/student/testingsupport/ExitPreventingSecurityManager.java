/*==========================================================================*\
 |  $Id: ExitPreventingSecurityManager.java,v 1.1 2012/03/05 14:17:44 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2012 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student.testingsupport;

import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;

//-------------------------------------------------------------------------
/**
 *  A custom security manager used by {@link student.TestCase} to detect
 *  and report calls to System.exit() without actually terminating the JVM.
 *  With this security manager in place, any call to System.exit() will be
 *  turned into a {@link ExitCalledException}.
 *  <p>
 *  This security manager is designed to work in conjunction with any other
 *  security manager currently installed.  It chains to an existing security
 *  manager and delegates all checks other than System.exit() checks to
 *  the other manager.  This way it can easily be "added" on top of a
 *  sandboxing security manager without requiring that manager to be
 *  rewritten.
 *  </p>
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2012/03/05 14:17:44 $
 */
public class ExitPreventingSecurityManager
    extends SecurityManager
{
    //~ Fields ................................................................

    private SecurityManager parent;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new manager with the given security manager as its "parent".
     * All checks other than System.exit() checks will be delegated to the
     * parent, if one is provided.
     *
     * @param parent The security manager to chain to (delegate to) for all
     *               other checks besides System.exit() calls.
     */
    public ExitPreventingSecurityManager(SecurityManager parent)
    {
        this.parent = parent;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void checkPermission(Permission perm)
    {
        if (parent != null)
        {
            parent.checkPermission(perm);
        }
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void checkPermission(Permission perm, Object context)
    {
        if (parent != null)
        {
            parent.checkPermission(perm, context);
        }
    }


    // ----------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void checkExit(int status)
    {
        if (parent != null)
        {
            parent.checkExit(status);
        }
        throw new ExitCalledException(status);
    }


    // ----------------------------------------------------------
    /**
     * Install a new instance of this class as the current security manager,
     * taking any existing security manager and using it as the parent to
     * chain to.
     */
    public static void install()
    {
        AccessController.doPrivileged( new PrivilegedAction<Void>()
        {
            public Void run()
            {
                SecurityManager current = System.getSecurityManager();
                if (current == null
                    || !(current instanceof ExitPreventingSecurityManager))
                {
                    System.setSecurityManager(
                        new ExitPreventingSecurityManager(current));
                }
                return null;
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * If the current security manager is an instance of this class,
     * uninstall it and replace it with its parent, if any.
     */
    public static void uninstall()
    {
        AccessController.doPrivileged( new PrivilegedAction<Void>()
        {
            public Void run()
            {
                SecurityManager current = System.getSecurityManager();
                if (current != null
                    && current instanceof ExitPreventingSecurityManager)
                {
                    System.setSecurityManager(
                        ((ExitPreventingSecurityManager)current).parent);
                }
                return null;
            }
        });
    }
}
