/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 39: Recursive Persistence of a Number                  */
/* Approx Completion Time: Way too long                           */
/******************************************************************/

#include <stdio.h>

int persistence(int num, int pers, int product);
int main( int argc, char* argv [] ){
  
  int x;
  int y;
  int pers=0;
  int product=1;
 
  printf("Enter an integer: ");
  scanf("%d", &x);
    
    if(x !=EOF){
    y=persistence(x, pers, product);
    printf("The persistence of the integer %d you entered was %d\n", x, y);
      return (main(argc, argv));
    }
    
    printf("EOF?? Fine then. Goodbye.\n");
    return 0;
}

int persistence(int num, int pers, int product){
  
  int lastnum;
   
  if(num>0){
    lastnum=num%10;
    product=product*lastnum;
    num=num/10; 
    return (persistence(num, pers, product));
  }  
    else if(num==0 && product>=10){
      num=product;
      product=1;
      pers++;
      return (persistence(num, pers, product));    
    }
      else if(num==0 && pers>0 && product<=9){ 
        return pers+1;
      }
        else{
          return pers;
        }
}

