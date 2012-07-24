/*************************************************/
/* Programmer: Joe LaMarca                       */
/* Program: Unfilled Box                         */
/* Approximate time of completion: 2 hours       */
/*************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int l;
  int h;
  int x;
  int y;

  printf("What is the length:");
  scanf("%d",&l);

  printf("What is the height:");
  scanf("%d",&h);

  {
  for(x=0;x<l;x++)
    printf("*");
    printf("\n");
  }
  for(y=0;y<h-2;y++){
    printf("*");  
    for(x=0;x<l-2;x++)
      printf(" ");
    printf("*"); 
    printf("\n");
  }
  for(x=0;x<l;x++)
    printf("*");
  printf("\n");

  return 0;
}

