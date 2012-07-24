/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment: One Dimensional Array       */
/*                                         */
/* Approximate Completion Time: 20 minutes */
/*******************************************/

#include <stdio.h>

int main (int argc, char*argv[]){
  int i;
  int integers[15];
  FILE *values;

  values=fopen("testdata26","r");
  
  for(i=15;i>0;i--)
    fscanf(values, "%d", &integers[(i-1)]);
  
  for(i=0;i<15;i++)
    printf("%d ", integers[i]);

  putchar ('\n');

  fclose(values);

  return 0;

}
  
