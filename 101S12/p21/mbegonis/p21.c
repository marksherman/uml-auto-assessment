/********************************************************************************/
/*                                                                              */
/*  Mike Begonis                                                                */
/*  Program p21                                                                 */
/*                                                                              */
/*  This program reads an unknown number of integer values from a file and      */
/*  then prints them back onto the screen.                                       */
/*                                                                              */
/*  Approx Completion Time: 5 minutes                                           */
/********************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

    int x;
    FILE *fin;
    
    fin = fopen("testdata21","r");
    
    while (fscanf(fin, "%d", &x) !=EOF){
        printf("%d\n",x);
    }
    fclose(fin);
    
    return 0;
}

