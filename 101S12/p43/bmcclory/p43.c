/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #43: Square Deal                              */
/*                                                       */
/* Approximate Completion Time: Several Hours ...        */
/*********************************************************/

/* Actions for moving in the spiral */

#define SPRL_UP 1
#define SPRL_DOWN -1
#define SPRL_RIGHT -1
#define SPRL_LEFT 1

/* What's actually happening as we move through the array */

#define MV_UP -5
#define MV_DOWN 5
#define MV_RIGHT 1
#define MV_LEFT -1

/* Actions for isPrime function */

#define NOTPRIME -1
#define PRIME '***'

#include <stdio.h>
#include <stdlib.h>

void fillBox(int startingValue, int num);
int isPrime(int num, int x);
int printBox();

int main(int argc, char* argv[]){

  int n;
  int startingValue;

  int* grid;

  printf("Type the size of the box: ");
  scanf("%d", &n);

  printf("Type the value you want to start at: ");
  scanf("%d", &startingValue);

  grid = (int*)malloc(n * n * sizeof(int));

  void fillBox(int startingValue, int n);

  printf("%d", grid);

  free(grid);

  return 0;
}

void fillBox(int startingValue, int num){ /* fills the box up and calls isPrime. Does not need to return anything  */

  int i, j;
  int tmp;

  int stopValue = (num * num);
  
  for(i = startingValue; i <= stopValue; i++){
    for(j = 0; j <= num; j++){
      tmp = (grid + j); /* How can I use pass by reference with the grid that is in main? */
    }
  }

  int isPrime(int startingValue, int stopValue);

  return;
}

int isPrime(int startingValue, int stopValue){

  int num;

  int x = 1;

  num = &startingValue;

  if(x == num){
    return num;
  }
  else if(num % x == 0){
    return NOTPRIME;
  }
  else{
    x += 1;
    int isPrime(int num, int x);
  }
}
