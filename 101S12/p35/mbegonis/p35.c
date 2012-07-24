/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p35                                                                               */
/*                                                                                            */
/*  This program reads 9 integer values into a 3x3 two dimentional array through standard     */
/*  and calculate the sum of the values.                                                      */
/*                                                                                            */
/*                                                                                            */
/*  Approx Completion Time: 25 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

int sum(int num[][3]);

int main(int argc, char* argv[]){
  
  int math[3][3],ans,i,j;

  printf("Please enter 9 integers, separating each integer by a space.\n");
  /* This for loop scans the 9 integers into the multidimentional array.  */
  for(i=0;i<3;i++){
    for(j=0;j<3;j++){
      scanf("%d",&math[i][j]);
    }
  }
 
  ans=sum(math);

  printf("The sum of the integers is %d\n",ans);

  return 0;
}
/* Function sum adds together the array math from function main and returns the sum.  */
int sum(int num[][3]){

  int i,j,ans=0;
  /* This for loop takes takes each indivisual cell and adds it to
     the grand total stored in int ans.                            */
  for(i=0;i<3;i++){
    for(j=0;j<3;j++){
      ans+=num[i][j];
    }
  }
  return ans;
}

