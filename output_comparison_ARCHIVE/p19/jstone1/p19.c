

/********************************************/
/*                                          */
/* Programmer: Josh Stone                   */
/*                                          */
/* Program: P19 - ARGV                      */
/*                                          */
/* Approx. completion time: 25 mins.        */
/*                                          */
/********************************************/



#include <stdio.h>


int main(int argc, char* argv []){

    int i;

    for(i = 0; i < argc; i ++){
 
       printf("%s\n",argv[i]);
  

    }

    return 0;

}
