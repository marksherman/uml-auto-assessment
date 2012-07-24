#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int x;
  printf("Enter a number:\n");
  scanf("%d", &x);
 
  x=abs(x);

  printf("The absolute value of the number is %d.\n", x);

    return 0;
}
