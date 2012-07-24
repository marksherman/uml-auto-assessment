/* Martin Kibusi */
/* Positive, zero and Negative*/

#include <stdio.h>

main(){
  int x;
  printf("Please enter single integer number \n ");
  scanf("%d",&x);
  
  if(x > 0){
    printf("The number is positive\n\n");
    return 0;
}
  else if(x == 0){
    printf("The number is zero\n\n");
    return 0;
}
    else(x < 0);
      printf("The number is negative\n\n");
      return 0;
}

