/****************************************************************/
/* Programmer: MARTIN KIBUSI                                    */
/*                                                              */
/*                                                              */
/*                                                              */
/*                                                              */
/*                                                              */
/****************************************************************/
#include<stdlib.h>

#ifndef linkedlist_h
#define linkedlist_h

typedef float DATA_T;
typedef int NUM;
typedef struct NODE {
	DATA_T       data;
	struct NODE* next;
} NODE;

NODE* make_node(   DATA_T new_data, NODE* new_next );
NODE* insert_node( DATA_T new_data, NODE* start    );
void  free_list(   NODE*  start   );
void forward_start(NODE* ptr);
void reverse_start(NODE* ptr);
#endif
