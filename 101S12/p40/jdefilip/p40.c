/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Finite State Machine         */ 
/* Approximate Time: 3 hours           */ 
/***************************************/ 

#include <stdio.h>

int language1 ( int c ); 
int language2 ( int c ); 
int language3 ( int c );  
int main ( int argc, char* argv[] ) 
{
  int c; 
  int l1_state;
  int l2_state; 
  int l3_state;
  printf( "Hi! Please enter a series of 0's and 1's. " ); 
  while ( (c = getchar()) != EOF ) {
     l1_state = language1( c ); 
     l2_state = language2( c ); 
     l3_state = language3( c );  
  }
  if ( l1_state == 3 ) 
    printf( "\nLanguage 1 accepts.\n" ); 
  else 
    printf( "\nLanguage 1 rejects.\n" ); 
  if ( l2_state == 2 ) 
    printf( "\nLanguage 2 accepts.\n" ); 
  else 
    printf( "\nLanguage 2 rejects.\n" ); 
  if ( l3_state == 1 )
    printf( "\nLanguage 3 accepts.\n" ); 
  else 
    printf( "\nLanguage 3 rejects.\n" );  
  return 0; 
}

int language1 ( int c ) { 
  static int state = 0; 
  if ( ( state == 0 ) && ( c == '0' ) ) {
    state = 1; 
    return state;  
  }
  else if ( ( state == 1 ) && ( c  == '1' ) ) {
    state = 2; 
    return state; 
    }
  else if ( ( state == 0 ) && ( c == '1') ) {
    state = 0; 
    return state;      
    }
  else if ( ( state == 1 ) && ( c == '0') ) {
    state = 1; 
    return state; 
  }  
  else if ( ( state == 2 ) && ( c == '1') ) {
    state = 0; 
    return state; 
  }
  else if ( ( state == 2 ) && ( c  == '0') ) {
    state = 3; 
    return state; 
  }
  else if ( ( state == 3 ) && ( c  == '0') ) {
    state = 3; 
    return state; 
  }
  else if ( ( state == 3 ) && ( c  == '1') ) {
      state = 3; 
      return state; 
  }
  else 
      return state; 
}

int language2 ( int c ) { 
  static int state = 0; 
  if ( ( state == 0 ) && ( c == '0' ) )  { 
      state = 1; 
      return state; 
    }
  else if ( ( state == 0 ) && ( c == '1' ) ) {
      state = 2; 
      return state; 
   }
  else if ( ( state == 1 ) && ( c == '0') ) { 
     state = 0; 
     return state; 
   }
  else if ( ( state == 1 ) && ( c == '1') ) { 
     state = 3; 
     return state; 
   }
  else if ( ( state == 2 ) && ( c == '0') ) { 
     state = 3; 
     return state; 
   }
  else if ( ( state == 2 ) && ( c == '1') ) { 
     state = 0; 
     return state; 
   }
  else if ( ( state == 3 ) && ( c == '0' ) ) { 
     state = 2; 
     return state; 
   }
  else if ( ( state == 3 ) && ( c == '1' ) ) { 
     state = 1; 
     return state; 
   }
  else 
    return state;
}
  
int language3 ( c ) {
  static int state = 0; /* state=0 represents the beginning state */ 
  if ( ( state == 0 ) && ( c == '0' ) ) {
    state = 1; 
    return state; 
  }
  else if ( ( state == 0 ) && ( c == '1' ) ) {
    state = 3; /* state=3 represents the fail state */  
    return state;
  } 
  else if ( ( state == 1 ) && ( c == '0' ) ) {
    state = 2; 
    return state; 
  }
  else if ( ( state == 1 ) && ( c == '1' ) ) {
    state = 2; 
    return state; 
  }
  else if ( ( state == 2 ) && ( c == '0' ) ) {
    state = 1; 
    return state; 
  }
  else if ( ( state == 2 ) && ( c == '1' ) ) {
    state = 1; 
    return state; 
  }
  else 
    return state; 
}




  



