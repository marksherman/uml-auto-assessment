
/**************************************************/
/*                                                */
/* Programmer: Josh Stone                         */
/*                                                */
/* Program: P15 - Solid Box of *                  */
/*                                                */
/* Approx. Completion Time: 1 hour                */
/*                                                */
/**************************************************/




#include <stdio.h>


int main (int argc, char* argv []){

    int L;

    int H;

    int i;

    int j;

    printf("Enter Two Numbers:");
  
    scanf("%d",&L);

    scanf("%d",&H);

    for(i = 0 ; i < H ; i++){ 
   
        for(j = 0 ; j < L; j++){
   
             printf("*");
        }
    
        printf("\n");

    } 

 

    return 0;

}
