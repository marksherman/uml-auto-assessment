/***************************/
/* Danny Packard           */
/* p42 malloc 2-d array    */
/* 3 hours                 */
/***************************/
#include<stdio.h>
#include<stdlib.h>
int main(int argc, char* argv[]){
  int r;
  printf("eneter how many rows\n");
  scanf("%d",&r);
  int c;
  printf("enter how many columns\n");
  scanf("%d",&c);
  int a[r][c];
  int *y;
  int sum=0;
  int i;
  int j;
  printf("enter the array \n");
  y=(int *)malloc(r*c*sizeof(int));
  for(i=0;i<r;i++){
    for(j=0;j<c;j++){
      scanf("%d",a[i*r+j]);
    }
  }
  printf("enter what row you want summed\n");
  int s;
  int rowsum=0;
  scanf("%d",&s);
  for(i=0;i<=r-1;i++){ 
   rowsum+=*a[s*r+i];
  }
  printf("%d\n",rowsum);
  printf("enter what column you want summed\n");
  int t;
  int columnsum=0;
  scanf("%d",&t);
  for(i=0;i<=c-1;i++){ 
    columnsum+=*a[i*c+t];
  }
  printf("%d\n",columnsum);
  for(i=0;i<r;i++){
    for(j=0;j<c;j++){
      sum+=*a[i*r+j];
    }
    printf("\n");
  }
  printf("the sum of the array is %d\n",sum);
      free(y);
  return 0;
}
/* This only works if you have the same number of rows as columns and I have   no idea why it won't work for different numbers of rows and columns*/  
