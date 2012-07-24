/****************************************************************/
/* Programmer: Jeremy Krugh                                     */
/*                                                              */
/* Program 4: The fscanf Function                               */
/*                                                              */
/* Approximate completion time: 35 minutes                      */
/****************************************************************/


#include <stdio.h>

int main(){

  int x;

  FILE* fin;

 fin = fopen ("testdata4", "r");
 fscanf(fin,"%d",&x);
 fclose(fin);
  printf("%d\n", x);

 return 0;
}
