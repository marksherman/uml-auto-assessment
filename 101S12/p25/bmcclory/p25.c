/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #25: Unfilled Box                             */
/*                                                       */
/* Approximate Completion Time: 60 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int L, H, H_max, L_max;
 
  printf("Enter the length: ");
  scanf("%d", &L_max);
  
  printf("Enter the height: ");
  scanf("%d", &H_max);

  for(H = H_max; H > 0; H--){
    for(L = L_max; L > 0; L--){
      if(L != 1 && L < L_max){
	if(H == 1 || H == H_max){
	  printf("%c", '*');
	}
	else{
	  printf("%c", 32);
	}
      }
      else{
	printf("%c", '*');
      }
    }
    printf("\n");
  }

return 0;
}
