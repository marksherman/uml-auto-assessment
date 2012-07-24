/****************************/
/* Danny Packard            */
/* p37 digitsum             */
/* 25 minutes               */
/****************************/
#include<stdio.h>
int digitsum(int y);
int main(int argc, char*argv[]){
  int x;
  FILE*fin;
  fin=fopen(argv[1],"r");
  while(fscanf(fin,"%d",&x)!=EOF){
    printf("%d\n",digitsum(x));
  }
  fclose(fin);
  return 0;
}
int digitsum(int y){
  int sum=0;
  while(y>0){
    sum+=y%10;
    y=y/10;
}
  return sum;
}

