/*************************************/
/*           Betty Makovoz           */
/*    Simulating Call By Reference   */
/*            28 Minutes             */
/*************************************/

# include <stdio.h>

void swap ( int *a , int *b);
int main (int argc , char*argv []) {

  int x;
  int y;

  printf ("Enter two numbers:\n");

  scanf("%d %d" , &x , &y);

  swap( &x , &y);

  printf("%d %d\n", x , y);

  return 0;

}

void swap ( int *a , int *b){

  int temp;

  temp = *a ;
  *a = *b ;
  *b = temp;

  return ;

}
