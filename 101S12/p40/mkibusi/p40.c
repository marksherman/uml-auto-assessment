/***************************************************************/
/* Programmer: MARTIN KIBUSI                                   */
/* 	       	      			                       */
/* Program 40:Multiple Deterministic Finite State Machines     */
/* 	   				                       */
/* Approximate completion time :6 hrs                          */
/***************************************************************/
#include<stdio.h>

int state1 =0;
int state2 =0;
int state3 =0;

void lan1(char num);
void lan2(char num);
void lan3(char num);

int main(int argc,char* argv[]){
  int i;
  char x;
  printf("Please enter characers between 0 and 1\n");
  
  while( (x = getchar())!= EOF){ /* while loop is looping to get each character from user iput*/  
	  if( x == '0' || x == '1' ){
		  lan1(x); /* This is an urgurment passed to function lan1*/
		  lan2(x); /* This is an urgurment passed to function lan2*/
		  lan3(x);  /* This is an urgurment passed to function lan3*/
	  }
  }
  if (state1 == 3)
    printf("\nLanguage 1 accepted \n");
  else
    printf("Language 1 rejected \n");
  if(state2 == 2)
    printf("Language 2 accepted \n");
  else
    printf("Language 2 rejected \n");
  if(state3 == 1)
    printf("Language 3 accepted \n");
  else
    printf("Language 3 rejected \n");
  printf("\n");
  return 0;
}
void lan1(char num){ /* lan1 is function is a logic to Language 1*/
  
  if( state1 == 0 && num == '1')
    state1 = 0;
  else if(state1 == 0 && num == '0')
    state1 = 1;
  else if(state1 == 1 && num == '0')
    state1 = 1;
  else if(state1 == 1 && num == '1')
    state1 = 2;
  else if(state1 == 2 && num == '1')
    state1 = 0;
  else if(state1 == 2 && num == '0')
    state1 = 3;
  else if(state1 == 3 && num == '0')
    state1 = 3;
  else if(state1 == 3 && num == '1')
    state1 = 3;
  else 
    state1 = 5;
  
  
}
void lan2(char num){  /* lan2 is function is a logic to a Language 2 */
  
  if( state2 == 0 && num == '0')
    state2 = 1;
  else if(state2 == 0 && num == '1')
    state2 = 2;
  else if(state2 == 1 && num == '0')
    state2 = 0;
  else if(state2 == 1 && num == '1')
    state2 = 3;
  else if(state2 == 2 && num == '0')
    state2 = 3;
  else if(state2 == 2 && num == '1')
    state2 = 0;
  else if(state2 == 3 && num == '0')
    state2 = 2;
  else if(state2 == 3 && num == '1')
    state2 = 1;
  else
    state2 = 5;
  
}
void lan3(char num){ /*lan3 is function is a logic to a language 3*/
  
  if( state3 == 0 && num == '0')
    state3 = 1;
  else if(state3 == 1 && num == '0')
    state3 = 2;
  else if(state3 == 1 && num == '1')
    state3 = 2;
  else if(state3 == 2 && num == '0')
    state3 = 1;
  else if(state3 == 2 && num == '1')
    state3 = 1;
  else
    state3 = 5;
}
