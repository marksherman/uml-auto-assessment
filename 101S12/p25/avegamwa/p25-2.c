/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p25: Unfilled Box              */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int main(int argc, char* argv[])
{
  int l, h, i, j;
  
         
  printf("Insert a number for row (1-21):\n");
  scanf("%d", &l);
 
  printf("Insert a number for column(1-21):\n");
  scanf("%d", &h);

  if ((l >= 1) && (l <= 21) && (h >= 1) && (h <= 21)){

      i = 0;

      while(i < h){
	i++;
	j = 0;
	
	while(j <= l){

	  if (((j > 1) && (j < l)) && ((i > 1) && (i <= l))) printf(" ");
	  
	  else if((j != 0 ) && (i != 0)) printf("*");
	  	  
	  j++;
	}

	printf("\n");
      }
      
      printf("\n");
  }
  else
    printf("Number must be between 1 and 21.\n");

  return 0;
}
