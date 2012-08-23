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

#include <memory>
#include <dereferee/memtab.h>

// ===========================================================================

namespace Dereferee
{

bool memtab_impl_1(memtab_entry*&, memtab_entry*, memtab_entry*); bool
memtab_impl_2(memtab_entry*&, const void*, bool&, memtab_entry*&); bool
memtab_impl_3(memtab_entry*&); bool memtab_impl_4(memtab_entry*&); bool
memtab_impl_5(memtab_entry*&); bool memtab_impl_6(memtab_entry*&); void
memtab_impl_7(memtab_entry*&); void memtab_impl_8(memtab_entry*&); bool
memtab_impl_9(memtab_entry*, memtab_entry*&, bool&, memtab_entry*&); bool
memtab_impl_10(memtab_entry*, memtab_entry*&, bool&, memtab_entry*&);
memtab_entry* memtab_alloc() { memtab_entry* entry = (memtab_entry*)malloc(
sizeof(memtab_entry)); entry->_1 = entry->_2 = entry->_0 = 0; entry->_3 = 0;
memset(&entry->info, 0, sizeof(mem_info)); entry->info.tag = (unsigned long)~0;
return entry; } void memtab_free(memtab_entry* entry) { if(
entry->info.type_name) free( entry->info.type_name); free(entry); } void
memtab_destroy_table(memtab_entry* entry) { if(entry) { memtab_destroy_table(
entry->_1); memtab_destroy_table(entry->_2); memtab_free(entry); } }
memtab_entry* memtab_find_address(memtab_entry* entry, const void* address) {
if(!entry) return 0; else if(entry->info.address <= address && address <=
(char*)entry->info.address + entry->info.block_size) return entry; else if(
address < entry->info.address) return memtab_find_address(entry->_1, address);
else return memtab_find_address(entry->_2, address); } bool memtab_insert_entry(
memtab_entry*& _a1, memtab_entry* _a2) { return memtab_impl_1(_a1, 0, _a2); }
memtab_entry* memtab_remove_address(memtab_entry*& entry, const void* address) {
memtab_entry* _a4 = 0; bool _a3 = false; memtab_impl_2(entry, address, _a3,
_a4); return _a4; } bool memtab_impl_1(memtab_entry*& _a1, memtab_entry* _a2,
memtab_entry* _a3) { if(!_a1) { _a1 = _a3; _a3->_0 = _a2; return true; } else {
bool _lr; if(_a3->info.address < _a1->info.address) { _lr = memtab_impl_1(
_a1->_1, _a1, _a3); if(_lr) _lr = memtab_impl_3(_a1); } else { _lr =
memtab_impl_1(_a1->_2, _a1, _a3); if(_lr) _lr = memtab_impl_4(_a1); } return
_lr; } } bool memtab_impl_2(memtab_entry*& _a1, const void* _a2, bool& _a3,
memtab_entry*& _a4) { if(!_a1) { _a3 = false; _a4 = 0; return false; } else {
bool _lr = false; if(_a1->info.address <= _a2 && _a2 <= (char*)_a1->info.address
+ _a1->info.block_size) { _a3 = true; if(_a1->_1) { _lr = memtab_impl_10(_a1,
_a1->_1, _a3, _a4); if(_a3 && _lr) _lr = memtab_impl_5(_a1); } else if(_a1->_2)
{ _lr = memtab_impl_9(_a1, _a1->_2, _a3, _a4); if(_a3 && _lr) _lr =
memtab_impl_6(_a1); } else { _a4 = _a1; _a4->_1 = _a4->_2 = _a4->_0 = 0;
_a1 = 0; _lr = true; } } else if(_a2 < _a1->info.address) { _lr =
memtab_impl_2(_a1->_1, _a2, _a3, _a4); if(_lr) _lr = memtab_impl_5(_a1); } else
{ _lr = memtab_impl_2(_a1->_2, _a2, _a3, _a4); if(_lr) _lr = memtab_impl_6(_a1);
} return _lr; } } bool memtab_impl_3(memtab_entry*& _a1) { bool _lr = false;
if(_a1->_3 < 0) { if(_a1->_1->_3 < 0) { _a1->_3 = _a1->_1->_3 = 0;
memtab_impl_8(_a1); } else { if(_a1->_1->_2->_3 < 0) { _a1->_3 = 1; _a1->_1->_3
= 0; } else if(_a1->_1->_2->_3 == 0) { _a1->_3 = _a1->_1->_3 = 0; } else {
_a1->_3 = 0; _a1->_1->_3 = -1; } _a1->_1->_2->_3 = 0; memtab_impl_7(_a1->_1);
memtab_impl_8(_a1); } _lr = false; } else if(_a1->_3 == 0) { _a1->_3 = -1;
_lr = true; } else { _a1->_3 = 0; _lr = false; } return _lr; }
bool memtab_impl_4(memtab_entry*& _a1) { bool _lr = false; if(_a1->_3 < 0) {
_a1->_3 = 0; _lr = false; } else if(_a1->_3 == 0) { _a1->_3 = 1; _lr = true; }
else { if(_a1->_2->_3 == 1) { _a1->_3 = _a1->_2->_3 = 0; memtab_impl_7(_a1); }
else { if(_a1->_2->_1->_3 < 0) { _a1->_3 = 0; _a1->_2->_3 = 1; } else
if(_a1->_2->_1->_3 == 0) { _a1->_3 = _a1->_2->_3 = 0; } else { _a1->_3 = -1;
_a1->_2->_3 = 0; } _a1->_2->_1->_3 = 0; memtab_impl_8(_a1->_2);
memtab_impl_7(_a1); } _lr = false; } return _lr; }
bool memtab_impl_5(memtab_entry*& _a1) { bool _lr = false; if(_a1->_3 < 0) {
_a1->_3 = 0; _lr = true; } else if(_a1->_3 == 0) { _a1->_3 = 1; _lr = false; }
else { if(_a1->_2->_3 < 0) { if(_a1->_2->_1->_3 < 0) { _a1->_3 = 0;
_a1->_2->_3 = 1; } else if(_a1->_2->_1->_3 == 0) { _a1->_3 = _a1->_2->_3 = 0; }
else { _a1->_3 = -1; _a1->_2->_3 = 0; } _a1->_2->_1->_3 = 0;
memtab_impl_8(_a1->_2); memtab_impl_7(_a1); _lr = true; } else
if(_a1->_2->_3 == 0) { _a1->_3 = +1; _a1->_2->_3 = -1; memtab_impl_7(_a1);
_lr = false; } else { _a1->_3 = _a1->_2->_3 = 0; memtab_impl_7(_a1); _lr = true;
} } return _lr; } bool memtab_impl_6(memtab_entry*& _a1) {
bool _lr = false; if(_a1->_3 < 0) { if(_a1->_1->_3 < 0) {
_a1->_3 = _a1->_1->_3 = 0; memtab_impl_8(_a1);_lr = true; } else
if(_a1->_1->_3 == 0) { _a1->_3 = -1; _a1->_1->_3 = 1; memtab_impl_8(_a1);
_lr = false; } else { if(_a1->_1->_2->_3 < 0) { _a1->_3 = 1; _a1->_1->_3 = 0;
} else if(_a1->_1->_2->_3 == 0) { _a1->_3 = _a1->_1->_3 = 0; } else {
_a1->_3 = 0; _a1->_1->_3 = -1; } _a1->_1->_2->_3 = 0; memtab_impl_7(_a1->_1);
memtab_impl_8(_a1); _lr = true; } } else if(_a1->_3 == 0) { _a1->_3 = -1;
_lr = false; } else { _a1->_3 = 0; _lr = true; } return _lr; }
void memtab_impl_7(memtab_entry*& _a1) { memtab_entry* temp = _a1; _a1 =
temp->_2; temp->_2 = _a1->_1; _a1->_1 = temp; _a1->_0 = temp->_0; temp->_0 =
_a1; if(temp->_2) temp->_2->_0 = _a1->_1; } void memtab_impl_8(memtab_entry*&
_a1) { memtab_entry* temp = _a1; _a1 = temp->_1; temp->_1 = _a1->_2; _a1->_2 =
temp; _a1->_0 = temp->_0; temp->_0 = _a1; if(temp->_1) temp->_1->_0 = _a1->_2;
} bool memtab_impl_9(memtab_entry* _a1, memtab_entry*& _a2, bool& _a3,
memtab_entry*& _a4) { bool _lr = true; if(!_a2) _a3 = false; else { if(_a2->_1)
{ _lr = memtab_impl_9(_a1, _a2->_1, _a3, _a4); if(_a3 && _lr)
_lr = memtab_impl_5(_a2); } else { mem_info temp = _a1->info;
_a1->info = _a2->info; _a2->info = temp; _a4 = _a2; _a2 = _a4->_2; if(_a2)
_a2->_0 = _a4->_0; _a4->_1 = _a4->_2 = _a4->_0 = 0; _a3 = true; } } return _lr;	
} bool memtab_impl_10(memtab_entry* _a1, memtab_entry*& _a2, bool& _a3,
memtab_entry*& _a4) { bool _lr = true; if(!_a2) _a3 = false; else { if(_a2->_2)
{ _lr = memtab_impl_10(_a1, _a2->_2, _a3, _a4); if(_a3 && _lr)
_lr = memtab_impl_6(_a2); } else { mem_info temp = _a1->info; _a1->info =
_a2->info; _a2->info = temp; _a4 = _a2; _a2 = _a4->_1; if(_a2) _a2->_0 =
_a4->_0; _a4->_1 = _a4->_2 = _a4->_0 = 0; _a3 = true; } } return _lr; }

} // namespace Dereferee
