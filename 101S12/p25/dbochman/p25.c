/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 25:   Unfilled Box                           */
/* Time:         25 minutes                             */
/********************************************************/
#include <stdio.h>
int main() 
{
  int w, h, i, j, f;
  printf("Insert two numbers between 1 and 20:\n");
  scanf("%d %d", &h, &w);
  if ((w >= 1)&&(w <=20)&&(h >= 1)&&(h <=20))
    {
      i = 0;
      while(i < w)
        {
	  i++;
	  j = 0;
	  while(j <= h)
	    {       
	      if (((j > 1)&&(j < h))&&((i > 1)&&(i < w))) 
		{
		  printf(" "); 
		}
	      else if((j !=0 )&&(i!=0))
		{
		  printf("*");
		} 
	      j++;
	    }
	  printf("\n");           
        }
    }
  else
    printf("the number must be between 1 and 20.\n");
  return 0;
}
