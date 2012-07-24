/*******************************/
/* Danny Packard               */
/* p31 inner product           */
/* longer than it should have  */
/*******************************/
#include<stdio.h>
float inner(float u[], float v[], int size);
int main(int argc, char*argv[]){
  float x[8];
  float y[8];
  int i;
  int j;
  float v;
    for(i=0;i<=7;i++)
  scanf("%f",&x[i]);
    for(j=0;j<=7;j++)
      scanf("%f",&y[j]);
   v=inner(x,y,8);
  printf("%f\n",v);
  return 0;
}
float inner(float u[],float v[],int size){
  int q;
  float z=0;
  for(q=0;q<size;q++)
    z+=u[q]*v[q];
  return z;
  }
