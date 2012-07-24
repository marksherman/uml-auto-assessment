/*************************************************************/
/*                                                           */
/*     Programmer: Chris Leger                               */
/*                                                           */
/*     Title: Multiple Deterministic Finite State Machines   */
/*                                                           */
/*     Time to Completion: 2 Hours                           */
/*                                                           */
/*************************************************************/

#include<stdio.h>
#define ACCEPT 1
#define REJECT 0
#define Q0 0
#define Q1 1
#define Q2 2
#define Q3 3
#define TRAP 4

int inlanguage( char c );
int FSM1( char c );
int FSM2( char c );
int FSM3( char c );

int main( int argc, char* argv[] ){

  char c;
  int fs1 = REJECT;
  int fs2 = REJECT;
  int fs3 = REJECT;

  printf( "Please enter a string of characters followed by EOF:\n" );
  c = getchar();
  while( c != EOF ){
	  if( c == '0' || c== '1' ){
		  fs1 = FSM1(c);
		  fs2 = FSM2(c);
		  fs3 = FSM3(c);
	  }
	  c = getchar();

  }

  if( fs1 == ACCEPT )
    printf( "\nLanguage 1 accepts.\n" );
  else
    printf( "\nLanguage 1 rejects.\n" );
  if( fs2 == ACCEPT )
    printf( "Language 2 accepts.\n" );
  else
    printf( "Language 2 rejects.\n" );
  if( fs3 == ACCEPT )
    printf( "Language 3 accepts.\n" );
  else
    printf( "Language 3 rejects.\n" );

  return 0;

}

int FSM1( char c ){
  
  static int state = Q0;


  if(inlanguage( c ) == 0 )
    state = TRAP;

  switch (state){
  case Q0:
    if( c == '0' )
      state = Q1;
    break;
  case Q1:
    if( c == '1' )
      state = Q2;
    break;
  case Q2:
    if( c == '1' )
      state = Q0;
    else
      state = Q3;
    break;
  case Q3:
  case TRAP:
    break;
  }
  return (state == Q3) ? ACCEPT : REJECT;

}

int FSM2( char c ){

  
  static int state = Q0;


  if(inlanguage( c ) == 0 )
    state = TRAP;

  switch (state){
  case Q0:
    if( c == '0' )
      state = Q1;
    else
      state = Q2;
    break;
  case Q1:
    if( c == '1' )
      state = Q3;
    else
      state = Q0;
    break;
  case Q2:
    if( c == '1' )
      state = Q0;
    else
      state = Q3;
    break;
  case Q3:
    if( c == '0' )
      state = Q2;
    else
      state = Q1;
    break;
  case TRAP:
    break;
  }
  return (state == Q2) ? ACCEPT : REJECT;
}

int FSM3( char c ){
 
  
  static int state = Q0;


  switch (state){
  case Q0:
    if( c == '0' )
      state = Q1;
    else
      state = TRAP;
    break;
  case Q1:
    state = Q2;
    break;
  case Q2:
    state = Q1;
    break;
  case TRAP:
    break;
  }
  return (state == Q1) ? ACCEPT : REJECT;
 
 
}

int inlanguage( char c ){

  if( (c == '0') || (c == '1') )
    return 1;
  else 
    return 0;

}
