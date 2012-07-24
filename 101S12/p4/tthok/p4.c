/****************************************/
/* Programmer: Thearisatya Thok         */
/*                                      */
/* Program 4 : The fscanf Function      */
/*                                      */
/* Approximate completion time: 1 hour  */
/****************************************/
#include<stdio.h>

int  main()
  {
    int num;
    FILE *fin;
    fin = fopen( "testdata4", "r");
    while(fscanf(fin, "%d", &num) != EOF )
      {
	printf("%d\n",num);
      } 
   fclose ( fin );
   return 0;
  }
