/****************************/
/*Name: Min Thet Khine      */
/*                          */
/*Program: Square Deal      */
/*                          */
/*Approximate time : 1 hour */
/****************************/
#include<stdio.h>
#include <stdlib.h>
/* if the num is prime it  returns 1, or else it will return 0   */
int IsPrime (int initial_value);

int main (int argc, char* argv[]){
  int N;
  int initial_value;
  int** array;
  int i = 0;
  int j = 0;
  int k = 2;
  int l = 0;
  /* prompt user for input */
  printf ("Please enter an odd integer (3 to 15)");
  scanf ("%d", &N);
  printf ("Please enter an integer as initial value: ");
  scanf ("%d", &initial_value);

  /* creat two dimentional array */
  array = (int**)(malloc (sizeof(int*)*N));
  for(i = 0; i < N; i++ )
    array[i]= (int*)(malloc(sizeof(int)*N));

  i = (N-1)/2 ;
  j = i;
  /* assign num to middle cell */
  array[i][j] = initial_value;
  initial_value++;

  while ( 1 ){
    /* going up */
    j++;
    for ( l = 0; l < k; l++, i--, initial_value++ )
      array[i][j] = initial_value;
    i++;
    j--;
    /* going left */
    for ( l = 0; l < k; l++, j--,initial_value++)
      array[i][j] = initial_value;
    i++;
    j++;
    /* going down */
    for ( l = 0; l < k ; l++, i++, initial_value++)
      array[i][j] = initial_value;
    j++;
    i--;
    /* going right */
    for (l = 0; l < k; l++, j++,initial_value++)
      array[i][j] = initial_value;
    j--;
    k += 2;
    /* loop stops when it is on last cell */
    if ((j+1)*(i+1)==N*N)
      break;
  }

  /* print out on the page */
  for (i = 0; i < N; i++){
    for (j=0; j<N; j++)
      if (IsPrime(array[i][j]))
        printf (" %-3d", array[i][j]);
      else
        printf ("*** ");
    putchar ('\n');
  }
  for (i = 0; i < N; i++)
    free (array[i]);
  free (array);
  return 0;

}

/* it takes a number, and returns 1 if it is prime; or returns 0*/
int IsPrime (int initial_value){
  int count = 2;
  int divisor = 2;

  for (divisor = 2; divisor < initial_value; divisor++)
    if (initial_value % divisor == 0)
      count ++;
  if (count == 2 )
    return 1;
  return 0;
}

