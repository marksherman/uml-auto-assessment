/**************************/
/* Danny Packard          */
/* p28 digit sum          */
/* 30 minutes             */
/**************************/
#include<stdio.h>
int digitsum();
int main(int argc, char*argv[]){
  int x;
  int z=0;
  FILE*fin;
  fin=fopen(argv[1],"r");
  while(fscanf(fin,"%d",&x)!=EOF){
    z+=digitsum(&x);
    continue;
    printf("%d",z);
  }
  fclose (fin);
  return 0;
}
int digitsum(int a){
  int y=10;
  int b;
  b=a%y;
  return b;
}
/* No idea honestly did not get to this one until tuesday night, I get that I  have to scan the variables in one by one and then have the function digitsum   add them up so 123=6.*/ 
 
  
