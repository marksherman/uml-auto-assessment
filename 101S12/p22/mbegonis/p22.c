/********************************************************************************/
/*                                                                              */
/*  Mike Begonis                                                                */
/*  Program p22                                                                 */
/*                                                                              */
/*  This program reads an unknown number of integer values from a file and      */
/*  then adds them together and prints their sum to the screen.                 */
/*                                                                              */
/*  Approx Completion Time: 5 minutes                                           */
/********************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

    int x,y=0;
    FILE *fin;
  
    fin = fopen("testdata22","r");
    while(fscanf(fin, "%d", &x)!=EOF){
        y=y+x;
    }
    printf("%d\n",y);
    
    fclose(fin);
	   
    return 0;
}
