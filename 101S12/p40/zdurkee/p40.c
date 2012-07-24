/**********************************************************************/
/*  Programmer: Zachary Durkee                                        */
/*                                                                    */
/*  Program 40: Multiple Deterministic Finite State Machines          */
/*                                                                    */
/*  Approximate completion time: 12 hours                             */
/**********************************************************************/


#include <stdio.h>

int Lang1( char symbol );

int Lang2( char symbol );

int Lang3( char symbol );

int main( int argc, char *argv[] ){

  char symbol;

  int L1state = 0;

  int L2state = 0;

  int L3state = 0;

  printf( "Enter a number:" );
  
  while ( (symbol = getchar() ) != EOF ){

    L1state = Lang1( symbol );

    L2state = Lang2( symbol );

    L3state = Lang3( symbol );

  }


  if( L1state == 0 )

    printf( "\nLanguage 1 rejects\n");

  else if( L1state == 1 )

    printf( "\nLanguage 1 rejects\n");

  else if( L1state == 2 )

    printf( "\nLanguage 1 rejects\n");

  else if( L1state == 3 )

    printf( "\nLanguage 1 accepts\n");



  if( L2state == 0 )

    printf( "Language 2 rejects\n");

  else if( L2state == 1 )

    printf( "Language 2 rejects\n");

  else if( L2state == 2 )

    printf( "Language 2 accepts\n");

  else if( L2state == 3 )

    printf( "Language 2 rejects\n");



  if( L3state == 0 )

    printf( "Language 3 rejects\n");

  else if( L3state == 1 )

    printf( "Language 3 accepts\n");

  else if( L3state == 2 )

    printf( "Language 3 rejects\n");

  return 0;

}



int Lang1( char symbol ){

  static int L1state = 0;

  switch( L1state ){

  case 0:

    if ( symbol == '0' )

      L1state = 1;

    else

      L1state = 0;

    break;

  case 1:

    if ( symbol == '1' )

      L1state = 2;

    else

      L1state = 1;

    break;

  case 2:

    if ( symbol == '0' )

      L1state = 3;

    else

      L1state = 0;

    break;

  case 3:

    L1state = 3;

    break;

  }

  return L1state;

}



int Lang2( char symbol ){

  static int L2state = 0;

  switch ( L2state ){

  case 0:

    if ( symbol == '0' )

      L2state = 1;

    else

      L2state = 2;

    break;

  case 1:

    if ( symbol == '0' )

      L2state = 0;

    else

      L2state = 3;

    break;

  case 2:

    if ( symbol == '0' )

      L2state = 3;

    else

      L2state = 0;

    break;

  case 3:

    if ( symbol == '0' )

      L2state = 2;

    else

      L2state = 1;

    break;

  }

  return L2state;

}



int Lang3( char symbol ){

  static int L3state = 0;

  switch( L3state ){

  case 0:

    if ( symbol == '0' )

      L3state = 1;

    else

      return 0;

    break;

  case 1:

    if ( symbol == '1' || symbol == '0' )

      L3state = 2;

    break;

  case 2:

    if ( symbol == '1' || symbol == '0' )

      L3state = 1;

    break;

  }

  return L3state;

}
