/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 26: One Dimensional Array       */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  int i;
  int array[15];
  FILE *testdata26;

  testdata26 = fopen( "testdata26", "r" );

  for( i = 0; i <= 15 ; i++){ 
    fscanf( testdata26, "%d", &array[i]);
  }
  for(i = 14; i >= 0; i--){
    printf( "%d ", array[i]);
  }
  printf("\n");
  fclose(testdata26);
  
  return 0;
}
   
