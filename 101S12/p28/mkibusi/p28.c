/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program :				      */
/* 	   				      */
/* Approximate completion time :              */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc,char* argv[] ) {
  FILE* fin;
  int sum,j, i,n, y, z; 
    
    fin = fopen(argv[1], "r");
      char x;
      for(i = 1;(x = fgetc(fin)) != EOF ; i++){
        putchar(x);
     
      }
      
      fclose(fin);

      for(j = 0; j <= argc  ; j++){
          y = x % 10;

        printf("The number %c \n", x);
        }

      printf("The sum %d \n", y );

   return 0;
}
                                                                                                                                                                     
