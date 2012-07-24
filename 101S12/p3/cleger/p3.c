/**********************************/
/*                                */
/*     Programmer: Chris Leger    */
/*                                */
/*     Title:Sum of Two Values    */
/*                                */
/*     Time to Completion:15 mins */
/*                                */
/**********************************/


#include<stdio.h>
int main()
{
  int n1,n2;
  
  int sum;
  
  printf("Enter two integers seperated by a space:");
  
  scanf("%d %d",&n1,&n2);
  
  sum = n1 + n2;
  
  printf("The Sum of the two integers you entered is: \n %d \n",sum);
  
  return(0);
}
