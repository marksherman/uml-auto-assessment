

/*********************************************************/
/*                                                       */
/* Programmer: Josh Stone                                */
/*                                                       */
/* Program: P7 - Positive, Negative, or Zero?            */
/*                                                       */
/* Approx. Completion Time:  15 mins.                    */
/*                                                       */
/*********************************************************/


#include <stdio.h>

int main ()

{
  int value;

printf("Please enter a number:");

scanf("%d",&value);

  if(value < 0)
   
   printf("The number is negative.\n");

  else if(value == 0)

   printf("The number is zero.\n");


  else

    printf("The number is positive.\n");
  
           
        return 0;

}
