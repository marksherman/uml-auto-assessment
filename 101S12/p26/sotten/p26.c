/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 26: 1-D Array           */
/*                                */
/*Approx. Completion Time: 25mins */
/*                                */
/**********************************/

#include <stdio.h>

int main(int argc, char*argv[]){
  int sam[15]; /*array length 15 named sam of type int*/ 
  int a;
  FILE *blah;
  blah=fopen("testdata26","r"); /* read file testdata26*/
  for (a=1;a<15;a++){ 
    fscanf(blah,"%d", &sam[a]);/*scanf the address of sam at the specified element i*/
  }
  for(a=14;a>0;a--){ /*accounts for the fact that an array starts at the 0th element*/
    printf("%d\n",sam[a]);
  }
  fclose (blah);
  return 0;
}

