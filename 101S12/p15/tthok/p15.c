/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 15 : Solid Box of Asterisks     */
/*                                         */
/* Approximate completion time:120 minutes */
/*******************************************/

#include <stdio.h>
int main()
{
  int L, H;
  int i, j;
     printf("Enter two numbers, L horizontal H vertical: ");
     scanf("%d", &L);
     scanf("%d", &H);

     for(j=0;j<H;j++)
       {
	 for(i=0;i<L;i++)
	   {
	     printf("*");
	   }
	 printf("\n");
       }
 
      return 0;  
}
