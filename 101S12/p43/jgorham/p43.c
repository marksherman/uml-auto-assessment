/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 43: Square Deal                                                      */
/*                                                                              */
/* Approximate Completion Time:  60 min                                         */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int** array_build(int n, int** square);
int fill(int current, int* prime);
int** build(int r, int c, int n, int** square, int init);

int main(){
  int** square;
  int init = 0;
  int n = 0;
  int r = 0;
  int c = 0;
  int hcount = 0;
  int wcount = 0;
  printf("Input the array size now: ");
  scanf("%d", &n);
  printf("Input the initial value: ");
  scanf("%d", &init);
  square = array_build(n, square);
  square = build(r, c, n, square, init);
  for(hcount = 0 ; hcount < n ; hcount++){
    for(wcount = 0 ; wcount < n ; wcount++){
      if(square[hcount][wcount] == -1)
	printf("***     ");
      else
	printf("%3d     ", square[hcount][wcount]);
    }
    printf("\n");
  }
  for(r = 0 ; r < n ; r++)
    free(square[r]);
  free(square);
  return 0;
}


int** array_build(int n, int** square){
  int i = 0;
  square = (int**) malloc(n * sizeof(int*));
  for(i = 0 ; i < n ; i++)
    square[i] = (int*) malloc(n * sizeof(int));
  return square;
}

int** build(int r, int c, int n, int** square, int init){
  int current = init;
  int Hbound = (n/2) + 1;
  int Lbound = (n/2) - 1;
  int cell = 0;
  int* prime = &cell;
  int j = 0;
  r = n/2;
  c = n/2;
  if(init > 2){
    for(j = 2 ; j < init ; j++){
      if((init % j) == 0){
	square[n/2][n/2] = -1;
	break;
      }
      else
	square[n/2][n/2] = init;
    }
  }
  else{
    if(init == 2)
      square[n/2][n/2] = init;
    else
      square[n/2][n/2] = -1;
  }
  while(r < (n-1) && c < (n-1)){
    while(c < Hbound){
      c++;
      current = fill(current, prime);
      square[r][c] = cell;
    }
    while(r > Lbound){
      r--;
      current = fill(current, prime);
      square[r][c] = cell;
    }
    while(c > Lbound){
      c--;
      current = fill(current, prime);
      square[r][c] = cell;
    }
    while(r < Hbound){
      r++;
      current = fill(current, prime);
      square[r][c] = cell;
    }
    while(c < Hbound){
      c++;
      current = fill(current, prime);
      square[r][c] = cell;
    }
    Hbound++;
    Lbound--;
  }
  return square;
}

int fill(int current, int* prime){
  int k = 0;
  current++;
  if(current > 2){
    for(k = 2 ; k < current ; k++){
      if((current % k) == 0){
	*prime = -1;
	break;
      }
      else
	*prime = current;
    }
  }
  else
    *prime = current;
  return current;
}
