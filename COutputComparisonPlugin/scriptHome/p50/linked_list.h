/****************************************************************/
/* Programmer: James W. DeFilippo                               */
/*                                                              */
/* linked_list.h: Provides prototypes for linked list tools     */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T                            */
/*                                                              */
/****************************************************************/


#ifndef linkedlist_h 

#define linkedlist_h

typedef float DATA_T; 


typedef struct NODE { 
	DATA_T       data;
	struct NODE* next;
} NODE;

NODE* make_node(   DATA_T new_data, NODE* new_next ); 
NODE* insert_node( DATA_T new_data, NODE* start    );
void  free_list(   NODE*  start   );

#endif
