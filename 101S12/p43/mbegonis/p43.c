/***********************************************/
/*                                             */
/*  Mike Begonis                               */
/*  Program p43                                */
/*                                             */
/*  This program takes in an odd integer       */
/*  from standard input and creates a multi-   */
/*  dimensional array with lengths and widths  */
/*  given by the odd integer.  Then it accepts */
/*  another integer whitch will be placed in   */
/*  the centermost square in the array.  The   */
/*  program will then move in a spiral placing */
/*  each consecutive number in each square.    */
/*  Then the program will print to the screen  */
/*  the box created.  If the number is prime,  */
/*  it will be printed to the screen.  If it   */
/*  isn't prime, *** will be printed instead.  */
/*                                             */
/*  Approx Completion Time:  3 hours           */
/***********************************************/

#include <stdlib.h>
#include <stdio.h>

/* print prints out the array */
void print(int* array, int n); 
/* prime_detector determines if each number is prime */
int prime_detector(int num); 
/* spiral determines if each number is prime  */
void spiral(int N, int initial_value, int* array);
  
int main(int argc, char* argv[]){
  /*  N is the array width and legnth.
   *  initial_value is the number that will be
   *  at the center of the array.
   */
  int N, initial_value;
  int *array;
  
  printf("Input the array size please: ");
  scanf("%d",&N);
  printf("Input the initial value please: ");
  scanf("%d",&initial_value);
  
  array=(int*)malloc(N*N*sizeof(int));
  
  
  spiral(N, initial_value, array);
  
  print(array, N);
  
  free(array);
  
  return 0;
}

/*  spiral is a void function that stores each
 *  consecutive number after initial_value
 *  into the array. It starts off by calculating
 *  the center square in the array.  Here the 
 *  legnth and width of the array is N.  The 
 *  center square will always be (N*N)/2.  After
 *  the center square has been reached, the for
 *  loops move around in a spiral, and stores
 *  each prime number, and -1 for non prime 
 *  numbers.  The pattern for moving in the spiral
 *  is that it will move right, up, left, down.  
 *  (or positive horizontal, negative vertical,
 *  negative horizontal, positive vertical).
 *  When moving horizontally, the next square is 
 *  always going to be + or - 1.  When moving
 *  vertically the next square is always going to
 *  be + or - N.  The if statement inside spiral
 *  is a failsafe so that when the program fills
 *  the array and tries to store outside of it,
 *  it'll break out of the storage loops.  spiral
 *  calls upon function prime_detector in order
 *  to determine whether each number is prime.  
 */

void spiral(int N, int initial_value, int* array){
  
  int i, j, n, array_size;
  array_size=(N*N);
  n=((N*N)-1)/2;
  --initial_value;
  
  for(i=0;i<N;i++){
    for(j=0;j<=i+i;j++){ 
      array[n]=prime_detector(++initial_value);
      n=n+1;
    }
    if(n==array_size)
      break;
    for(j=0;j<=i+i;j++){ 
      array[n]=prime_detector(++initial_value);
      n=n-N;
    }
    for(j=0;j<=i+i+1;j++){ 
      array[n]=prime_detector(++initial_value);
      n=n-1;
    }
    for(j=0;j<=i+i+1;j++){ 
      array[n]=prime_detector(++initial_value);
      n=n+N;
    }
  }
  return;
}

/*  prime_detector takes in each value stored
 *  in the array and calculates whether its
 *  prime or not.  starting from 2, the function
 *  uses % to determine whether or not the value
 *  is prime.  
 */

int prime_detector(int num){
  
  int i;

  for(i=2;i<num;i++){
    if(num%i==0)
      return -1; /* If number is not prime return -1. */
  }
  
  return num;  /* If number is prime return num. */
}

/*  Function print will print out the box.
 *  If the number is not prime then it will
 *  print out *** followed 4 spaces.  Depending
 *  on if the prime numbers are single, double, or
 *  triple digit, the function will print out extra
 *  spaces to compensate.
 */ 

void print(int* array, int n){
  int i,j=0;
  
  for(i=0;i<(n*n);i++){
    if(j==n){
      printf("\n");
      j=0;
    }
    if(array[i]==-1)
      printf("***    ");
    else if(array[i]<10)
      printf("%d      ",array[i]);
    else if(array[i]<100)
      printf("%d     ",array[i]);
    else
      printf("%d    ",array[i]);
    j++;
  }
  printf("\n");
  
  return;
}


