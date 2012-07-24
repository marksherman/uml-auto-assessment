/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment: Area of a Rectangle         */
/*                                         */
/* Approximate Completion Time: 10 minutes */
/*******************************************/
#include <stdio.h>
int main (int argc,char* argv[]){

 float x;
 float y;
 
 printf("Please enter the length and width separated by a space:\n");
 scanf("%f%f",&x,&y); 
 y=x*y;
 printf("The area of this rectangle is %f.\n",y);

return 0;

}
