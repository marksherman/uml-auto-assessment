/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 43: Square Deal                                        */
/* Approx Completion Time: 2 hours                                */
/******************************************************************/

#include<stdio.h>

int primetest(int n);
int main(int argv, char* argc[]){
  
  int n,i,x,y,s,ans;
  int a=1;
  int j=0;
  int k=0;
  int arr[225][225];  
  
  printf( "Please enter an odd integer between 3 and 15: " );
    scanf("%d", &n);
  printf("Please enter an initial value: ");
    scanf("%d", &i);
  printf("\n");

/*Fill the Array*/
  y=n/2;
  x=n/2;

  while(a<n*n){
  /*right*/
    for(s=0;s<a;s++,x++,i++)
      arr[y][x]=i;
  /*up*/
    for(s=0;s<a;s++,y--,i++)
      arr[y][x]=i;
      a++;
  /*left*/
      for(s=0;s<a;s++,x--,i++)
      arr[y][x]=i;
  /*down*/
      for(s=0;s<a;s++,y++,i++)
      arr[y][x]=i;
      a++;
  }

/*Print the Array*/
  for(j=0;j<n;j++){
    for(k=0;k<n;k++){
      ans=primetest(arr[j][k]);
      if(ans==0){
        printf("%8d",arr[j][k]);
      }
        else{
          printf("%8s","***");
        }
      i++;
    }
    printf("\n");
  }

  return 0;
}

int primetest(int n){

  int i=1;
  int count=0;

  for(i=1;i<=n;i++){
    if((n%i)==0){
      count++;
    }
  }
  if(count==2){
    return 0;
  }
  else{
    return 1;
  }
}

