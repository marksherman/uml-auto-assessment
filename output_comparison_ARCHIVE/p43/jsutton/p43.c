/****************************************************************/
/* Programmer: Joanna Sutton                                   */
/*                                                             */
/* Assignment: Square Deal                                     */
/*                                                             */
/* Approximate Completion Time: Over 7 hours, stumped          */
/***************************************************************/
#include <stdio.h>
#include <stdlib.h>
int primecheck(int n);
int main (int argc, char*argv[]){
  int n,i,k,j,l,initial_value,bound,bound2,row,column;
  int** array;
  
  printf("Please enter an odd integer between 3 and 15 followed by an initial value. \n");
  scanf("%d%d", &n,&initial_value);
  bound=initial_value+(n^2)-1;
  bound2=n/2;
  row=n/2+1;
  column=n/2+1;

  array=(int**)malloc(n*sizeof(int*));
  for(l=0;l<n;l++)
    array[l]=(int*)malloc(n*sizeof(int));

  while(initial_value<bound+1){
    for(j=0;j<bound2;j++){
      if(primecheck(initial_value)==1)
	array[row][column]=initial_value;
      else
	array[row][column]=-1;
      if(j==bound2-1)
	break;
      initial_value++;
      column++;
    }
    initial_value++;
    row++;
  
    for(j=0;j<bound2-1;j++){
      if(primecheck(initial_value)==1)
	array[row][column]=initial_value;
      else
	array[row][column]=-1;
      if (j==bound2-2)
	break;
      initial_value++;
      row++;
    }

    initial_value++;
    column--;
    bound2++;

    for(j=0;j<bound2;j++){
      if(primecheck(initial_value)==1)
	array[row][column]=initial_value;
      else
	array[row][column]=-1;
      if(j==bound2-1)
	break;
      initial_value++;
      column--;
    }
    initial_value++;
    row--;
    
    for(j=0;j<bound2-1;j++){
      if(primecheck(initial_value)==1)
	array[row][column]=initial_value;
      else
	array[row][column]=-1;
      if(j==bound-2)
	break;
      initial_value++;
      row--;
    }
    
    initial_value++;
    column++;

  }


  for(i=0;i<row;i++){
    for(k=0;k<column;k++)
      printf("%d",array[i][k]);
    putchar('\n');
  }
  
  return 0;

}

int primecheck(int initial_value){
  int i;
  int count=0;
  
  for(i=2;i<=initial_value;i++)
    if((initial_value%i)==0)
      count ++;

  return(count==1);
}
