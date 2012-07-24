/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p37                                                                               */
/*                                                                                            */
/*  This program reads a file, designated by the user, and outputs the sum of the             */
/*  indivisual digits.                                                                        */
/*                                                                                            */
/*  Approx Completion Time: 15 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

int magic(int num);

int main(int argc, char* argv[]){
  
  int ans;
  FILE *fin;
  
  fin = fopen(argv[1],"r");
  /* This while loop takes the next integer stored each time it runs.
   * That number is then stored in int ans which is passed on to 
   * function magic, which computes the sums of the digits.
   */
  while(fscanf(fin,"%d",&ans)!=EOF){
    ans=magic(ans);
    printf("%d ",ans);
  }
  fclose(fin);
  
  return 0;
}
/* Function magic takes the integer value stored in int ans from
 * function main and separates its digits.  It then adds the 
 * digits together and returns the sum back to main.
 */
int magic(int num){
  int sum=0;
  
  while(num>0){
    sum+=num%10;
    num=num/10;
  }
  
  return sum;
}
