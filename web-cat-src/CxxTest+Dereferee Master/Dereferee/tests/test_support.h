/*
 *	This file is part of Dereferee, the diagnostic checked pointer library.
 *
 *	Dereferee is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Dereferee is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Dereferee; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

#ifndef TEST_SUPPORT_H
#define TEST_SUPPORT_H

// ===========================================================================
/**
 * Support classes used by various test cases.
 */
class Base
{
protected:
	int _tag;

public:
	Base(int t = 0) : _tag(t) { }
	virtual ~Base() { }
	virtual int tag() const { return _tag; }
};


class Derived : public Base
{
private:
	int _dummy;

public:
	Derived(int t = 0) : Base(t), _dummy(1) { }
	virtual int tag() const { return _tag * _tag; }
};


class NoDefaultCtor
{
private:
	int _value;

public:
	NoDefaultCtor(int t, int u) : _value(t + u) { }
	NoDefaultCtor(const NoDefaultCtor& rhs) { _value = rhs._value; }
	
	int value() const { return _value; }
};


class AbstractBase
{
public:
	virtual ~AbstractBase() { }
	
	virtual void method() = 0;
};


class AbstractBaseSubclass : public AbstractBase
{
public:
	virtual void method() { }	
};

#endif // TEST_SUPPORT_H
