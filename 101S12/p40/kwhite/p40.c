/************************************************/
/* Programmer: Kyle White                       */
/* Program  40: FSM Project                     */
/* Approximate completion time: 1 hour          */
/*                                              */
/************************************************/


#include <stdio.h>

int lan_1_state=0;
int lan_2_state=0;
int lan_3_state=0;

void language_3 ( char number );
void language_2 ( char number );
void language_1 ( char number );
int main (int argc, char* argv [])

{

  char string;

  printf( "\nPlease enter a string of characters: " );

  while ( ( scanf( "%c", &string ) ) != EOF ){

    language_1( string );
    language_2( string );
    language_3( string );

  }

  putchar ( '\n' );

  if ( lan_1_state == 3 )

    printf ( "Language 1 accepts\n" );

  else

    printf ( "language 1 rejects\n" );

  if ( lan_2_state == 2 )

    printf ( "Language 2 accepts\n" );

  else

    printf ( "Language 2 rejects\n" );

  if ( lan_3_state == 1 )

    printf ( "Language 3 accepts\n" );

  else
  
  printf ( "Language 3 rejects\n" );

  putchar ( '\n' );

  return 0;
  
}

void language_1 ( char number )

{

  if ( lan_1_state == 0 && number == '0' )

    lan_1_state = 1;

  else if ( lan_1_state == 0 && number == '1' )

    lan_1_state = 0;

  else if ( lan_1_state == 1 && number == '0' )

    lan_1_state = 1;

  else if ( lan_1_state == 1 && number == '1' )

    lan_1_state = 2;

  else if ( lan_1_state == 2 && number == '0' )

    lan_1_state = 3;

  else if ( lan_1_state == 2 && number == '1' )

    lan_1_state = 0;

  else if ( lan_1_state == 3 && number == '0' )

    lan_1_state = 3;

  else if ( lan_1_state == 3 && number == '1' )

    lan_1_state = 3;

}

void language_2 ( char number )

{

  if ( lan_2_state == 0 && number == '0' )

    lan_2_state = 1;

  else if ( lan_2_state == 0 && number == '1' )

    lan_2_state = 2;

  else if ( lan_2_state == 1 && number == '0' )

    lan_2_state = 0;

  else if ( lan_2_state == 1 && number == '1' )

    lan_2_state = 3;

  else if ( lan_2_state == 2 && number == '0' )

    lan_2_state = 3;

  else if ( lan_2_state == 2 && number == '1' )

    lan_2_state = 0;

  else if ( lan_2_state == 3 && number == '0' )

    lan_2_state = 2;

  else if ( lan_2_state == 3 && number == '1' )

    lan_2_state = 1;

}

void language_3 ( char number )

{
  
  if ( lan_3_state == 0 && number == '0' )
    
    lan_3_state = 1;
  
  else if ( lan_3_state == 0 && number == '1' )
    
    lan_3_state = -1;
  
  else if ( lan_3_state == 1 && number == '0' )
    
    lan_3_state = 2;
  
  else if ( lan_3_state == 1 && number == '1' )
    
    lan_3_state = 2;
  
  else if ( lan_3_state == 2 && number == '0' )
    
    lan_3_state = 1;
  
  else if ( lan_3_state == 2 && number == '1' )
    
    lan_3_state = 1;
  
}
