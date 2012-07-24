/************************************/
/*                                  */
/*     Programmer: Chris Leger      */
/*                                  */
/*     Title: The scanf Function    */
/*                                  */
/*     Time to Completion: 15 mins  */
/*                                  */
/************************************/

#include<stdio.h>
int main()
{
  int num;
    
    printf("Enter any whole number:");
    
    scanf("%d",&num);
          
    printf("The number you entered was:\n %d \n",num);
    return 0;
}
