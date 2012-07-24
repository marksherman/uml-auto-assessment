/********************************/
/* Danny Packard                */
/* p39 Recursive Persistence    */
/* Long Time                    */
/********************************/
#include<stdio.h>
int persistence(int n,int m);
int counter(int z,int w);
void scan(int x,int y);
int  main(int argc, char*argv[]){
  int p;
  int q=0;
  scan(p,q);
    return 0;
}
void scan(x,y){
  if((scanf("%d",&x)!=EOF)){
    printf("%d\n",counter(x,y));
    scan(x,y);
  }
  else  if(scanf("%d",&x)==EOF){
    printf("you entered EOF");
    printf("\n");
  }
  return;
}
int persistence(int n,int m){
  int pers=1;
  if(n>0){
    pers*=n%10;
    n=n/10;
    return pers*=persistence(n,m);
  }
  return pers;
}
int counter(int z, int w){
  if (z<10)
    return w;
  else
    return counter(persistence(z,w),w+1);
}



