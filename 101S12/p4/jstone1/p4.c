
/****************************************************/
/*                                                  */
/* Programmer: Josh Stone                           */
/*                                                  */
/* Program: P4.c - The fscanf function              */
/*                                                  */
/* Approx. Completion Time:  25 mins.               */
/*                                                  */
/****************************************************/

#include <stdio.h>

int main ()
{
    FILE* fin;
    int value;

    fin = fopen("testdata4","r");
    fscanf(fin,"%d",&value);
    fclose(fin);
    printf("%d\n",value);

         return 0;
}
