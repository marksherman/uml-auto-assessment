/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 40:   Multiple Deterministic FSMs            */
/* Time:         4 days                                 */
/********************************************************/
#include <stdio.h>
int  stateone = 0, S1 = 0,  statetwo = 0, S2 = 0, statethree = 0, S3 = 0, c;
void L1 ();
void L2 ();
void L3 ();
int main ( int argc, char *argv[] ) {
    printf( "Please enter a string of 1's and 0's \n" );
    while ( (c = getchar()) != EOF && ( c != '\n' ) ) {
	L1 ();
	L2 ();
	L3 ();
}
    if ( (stateone != 3) )
	printf( "\nLanguage One Rejects\n" );
    
    else if ( (stateone == 3) ) 
	printf( "\nLanguage One Accepts\n" );
    
    if ( (statetwo != 2) )
        printf( "\nLanguage Two Rejects\n" );
    
    else if ( (statetwo == 2) )
        printf( "\nLanguage Two Accepts\n" );
    
    if ( (statethree != 2) )
        printf( "\nLanguage Three Rejects\n\n" );
    
    else if ( (statethree ==  2) )
        printf( "\nLanguage Three Accepts\n\n" );
    
    return 0;   
}

void L1 () {

/***State Zero******/
    switch(c){
    case 48 : if ( stateone == 0 ){
	    stateone = 1;
	}
	break;
    case 49 : if ( stateone == 0 ){
	    stateone = 0;
	}
	break;
    case EOF : break;
    default : break;
}

/***State One******/
    switch(c){
    case 48 : if ( S1 == 1 ){
	    stateone = 1;
	}
	break;
    case 49 : if ( S1 == 1 ){ 
	    stateone = 2;
	}
	break;
    case EOF : break;
    default : break;
    }

/***State Two******/
    switch(c){
    case 48 : if ( S1 == 2 ){
	    stateone = 3;
	}
	break;
    case 49 : if ( S1 == 2 ){
	    stateone = 0;
	}
	break;
    case EOF : break;
    default : break;
    }

/***State Three******/
    switch(c){
    case 48 : if ( stateone == 3 ){
            stateone = 3;
        }
	break;
    case 49 : if ( stateone == 3 ){
            stateone = 3;
        }
	break;
    case EOF : break;
    default : break;
    }
    S1 = stateone;
    return;
}

void L2 () {
/***State Zero******/
    switch(c){
    case 48 : if ( statetwo == 0 ){
            statetwo = 1;
        }
        break;
    case 49 : if ( statetwo == 0 ){
            statetwo = 2;
        }
        break;
    case EOF : break;
    default : break;
    }



/***State One*******/
    switch(c){
    case 48 : if ( S2 == 1 ){
            statetwo = 0;
        }
	break;
    case 49 : if (  S2 == 1 ){
            statetwo = 3;
        }
	break;
    case EOF : break;
    default : break;
    }

/***State Two*******/
    switch(c){
    case 48 : if ( S2 == 2 ){
            statetwo = 3;
        }
	break;
    case 49 : if ( S2 == 2 ){
            statetwo = 0;
        }
	break;
    case EOF : break;
    default : break;
    }

/***State Three*******/
    switch(c){
    case 48 : if ( S2 == 3 ){
            statetwo = 2;
        }
	break;
    case 49 : if ( statetwo == 0 ){
            statetwo = 1;
        }
	break;
    case EOF : break;
    default : break;
    }

    S2 = statetwo;
    return;
}

void L3 () {
/***State Zero******/
    switch(c){
    case 48 : if ( statethree == 0 ){
            statethree = 2;
	}
	break;
    case 49 : if ( statethree == 0 ){
            statethree = 0;
        }
	break;
    case EOF : break;
    default : break;
    }

/***State One*******/
    switch(c){
    case 48 : if ( S3 == 1 ){
            statethree = 2;
        }
	break;
    case 49 : if ( S3 == 1 ){
            statethree = 2;
        }
	break;
    case EOF : break;
    default : break;
    }

/***State Two*******/
    switch(c){
    case 48 : if ( S3 == 2 ){
            statethree = 1;
        }
	break;
    case 49 : if ( S3 == 2 ){
            statethree = 1;
        }
	break;
    case EOF : break;
    default : break;
    }

    S3 = statethree;
    return;
}
