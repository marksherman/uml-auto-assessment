/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program 33: Recursive Factorial                       */
/*                                                       */
/* Approximate completion time: 15 minutes               */
/*********************************************************/
#include <stdio.h>
int RecursiveFact(int a);
int main(int argc, char* argv[])
{
  int fact, answer;
  printf("Input a 0 or a positive number: ");
  /*Ask for input*/
  scanf("%d", &fact);
  /*Read input and store into variable fact*/
  answer = RecursiveFact(fact);
  /*Call the function to find the factorial of the integer and store it into answer*/
  printf("The computed factorial of the integer is %d.\n", answer);
  /*Print out computed factorial*/
  return 0;
}
int RecursiveFact(int a)
{
  int ans;
  if(a == 0)
    {
      ans = 1;
    }
    /*If int is 0, the factorial will be 1.*/
  else 
    ans = a * RecursiveFact(a - 1);
  /*Calls upon the function again to compute full factorial*/
  return ans;
}
