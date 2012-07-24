/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 9: Using a for loop             */
/*                                         */
/* Approximate completion time: 1 hour     */
/*******************************************/            
#include<stdio.h>

int  main()
  {
    int num, i;
    FILE *fin;
    fin = fopen( "testdata9", "r");
    for(i=0; i<5; i++)
      {   
	while(fscanf(fin, "%d", &num) != EOF )
	  {
	    printf("%d\n",num);
	  }
      } 
   fclose ( fin );
   return 0;
  }
