/****************************************************************/
/*                                                              */
/* Programmer: Jeremy Krugh                                     */
/*                                                              */
/* Program 27: Reverse                                          */
/*                                                              */
/* Approximate completion time: 25 minutes                      */
/****************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int reverse[10];
  int i;

  printf("Enter ten numbers and then hit enter, followed by a ctrl+d:");

  for(i = 10; scanf("%d", &reverse[i]) !=EOF; i--);

  printf("The numbers reversed are:");

  for(i = 1; i <= 10; i++)

    printf("%d ", reverse[i]);

  printf("\n");

  return 0;
} 
