/************************************************/
/* Programmer: Joanna Sutton                    */
/*                                              */
/* Assignment: The abs Function                 */
/*                                              */
/* Approximate Completion Time: 10 minutes      */
/************************************************/

#include <stdlib.h>
#include <stdio.h>
int main(int agrc, char* argv[]){
 int x;
 
 printf("Please enter a number:");
 scanf("%d", &x);
 x=abs(x);
 printf("The absolute value of that number is: %d\n",x);

 return 0;
}
