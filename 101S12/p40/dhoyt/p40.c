/**************************/
/* Programmer: David Hoyt */
/* Program: FSM           */
/* Time: 3 hrs            */

#include <stdio.h>
#include <stdlib.h>

int x;

int lang_1( int state );
int lang_2( int state );
int lang_3( int state );

int main(){

  int state1=0, state2=0, state3=0;

  printf( "Enter inputs  one at a time, ctrl+d when done\n" );

  while( (scanf( "%d", &x )) != EOF ){

    state1 = lang_1( state1 );

    state2 = lang_2( state2 );

    state3 = lang_3( state3 );

    printf( "Enter next character:\n" );

  }

  if( state1==3 )

    printf( "Language 1 accepts\n" );

  else

    printf( "Language 1 rejects\n" );
  
  if( state2==2 )

    printf( "Language 2 accepts\n" );

  else

    printf( "Language 2 rejects\n" );

  if( state3==1 )

    printf( "Language 3 accepts\n" );

  else 

    printf( "Language 3 rejects\n" );

    return 0;

}

int lang_1( state ){

  if( state==0 ){

    if( x==1 )

      state = 0;

    else{

      state = 1;

    }

    return state;

  }

  if( state==1 ){

    if( x==1 )

      state = 2;

    else{ 

      state = 1;

    }

    return state;

  }

  if( state == 2 ){

    if( x==1 )

      state=0;

    else{

      state = 3;

    }

    return state;

  }

  if( state==3 ){

    return state;

  }

  return state;

}

int lang_2( state ){

  if( state==0 ){

    if( x==1 )

      state = 2;

    else{ 

      state = 1;

    }

    return state;

  }

  if( state==1 ){

    if( x==1 )

      state = 3;

    else{

      state = 0;

    }

    return state;

  }

  if( state==2 ){

    if( x==1 )

      state = 0;

    else{

      state = 3;
    
    }

    return state;

  }

  if( state==3 ){

    if( x==1)

      state = 1;

    else {

      state = 2;

    }

    return state;

  }

  return state;

}

int lang_3( state ){

  if( state==0 ){

    if( x==1 )

      state = 5;

    else{

      state = 1;

    }

    return state;

  }

  if( state==1 ){

    if( x==1 )

      state = 2;

    else{

      state = 2;

    }

    return state;

  }

  if( state==2 ){

    if( x==1 )

      state = 1;

    else{

      state = 1;

    }

    return state;

  }

  return state;

}
