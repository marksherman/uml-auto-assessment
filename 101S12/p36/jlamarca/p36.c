/*******************************************************/
/* Programmer: Joe LaMarca                             */
/* Program: p36 persistence of a number                */
/* Aproximate time of completion: 1.5 hours            */
/*******************************************************/

#include <stdio.h>

int persistence(int a);

int main(int argc, char* argv[]){

  int x;
  int y;

  printf("Enter a number then generate an EOF:");
  while(scanf("%d", &x)!=EOF)
    y=persistence(x);

  printf("The persistence of that number is: %d\n", y);

    return 0;
}

int persistence(int a){

  int count=0;
  int z;

  while(a>9){
    z=1;
    do{
      z*=(a%10);
      a/=10;
    }
    while(a>0);
    a=z;
    count++;
  }

  return count;
}
