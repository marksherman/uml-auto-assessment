/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name :(p30)Simulating Call by Reference */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include <stdio.h>
void swap(int *a, int *b);      /*creates the function 'swap'  */
int main(int argc, char *argv[]){
  int x, y;                      /* declares two integer types x and y */
  x= atoi(argv[1]);              
  y= atoi(argv[2]);
  swap(&x, &y);                 /* swaps the address of x and y */
  return 0;

}
void swap(int *a, int *b){     /* calls the swap function */
  int temp;                    /* declares 'temp' which is of type integer */
  temp= *a;                    /* temp equals the pointer to a   */
  *a= *b;                      /* the pointer to a equals the pointer to b */
  *b= temp;                    /* temp is stored in the pointer to b */
  printf("The respective values of a and b are now %d and %d.\n", *a, *b);
  /* prints out the new values of a and b which are swapped  */

  return ;
}
