#ifndef __CHKPTR_CASTS_H__
#define __CHKPTR_CASTS_H__

/**
 * This file contains template specializations to support dynamic and static
 * casts of checked pointer types.
 * 
 * If your compiler has issues with partial template specializations, define
 * the CHKPTR_OMIT_CAST_SUPPORT when you compile and these definitions will
 * not be included.
 */


// ---------------------------------------------------------------------------
//   STATIC CAST
// ---------------------------------------------------------------------------
/**
 * This is the unspecialized version of the static_cast template. This allows
 * us to fall back to the compiler default static_cast operation if the
 * source argument is not a checked pointer, i.e.
 * 
 *    T p;
 *    TCast q = static_cast<TCast>(p);
 */
template <typename TCast, typename T>
class __static_cast_impl
{
public:
	static TCast cast(T valueToCast)
	{
		return static_cast<TCast>(valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the static cast operation, and returning a raw pointer type. This
 * supports these syntaxes:
 * 
 *    Ptr<T> p;
 *    TCast* q = static_cast<TCast*>(p);
 *       or
 *    Ptr<TCast> q = static_cast<TCast*>(p);
 * 
 * The second syntax works by virtue of the Ptr<> copy constructor and
 * assignment operator taking a raw pointer as an argument.
 */
template <typename TCast, typename T>
class __static_cast_impl< TCast, Ptr<T> >
{
public:
	static TCast cast(Ptr<T> valueToCast)
	{
		return static_cast<TCast>((T*)valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the static cast AND as the destination type in the template argument.
 * This is provided for consistency, so students can use the checked pointer
 * syntax in both locations. This supports:
 * 
 *    Ptr<T> p;
 *    Ptr<TCast> q = static_cast< Ptr<TCast> >(p);
 */
template <typename TCast, typename T>
class __static_cast_impl< Ptr<TCast>, Ptr<T> >
{
public:
	static Ptr<TCast> cast(Ptr<T> valueToCast)
	{
		return static_cast<TCast*>((T*)valueToCast);
	}
};

/**
 * This stub redirects the cast operation to the appropriate specialization
 * of __static_cast_impl, since non-class functions cannot be specialized
 * in C++.
 */
template <typename TCast, typename T>
TCast __checked_static_cast(T valueToCast)
{
	return __static_cast_impl<TCast, T>::cast(valueToCast);
}


// ---------------------------------------------------------------------------
//   DYNAMIC CAST
// ---------------------------------------------------------------------------
/**
 * This is the unspecialized version of the dynamic_cast template. This allows
 * us to fall back to the compiler default dynamic_cast operation if the
 * source argument is not a checked pointer, i.e.
 * 
 *    T* p;
 *    TCast* q = dynamic_cast<TCast*>(p);
 */
template <typename TCast, typename T>
class __dynamic_cast_impl
{
public:
	static TCast cast(T valueToCast)
	{
		return dynamic_cast<TCast>(valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the dynamic cast operation, and returning a raw pointer type. This
 * supports these syntaxes:
 * 
 *    Ptr<T> p;
 *    TCast* q = dynamic_cast<TCast*>(p);
 *       or
 *    Ptr<TCast> q = dynamic_cast<TCast*>(p);
 * 
 * The second syntax works by virtue of the Ptr<> copy constructor and
 * assignment operator taking a raw pointer as an argument.
 */
template <typename TCast, typename T>
class __dynamic_cast_impl< TCast, Ptr<T> >
{
public:
	static TCast cast(Ptr<T> valueToCast)
	{
		return dynamic_cast<TCast>((T*)valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the dynamic cast AND as the destination type in the template argument.
 * This is provided for consistency, so students can use the checked pointer
 * syntax in both locations. This supports:
 * 
 *    Ptr<T> p;
 *    Ptr<TCast> q = dynamic_cast< Ptr<TCast> >(p);
 */
template <typename TCast, typename T>
class __dynamic_cast_impl< Ptr<TCast>, Ptr<T> >
{
public:
	static Ptr<TCast> cast(Ptr<T> valueToCast)
	{
		return dynamic_cast<TCast*>((T*)valueToCast);
	}
};

/**
 * This stub redirects the cast operation to the appropriate specialization
 * of __dynamic_cast_impl, since non-class functions cannot be specialized
 * in C++.
 */
template <typename TCast, typename T>
TCast __checked_dynamic_cast(T valueToCast)
{
	return __dynamic_cast_impl<TCast, T>::cast(valueToCast);
}


// ---------------------------------------------------------------------------
//   CONST CAST
// ---------------------------------------------------------------------------
/**
 * This is the unspecialized version of the const_cast template. This allows
 * us to fall back to the compiler default const_cast operation if the
 * source argument is not a checked pointer, i.e.
 * 
 *    T p;
 *    TCast q = const_cast<TCast>(p);
 */
template <typename TCast, typename T>
class __const_cast_impl
{
public:
	static TCast cast(T valueToCast)
	{
		return const_cast<TCast>(valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the const cast operation, and returning a raw pointer type. This
 * supports these syntaxes:
 * 
 *    Ptr<T> p;
 *    TCast* q = const_cast<TCast*>(p);
 *       or
 *    Ptr<TCast> q = const_cast<TCast*>(p);
 * 
 * The second syntax works by virtue of the Ptr<> copy constructor and
 * assignment operator taking a raw pointer as an argument.
 */
template <typename TCast, typename T>
class __const_cast_impl< TCast, Ptr<T> >
{
public:
	static TCast cast(Ptr<T> valueToCast)
	{
		return const_cast<TCast>((T*)valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the const cast AND as the destination type in the template argument.
 * This is provided for consistency, so students can use the checked pointer
 * syntax in both locations. This supports:
 * 
 *    Ptr<T> p;
 *    Ptr<TCast> q = const_cast< Ptr<TCast> >(p);
 */
template <typename TCast, typename T>
class __const_cast_impl< Ptr<TCast>, Ptr<T> >
{
public:
	static Ptr<TCast> cast(Ptr<T> valueToCast)
	{
		return const_cast<TCast*>((T*)valueToCast);
	}
};

/**
 * This stub redirects the cast operation to the appropriate specialization
 * of __const_cast_impl, since non-class functions cannot be specialized
 * in C++.
 */
template <typename TCast, typename T>
TCast __checked_const_cast(T valueToCast)
{
	return __const_cast_impl<TCast, T>::cast(valueToCast);
}


// ---------------------------------------------------------------------------
//   REINTERPRET CAST
// ---------------------------------------------------------------------------
/**
 * This is the unspecialized version of the reinterpret_cast template. This
 * allows us to fall back to the compiler default reinterpret_cast operation
 * if the source argument is not a checked pointer, i.e.
 * 
 *    T p;
 *    TCast q = reinterpret_cast<TCast>(p);
 */
template <typename TCast, typename T>
class __reinterpret_cast_impl
{
public:
	static TCast cast(T valueToCast)
	{
		return reinterpret_cast<TCast>(valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the reinterpret cast operation, and returning a raw pointer type. This
 * supports these syntaxes:
 * 
 *    Ptr<T> p;
 *    TCast* q = reinterpret_cast<TCast*>(p);
 *       or
 *    Ptr<TCast> q = reinterpret_cast<TCast*>(p);
 * 
 * The second syntax works by virtue of the Ptr<> copy constructor and
 * assignment operator taking a raw pointer as an argument.
 */
template <typename TCast, typename T>
class __reinterpret_cast_impl< TCast, Ptr<T> >
{
public:
	static TCast cast(Ptr<T> valueToCast)
	{
		return reinterpret_cast<TCast>((T*)valueToCast);
	}
};

/**
 * This specialization supports passing a checked pointer as the source of
 * the reinterpret cast AND as the destination type in the template argument.
 * This is provided for consistency, so students can use the checked pointer
 * syntax in both locations. This supports:
 * 
 *    Ptr<T> p;
 *    Ptr<TCast> q = reinterpret_cast< Ptr<TCast> >(p);
 */
template <typename TCast, typename T>
class __reinterpret_cast_impl< Ptr<TCast>, Ptr<T> >
{
public:
	static Ptr<TCast> cast(Ptr<T> valueToCast)
	{
		return reinterpret_cast<TCast*>((T*)valueToCast);
	}
};

/**
 * This stub redirects the cast operation to the appropriate specialization
 * of __reinterpret_cast_impl, since non-class functions cannot be specialized
 * in C++.
 */
template <typename TCast, typename T>
TCast __checked_reinterpret_cast(T valueToCast)
{
	return __reinterpret_cast_impl<TCast, T>::cast(valueToCast);
}


/**
 * These macros override the definitions of the cast operators
 * so that our specialized versions are called.
 */
#define dynamic_cast __checked_dynamic_cast
#define static_cast __checked_static_cast
#define const_cast __checked_const_cast
#define reinterpret_cast __checked_reinterpret_cast


#endif // __CHKPTR_CASTS_H__
