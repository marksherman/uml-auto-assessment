/********************************************************************/
/* Programmer: Jeremy Krugh                                         */
/*                                                                  */
/* Program 36: Persistence of a Number                              */
/*                                                                  */
/* Approximate completion time: 45 minutes                          */
/********************************************************************/

#include <stdio.h>

int pers(int z);

int main(int argc, char* argv[]){

  int x;
  int y;
 
  printf("Enter a positive integer then enter EOF (crtl-d): ");

  while (scanf("%d", &x) != EOF);

  y = pers(x);

  printf("The persistence of the interger is: %d\n", y);

  return 0;
}

int pers(int z){

  int w;
  int count = 0;

  while(z > 9){
    w = 1;
    do{
      w *= (z%10);
      z /= 10;
    }
    while(z > 0);
    z = w;
    count++;
  }

  return count;
}
