/*************************************************/
/*Programmer: Joanna Sutton                      */
/*                                               */
/*Assignment: The fscanf Function                */
/*                                               */
/*Approximate Completion Time: 15 minutes        */
/*************************************************/

#include <stdio.h>
int main(){
 FILE *fin;
 int x;

  fin=fopen("testdata4", "r");
  fscanf(fin,"%d",&x);
  printf("%d\n",x);
  fclose(fin);

 return 0;
}
