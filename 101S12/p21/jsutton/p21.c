/******************************************/
/* Programmer: Joanna Sutton              */
/*                                        */
/* Assignment: Scanf returns what?        */
/*                                        */
/* Approximate Completion Time:15 minutes */
/******************************************/

#include <stdio.h>

int main (int argc, char*argv []){
  FILE *integers;
  int x;
  integers=fopen("testdata21","r");

  while(fscanf(integers,"%d",&x)!=EOF){
    printf("%d\n",x);
  }

  fclose(integers);

  return 0;
}
