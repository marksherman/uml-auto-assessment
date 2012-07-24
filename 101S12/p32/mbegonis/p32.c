/************************************************************************************/
/*                                                                                  */
/*  Mike Begonis                                                                    */
/*  Program p32                                                                     */
/*                                                                                  */
/*  This program takes in an integer value through standard input, computs the      */
/*  factorial of that integer, and prints the value to the screen.                  */
/*                                                                                  */
/*  Approx Completion Time: 15 minutes                                              */
/************************************************************************************/


#include <stdio.h>

int puppy(int *apples);

int main(int argc, char* argv[]){
  
  int x, fact;

  printf("Please enter an integer greater than or equal to 0: ");
  scanf("%d",&x);                                                      /* Scanf reads the integer that will be factorialized  */

  fact=puppy(&x);
 
  printf("The factorial sum of !%d is %d\n", x, fact);
  
  return 0;
}

/* Function puppy takes in the integer value and computes the factorial  */
int puppy(int *apples){
  
  int i, num=1;

  if(*apples==0){
    return 1;
  }

  for(i=1;i<=*apples;i++){
    num=num*i;
  }

  return num;
}


