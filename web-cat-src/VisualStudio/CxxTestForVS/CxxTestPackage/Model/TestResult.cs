/*==========================================================================*\
 |  $Id: TestResult.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using System.Text;

namespace WebCAT.CxxTest.VisualStudio.Model
{
    public class TestResult : IComparable
    {

        private string testName;

        private string testClass;

        private string testFileName;

        private int testLineNumber;

        private bool passedFlag;

        private string runDescription;

        public TestResult(string name, string tClass, string filename, int line, bool passed, string description)
        {
            testName = name;
            testClass = tClass;
            testFileName = filename;
            testLineNumber = line;
            passedFlag = passed;
            runDescription = description;
        }

        public string Name
        {
            get
            {
                return testName;
            }
            set
            {
                testName = value;
            }
        }


        public string Class
        {
            get
            {
                return testClass;
            }
            set
            {
                testClass = value;
            }
        }

        public string Filename
        {
            get
            {
                return testFileName;
            }
            set
            {
                testFileName = value;
            }
        }

        public int Line
        {
            get
            {
                return testLineNumber;
            }
            set
            {
                testLineNumber = value;
            }
        }

        public string Description
        {
            get
            {
                return runDescription;
            }
            set
            {
                runDescription = value;
            }
        }

        public bool Passed
        {
            get
            {
                return passedFlag;
            }
            set
            {
                passedFlag = value;
            }
        }

        public int CompareTo(object obj)
        {
            TestResult result = (TestResult)obj;
            return this.Name.CompareTo(result.Name);
        }

        public override string  ToString()
        {
            if (Passed)
                return "PASSED: " + Class + "::" + Name;
            return "FAILED: " + Class + "::" + Name + "\r\n" + Filename + ":" + Line + " " +
                Description;
        }   
        

    }
}
