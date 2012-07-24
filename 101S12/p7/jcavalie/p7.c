/***************/
/*JOhn Cavalieri*/
/*p7 positive, negative or zero?*/
/*8 mins */

#include<stdio.h>
int main(){
  
  int x;
  
  printf("Enter integer value\n");
  scanf("%d", &x);

  if( x > 0){

    printf("The number is greater than zero\n");
  }else if( x < 0){
    
    printf("The number is less than zero\n");
  }else( printf("The number is zero\n"));

  return 0;
 }
