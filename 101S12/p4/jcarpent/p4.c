/*************************************************************************/
/* Programmer: John Carpenter                                            */
/*                                                                       */
/* Program 4: The fscanf Function                                        */
/*                                                                       */
/* Approximate completion time: 25 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main(){

	int x;
	
	FILE* fin;
	
	fin = fopen( "testdata4", "r");

	fscanf( fin, "%d", &x );
	
      	fclose( fin );

	printf( "The integer in my file is %d.\n", x );

return 0;

}
