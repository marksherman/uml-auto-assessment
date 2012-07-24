/***********************************************/
/* Program : Persistence of a Number           */
/*                                             */
/* Programmer : Min Thet Khine                 */
/*                                             */
/* Approximate complement time : 25 mins       */
/***********************************************/

#include <stdio.h>
#include <stdlib.h>
int persistence (int num);
int product (int num);
void output ( void );

int main (int argv, char* argc[])
{  
  /* prompts user to enter an integer*/

  output ();

  return 0;
}
      
int product (int num){   
  if (num <= 9 )        
    return num;
  return num % 10 * product (num / 10);
}      

int persistence (int num ){ /* this function takes an integer, returns the */
  if (num <= 9)             /* persistence of this integer. */
    return 0;
   return 1 + persistence(product (num));
}

void output( void ){
  int num;
  printf ("Please enter an integer (enter EOF to terminate): \n");
  if (scanf("%d",&num) == EOF)
    return ;
  printf ("The persistence number of the number you entered is %d\n", persistence (num));
  output ();
}
