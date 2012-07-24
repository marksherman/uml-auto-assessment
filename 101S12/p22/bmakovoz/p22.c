/************************************/
/*          Betty Makovoz           */
/*         Sum of a Bunch           */
/*           15 Minutes             */
/************************************/

# include <stdio.h>
int main (int argc, char*argv[]){
  int x;
  int sum;
  x=0;
  sum=0;
  FILE*fin;
  fin=fopen("testdata22","r");
  while(fscanf(fin,"%d",&x)!=EOF){
    sum +=x;
  }
  printf("The sum of the values in testdata22 is:%d\n",sum);
  fclose(fin);
  return 0;
}
