/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 40: Multiple Deterministic Finite State  */
/*             Machines                             */
/*                                                  */
/* Approximate completion time: 6 hours             */
/****************************************************/

#include <stdio.h>

int i = 0;
int state1 = 0; /* Global variables for the states */
int state2 = 0;
int state3 = 0;

void language1( int x );
void language2( int x );
int main( int argc, char* argv[] ){

  int x;

  printf("\nPlease a string of characters\n");
  printf("Enter a EOF (-1) at the end\n");

  for( ;( x = getchar()) != EOF; i++ ){ /*Unless input is EOF, it loops */
    language1( x );
    language2( x );
    if( x == 48 ){ /* If first input is zero, set state3 to 2 */
      state3 = 2;
    }
  }

  if( (state3 == 2) && ((i-1)%2 == 1) ){ /* Even odd checker for third FSM */
    state3 = 3;
  }
  
  if( state1 == 3 ){
    printf("\nLanguage 1 accepts.");
  }
  else if( state1 != 3 ){
    printf("\nLanguage 1 rejects.");
  }

  if( state2 == 2 ){
    printf("\nLanguage 2 accepts.");
  }
  else if( state2 != 2 ){
    printf("\nLanguage 2 rejects.");
  }

  if( state3 == 3 ){
    printf("\nLanguage 3 accepts.\n");
  }
  else{
    printf("\nLanguage 3 rejects.\n");
  }

  return 0;
}

void language1( int x){

  switch( x ){    
  case 48: /* Checks current state and changes state when input is zero */
    if( state1 == 0 ){
      state1 = 1;
      break;
    }
    else if( state1 == 1 ){
      state1 = 1;
      break;
    }
    else if( state1 == 2 ){
      state1 = 3;
      break;
    }
    else if( state1 == 3 ){
      state1 = 3;
      break;
    }

  case 49: /* Checks current state and changes state when input is one */
    if( state1 == 0 ){
      state1 = 0;
      break;
    }
    else if( state1 == 1 ){
      state1 = 2;
      break;
    }
    else if( state1 == 2 ){
      state1 = 0;
      break;
    }
    else if( state1 == 3 ){
      state1 = 3;
      break;
    }
    
  default: /* If input is not zero/one (EOF) break and return */
    break; /* I thought this was a reason my program was not working, */
           /* mess anything up so I kept it in. */
  }
  return;
}

void language2( int x ){

  switch( x ){
  case 48: /* Checks current state and changes state when input is zero */
    if( state2 == 0 ){
      state2 = 1;
      break;
    }
    else if( state2 == 1 ){
      state2 = 0;
      break;
    }
    else if( state2 == 2 ){
      state2 = 3;
      break;
    }
    else if( state2 == 3 ){
      state2 = 2;
      break;
    }

  case 49: /* Checks current state and changes state when input is one */
    if( state2 == 0 ){
      state2 = 2;
      break;
    }
    else if( state2 == 1 ){
      state2 = 3;
      break;
    }
    else if( state2 == 2 ){
      state2 = 0;
      break;
    }
    else if( state2 == 3 ){
      state2 = 1;
      break;
    }

  default:
    break;

  }

  return;

}
