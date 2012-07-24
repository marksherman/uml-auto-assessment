/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 36:Persistence by recursive	      */
/* 	   				      */
/* Approximate completion time : 45min        */
/**********************************************/
#include <stdio.h>

int pers(int a);

int main(int argc,char* argv[]){  
  int x, b;
  
  printf("Please enter the bunch number for percistence \n");
  scanf("%d", &x);
  
  b = pers(x);         
  printf("The persistence number is %d \n",b);
  
  return 0;
}
int pers(int b){
  int i = 0, num = 1;
  if(b <= 9){                                                                                                                                              
    return i;                                                                                                                                              
  } else{                                                                                                                                                 
    
    if(b == 0)
      return 0;
    else
      return pers(num *((b/10) % 10));  
  }
  return (pers(b), i++);

return i;
}
