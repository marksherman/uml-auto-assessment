/********************************************************/
/* Programmer: Joe LaMarca                              */
/* Program: p16 count characters                        */
/* Approximate time of completion: 15 min               */
/********************************************************/

#include <stdio.h> 

int main (int argc, char* argv[]){

  int x;
  int num;
  
  while((x=getchar())!=EOF)
    num++;

  printf("The number of characters is:%d\n",num-1);

  return 0;
}
