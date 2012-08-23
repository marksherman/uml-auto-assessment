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

// ===========================================================================
/*
 * The definitions in this file are implementations of functions that manage
 * the memory allocation table used by Dereferee to track memory usage.
 */

#include <memory>
#include <dereferee/memtab.h>

// ===========================================================================

namespace Dereferee
{

bool memtab_insert_entry_impl(memtab_entry*& entry, memtab_entry* parent,
							  memtab_entry* new_entry);

bool memtab_remove_address_impl(memtab_entry*& entry, const void* address,
								bool& found, memtab_entry*& entry_to_return);

bool memtab_balance_left_insert(memtab_entry*& entry);

bool memtab_balance_right_insert(memtab_entry*& entry);

bool memtab_balance_left_remove(memtab_entry*& entry);

bool memtab_balance_right_remove(memtab_entry*& entry);

void memtab_rotate_left(memtab_entry*& entry);

void memtab_rotate_right(memtab_entry*& entry);

bool memtab_replace_from_left(memtab_entry* entry,
							  memtab_entry*& left_subtree, bool& found,
							  memtab_entry*& entry_to_return);

bool memtab_replace_from_right(memtab_entry* entry,
							   memtab_entry*& right_subtree,
							   bool& found, memtab_entry*& entry_to_return);

// ---------------------------------------------------------------------------
int memtab_table_size(memtab_entry* entry)
{
	if(entry)
	{
		return 1 + memtab_table_size(entry->left) +
			memtab_table_size(entry->right);
	}
	else
	{
		return 0;
	}
}

// ---------------------------------------------------------------------------
void memtab_print_table(memtab_entry* entry, int level)
{
	if(entry)
	{
		memtab_print_table(entry->right, level + 1);

		if(level == 0)
			printf("* ");
		else
			printf("  ");

		for(int i = 0; i < level; i++)
			printf("---");

		printf(" %p-%p %s\n", entry->info.address,
			   (char*)entry->info.address + entry->info.block_size,
			   entry->info.is_array? ", array": "");

		memtab_print_table(entry->left, level + 1);
	}
}

// ---------------------------------------------------------------------------
memtab_entry* memtab_alloc()
{
	memtab_entry* entry = (memtab_entry*)malloc(sizeof(memtab_entry));

	entry->left = 0;
	entry->right = 0;
	entry->parent = 0;
	entry->balance = 0;

	bzero(&entry->info, sizeof(mem_info));
	entry->info.tag = (unsigned long)~0;
	
	return entry;
}

// ---------------------------------------------------------------------------
void memtab_free(memtab_entry* entry)
{
	if(entry->info.type_name)
		free(entry->info.type_name);

	free(entry);
}

// ---------------------------------------------------------------------------
void memtab_destroy_table(memtab_entry* entry)
{
	if(entry)
	{
		memtab_destroy_table(entry->left);
		memtab_destroy_table(entry->right);
		memtab_free(entry);
	}
}

// ---------------------------------------------------------------------------
memtab_entry* memtab_find_address(memtab_entry* entry, const void* address)
{
	if(!entry)
	{
		return 0;
	}
	else if(entry->info.address <= address &&
			address <= (char*)entry->info.address + entry->info.block_size)
	{
		return entry;
	}
	else if(address < entry->info.address)
	{
		return memtab_find_address(entry->left, address);
	}
	else
	{
		return memtab_find_address(entry->right, address);
	}
}

// ---------------------------------------------------------------------------
bool memtab_insert_entry(memtab_entry*& entry, memtab_entry* new_node)
{
	return memtab_insert_entry_impl(entry, 0, new_node);
}

// ---------------------------------------------------------------------------
bool memtab_insert_entry_impl(memtab_entry*& entry, memtab_entry* parent,
							  memtab_entry* new_entry)
{
	if(!entry)
    {
		entry = new_entry;
		new_entry->parent = parent;
		return true;
	}
	else
	{
		bool rebalance;

		if(new_entry->info.address < entry->info.address)
		{
			rebalance = memtab_insert_entry_impl(entry->left, entry,
												 new_entry);

			if(rebalance)
				rebalance = memtab_balance_left_insert(entry);
		}
		else
		{
			rebalance = memtab_insert_entry_impl(entry->right, entry,
												 new_entry);

			if(rebalance)
				rebalance = memtab_balance_right_insert(entry);
		}

		return rebalance;
	}
}

// ---------------------------------------------------------------------------
memtab_entry* memtab_remove_address(memtab_entry*& entry, const void* address)
{
	memtab_entry* entry_to_return = 0;
	bool found = false;
	memtab_remove_address_impl(entry, address, found, entry_to_return);
	return entry_to_return;
}

// ---------------------------------------------------------------------------
bool memtab_remove_address_impl(memtab_entry*& entry, const void* address,
								bool& found, memtab_entry*& entry_to_return)
{
	if(!entry)
	{
		found = false;
		entry_to_return = 0;
		return false;
	}
	else
	{
		bool rebalance = false;
		
		if(entry->info.address <= address &&
			address <= (char*)entry->info.address + entry->info.block_size)
		{
			found = true;

			if(entry->left)
			{
				rebalance = memtab_replace_from_right(entry, entry->left,
													  found, entry_to_return);

				if(found && rebalance)
					rebalance = memtab_balance_left_remove(entry);
			}
			else if(entry->right)
			{
				rebalance = memtab_replace_from_left(entry, entry->right,
													 found, entry_to_return);

				if(found && rebalance)
					rebalance = memtab_balance_right_remove(entry);
			}
			else
			{
				entry_to_return = entry;
				entry_to_return->left = 0;
				entry_to_return->right = 0;
				entry_to_return->parent = 0;
				entry = 0;

				rebalance = true;
			}
		}
		else if(address < entry->info.address)
		{
			rebalance = memtab_remove_address_impl(entry->left, address,
												   found, entry_to_return);

			if(rebalance)
				rebalance = memtab_balance_left_remove(entry);
		}
		else
		{
			rebalance = memtab_remove_address_impl(entry->right, address,
												   found, entry_to_return);

			if(rebalance)
				rebalance = memtab_balance_right_remove(entry);
		}

		return rebalance;
	}
}

// ---------------------------------------------------------------------------
bool memtab_replace_from_left(memtab_entry* entry, memtab_entry*& left_subtree,
							  bool& found, memtab_entry*& entry_to_return)
{
	bool rebalance = true;
    
	if(!left_subtree)
	{
		found = false;
	}
	else
	{
		if(left_subtree->left)
		{
			rebalance = memtab_replace_from_left(entry, left_subtree->left,
												 found, entry_to_return);
			
			if(found && rebalance)
				rebalance = memtab_balance_left_remove(left_subtree);
		}
		else
		{
			mem_info temp = entry->info;
			entry->info = left_subtree->info;
			left_subtree->info = temp;
			
			entry_to_return = left_subtree;
			left_subtree = entry_to_return->right;

			if(left_subtree)
				left_subtree->parent = entry_to_return->parent;

			entry_to_return->left = 0;
			entry_to_return->right = 0;
			entry_to_return->parent = 0;

			found = true;
		}
	}
	
	return rebalance;	
}

// ---------------------------------------------------------------------------
bool memtab_replace_from_right(memtab_entry* entry,
							   memtab_entry*& right_subtree, bool& found,
							   memtab_entry*& entry_to_return)
{
	bool rebalance = true;
    
	if(!right_subtree)
	{
		found = false;
	}
	else
	{
		if(right_subtree->right)
		{
			rebalance = memtab_replace_from_right(entry, right_subtree->right,
												  found, entry_to_return);
			
			if(found && rebalance)
				rebalance = memtab_balance_right_remove(right_subtree);
		}
		else
		{
			mem_info temp = entry->info;
			entry->info = right_subtree->info;
			right_subtree->info = temp;
			
			entry_to_return = right_subtree;
			right_subtree = entry_to_return->left;

			if(right_subtree)
				right_subtree->parent = entry_to_return->parent;

			entry_to_return->left = 0;
			entry_to_return->right = 0;
			entry_to_return->parent = 0;
			
			found = true;
		}
	}
	
	return rebalance;	
}

// ---------------------------------------------------------------------------
bool memtab_balance_left_insert(memtab_entry*& entry)
{
	bool rebalance = false;
	
	if(entry->balance == -1)
	{
		if(entry->left->balance == -1)
		{
			entry->balance       = 0;
			entry->left->balance = 0;

			memtab_rotate_right(entry);
		}
		else
		{
			if(entry->left->right->balance == -1)
			{
				entry->balance       = +1;
				entry->left->balance = 0;
			}
			else if(entry->left->right->balance == 0)
			{
				entry->balance       = 0;
				entry->left->balance = 0;
			}
			else // if(entry->left->right->balance == +1)
			{
				entry->balance       = 0;
				entry->left->balance = -1;
			}

			entry->left->right->balance = 0;

			memtab_rotate_left(entry->left);
			memtab_rotate_right(entry);
		}

		rebalance = false;
	}
	else if(entry->balance == 0)
	{
		entry->balance = -1;
		rebalance = true;
	}
	else // if(entry->balance == +1)
	{
		entry->balance = 0;
		rebalance = false;
	}

	return rebalance;	
}

// ---------------------------------------------------------------------------
bool memtab_balance_right_insert(memtab_entry*& entry)
{
    bool rebalance = false;
	
    if(entry->balance == -1)
    {
        entry->balance = 0;
        rebalance = false;
    }
    else if(entry->balance == 0)
    {
        entry->balance = +1;
        rebalance = true;
    }
    else // if(entry->balance == +1)
    {
        if( entry->right->balance == +1 )
        {
            entry->balance        = 0;
            entry->right->balance = 0;
            
            memtab_rotate_left(entry);
        }
        else
        {
            if(entry->right->left->balance == -1)
            {
                entry->balance        = 0;
                entry->right->balance = +1;
            }
            else if(entry->right->left->balance == 0)
            {
                entry->balance        = 0;
                entry->right->balance = 0;
            }
            else // if(entry->right->left->balance == +1)
            {
                entry->balance        = -1;
                entry->right->balance = 0;
            }
			
            entry->right->left->balance = 0;
			
            memtab_rotate_right(entry->right);
            memtab_rotate_left(entry);
        }
		
        rebalance = false;
    }
    
    return rebalance;
}

// ---------------------------------------------------------------------------
bool memtab_balance_left_remove(memtab_entry*& entry)
{
    bool rebalance = false;
	
    if(entry->balance == -1)
    {
        entry->balance = 0;
        rebalance = true;
    }
    else if(entry->balance == 0)
    {
        entry->balance = +1;
        rebalance = false;
    }
    else // if(entry->balance == +1)
    {
        if(entry->right->balance == -1)
        {
            if(entry->right->left->balance == -1)
            {
                entry->balance        = 0;
                entry->right->balance = +1;
            }
			
            else if(entry->right->left->balance == 0)
            {
                entry->balance        = 0;
                entry->right->balance = 0;
            }
			
            else if(entry->right->left->balance == +1)
            {
                entry->balance        = -1;
                entry->right->balance = 0;
            }
			
            entry->right->left->balance = 0;
			
            memtab_rotate_right(entry->right);
            memtab_rotate_left(entry);
			
            rebalance = true;
        }
        else if(entry->right->balance == 0)
        {
            entry->balance        = +1;
            entry->right->balance = -1;
			
            memtab_rotate_left(entry);
            
            rebalance = false;
        }
        else // if(entry->right->balance == +1)
        {
            entry->balance        = 0;
            entry->right->balance = 0;
			
            memtab_rotate_left(entry);
            
            rebalance = true;
        }
    }
    
    return rebalance;
}

// ---------------------------------------------------------------------------
bool memtab_balance_right_remove(memtab_entry*& entry)
{
    bool rebalance = false;
	
    if(entry->balance == -1)
    {
        if(entry->left->balance == -1)
        {
            entry->balance       = 0;
            entry->left->balance = 0;
			
            memtab_rotate_right(entry);
            
            rebalance = true;
        }
        else if(entry->left->balance == 0)
        {
            entry->balance       = -1;
            entry->left->balance = +1;
            
            memtab_rotate_right(entry);
            
            rebalance = false;
        }
        else // if(entry->left->balance == +1)
        {
            if(entry->left->right->balance == -1)
            {
                entry->balance       = +1;
                entry->left->balance = 0;
            }
            else if(entry->left->right->balance == 0)
            {
                entry->balance       = 0;
                entry->left->balance = 0;
            }
            else // if(entry->left->right->balance == +1)
            {
                entry->balance       = 0;
                entry->left->balance = -1;
            }
			
            entry->left->right->balance = 0;
			
            memtab_rotate_left(entry->left);
            memtab_rotate_right(entry);
			
            rebalance = true;
        }
    }
    else if(entry->balance == 0)
    {
        entry->balance = -1;
        rebalance = false;
    }
    else // if(entry->balance == +1)
    {
        entry->balance = 0;
        rebalance = true;
    }
    
    return rebalance;
}

// ---------------------------------------------------------------------------
void memtab_rotate_left(memtab_entry*& entry)
{
	memtab_entry* temp = entry;
	entry = temp->right;
	temp->right = entry->left;
	entry->left = temp;
	
	entry->parent = temp->parent;
	temp->parent = entry;
	if(temp->right)
		temp->right->parent = entry->left;
}

// ---------------------------------------------------------------------------
void memtab_rotate_right(memtab_entry*& entry)
{
	memtab_entry* temp = entry;
	entry = temp->left;
	temp->left = entry->right;
	entry->right = temp;
	
	entry->parent = temp->parent;
	temp->parent = entry;
	if(temp->left)
		temp->left->parent = entry->right;
}

} // namespace Dereferee
