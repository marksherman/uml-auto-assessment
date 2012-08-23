/*==========================================================================*\
 |  $Id: PassFailBar.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest integration package for Visual
 |	Studio.NET.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using System.Windows.Forms.VisualStyles;

namespace WebCAT.CxxTest.VisualStudio.Controls
{
	// --------------------------------------------------------------------
	/// <summary>
	/// Specifies the mode of the pass/fail bar; that is, whether it displays
	/// a text message or a green/red bar depicting a pass/fail rate.
	/// </summary>
	public enum PassFailBarMode
	{
		/// <summary>
		/// The pass/fail bar will display a text message.
		/// </summary>
		Text,

		/// <summary>
		/// The pass/fail bar will display a green/red division depicting a
		/// pass/fail rate.
		/// </summary>
		Bar
	}


	// --------------------------------------------------------------------
	/// <summary>
	/// The PassFailBar is similar to a progress bar that is partitioned into
	/// two proportional regions -- the "pass" section on the left, and the
	/// "fail" section on the right.
	/// </summary>
	public partial class PassFailBar : UserControl
    {
		//~ Constructor ......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Creates a new instance of the PassFailBar.
		/// </summary>
        public PassFailBar()
        {
            InitializeComponent();
        }


		//~ Properties .......................................................

		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the mode of the bar; that is, whether it displays a
		/// text message or a color-filled bar.
		/// </summary>
		[Category("Appearance"),
		 DefaultValue(PassFailBarMode.Text)]
		public PassFailBarMode Mode
		{
			get
			{
				return mode;
			}
			set
			{
				mode = value;
				Invalidate();
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the minimum value of the bar.
		/// </summary>
		[Category("Behavior"),
		 DefaultValue(0)]
        public int Minimum
        {
            get
            {
                return minimum;
            }
			set
            {
                // Prevent a negative value.
                if (value < 0)
                    minimum = 0;

                // Make sure that the minimum value is never set higher than
				// the maximum value.
                if (value > maximum)
                {
                    minimum = value;
                    minimum = value;
                }

                // Ensure value is still in range
                if (val < minimum)
                    val = minimum;

                // Invalidate the control to get a repaint.
                Invalidate();
            }
        }


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the maximum value of the bar.
		/// </summary>
		[Category("Behavior"),
		 DefaultValue(100)]
		public int Maximum
        {
            get
            {
                return maximum;
            }
            set
            {
                // Make sure that the maximum value is never set lower than the minimum value.
                if (value < minimum)
                    minimum = value;

                maximum = value;

                // Make sure that value is still in range.
                if (val > maximum)
                    val = maximum;

                // Invalidate the control to get a repaint.
                Invalidate();
            }
        }


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the current value of the bar. The region from the
		/// minimum value to the current value will be painted in the "pass"
		/// color; the region from the current value to the maximum value will
		/// be painted in the "fail" color.
		/// </summary>
		[Category("Behavior"),
		 DefaultValue(0)]
		public int Value
        {
            get
            {
                return val;
            }
            set
            {
                // Make sure that the value does not stray outside the valid range.
                if (value < minimum)
                    val = minimum;
                else if (value > maximum)
                    val = maximum;
                else
                    val = value;

				Invalidate();
            }
        }


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the text message to display in the bar when the mode
		/// is set to PassFailBarMode.Text.
		/// </summary>
		[Browsable(true),
		 Category("Appearance"),
		 DefaultValue(null)]
		public override string Text
		{
			get
			{
				return base.Text;
			}
			set
			{
				base.Text = value;
				Invalidate();
			}
		}


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the color that the passing section of the bar will be
		/// painted in.
		/// </summary>
		[Category("Appearance"),
		 DefaultValue(typeof(Color), "LimeGreen")]
		public Color PassColor
        {
            get
            {
                return passColor;
            }
            set
            {
                passColor = value;
                Invalidate();
            }
        }


		// ------------------------------------------------------
		/// <summary>
		/// Gets or sets the color that the failing section of the bar will be
		/// painted in.
		/// </summary>
		[Category("Appearance"),
		 DefaultValue(typeof(Color), "Red")]
		public Color FailColor
		{
			get
			{
				return failColor;
			}
			set
			{
				failColor = value;
				Invalidate();
			}
		}


		//~ Methods ..........................................................

		// ------------------------------------------------------
		protected override void OnResize(EventArgs e)
		{
			Invalidate();
		}


		// ------------------------------------------------------
		protected override void OnPaint(PaintEventArgs e)
		{
			Graphics g = e.Graphics;
			Rectangle contentRect;

			// Draw a three-dimensional border around the control.
			ControlPaint.DrawBorder3D(g, ClientRectangle, Border3DStyle.SunkenOuter);

			contentRect = ClientRectangle;
			contentRect.Inflate(-1, -1);

			if (mode == PassFailBarMode.Text)
			{
				if (Text != null)
				{
					TextRenderer.DrawText(g, Text, Font, contentRect,
						ForeColor, BackColor,
						TextFormatFlags.HorizontalCenter |
						TextFormatFlags.VerticalCenter);
				}
			}
			else
			{
				using (SolidBrush brush = new SolidBrush(failColor))
				{
					g.FillRectangle(brush, contentRect);
				}

				// Calculate area for drawing the progress.
				if (maximum - minimum != 0)
				{
					float percent = (float)(val - minimum) / (float)(maximum - minimum);
					contentRect.Width = (int)((float)contentRect.Width * percent);
				}

				// Draw the progress meter.
				using (SolidBrush brush = new SolidBrush(passColor))
				{
					g.FillRectangle(brush, contentRect);
				}
			}
		}

		
		//~ Instance variables ...............................................

		// The minimum value of the bar.
		private int minimum = 0;

		// The maximum value of the bar.
		private int maximum = 100;

		// The current value of the bar.
		private int val = 0;

		// The color to paint the passing (left) section of the bar.
		private Color passColor = Color.LimeGreen;

		// The color to paint the failing (right) section of the bar.
		private Color failColor = Color.Red;
		private PassFailBarMode mode = PassFailBarMode.Text;
	}
}
