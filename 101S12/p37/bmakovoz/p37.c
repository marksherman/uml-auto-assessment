/****************************************/
/*            Betty Makovoz             */
/*              Digit Sum               */
/*             20 minutes               */
/****************************************/

# include <stdio.h>

int digitsum (int n);

int main (int argc, char*argv[]){
  int x=0;
  FILE*fin;
  fin=fopen( argv [1], "r");
  while((fscanf(fin,"%d",&x)) != EOF){
  printf("%d\n", digitsum(x));
  }
  fclose (fin);
  return 0;
}

int digitsum (int x){
  int sum=0;
  while (x>0){
    sum += x % 10;
    x= x/10;
  }
  return sum;
}
