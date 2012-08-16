#=============================================================================
# python _plugin.import_utilities.py
#
# This module provides utility functions that allow adding implicit
# imports to specific Python modules.
#
# author:  Stephen Edwards
# author:  Last changed by $Author: stedwar2 $
# version: $Revision: 1.1 $, $Date: 2012/01/28 22:55:24 $
#=============================================================================

import types

#-----------------------------------------------------------------------------
def add_media_to(module):
    """Adds the effect of "from media import *" to the specified module.

    The method ensures that the module has been loaded and modifies the
    module symbol table by adding all of the exported definitions from
    the module "media" to it.

    Args:
        module:  A module (either a module object, or a string containing
                 a module's fully qualified dotted name)
    """
    add_module_to_module('media', module)


#-----------------------------------------------------------------------------
def add_module_to_module(module_to_add, target_module, names = None):
    """Adds the effect of "from <module_to_add> import <names>" to the target.

    The method ensures that both modules have been loaded and modifies the
    symbol table for the target module by adding all of the exported
    definitions (listed in names) from the specified module to add.

    Args:
        module_to_add:  A module (either a module object, or a string
            containing a module's fully qualified dotted name) identifying the
            thing to import.
        target_module:  A module (either a module object, or a string
            containing a module's fully qualified dotted name) identifying
            where to add the imports.
        names: A list of string names defining what to import.  If None,
            all names from the first module will be added, except for those
            beginning with __ (like "from <module_to_add> import *").
    """

    # Turn strings into modules, if necessary
    if type(module_to_add) != types.ModuleType:
        return add_module_to_module(__import__(module_to_add), target_module)
    if type(target_module) != types.ModuleType:
        return add_module_to_module(module_to_add, __import__(target_module))

    if names == None:
        names = [name for name in module_to_add.__dict__ if name[:2] != '__']

    for name in names:
        if name not in target_module.__dict__:
            target_module.__dict__[name] = module_to_add.__dict__[name]
