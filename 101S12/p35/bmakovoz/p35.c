/************************************/
/*           Betty Makovoz          */
/*  Passing a Two Dimensional Array */
/*            20 Minutes            */
/************************************/

# include <stdio.h>

int sum ( int array[][3], int row, int column );

int main ( int argc, char*argv []){
  int x [3][3];
  int a;
  int b;
  int c;
  for ( a=0 ; a<3 ; a++){
    for ( b=0 ; b<3 ; b++){
      printf(" please enter the number\n");
      scanf("%d",&x[a][b]);
    }
  }
  c = sum(x,a,b);
  printf("The sum is:%d\n",c);
  return 0;
}

int sum ( int array[][3], int row, int column){
  int x=0;
  int i,j;
  for ( i=0; i<3 ; i++)
    for (j=0 ; j<3 ; j++)
      x += array [i][j];
  return x ;
}
