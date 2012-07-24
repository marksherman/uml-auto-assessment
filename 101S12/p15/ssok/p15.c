/*Program: Scott Sok*/

/*Program p15: Solid Box of Asterisks*/

/*Approximate completion time: About 2 hours*/

#include <stdio.h>

int main()
{
  
  int x, y, i, j;
 
  printf("please enter the height and length :\n");
  scanf("%d%d", &x, &y);
  
  for(i = 0; i < x; i++){
    printf("\n");
    
    for(j = 0; j < y; j++)
      printf("*");
  
}
   
    printf("\n");
  
    return 0;
}

