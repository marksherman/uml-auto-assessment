/****************************************/
/* Programmer: Thearisatya Thok         */
/*                                      */
/* Program 10: Sum of Twenty            */
/*                                      */
/* Approximate completion time: 1 hour  */
/****************************************/         

#include<stdio.h>

int  main()
  {
    int num, sum = 0;
    FILE *fin;
    fin = fopen( "testdata10", "r"); 
	while(fscanf(fin, "%d", &num) != EOF )
	  {
	    sum = sum + num;
	  }
   printf ("%d\n", sum);
   fclose ( fin );
   return 0;
  }
