/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program:P30 Call by Reference   */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/

#include <stdio.h>
#include <stdlib.h>

int swap(int *a, int *b);

int main(int argc, char *argv[]){

  int x, y;
  
   x = atoi( argv[1]) ;
   y = atoi( argv[2]) ;
  
  swap(&x,&y);
  printf("%d %d\n",x,y);
  return 0;
}

int swap(int *a, int *b){
  int temp;
  temp = *a;
  *a = *b;
  *b = temp;
  return temp; 
}
