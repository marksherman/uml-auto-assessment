/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 36: Persistence of Number       */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int persistence(int x);
int main (int argc, char *argv[])
{

  int x;
  int value = 0;
  
  printf("Please Enter An integer\n");
  scanf("%d", &x);

  value = persistence(x);
  printf("The persistence of %d is %d \n", x, value);
  
  return 0;

}

int persistence( int x)
{
  int count = 0;
  int y;
  
  while( x > 9 )
    {
      y = 1;
      do
	{
	  y *= (x % 10);
	  x /= 10;
	}
      while( x > 0);
      x = y;
      ++count;
    }

  return count;
}


    
  
