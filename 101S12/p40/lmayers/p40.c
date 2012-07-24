/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Multiple Deterministic Finit State Machines                  */
/*                                                                       */
/* Approximate completion time:  2 hours                                 */
/*************************************************************************/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
extern int FSM1( int state, int x);
extern int FSM2( int state, int x);
extern int FSM3( int state, int );


int main( int argc, char *argv[] ) {
  
  int S;
  int State1 = 0, State2 = 0, State3 = 0;
  
  printf("Please enter enter a string of characters using 0's and 1's: \n");
  
  while((S = getchar())!= EOF){
    State1 = FSM1 (State1, S);
    State2 = FSM2 (State2, S);
    State3 = FSM3 (State3, S);
  }
  
  if (State1 == 3 ){
    printf("Language 1 accepts\n");
    
  } else 
    printf("Language 1 rejects\n");
  
  if (State2 == 2 ){
    printf("Language 2 accepts\n");
    
  } else 
    printf("Language 2 rejects\n");
  
  if (State3 == 1 ){
    printf("Language 3 accepts\n");
    
  }else
    printf("Language 3 rejects\n");
  return 0;
}

int FSM1(int state, int x){
  if (state == 3) return 3;
  if (state == 0 && x == 0) return 1;
  if (state == 1 && x == 1) return 0;
  if (state == 1 && x == 0) return 1;
  if (state == 1 && x == 1) return 2;
  if (state == 2 && x == 1) return 0;
  if (state == 2 && x == 0) return 3;
  
  return 0;
}

int FSM2(int state, int x){
  if (state == 0 && x == 0) return 1;
  if (state == 0 && x == 1) return 2;
  if (state == 1 && x == 1) return 3;
  if (state == 1 && x == 0) return 0;
  if (state == 2 && x == 0) return 3;
  if (state == 2 && x == 1) return 0;
  if (state == 3 && x == 0) return 2;
  if (state == 3 && x == 1) return 1;
  
  return 0;
}

int FSM3(int state, int x){
  if (state == 3) return 3;
  if (state == 0 && x == 1) return 3;
  if (state == 0 && x == 0) return 1;
  if (state == 2) return 1;
  else
    return 2;
}
