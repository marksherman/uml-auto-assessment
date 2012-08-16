package acm.program;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;



import junit.framework.Assert;

import abbot.finder.BasicFinder;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import acm.gui.IntField;
import acm.gui.DoubleField;

public class StudentTestableProgram extends TestableProgram
{
    public void assertHasJButtonWithText(String buttonText)
    {
        assertHasGUIObject("No button with text: " + buttonText + " found.", GUIFilter.withText(buttonText).and(GUIFilter.type(JButton.class)));
    }
    
    public void assertHasJLabelWithText(String labelText)
    {
        assertHasGUIObject("No label with text: " + labelText + " found.", GUIFilter.withText(labelText).and(GUIFilter.type(JLabel.class)));
    } 
    
    public void assertHasIntFieldWithText(String fieldText)
    {
        assertHasGUIObject("No IntField with text: " + fieldText + " found.", GUIFilter.withText(fieldText).and(GUIFilter.type(IntField.class)));
    }
    
    public void assertHasDoubleFieldWithText(String fieldText)
    {
        assertHasGUIObject("No IntField with text: " + fieldText + " found.", GUIFilter.withText(fieldText).and(GUIFilter.type(DoubleField.class)));
    }
    
    public void assertHasGUIObject(GUIFilter filter)
    {
        assertHasGUIObject("No object found that matched specified filter", filter);
    }
}
