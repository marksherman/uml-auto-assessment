#ifndef __CXXTEST__APPENDER_H
#define __CXXTEST__APPENDER_H

#include <cstring>

// Get rid of annoying STDC deprecation warnings in MSVC++.
#ifdef _MSC_VER
#	pragma warning(push)
#	pragma warning(disable: 4996)
#endif

namespace CxxTest
{

class Appender
{
public:
	Appender() { }

	virtual ~Appender() { }

	virtual void append(char ch) = 0;

	virtual void append_str(const char* str)
	{
		while(*str != 0)
			append(*str++);
	}

	virtual void append_int(unsigned int num)
	{
		char buffer[32];
		sprintf(buffer, "%lu", num);
		append_str(buffer);
	}

	virtual void flush() = 0;
};

class FileAppender : public Appender
{
public:
	FileAppender(FILE* f) : file(f) { }

	void append(char ch)
	{
		fputc(ch, file);
	}

	void flush()
	{
		fflush(file);
	}

private:
	FILE* file;
};

class StringAppender : public Appender
{
public:
	StringAppender()
	{
		capacity = 256;
		buffer = (char*)malloc(capacity);
		size = 0;
	}

	~StringAppender() { free(buffer); }

	void append(char ch)
	{
		if(size + 1 == capacity)
			grow();

		buffer[size++] = ch;
		buffer[size] = '\0';
	}

	void flush() { }

	const char* str() { return buffer; }

private:
	void grow()
	{
		capacity *= 2;
		buffer = (char*)realloc(buffer, capacity);
	}

	char* buffer;
	int size;
	int capacity;
};

} // end namespace CxxTest

#ifdef _MSC_VER
#	pragma warning(pop)
#endif

#endif // __CXXTEST__APPENDER_H
