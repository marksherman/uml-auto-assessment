/******************************/
/*        Betty Makovoz       */
/*     Recursive Digit Sum    */
/*        30 Minutes          */
/******************************/

# include <stdio.h>

int digitsum (int x);

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
  if (x<10){
    return x;
  }
    else {
      sum=(x % 10)+ digitsum((x-(x % 10))/10);
    }
  return sum;
  }
