/*********************************/
/* Danny Packard                 */
/* p35 two dim. array            */
/* 45 minutes                    */
/*********************************/
#include<stdio.h>
int sum(int i,int j,int array[i][j]){
  int v=0;
  for(i=0;i<3;i++)
    for(j=0;j<3;j++)
      v+=array[i][j];
    return(v/9);
  }
int main(int argc, char*argv[]){
  int x[3][3];
  int q;
  int p;
  int z=0;
  for(q=0;q<3;q++)
    for(p=0;p<3;p++){
      printf("enter a number\n");
      scanf("%d",&x[q][p]);
    }
  z=sum(q,p,x);
  printf("%d\n",z);
  return 0;
}

