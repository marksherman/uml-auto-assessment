/******************************************/
/* Programmer: Joanna Sutton              */
/*                                        */
/* Assignment: Bigger than 100?           */
/*                                        */
/* Approximate Completion Time: 15minutes */
/******************************************/

#include <stdio.h>
int main(){
  int x;
 
  printf("Please enter a number:\n");
  scanf("%d",&x);
    if(x>100){
      printf("The number is bigger then 100.\n");
    }

    if(x<=100){
      printf("The number is not bigger than 100.\n");
    }
  
return 0;
}
