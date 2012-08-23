#ifndef __cxxtest__SafeArray_h__
#define __cxxtest__SafeArray_h__

//
// A dynamic array similar to the STL vector, but with only basic
// functionality. This class is safe for use inside CxxTest and Dereferee
// internals since it uses malloc/realloc/free, rather than new/delete, to
// manage its memory. Note that because of this, this class should only be
// used to hold PODs, not objects that require special copy semantics.
//

#include <cstdlib>
#include <cstring>

namespace CxxTest
{

template <typename T>
class SafeArray
{
public:
    //~ Constructors/Destructor ..............................................
    
    // ----------------------------------------------------------
    /**
     * Constructs an empty array.
     */
    SafeArray()
    {
        init();
    }


    // ----------------------------------------------------------
    /**
     * Constructs a new array from the contents of the given array.
     */
    SafeArray(const SafeArray& src)
    {
        copyFrom(src);
    }


    // ----------------------------------------------------------
    /**
     * Assigns the contents of the specified array to this array.
     */
    SafeArray& operator=(const SafeArray& rhs)
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
     * Releases any resources used by the array.
     */
    ~SafeArray()
    {
        free(buffer);
    }


    //~ Public methods .......................................................

    // ----------------------------------------------------------
    /**
     * Appends the specified element to the end of the array.
     *
     * @param elem the element to append
     */
    void push_back(const T& elem)
    {
        if (_size == _capacity)
        {
            grow();
        }
        
        buffer[_size++] = elem;
    }


    // ----------------------------------------------------------
    /**
     * Accesses an element in the array by its index.
     *
     * @param index the 0-based index of the element to access
     * @return a reference to the index-th element
     */
    T& operator[](unsigned int index) const
    {
        return buffer[index];
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether or not the array is empty.
     *
     * @return true if the array is empty; otherwise, false.
     */
    bool empty() const
    {
        return _size == 0;
    }


    // ----------------------------------------------------------
    /**
     * Gets the number of elements in the array.
     */
    size_t size() const
    {
        return _size;
    }
    
    
    // ----------------------------------------------------------
    /**
     * Gets the pointer to the data in the array.
     */
    const T* data() const
    {
        return buffer;
    }


private:
    //~ Private methods ......................................................

    // ----------------------------------------------------------
    void init()
    {
        _capacity = 32;
        _size = 0;
        buffer = (T*) malloc(_capacity * sizeof(T));
    }
    
    
    // ----------------------------------------------------------
    void grow()
    {
        _capacity *= 2;
        buffer = (T*) realloc(buffer, _capacity * sizeof(T));
    }


    // ----------------------------------------------------------
    void copyFrom(const SafeArray& src)
    {
        _capacity = src._capacity;
        _size = src._size;
        
        buffer = (T*) malloc(_capacity * sizeof(T));
        memcpy(buffer, src.buffer, _size * sizeof(T));
    }


    //~ Instance variables ...................................................

    T* buffer;
    size_t _size;
    size_t _capacity;
};

} // end namespace CxxTest


#endif // __cxxtest__SafeArray_h__
