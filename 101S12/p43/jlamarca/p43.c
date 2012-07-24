/*********************************************************/
/* Programmer: Joe LaMarca                               */
/* Program: Square Deal                                  */
/* Approximate time of completion: 4 hours               */
/*********************************************************/

#include <stdio.h>
#include <stdlib.h>

int prime(int a);

int main(int argc, char* argv[]){

  int n,initial_value;
  int z;
  int x;
  int i;
  int y;

  printf("Input the array size:");
  scanf("%d", &n);
  
  printf("Input the initial value:");
  scanf("%d", &initial_value);

  x=((n-1)*2);
  z=(n*n)+ (initial_value-1);
  z=z-x;

  for(i=0; i<n; i++)
    printf(" %d", prime(z-i));

  x=(n-1);
  printf("\n %d",prime(z+1));    
  y=initial_value + x;
  for(i=0; i<(n-2); i++)
    printf(" %d", prime(y-i));
  printf(" %d", prime(z-(n+1)));

  printf("\n");
  printf(" %d", prime(z+2));
  printf(" %d", prime(initial_value+n));
  printf(" %d", prime(initial_value));
  printf(" %d", prime(initial_value+1));
  printf(" %d", prime(z-n));

  printf("\n");
  
  printf(" %d", prime(z+3));
  printf(" %d", prime(initial_value+(n+1)));
  printf(" %d", prime(initial_value+(n+2)));
  printf(" %d", prime(initial_value+(n+3)));
  printf(" %d", prime(initial_value+(n+4)));

  printf("\n");

  y=z+x;
  for(i=0; i<=x; i++)
    printf(" %d", prime(y+i));

  printf("\n");


  return 0;
}

int prime(int a){

  int i=0;

  for(i=2; i<a; i++){
    if(a%i==0 && i!=a)
      return 00;
  }

  return a;
}
