/*==========================================================================*\
 |  $Id: Types.java,v 1.2 2011/03/07 22:56:53 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package student.testingsupport.reflection.internal;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.ref.SoftReference;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import student.testingsupport.reflection.ReflectionError;
import student.web.internal.MRUMap;

//-------------------------------------------------------------------------
/**
 *  A set of static utility methods to look up {@link Class} objects, where
 *  the results are backed by an internal cache.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/03/07 22:56:53 $
 */
public class Types
{
    //~ Fields ................................................................

    private static MRUMap<ClassLoader, Map<String, PackageContent>>
        classesForPackage =
        new MRUMap<ClassLoader, Map<String, PackageContent>>(10, 0);
    private static MRUMap<ClassLoader, PackageContent> allClasses =
        new MRUMap<ClassLoader, PackageContent>(10, 0);
    private static Set<String> searchLocations = null;
    private static boolean searchDirectoriesOnly = true;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * This class contains only static helper methods, and so it should
     * never be instantiated.
     */
    private Types()
    {
        // never called
    }


    // ----------------------------------------------------------
    // The real initialization is static
    static
    {
        String searchPath = null;

        // TODO: do privileged ...
        searchPath =
            System.getProperty(Types.class.getName() + ".searchPath");

        if (searchPath != null)
        {
            restrictSearchesTo(searchPath);
        }
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Restrict type searches that look for all classes in a package, or
     * all classes visible to a class loader, to the specified list of
     * search locations (the default is the search path in the system
     * property
     * <code>student.testingsupport.reflection.internal.Types.searchPath</code>,
     * or the set of directories visible from the class loader where
     * this class was loaded).
     * This restriction affects all type searches when a fully qualified
     * class name is not provided (e.g., when using
     * {@link #classesInPackage(String, ClassLoader)} or
     * {@link #allClasses(ClassLoader)} or
     * {@link #allClassesWithSimpleName(String, ClassLoader)}.
     * @param searchPath The search path to use.  A classpath-like string
     * denoting the classpath locations where searching should be performed.
     */
    public static void restrictSearchesTo(String searchPath)
    {
        synchronized (allClasses)
        {
            synchronized (classesForPackage)
            {
                allClasses.clear();
                classesForPackage.clear();
                searchDirectoriesOnly = false;
                if (searchPath == null)
                {
                    searchLocations = null;
                }
                else
                {
                    searchLocations = new HashSet<String>();
                    String[] locations = searchPath.split(
                        "[:;]|\\Q" + File.pathSeparator + "\\E");

                    for (String location : locations)
                    {
                        if (!location.isEmpty())
                        {
                            if (location.toLowerCase().endsWith(".jar"))
                            {
                                searchLocations.add(
                                    "jar:file:" + location+ "!/");
                            }
                            else
                            {
                                if (!location.endsWith("/"))
                                {
                                    location = location + "/";
                                }
                                searchLocations.add("file:" + location);
                            }
                        }
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Indicate whether type searches that look for all classes in a package,
     * or all classes visible to a class loader, should look only in
     * directories, or in both directories and jar files.
     * @param choice If true, searches will only examine directories; if
     *               false, searches will also examine jar files.
     */
    public static void searchDirectoriesOnly(boolean choice)
    {
        synchronized (allClasses)
        {
            synchronized (classesForPackage)
            {
                allClasses.clear();
                classesForPackage.clear();
                searchDirectoriesOnly = choice;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Determine whether an entity's modifiers indicate it has "default"
     * visibility (that is, package-level visibility).
     * @param modifiers The modifiers to check.
     * @return True if the modifiers indicate default visibility (that is,
     * they do not include private, protected, or public).
     */
    public static boolean isPackageVisible(int modifiers)
    {
        return (modifiers
            & (Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC))
            == 0;
    }


    // ----------------------------------------------------------
    /**
     * Determine whether a specified class has "default" visibility (that is,
     * package-level visibility).
     * @param clazz The class to check.
     * @return True if the class has default visibility (that is, it is not
     * private, protected, or public).
     */
    public static boolean isPackageVisible(Class<?> clazz)
    {
        return isPackageVisible(clazz.getModifiers());
    }


    // ----------------------------------------------------------
    /**
     * Determine whether a class is declared in a given package.
     * @param clazz The class to check.
     * @param packageName The name of the package to check for.  Null (or the
     *                    empty string) mean the "default" package.
     * @return True if clazz is declared in the given package.
     */
    public static boolean isInPackage(Class<?> clazz, String packageName)
    {
        if (packageName == null  ||  packageName.isEmpty())
        {
            return clazz.getPackage() == null;
        }
        else
        {
            return clazz.getPackage() != null
                && packageName.equals(clazz.getPackage().getName());
        }
    }


    // ----------------------------------------------------------
    /**
     * Determine whether two classes are declared in the same package.
     * @param class1 The first class to check.
     * @param class2 The second class to check.
     * @return True if class1 is declared in the same package as class2.
     */
    public static boolean areInSamePackage(Class<?> class1, Class<?> class2)
    {
        Package pkg = class1.getPackage();
        String pkgName = (pkg == null) ? null : pkg.getName();
        return isInPackage(class2, pkgName);
    }


    // ----------------------------------------------------------
    /**
     * Look up the class for a given name.  The lookup is performed using
     * the thread's context class loader, or if that is unsuccessful, the
     * class loader in which Types was loaded.
     * @param fqcn The fully qualified class name.
     * @return The class with the given name, or null if none can be found.
     */
    public static Class<?> classForName(String fqcn)
    {
        Class<?> result = classForName(
            fqcn, Thread.currentThread().getContextClassLoader());
        if (result == null)
        {
            result = classForName(fqcn, Types.class.getClassLoader());
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up the class for a given name in the given class loader.
     * @param fqcn The fully qualified class name.
     * @param loader The class loader to use.
     * @return The class with the given name, or null if none can be found.
     */
    public static Class<?> classForName(String fqcn, ClassLoader loader)
    {
        if (loader == null)
        {
            // Use the default mechanism
            return classForName(fqcn);
        }

        Class<?> result = null;
        try
        {
            result = loader.loadClass(fqcn);
        }
        catch (NoClassDefFoundError e)
        {
            System.out.println(
                "Unable to load class "
                + fqcn
                + " because of "
                + e.getClass().getName()
                + ": "
                + e.getMessage());
            // let null be returned
        }
        catch (ClassNotFoundException e)
        {
            // ignore this, and let null be returned
        }
        catch (OutOfMemoryError e)
        {
            if (e.getMessage() != null
                && e.getMessage().contains("PermGen"))
            {
                long currentMaxPermSize = maxPermSize();
                System.out.println(
                    "Insufficient PermGen space to load all classes in "
                    + "method\n"
                    + Types.class.getName()
                    + ".allClasses().\nCurrent max PermGen size = "
                    + currentMaxPermSize
                    + ".\n"
                    + "Increase availabe PermGen by adding this command "
                    + "line parameter:\n-XX:MaxPermSize="
                    + recommendedNewMaxSize(currentMaxPermSize)
                    );
            }
            throw e;
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Find all the classes declared in the given package and visible using
     * the given class loader.
     * @param packageName The package name.  Null or an empty string
     *                    represent the "default" package.
     * @param loader The class loader to use.
     * @return All classes declared in the given package, visible in the
     * given class loader.
     */
    public static List<Class<?>> classesInPackage(
        String packageName, ClassLoader loader)
    {
        if (packageName == null)
        {
            packageName = "";
        }
        if (loader == null)
        {
            loader = Thread.currentThread().getContextClassLoader();
        }

        List<Class<?>> result = null;
        synchronized (classesForPackage)
        {
            Map<String, PackageContent> forLoader =
                classesForPackage.get(loader);
            if (forLoader == null)
            {
                forLoader = new HashMap<String, PackageContent>();
                classesForPackage.put(loader, forLoader);
            }

            PackageContent pkg = forLoader.get(packageName);
            if (pkg == null)
            {
                pkg = new PackageContent();
                forLoader.put(packageName, pkg);
                Set<String> names = new TreeSet<String>();
                scanClassLoaderForNames(loader, names, packageName, false);
                pkg.setNames(names);
            }
            result = pkg.classes(loader);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Find all the classes visible using the given class loader.
     * <b>Use with care!</b>
     * Note that this method may require extra PermGen space, since it will
     * force <b>all</b> classes to be loaded simultaneously.  Add
     * <code>-XX:MaxPermSize=128m</code> (or some other size) as a command
     * line argument if you must use this method but it runs out of PermGen
     * space.  If PermGen space is exhausted, a diagnostic message
     * suggesting a larger size is necessary.
     * @param loader The class loader to use.
     * @return All classes visible in the given class loader.
     */
    public static List<Class<?>> allClasses(ClassLoader loader)
    {
        if (loader == null)
        {
            loader = Thread.currentThread().getContextClassLoader();
        }
        List<Class<?>> result = null;
        synchronized (allClasses)
        {
            PackageContent all = allClasses.get(loader);
            if (all == null)
            {
                all = new PackageContent();
                allClasses.put(loader, all);
                Set<String> names = new TreeSet<String>();
                scanClassLoaderForNames(loader, names, "", true);
                all.setNames(names);
            }
            result = all.classes(loader);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Find all the classes with the given simple name that are visible using
     * the given class loader, in any package.
     * @param simpleName The simple name (without any package prefix)
     * to look for.
     * @param loader The class loader to use.
     * @return All classes visible in the given class loader.
     */
    public static List<Class<?>> allClassesWithSimpleName(
        String simpleName, ClassLoader loader)
    {
        if (loader == null)
        {
            loader = Thread.currentThread().getContextClassLoader();
        }
        List<Class<?>> result = null;
        synchronized (allClasses)
        {
            PackageContent all = allClasses.get(loader);
            if (all == null)
            {
                all = new PackageContent();
                allClasses.put(loader, all);
                Set<String> names = new TreeSet<String>();
                scanClassLoaderForNames(loader, names, "", true);
                all.setNames(names);
            }
            result = all.classesWithSimpleName(simpleName, loader);
        }
        return result;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    private static void scanClassLoaderForNames(
        ClassLoader loader,
        Set<String> accumulator,
        String packageName,
        boolean recurse)
    {
        String packageDir = "";
        if (packageName == null)
        {
            packageName = "";
        }
        else
        {
            packageDir = packageName.replace('.', '/');
        }

        Set<URL> locations = new HashSet<URL>();
        try
        {
            Enumeration<URL> urls = loader.getResources(packageDir);
            while (urls.hasMoreElements())
            {
                locations.add(urls.nextElement());
            }
        }
        catch (IOException e)
        {
            throw new ReflectionError(
                "IO exception scanning class loader for classes: "
                + e.getMessage());
        }
        if (packageName.isEmpty())
        {
            // Then it is the default package, and we need to grab the
            // jars too.
            try
            {
                Enumeration<URL> urls = loader.getResources("META-INF");
                while (urls.hasMoreElements())
                {
                    URL location = urls.nextElement();
                    if ("jar".equals(location.getProtocol()))
                    {
                        String locString = location.toString();
                        location = new URL(locString.substring(
                            0, locString.length() - "META-INF".length()));
                        locations.add(location);
                    }
                }
            }
            catch (IOException e)
            {
                throw new ReflectionError(
                    "IO exception scanning class loader for classes: "
                    + e.getMessage());
            }
        }

//        System.out.println(
//            "locations for package " + packageName + " = " + locations);

        for (URL location : locations)
        {
            scanClasspathLocationForNames(
                location, accumulator, packageName, recurse);
        }
    }


    // ----------------------------------------------------------
    private static void scanClasspathLocationForNames(
        URL location,
        Set<String> accumulator,
        String packageName,
        boolean recurse)
    {
        // Check searchLocations to make sure this location is included
        if (searchLocations != null)
        {
            String urlBase = location.toString();
            if (packageName != null && !packageName.isEmpty())
            {
                urlBase = urlBase.substring(
                    0, urlBase.length() - packageName.length());
            }
            if (!searchLocations.contains(urlBase))
            {
//                System.out.println("[skipping url: " + location + "]");
                return;
            }
        }

//        System.out.println("[scanning url: " + location + "]");

        // Check to see if it is a plain directory in the file system
        File directory = new File(location.getFile());
        if (directory.exists())
        {
            if (directory.isDirectory())
            {
                scanClasspathLocationForNames(
                    directory, accumulator, packageName, recurse);
            }
        }
        else if (!searchDirectoriesOnly)
        {
            // If it isn't a directory, then it is a jar file URL
            try
            {
                JarURLConnection conn =
                    (JarURLConnection)location.openConnection();
                JarFile jarFile = conn.getJarFile();
                scanClasspathLocationForNames(
                    jarFile, accumulator, packageName, recurse);
                jarFile.close();
            }
            catch (IOException e)
            {
                throw new ReflectionError(
                    "IO exception scanning "
                    + location
                    + " for classes: "
                    + e.getMessage());
            }
        }
    }


    // ----------------------------------------------------------
    private static void scanClasspathLocationForNames(
        File directory,
        Set<String> accumulator,
        String packageName,
        boolean recurse)
    {
//        System.out.println("[scanning directory: " + directory + "]");
        for (String fileName : directory.list())
        {
//            System.out.println("    [scanning file: " + fileName + "]");
            // we are only interested in .class files
            if (fileName.endsWith(".class"))
            {
                // removes the .class extension
                String className = fileName.substring(
                    0, fileName.length() - ".class".length());

                // Ignore inner classes
                if (className.indexOf('$') > 0)
                {
                    continue;
                }

                // Compute fully qualified class name
                String fqcn = packageName;
                if (fqcn == null || fqcn.isEmpty())
                {
                    fqcn = className;
                }
                else
                {
                    fqcn += '.' + className;
                }

//                System.out.println("    [adding class: " + fqcn + "]");
                accumulator.add(fqcn);
            }
            else if (recurse)
            {
                File subPackage = new File(directory, fileName);
                if (subPackage.exists() && subPackage.isDirectory())
                {
                    String subPackageName = packageName;
                    if (subPackageName == null || subPackageName.isEmpty())
                    {
                        subPackageName = fileName;
                    }
                    else
                    {
                        subPackageName += '.' + fileName;
                    }
                    scanClasspathLocationForNames(
                        subPackage, accumulator, subPackageName, recurse);
                }
            }
        }
    }


    // ----------------------------------------------------------
    private static void scanClasspathLocationForNames(
        JarFile location,
        Set<String> accumulator,
        String packageName,
        boolean recurse)
    {
//        System.out.println("[scanning jar file: " + location + "]");
        String packagePrefix = packageName;
        if (packagePrefix == null)
        {
            packagePrefix = "";
        }
        else if (!packagePrefix.isEmpty())
        {
            packagePrefix = packagePrefix.replace('.', '/');
            if (!packagePrefix.endsWith("/"))
            {
                packagePrefix += '/';
            }
        }
        Enumeration<JarEntry> entries = location.entries();
        while (entries.hasMoreElements())
        {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
//            System.out.println("    [scanning entry: " + name + "]");
            if (name.startsWith(packagePrefix)
                && name.endsWith(".class"))
            {
                name = name.substring(0, name.length() - ".class".length());
//                System.out.println("      [scanning class: " + name + "]");
                if (recurse
                    || name.lastIndexOf('/') == packagePrefix.length() - 1)
                {
                    // Translate to fully qualified class name
                    name = name.replace('/', '.');
                    // Ignore inner classes
                    int pos = name.lastIndexOf('.');
                    if (pos < 0)
                    {
                        pos = 0;
                    }
                    if (name.lastIndexOf('$') < pos)
                    {
//                        System.out.println(
//                            "            [adding: " + name + "]");
                        accumulator.add(name);
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    private static class PackageContent
    {
        private List<String> classNames = new ArrayList<String>();
        private SoftReference<List<Class<?>>> classes = null;

        public void setNames(Collection<String> newNames)
        {
            classNames.clear();
            classNames.addAll(newNames);
            classes = null;
        }

        @SuppressWarnings("unused")
        public List<String> classNames()
        {
            return classNames;
        }

        public List<Class<?>> classesWithSimpleName(
            String name, ClassLoader loader)
        {
            List<Class<?>> result = new ArrayList<Class<?>>();
            for (String className : classNames)
            {
                int pos = className.lastIndexOf('.') + 1;
                if (name.equals(className.substring(pos)))
                {
                    Class<?> c = classForName(className, loader);
                    if (c != null)
                    {
                        result.add(c);
                    }
                }
            }
            return result;
        }

        public List<Class<?>> classes(ClassLoader loader)
        {
            List<Class<?>> result = null;
            if (classes != null)
            {
                result = classes.get();
            }
            if (result == null)
            {
                result = new ArrayList<Class<?>>(classNames.size());
                for (String name : classNames)
                {
                    Class<?> c = classForName(name, loader);
                    if (c != null)
                    {
                        result.add(c);
                    }
                }
                classes = new SoftReference<List<Class<?>>>(result);
            }
            return result;
        }
    }

    private static long maxPermSize()
    {
        for (MemoryPoolMXBean mx : ManagementFactory.getMemoryPoolMXBeans())
        {
            if (mx.getName() != null && mx.getName().contains("Perm Gen"))
            {
              return mx.getUsage().getMax();
            }
        }
        return 0;
    }

    private static String recommendedNewMaxSize(long oldMaxSize)
    {
        int recommended = 64;
        long rawRecommended = 64 * 1024 * 1024;
        while (rawRecommended <= oldMaxSize)
        {
            recommended *= 2;
            rawRecommended = recommended * 1024 * 1024;
        }
        return recommended + "m";
    }
}
