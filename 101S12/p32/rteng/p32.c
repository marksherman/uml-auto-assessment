/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program 32: Non-Recursive Factorial                   */
/*                                                       */
/* Approximate completion time: 20 minutes               */
/*********************************************************/
#include <stdio.h>
int NonRecursiveFact(int a);
int main(int argc, char* argv[])
{
  int fact, answer;
  printf("Input a 0 or a positive number: ");
  /*Ask for input*/
  scanf("%d", &fact);
  /*Read input and store into variable fact*/
  answer = NonRecursiveFact(fact);
  /*Call the function to find the factorial of the integer and store it into answer*/
  printf("The computed factorial of the integer is %d.\n", answer);
  /*Print out computed factorial*/
  return 0;
}
int NonRecursiveFact(int a)
{
  int ans = 1, i;
  for(i = 1; i <= a; i++)
    /*Will loop 'a' times.*/
    {
      if(a == 0)
	/*If int is 0, the factorial will be 1.*/
	{
	  ans = 1;
	  break;
	}
      ans = ans * i;
      /*Loops to multiply the integer by (integer - 1)*/
    }
  return ans;
}
