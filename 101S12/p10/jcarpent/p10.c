/*************************************************************************/
/* Programmers: John Carpenter                                           */
/*                                                                       */
/* Program 10: Sum of Twenty                                             */
/*                                                                       */
/* Approximate completion time: 30 minutes                               */
/*************************************************************************/

#include <stdio.h>

int main(){  
	
       	int num, sum = 0;

	FILE* fin;

	fin = fopen( "testdata10", "r" );

        while( fscanf( fin, "%d", &num)!= EOF )

		sum = sum + num;

	printf( "The sum of the numbers on file is %d\n", sum );

        fclose( fin );

return 0;

}
