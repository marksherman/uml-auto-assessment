/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 25                                                                   */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int i, c;
  int length = 0;
  int height = 0;
  printf("Please enter lenth: ");
  scanf("%d", &length);
  printf("Please enter height: ");
  scanf("%d", &height);
  for(i = 0 ; i < length ; i++)
    printf("*");
  printf("\n");
  for(i = 0 ; i < (height - 2) ; i++){
    printf("*");
    for(c = 0 ; c < (length - 2) ; c++)
      printf(" ");
    printf("*\n");
  }
  if(height != 1)
    for(i = 0 ; i < length ; i++)
      printf("*");
  printf("\n");
  return 0;
}
