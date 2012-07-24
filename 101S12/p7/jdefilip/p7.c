/********************************/
/* Author: James DeFilippo      */
/* Title :                       */
/* Approximate Time:            */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  int x; /* declare some integer x such that an address is initialized in memory for scanf to read into */
  printf("Hi! Please enter a number.\n");  /* prompts the user for a meaningful value */
  scanf( "%d", &x ); /* reads from standard input for some decimal value to put in the address of integer x */
  if (x == 0) 
    printf("The number is zero.\n"); /* first conditional expression */
  else if (x >= 0)
    printf("The number is a positive number.\n"); /* fall-back condition */ 
  else if (x <= 0) /* fall-back condition */ 
    printf("The number is a negative number.\n"); 
  return 0; 
}
