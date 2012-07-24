
/*****************************************************/
/*                                                   */
/* Programmer: Josh Stone                            */
/*                                                   */
/* Program: P8 - Horizontal line of *'s              */
/*                                                   */
/* Approx. Completion Time: 35 mins                  */
/*                                                   */
/*****************************************************/



#include <stdio.h>

 int main ( ) {

   int num;
   int i;
   FILE *fin;

   fin = fopen("testdata8","r");

   fscanf(fin,"%d",&num);

   for(i = 0 ; i < num ; i++ ){

     printf("*");

   }

   printf("\n");
   fclose(fin);

   return 0;

 }
