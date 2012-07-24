/****************************************************************************/
/* Programmer: Jeremy Krugh                                                 */
/*                                                                          */
/* Program 40: FSM Project                                                  */
/*                                                                          */
/* Approximate Completion time:  6 hours                                    */
/****************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int x;
  int state = 0;

  printf("Please enter a string of characters: ");
  getchar();

  while((x = getchar ()) != EOF){
    if(x == 0){
      state = 1;
    }
      else
	state = 0;
      if(x == 1){
	state = 2;
      }
	else
	  state = 1;
	if(x == 0){
	  state = 3;
	}
	  else
	    state = 0;
	if(x == 1){
	  state = 3;
	}
	else
	  state = 3;
	  
  }

  if(state == 3)
    printf("\nL1 Accepts\n");
  else
    printf("\nL1 Rejects\n");
  /************************************/
  while((x = getchar()) != EOF){
    if(x == 1){
      state = 2;
    }
    else
      state = 1;
    if(x == 1){
      state = 3;
    }
    else
      state = 0;
    if(x == 0){
      state = 2;
    }
    else
      state = 1;
  }

  if(state == 2)
    printf("\nL2 Accepts\n");
  else
    printf("\nL2 Rejects\n");
  /************************************/ 
  while((x = getchar()) != EOF){
    if(x%2 == 0 && x == 0)
      printf("\nL3 Accepts\n");     
	if(x%2 == 1 && x == 1)
	  printf("\nL3 Rejects\n");
	    }
   return 0;
}

/* I tried for hours to make this work but I cant make the code match the diagrams. I understand what the langages accept and reject, I just cant figure out how to make the program reflect that. I got L2 and L1 to print wether they reject or accept but I am stuck as to whre to go from here.*/
