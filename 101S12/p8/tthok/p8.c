/************************************************/
/*                                              */
/* Programmer: Thearisatya Thok                 */      
/*                                              */
/* Program 8: One Horizontal Line of Asterisks  */
/*                                              */
/* Approximate completion time: 1 hour          */
/*                                              */
/************************************************/
           
#include<stdio.h>
int  main()
  {
    int num, i;
    FILE *fin;
    fin = fopen( "testdata8", "r");      
    while (fscanf(fin, "%d", &num) != EOF )
	  {
	    for (i=0; i<num; i++){
	    printf ("*");
	    }
	    printf ("\n");
	  } 
   fclose ( fin );
   return 0;
  }
