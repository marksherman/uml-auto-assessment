/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 21: Scanf returns		      */
/* 	   				      */
/* Approximate completion time :20 min        */
/**********************************************/

#include<stdio.h>

int main(int argc,char* argv[]){
  FILE *fin;
  int x , i;
  fin = fopen("testdata21", "r");

  for( i = 0; (fscanf(fin,"%d",&x)) != EOF; i++){
   
    printf(" %d \n", x);
  }
  fclose(fin);
  return 0;
}
