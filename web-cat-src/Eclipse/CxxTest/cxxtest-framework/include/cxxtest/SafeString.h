#ifndef __cxxtest__SafeString_h__
#define __cxxtest__SafeString_h__

//
// A string-like class that is safe for use inside CxxTest and Dereferee
// internals since it uses malloc/realloc/free, rather than new/delete, to
// manage its memory. Functionality is very basic; supports only copying,
// appending, and accessing the base pointer.
//

#include <cstdlib>
#include <cstring>

namespace CxxTest
{

class SafeString
{
public:
    //~ Constructors/Destructor ..............................................
    
    // ----------------------------------------------------------
    /**
     * Constructs an empty string.
     */
    SafeString()
    {
        init();
    }


    // ----------------------------------------------------------
    /**
     * Constructs a new string from the contents of the given string.
     */
    SafeString(const SafeString& src)
    {
        copyFrom(src);
    }


    // ----------------------------------------------------------
    /**
     * Constructs a new string from the contents of the given C-string.
     */
    SafeString(const char* src)
    {
        init();
        
        while (*src)
        {
            append(*src);
            src++;
        }
    }


    // ----------------------------------------------------------
    /**
     * Assigns the contents of the specified string to this string.
     */
    SafeString& operator=(const SafeString& rhs)
    {
        if (this != &rhs)
        {
            free(buffer);
            copyFrom(rhs);
        }
        
        return *this;
    }


    // ----------------------------------------------------------
    /**
     * Assigns the contents of the specified C-string to this string.
     */
    SafeString& operator=(const char* rhs)
    {
        free(buffer);
        copyFrom(rhs);
        
        return *this;
    }


    // ----------------------------------------------------------
    /**
     * Releases any resources used by the string.
     */
    ~SafeString()
    {
        free(buffer);
    }


    //~ Public methods .......................................................

    // ----------------------------------------------------------
    /**
     * Appends a single character to this string and returns the new string.
     */
    SafeString operator+(char ch) const
    {
        SafeString result(*this);
        return result += ch;
    }


    // ----------------------------------------------------------
    /**
     * Appends a C-string to this string and returns the new string.
     */
    SafeString operator+(const char* str) const
    {
        SafeString result(*this);
        return result += str;
    }


    // ----------------------------------------------------------
    /**
     * Appends another SafeString to this string and returns the new string.
     */
    SafeString operator+(const SafeString& str) const
    {
        SafeString result(*this);
        return result += str;
    }


    // ----------------------------------------------------------
    /**
     * Appends a single character to this string.
     */
    SafeString& operator+=(char ch)
    {
        append(ch);
        return *this;
    }


    // ----------------------------------------------------------
    /**
     * Appends the contents of a C-string to this string.
     */
    SafeString& operator+=(const char* rhs)
    {
        while (*rhs)
        {
            append(*rhs);
            rhs++;
        }

        return *this;
    }


    // ----------------------------------------------------------
    /**
     * Appends the contents of another SafeString to this string.
     */
    SafeString& operator+=(const SafeString& rhs)
    {
        const char* str = rhs.c_str();

        while (*str)
        {
            append(*str);
            str++;
        }

        return *this;
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether or not the string is empty.
     */
    bool empty() const
    {
        return size == 0;
    }


    // ----------------------------------------------------------
    /**
     * Gets the pointer to the characters in this string.
     */
    const char* c_str() const
    {
        return buffer;
    }


private:
    //~ Private methods ......................................................

    // ----------------------------------------------------------
    void init()
    {
        capacity = 32;
        buffer = (char*) malloc(capacity);
        size = 0;
    }
    
    
    // ----------------------------------------------------------
    void grow()
    {
        capacity *= 2;
        buffer = (char*) realloc(buffer, capacity);
    }


    // ----------------------------------------------------------
    void append(char ch)
    {
        if(size + 1 == capacity)
        {
            grow();
        }

        buffer[size++] = ch;
        buffer[size] = '\0';
    }


    // ----------------------------------------------------------
    void copyFrom(const SafeString& src)
    {
        capacity = src.capacity;
        size = src.size;
        
        buffer = (char*) malloc(capacity);
        memcpy(buffer, src.buffer, size);
        buffer[size] = '\0';
    }


    //~ Instance variables ...................................................

    char* buffer;
    int size;
    int capacity;
};

} // end namespace CxxTest


#endif // __cxxtest__SafeString_h__
