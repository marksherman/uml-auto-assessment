/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 20: Reverse commmand line       */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/

#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* argv[])
{

  int i;

  for( i = argc - 1; i >= 0; i--){
    printf("%s\n", argv[i]);
  }
    return 0;

}
