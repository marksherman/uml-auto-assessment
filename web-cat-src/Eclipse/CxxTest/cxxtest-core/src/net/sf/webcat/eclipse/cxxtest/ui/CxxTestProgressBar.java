/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A progress bar with a red/green indication for success or failure.
 * 
 * Influenced greatly by the same JUnit class.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestProgressBar extends Canvas
{
	private static final int DEFAULT_WIDTH = 160;
	private static final int DEFAULT_HEIGHT = 18;

	private int currentTickCount = 0;
	private int maxTickCount = 0;	
	private int colorBarWidth = 0;
	private Color okColor;
	private Color failureColor;
	private boolean error;
	
	public CxxTestProgressBar(Composite parent)
	{
		super(parent, SWT.NONE);
		
		addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e)
			{
				colorBarWidth = scale(currentTickCount);
				redraw();
			}
		});	

		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e)
			{
				paint(e);
			}
		});

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e)
			{
				failureColor.dispose();
				okColor.dispose();
			}
		});

		Display display = parent.getDisplay();
		failureColor = new Color(display, 159, 63, 63);
		okColor = new Color(display, 95, 191, 95);
	}

	public void setMaximum(int max)
	{
		maxTickCount= max;
	}
		
	public void reset()
	{
		error = false;
		currentTickCount = 0;
		colorBarWidth = 0;
		maxTickCount = 0;
		redraw();
	}
	
	private void paintStep(int startX, int endX)
	{
		GC gc = new GC(this);	
		setStatusColor(gc);
		Rectangle rect = getClientArea();
		startX = Math.max(1, startX);
		gc.fillRectangle(startX, 1, endX - startX, rect.height - 2);
		gc.dispose();		
	}

	private void setStatusColor(GC gc)
	{
		if(error)
			gc.setBackground(failureColor);
		else
			gc.setBackground(okColor);
	}

	private int scale(int value)
	{
		if(maxTickCount > 0)
		{
			Rectangle r = getClientArea();
			if(r.width != 0)
				return Math.max(0, value * (r.width - 2) / maxTickCount);
		}

		return value; 
	}
	
	private void drawBevelRect(GC gc, int x, int y, int w, int h,
			Color topleft, Color bottomright)
	{
		gc.setForeground(topleft);
		gc.drawLine(x, y, x + w - 1, y);
		gc.drawLine(x, y, x, y + h - 1);
		
		gc.setForeground(bottomright);
		gc.drawLine(x + w, y, x + w, y + h);
		gc.drawLine(x, y + h, x + w, y + h);
	}
	
	private void paint(PaintEvent event)
	{
		GC gc = event.gc;
		Display disp = getDisplay();
			
		Rectangle rect = getClientArea();
		gc.fillRectangle(rect);

		drawBevelRect(gc, rect.x, rect.y, rect.width - 1, rect.height - 1,
			disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
			disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		
		setStatusColor(gc);
		colorBarWidth = Math.min(rect.width - 2, colorBarWidth);
		gc.fillRectangle(1, 1, colorBarWidth, rect.height - 2);
	}	
	
	public Point computeSize(int wHint, int hHint, boolean changed)
	{
		checkWidget();

		Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		if(wHint != SWT.DEFAULT) size.x = wHint;
		if(hHint != SWT.DEFAULT) size.y = hHint;

		return size;
	}
	
	public void step(int failures)
	{
		currentTickCount += failures;
		int x = colorBarWidth;

		colorBarWidth = scale(currentTickCount);

		if(!error && failures > 0)
		{
			error = true;
			x = 1;
		}

		if(currentTickCount == maxTickCount)
			colorBarWidth = getClientArea().width - 1;

		if(!error && currentTickCount == 0)
			colorBarWidth = getClientArea().width - 1;

		paintStep(x, colorBarWidth);
	}

	public void refresh(boolean hasErrors)
	{
		error = hasErrors;
		redraw();
	}	
}
