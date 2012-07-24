/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p36: Persistence               */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int persistence(int y);
int main (int argc, char *argv[])
{

  int x;
  int value = 0;
  printf("Please Enter An integer\n");
  scanf("%d", &x);

  value = persistence(x);
  printf("The persistence number of %d is %d \n", x, value);

  return 0;

}

int persistence( int y ){
  
  int count = 0;

  while( y > 9 )
    {
      int temp = 1;

      do
        {
          temp *= ( y % 10 );
          y /= 10;
        }
      while( y > 0 );

      y = temp;
      ++count;
    }

  return count;
}
