/*************************************************************/
/*Programmer : Min Thet Khine                                */   
/*                                                           */
/*Program name: Malloc up Space for a Two-Dimensional Array  */
/*                                                           */
/*Approximate time : 30 minutes                              */
/*************************************************************/
#include<stdio.h>
#include<stdlib.h>
int main(int argc, char * argv[]){
  int r, c, i, j, a;
  int *array;
  int sum=0;
  printf("Please enter values for r and c");
  scanf("%d %d", &r, &c);
  a= r*c;
  array= (int *)malloc(sizeof(int *) * r * c);

  for(i=0; i<r; i++){
    for(j=0; j<c ; j++){
       
      array[i * c + j] = i + j;
      printf("Add %d integers to fill matrix\n", a );
      scanf("%d", &array[i*c+j]);
      sum += array[i*c+j];
    }
  }
  printf("The sum is %d\n", sum);
  return 0;
}
