package acm.program;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import abbot.finder.BasicFinder;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.gui.DoubleField;
import acm.gui.IntField;
import acm.program.TestableGraphicsProgram.GraphicFilter;
import junit.framework.Assert;

public class TestableProgram extends Program
{
    
    public Component getGUIObject(GUIFilter filter)
    {   
        Component c = null;
        BasicFinder finder = new BasicFinder();
        try
        {
            return finder.find(filter2matcher(filter));
        }
        catch ( ComponentNotFoundException e )
        {
            return null;
        }
        catch ( MultipleComponentsFoundException e )
        {
            return e.getComponents()[0];
        }
        
    }
    
    public <T extends Component> T getGUIObject(Class<T> type, GUIFilter filter)
    {
        return (T)getGUIObject(filter.and(GUIFilter.type(type)));
    }
    
    public Component getExactlyOneGUIObject(GUIFilter filter)
    {
        IllegalStateException ise;

        Component[] comps = getAllGUIObjects(filter);
        if(comps.length == 0)
        {
            ise = new IllegalStateException("No Components matching filter found.");
            StackTraceElement[] stack = ise.getStackTrace();
            for(StackTraceElement ste : stack)
            {
                if(ste.getClassName().startsWith("org.junit.") || ste.getClassName().startsWith("junit."))
                {
                    Assert.fail(ise.getMessage());
                }
            }
            throw ise;
        }
        else if(comps.length > 1)
        {
            ise = new IllegalStateException("Multiple Components matching filter found.");
            StackTraceElement[] stack = ise.getStackTrace();
            for(StackTraceElement ste : stack)
            {
                if(ste.getClassName().startsWith("org.junit.") || ste.getClassName().startsWith("junit."))
                {
                    Assert.fail(ise.getMessage());
                }
            }
            throw ise;
        }
        return comps[0];
    }
    
    public <T extends Component> T getExactlyOneGUIObject(Class<T> type, GUIFilter filter)
    {
        return (T)getExactlyOneGUIObject(filter.and(GUIFilter.type(type)));
    }
    
    public Component[] getAllGUIObjects(GUIFilter filter)
    {
        Component c = null;
        BasicFinder finder = new BasicFinder();
        
        try
        {
            c = finder.find(filter2matcher(filter));
            Component[] comps = {c};
            return comps;
        }
        catch ( ComponentNotFoundException e )
        {
            return null;
        }
        catch ( MultipleComponentsFoundException e )
        {
            Component[] comps = e.getComponents();
            return comps;
        }  
    }
    
    public <T extends Component> T[] getAllGUIObjects(Class<T> type, GUIFilter filter)
    {
        Component[] comps = getAllGUIObjects(filter.and(GUIFilter.type(type)));

        T[] tobjs = (T[]) Array.newInstance(type, comps.length);
        for (int i = 0; i < comps.length; i++)
        {
            tobjs[i] = (T) comps[i];
        }

        return tobjs;
    }
    
    public boolean hasGUIObject(GUIFilter filter)
    {
        return getGUIObject(filter) != null;
    }
    
    public void assertHasGUIObject(String hint, GUIFilter filter)
    {
        if(!hasGUIObject(filter))
            Assert.fail(hint);
    }
    
    public void assertNoGUIObject(String hint, GUIFilter filter)
    {
        if(hasGUIObject(filter))
            Assert.fail(hint);
    }
    

    
    
    
    private Matcher filter2matcher(final GUIFilter filter)
    {
        return new Matcher()
        {
            public boolean matches( Component comp )
            {
                return filter.test(comp);
            }
            
        };
    }
    
    public static abstract class GUIFilter extends Filter
    {
        public final GUIFilter and(final GUIFilter otherFilter) 
        {
            final GUIFilter self = this;
            GUIFilter gf =  new GUIFilter(){
                public boolean test(Component comp)  
                {
                   return self.test(comp) && otherFilter.test(comp);    
                }
            };
            gf.description = "(" + this.description + " AND " + otherFilter.description + ")";
            return gf;
        }
        
        public final GUIFilter or(final GUIFilter otherFilter)
        {
            final GUIFilter self = this;
            GUIFilter gf = new GUIFilter(){
                public boolean test(Component comp)
                {
                    return self.test(comp) || otherFilter.test(comp);
                }
            };
            gf.description = "(" + this.description + " OR " + otherFilter.description + ")";
            return gf;
        }
        
        public static final GUIFilter not(final GUIFilter otherFilter)
        {
            GUIFilter gf = new GUIFilter(){
                public boolean test(Component comp)
                {
                    return !otherFilter.test(comp);
                }
            };
            gf.description = " NOT " + otherFilter.description;
            return gf;
        }
        
        public static final GUIFilter withText(final String text)
        {
            GUIFilter gf = new GUIFilter()
            {
                public boolean test( Component c )
                {                  
                    Method m = null;
                    try
                    {
                        m = c.getClass().getMethod("getText");
                        return ((String)m.invoke(c)).equals(text);                      
                    }
                    catch ( SecurityException e )
                    {
                        return false;
                    }
                    catch ( NoSuchMethodException e )
                    {
                        return false;
                    }
                    catch ( IllegalArgumentException e )
                    {
                        return false;
                    }
                    catch ( IllegalAccessException e )
                    {
                        return false;
                    }
                    catch ( InvocationTargetException e )
                    {
                        return false;
                    }            
                }   
            };
            gf.description = "with text: \"" + text + "\"";
            return gf;
        }
        public static final GUIFilter type(final Class c)
        {
            GUIFilter gf = new GUIFilter()
            {
                public boolean test(Component comp)
                {
                    return c.isAssignableFrom(comp.getClass());
                }
            };
            gf.description = "class: " + c.getSimpleName();
            return gf;
        }
        
        public static final GUIFilter withName(final String name)
        {
            GUIFilter gf = new GUIFilter()
            {
                public boolean test( Component c )
                {
                    if(c.getName() == null)
                        return false;
                    else
                        return c.getName().equals(name);
                }
            };
            gf.description = "named " + name;
            return gf;
        }
        
        public static final GUIFilter withValue(final String value)
        {
            GUIFilter gf = new GUIFilter()
            {
                public boolean test( Component c )
                {                
                    if(JTextField.class.isAssignableFrom(c.getClass()))
                        return ((JTextField)c).getText().equals(value);    
                    return false;
                }   
            };
            gf.description = "with value: " + value;
            return gf;
        }
        
        public static final GUIFilter hasFocus = new GUIFilter() {
            { this.setDescription("has focus"); }
            public boolean test( Component c )
            {
                return c.isFocusOwner();
            }
            
        };
        
        public static final GUIFilter doesNotHaveFocus = new GUIFilter() {
            { this.setDescription("does not have focus"); }
            public boolean test( Component c )
            {
                return !c.isFocusOwner();
            }    
        };
        
        public static final GUIFilter enabled = new GUIFilter() {
            {this.setDescription("enabled"); }
            public boolean test(Component c)
            {
                return c.isEnabled();
            }
        };
        
        public static final GUIFilter not_enabled = new GUIFilter() {
            { this.setDescription("not enabled"); }
            public boolean test(Component c)
            {
                return !c.isEnabled();
            }
        };
        
        public abstract boolean test(Component c);

    }

}
