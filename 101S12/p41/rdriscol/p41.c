/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: P41 Malloc Up Space    */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/
#include <stdio.h>
#include <stdlib.h>

void *malloc();

int main( int argc, char *agrv[]){

	int *variable;
	int sum;
	int i,n;
	
	printf( "Please enter any number here:");
	scanf( "%d", &n );
	
	variable = (int *) malloc( n * sizeof(int));			       
        *variable = variable [i];
	
	for( i = 0; i < n ; i++){
		
		printf( "Please enter another number:" );
	
		while(scanf( "%d", &variable[i])!=EOF){
			for( i = 0; i < n ; i++){
				sum += variable[i];
			}

			printf( "The sum is: %d",sum );
			
			free(variable);
		}	
	}
	return 0;
}		      
