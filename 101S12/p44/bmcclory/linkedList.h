/****************************************************************/
/* Programmer: Brian McClory                                    */
/*                                                              */
/* linkedList.h: Provides prototypes for linked list tools      */
/* - The last node in the list will have next == NULL           */
/* - The payload data type is DATA_T                            */
/*                                                              */
/****************************************************************/

#ifndef linkedList_h
#define linkedList_h

typedef float DATA_T;

typedef struct NODE {
  DATA_T data;
  struct NODE* next;
} NODE;

NODE* makeNode(DATA_T newData, NODE* newNext);
NODE* insertNode(DATA_T newData, NODE* start);

void freeList(NODE* start);

#endif
