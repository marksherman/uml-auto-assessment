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

#include <dereferee/config.h>

#ifndef DEREFEREE_DISABLED


#ifdef _MSC_VER
#	ifndef _CRT_SECURE_NO_DEPRECATE
#		define _CRT_SECURE_NO_DEPRECATE
#	endif
#	ifndef _CRT_NONSTDC_NO_DEPRECATE
#		define _CRT_NONSTDC_NO_DEPRECATE
#	endif
#endif

#include <cstdarg>
#include <cstdio>
#include <memory>
#include <dereferee/manager.h>
#include <dereferee/allocation_info_impl.h>

// ===========================================================================

#define __DMI ::Dereferee::manager::instance()

#define DEREFEREE_SAFETY_SIZE 16
#define DEREFEREE_SAFETY_CHAR '!'


// ===========================================================================

#define _DEREFEREE_XQUOTE(x) _DEREFEREE_QUOTE(x)
#define _DEREFEREE_QUOTE(x) #x

/**
 * Provide a default set of empty platform options if none were provided at
 * compile-time.
 */
#ifndef DEREFEREE_PLATFORM_OPTIONS
#	define _DEREFEREE_PLATFORM_OPTIONS ""
#else
#	define _DEREFEREE_PLATFORM_OPTIONS \
		_DEREFEREE_XQUOTE(DEREFEREE_PLATFORM_OPTIONS)
#endif


/**
 * Provide a default set of empty listener options if none were provided at
 * compile-time.
 */
#ifndef DEREFEREE_LISTENER_OPTIONS
#	define _DEREFEREE_LISTENER_OPTIONS ""
#else
#	define _DEREFEREE_LISTENER_OPTIONS \
		_DEREFEREE_XQUOTE(DEREFEREE_LISTENER_OPTIONS)
#endif


namespace Dereferee
{

// ===========================================================================
platform* current_platform()
{
	return __DMI->_platform;
}

// ---------------------------------------------------------------------------
listener* current_listener()
{
	return __DMI->_listener;
}

// ---------------------------------------------------------------------------
void visit_allocations(allocation_visitor visitor, void* arg)
{
	__DMI->visit_allocations(visitor, arg);
}


// ===========================================================================
/**
 * The singleton instance of the Dereferee memory manager.
 */
manager* manager::_instance = 0;

// ---------------------------------------------------------------------------
/**
 * This function destroys the Dereferee memory manager when program execution
 * is complete. It is registered with the runtime via atexit() when the
 * memory manager is first created, so that its destruction takes place in the
 * proper order if any statically initialized objects call new/new[] and
 * request memory before main() is entered.
 */
void destroy_manager()
{
	__DMI->report_usage();

#ifndef DEREFEREE_NO_DESTROY_MANAGER_AT_END
	delete __DMI;
#endif
}

// ===========================================================================
/*
 * Implementation of the Dereferee::manager methods.
 */

// ---------------------------------------------------------------------------
manager::manager()
{
	_uninit_handle = malloc(4);

	_next_tag = 0;
	_checked_table_size = 0;
	_unchecked_table_size = 0;

	_checked_table = NULL;
	_unchecked_table = NULL;

	initialize_platform();
	initialize_listener();
}

// ------------------------------------------------------------------
void manager::initialize_platform()
{
	const char* sep_semi = ";";
	const char* sep_eq = "=";

	char* next_pair, *value, *pair_start;

	// Parse the options specified at compile-time with the
	// DEREFEREE_PLATFORM_OPTIONS preprocessor definition.
	int ct_option_len = strlen(_DEREFEREE_PLATFORM_OPTIONS);
	char* ct_option_buf = (char*)malloc(ct_option_len + 1);
	strncpy(ct_option_buf, _DEREFEREE_PLATFORM_OPTIONS, ct_option_len);

	size_t num_options = 0;
	size_t cap_options = 4;
	option* options = (option*)malloc(cap_options * sizeof(option));

	for(pair_start = DEREFEREE_STRTOK(ct_option_buf, sep_semi, &next_pair);
		pair_start != NULL;
		pair_start = DEREFEREE_STRTOK(NULL, sep_semi, &next_pair))
	{
		if(DEREFEREE_STRTOK(pair_start, sep_eq, &value))
		{
			if(pair_start[0] && value && value[0])
			{
				add_option(options, num_options, cap_options,
						pair_start, value);
			}
		}
	}

	// Parse the options specified at runtime with the
	// DEREFEREE_PLATFORM_OPTIONS environment variable, overriding any of the
	// previously specified options that have the same key.
	char* envvar = getenv("DEREFEREE_PLATFORM_OPTIONS");
	char* rt_option_buf = NULL;

	if(envvar != NULL)
	{
		int rt_option_len = strlen(envvar);
		rt_option_buf = (char*)malloc(rt_option_len + 1);
		strncpy(rt_option_buf, envvar, rt_option_len);

		for(pair_start = DEREFEREE_STRTOK(rt_option_buf, sep_semi, &next_pair);
			pair_start != NULL;
			pair_start = DEREFEREE_STRTOK(NULL, sep_semi, &next_pair))
		{
			if(DEREFEREE_STRTOK(pair_start, sep_eq, &value))
			{
				if(pair_start[0] && value && value[0])
				{
					add_option(options, num_options, cap_options,
							pair_start, value);
				}
			}
		}
	}

	// Terminate the options array with a NULL entry.
	if(num_options == cap_options)
	{
		cap_options++;
		options = (option*)realloc(options, cap_options * sizeof(option));
	}

	options[num_options].key = NULL;
	options[num_options].value = NULL;

	_platform = create_platform(options);

	if(rt_option_buf)
	{
		free(rt_option_buf);
	}

	free(ct_option_buf);
}

// ------------------------------------------------------------------
void manager::initialize_listener()
{
	const char* sep_semi = ";";
	const char* sep_eq = "=";

	char* next_pair, *value, *pair_start;

	// Parse the options specified at compile-time with the
	// DEREFEREE_LISTENER_OPTIONS preprocessor definition.
	int ct_option_len = strlen(_DEREFEREE_LISTENER_OPTIONS);
	char* ct_option_buf = (char*)malloc(ct_option_len + 1);
	strncpy(ct_option_buf, _DEREFEREE_LISTENER_OPTIONS, ct_option_len);

	size_t num_options = 0;
	size_t cap_options = 4;
	option* options = (option*)malloc(cap_options * sizeof(option));

	for(pair_start = DEREFEREE_STRTOK(ct_option_buf, sep_semi, &next_pair);
		pair_start != NULL;
		pair_start = DEREFEREE_STRTOK(NULL, sep_semi, &next_pair))
	{
		if(DEREFEREE_STRTOK(pair_start, sep_eq, &value))
		{
			if(pair_start[0] && value && value[0])
			{
				add_option(options, num_options, cap_options,
						pair_start, value);
			}
		}
	}

	// Parse the options specified at runtime with the
	// DEREFEREE_LISTENER_OPTIONS environment variable, overriding any of the
	// previously specified options that have the same key.
	char* envvar = getenv("DEREFEREE_LISTENER_OPTIONS");
	char* rt_option_buf = NULL;

	if(envvar != NULL)
	{
		int rt_option_len = strlen(envvar);
		rt_option_buf = (char*)malloc(rt_option_len + 1);
		strncpy(rt_option_buf, envvar, rt_option_len);

		for(pair_start = DEREFEREE_STRTOK(rt_option_buf, sep_semi, &next_pair);
			pair_start != NULL;
			pair_start = DEREFEREE_STRTOK(NULL, sep_semi, &next_pair))
		{
			if(DEREFEREE_STRTOK(pair_start, sep_eq, &value))
			{
				if(pair_start[0] && value && value[0])
				{
					add_option(options, num_options, cap_options,
							pair_start, value);
				}
			}
		}
	}

	// Terminate the options array with a NULL entry.
	if(num_options == cap_options)
	{
		cap_options++;
		options = (option*)realloc(options, cap_options * sizeof(option));
	}

	options[num_options].key = NULL;
	options[num_options].value = NULL;

	_listener = create_listener(options, _platform);

	if(rt_option_buf)
	{
		free(rt_option_buf);
	}

	free(ct_option_buf);
}

// ------------------------------------------------------------------
void manager::add_option(option*& options,
		size_t& num_options, size_t& cap_options, const char* key,
		const char* value)
{
	// Check to see if the option is already in the array; if so,
	// replace the value.
	for(size_t i = 0; i < num_options; i++)
	{
		if(strcmp(options[i].key, key) == 0)
		{
			options[i].value = value;
			return;
		}
	}

	// We didn't find the key, so add a new entry.
	if(num_options == cap_options)
	{
		cap_options *= 2;
		options = (option*)realloc(options, cap_options * sizeof(option));
	}

	options[num_options].key = key;
	options[num_options].value = value;

	num_options++;
}

// ------------------------------------------------------------------
manager* manager::instance()
{
	if(_instance == 0)
	{
		_instance = new manager();
		atexit(destroy_manager);
	}

	return _instance;
}

// ------------------------------------------------------------------
manager::~manager()
{
	memtab_destroy_table(_checked_table);
	memtab_destroy_table(_unchecked_table);

	destroy_listener(_listener);
	destroy_platform(_platform);

	free(_uninit_handle);
}

// ------------------------------------------------------------------
memtag_t manager::next_tag()
{
	return _next_tag++;
}

// ------------------------------------------------------------------
unsigned long manager::move_to_checked(const void* address)
{
	memtab_entry* curr_node = memtab_find_address(_unchecked_table, address);
	if(curr_node)
	{
		memtab_entry* rem_node = memtab_remove_address(_unchecked_table,
													   address);
		memtab_insert_entry(_checked_table, rem_node);

		_unchecked_table_size--;
		_checked_table_size++;

		return rem_node->info.tag;
	}

	return default_memtag;
}

// ------------------------------------------------------------------
void manager::remove_checked(const void* address)
{
	memtab_entry* curr_node = memtab_find_address(_checked_table, address);
	if(curr_node)
	{
		memtab_entry* rem_node =
			memtab_remove_address(_checked_table, address);
		memtab_free(rem_node);

		_checked_table_size--;
	}
}

// ------------------------------------------------------------------
void manager::remove_unchecked(const void* address)
{
	memtab_entry* curr_node = memtab_find_address(_unchecked_table, address);
	if(curr_node)
	{
		memtab_entry* rem_node = memtab_remove_address(_unchecked_table,
													   address);
		memtab_free(rem_node);

		_unchecked_table_size--;
	}
}

// ------------------------------------------------------------------
bool manager::is_checked(const void* address, memtag_t tag)
{
	memtab_entry* curr_node = memtab_find_address(_checked_table, address);

	if(!curr_node)
		return false;
	else
		return curr_node->info.tag == tag;
}

// ------------------------------------------------------------------
void* manager::uninit_handle()
{
	return _uninit_handle;
}

// ------------------------------------------------------------------
void manager::retain(const void* address)
{
	memtab_entry* curr_node = memtab_find_address(_checked_table, address);
	curr_node->info.ref_count++;
}

// ------------------------------------------------------------------
void manager::release(const void* address)
{
	memtab_entry* curr_node = memtab_find_address(_checked_table, address);
	curr_node->info.ref_count--;
}

// ------------------------------------------------------------------
refcount_t manager::ref_count(const void* address)
{
	memtab_entry* curr_node = memtab_find_address(_checked_table, address);
	return curr_node->info.ref_count;
}

// ------------------------------------------------------------------
mem_info* manager::address_info(const void* address, bool* is_checked)
{
	memtab_entry* curr_node;

	curr_node = memtab_find_address(_unchecked_table, address);
	if(curr_node)
	{
		if(is_checked)
			*is_checked = false;

		return &curr_node->info;
	}

	curr_node = memtab_find_address(_checked_table, address);
	if(curr_node)
	{
		if(is_checked)
			*is_checked = true;

		return &curr_node->info;
	}

	return NULL;
}

// ------------------------------------------------------------------
void manager::warning(warning_code code, ...)
{
	va_list args;
	va_start(args, code);

	_listener->warning(code, args);

	va_end(args);
}

// ------------------------------------------------------------------
void manager::error(error_code code, ...)
{
	va_list args;
	va_start(args, code);

	_listener->error(code, args);

	va_end(args);
}

// ------------------------------------------------------------------
void manager::visit_allocations(allocation_visitor visitor, void* arg)
{
    visit_allocation_entry(_checked_table, visitor, arg);
    visit_allocation_entry(_unchecked_table, visitor, arg);
}

// ------------------------------------------------------------------
void manager::visit_allocation_entry(memtab_entry* entry,
                                     allocation_visitor visitor,
                                     void* arg)
{
	if(entry)
	{
		visit_allocation_entry(entry->_1, visitor, arg);
        allocation_info_impl aii(entry->info);
        visitor(aii, arg);
		visit_allocation_entry(entry->_2, visitor, arg);
	}
}

// ------------------------------------------------------------------
void manager::count_leaked_entries(memtab_entry* entry, size_t& total_leaks)
{
	if(entry)
	{
		count_leaked_entries(entry->_1, total_leaks);

        allocation_info_impl alloc_info(entry->info);
		if(_listener->should_report_leak(alloc_info))
		{
			total_leaks++;
		}

		count_leaked_entries(entry->_2, total_leaks);
	}
}

// ------------------------------------------------------------------
void manager::report_leaked_entry(memtab_entry* entry, size_t max_log,
								  size_t& reports_logged)
{
	if(entry)
	{
		report_leaked_entry(entry->_1, max_log, reports_logged);

        allocation_info_impl alloc_info(entry->info);

		if(reports_logged < max_log
		    && _listener->should_report_leak(alloc_info))
		{
			_listener->report_leak(alloc_info);
			reports_logged++;
		}

		report_leaked_entry(entry->_2, max_log, reports_logged);
	}
}

// ------------------------------------------------------------------
void manager::report_usage()
{
	size_t total_leaks = 0;

	count_leaked_entries(_checked_table, total_leaks);
	count_leaked_entries(_unchecked_table, total_leaks);

	_usage_stats.set_leaks(total_leaks);

	_listener->begin_report(_usage_stats);

	size_t reports_logged = 0;
	size_t max_log = _listener->maximum_leaks_to_report();

	report_leaked_entry(_checked_table, max_log, reports_logged);
	report_leaked_entry(_unchecked_table, max_log, reports_logged);

	if(total_leaks > reports_logged)
	{
		_listener->report_truncated(reports_logged, total_leaks);
	}

	_listener->end_report();
}

// ------------------------------------------------------------------
void* manager::allocate_memory(size_t size, bool is_array)
	DEREFEREE_THROW_BAD_ALLOC
{
	size_t alloc_size = size + (2 * DEREFEREE_SAFETY_SIZE);

	// Check for overflow after we add the safety size to the buffer.
	if(alloc_size < size)
		throw(std::bad_alloc());

	char* address = (char*)malloc(alloc_size);
	if(!address)
		throw(std::bad_alloc());

	_usage_stats.record_allocation(size, is_array);

	char* client_ptr = address + DEREFEREE_SAFETY_SIZE;

	memset(address, DEREFEREE_SAFETY_CHAR, DEREFEREE_SAFETY_SIZE);
	memset(client_ptr + size, DEREFEREE_SAFETY_CHAR, DEREFEREE_SAFETY_SIZE);

	memtab_entry* new_node = memtab_alloc();

	new_node->info.address = client_ptr;
	new_node->info.is_array = is_array;
	new_node->info.block_size = size;
	new_node->info.tag = next_tag();
	new_node->info.ref_count = 0;
	new_node->info.backtrace = _platform->get_backtrace(NULL, NULL);
    new_node->info.user_info = _listener->get_allocation_user_info(
        allocation_info_impl(new_node->info));

	memtab_insert_entry(_unchecked_table, new_node);
	_unchecked_table_size++;

	return client_ptr;
}

// ------------------------------------------------------------------
void manager::free_memory(void* address, bool is_array)
{
	if(address == uninit_handle())
	{
		if(is_array)
		{
			error(error_array_delete_dead_uninitialized);
		}
		else
		{
			error(error_nonarray_delete_dead_uninitialized);
		}
	}
	else if(address != 0)
	{
		bool is_checked;
		mem_info* addr_info = address_info(address, &is_checked);

		if(!addr_info)
		{
			if(is_array)
			{
				error(error_array_delete_dead_deleted);
			}
			else
			{
				error(error_nonarray_delete_dead_deleted);
			}
		}
		else
		{
			if(!is_array && addr_info->is_array)
			{
				error(error_nonarray_delete_on_array);
			}
			else if(is_array && !addr_info->is_array)
			{
				error(error_array_delete_on_non_array);
			}

			size_t size = addr_info->block_size;

			_usage_stats.record_deallocation(size, is_array);

            allocation_info_impl aii(*addr_info);
            _listener->free_allocation_user_info(aii);

			_platform->free_backtrace(addr_info->backtrace);

			if(is_checked)
				remove_checked(address);
			else
				remove_unchecked(address);

			char* block_start = (char*)address - DEREFEREE_SAFETY_SIZE;
			char* client_end = (char*)address + size;

			char safety_check[DEREFEREE_SAFETY_SIZE];
			bool low_damage = false, high_damage = false;

			memset(safety_check, DEREFEREE_SAFETY_CHAR, DEREFEREE_SAFETY_SIZE);

			if(memcmp(block_start, safety_check, DEREFEREE_SAFETY_SIZE))
				low_damage = true;

			if(memcmp(client_end, safety_check, DEREFEREE_SAFETY_SIZE))
				high_damage = true;

			if(low_damage || high_damage)
			{
				memory_corruption_location damage_type;

				if(low_damage && high_damage)
				{
					damage_type = memory_corruption_both;
				}
				else if(low_damage)
				{
					damage_type = memory_corruption_before;
				}
				else
				{
					damage_type = memory_corruption_after;
				}

				warning(warning_memory_boundary_corrupted, damage_type);
			}

			// Zero out the memory before freeing it. This is useful if a
			// dangling pointer to an object with a vtable has a method
			// called on it; in this case, a null pointer dereference
			// will result.
			memset(block_start, 0, size + 2 * DEREFEREE_SAFETY_SIZE);
			free(block_start);
		}
	}
	else
	{
		_usage_stats.record_null_deallocation(is_array);
	}
}

// ---------------------------------------------------------------------------
void* manager::operator new(size_t size)
{
	return malloc(size);
}

// ---------------------------------------------------------------------------
void manager::operator delete(void* ptr)
{
	free(ptr);
}

} // namespace Dereferee


// ------------------------------------------------------------------
#ifndef DEREFEREE_NO_OVERLOADED_MEMORY_OPS

void* operator new(size_t size) DEREFEREE_THROW_BAD_ALLOC
{
	return __DMI->allocate_memory(size, false);
}

// ------------------------------------------------------------------
void* operator new(size_t size, const std::nothrow_t& /*nothrow*/) throw()
{
	try
	{
		return ::operator new(size);
	}
	catch(std::bad_alloc)
	{
		return NULL;
	}
}

// ------------------------------------------------------------------
void* operator new[](size_t size) DEREFEREE_THROW_BAD_ALLOC
{
	return __DMI->allocate_memory(size, true);
}

// ------------------------------------------------------------------
void* operator new[](size_t size, const std::nothrow_t& /*nothrow*/) throw()
{
	try
	{
		return ::operator new[](size);
	}
	catch(std::bad_alloc)
	{
		return NULL;
	}
}

// ------------------------------------------------------------------
void operator delete(void* address)
{
	__DMI->free_memory(address, false);
}

// ------------------------------------------------------------------
void operator delete[](void* address)
{
	__DMI->free_memory(address, true);
}

#endif // DEREFEREE_NO_OVERLOADED_MEMORY_OPS

// ------------------------------------------------------------------
void* operator new[](size_t size, Dereferee::cookie_info& cookie_calc)
	DEREFEREE_THROW_BAD_ALLOC
{
	cookie_calc.cookie_size = size - cookie_calc.type_size;
	throw std::bad_alloc();
}


#endif // DEREFEREE_DISABLED
