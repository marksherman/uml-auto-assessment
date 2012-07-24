/*******************************************/
/* Danny Packard                           */
/* p43 square deal                         */
/* long enough                             */
/*******************************************/
#include <stdio.h>
int prime(int x);
int main(int argc, char*argv[]){
  int n;
  int i; 
  int j;
  int k;
  int a[n][n];
  printf("enter the size of the array:\n");
  scanf( "%d", &n ) ;
  printf("enter the intial value:\n");
  scanf("%d",&k);
  for(i=0;i<n;i++){
    for(j=0;j<n;j++){
      if(prime(k++)==1)/*need k++ otherwise k won't change*/
	    printf("%d ",k-1);/*need this minus one to counter act the k++*/
	  else
	    printf( "*** ");
	}
      printf("\n");
      a[j][k] = a[j+1][k+1];
    }
  return 0;
}
int prime(int x){
  int i;
  int c=0;
  for(i=1;i<=x;i++ ){
      if((x%i)==0) 
       c++;
    }
  return (c==2);
}
/* I have no idea how to make it sprial out like it's suppose too, I can do it  this way though which is better than nothing*/
