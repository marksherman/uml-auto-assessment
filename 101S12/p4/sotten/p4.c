/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/* Program 4: The fscanf Function */
/*                                */
/*Approx. Completion Time: 25mins */
/*                                */
/**********************************/

#include <stdio.h>

int d;

FILE* fin;

 fin=fopen("testdata4","r");

 fscanf(fin,"%d",&d);

 fclose(fin)
 
 printf("The Interger Value located in the file testdata4 is:%d\n",d);

 return 0;
}

