/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 9: Using a for Loop                 */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
int main(){  

  FILE* fin;

  int x, y;

  fin = fopen( "testdata9", "r");

  for( x=0; x<5; x++){

    fscanf( fin, "%d", &y);
    
    printf( "%d", y);

  }

  putchar('\n');

  fclose( fin );

  return 0;
}
