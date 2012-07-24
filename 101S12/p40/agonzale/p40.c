/************************************************************/
/* Programmer: Alexander Gonzalez                           */
/*                                                          */
/* Assignment: Multiple Deterministic Finite State Machines */
/*                                                          */
/* Completion time: 4+ hours                                */
/************************************************************/

#include <stdio.h>
#include <stdlib.h>

int language1(int string1);
int language2(int string2);
int language3(int string3);

int main(int argc, char *argv[]) {
    
    int x, q0, q1, q2, q3;

    printf("Please enter 0's or 1's till EOF:\n");
    
    scanf("%d", &x);

    if (language1(x) == q3) {
        printf("language 1 accepts \n");
    }
    else {
        printf("language 1 is rejected \n");
    }

    if (language2(x) == q2) {
        printf("language 2 accepts \n");
    }
    else {
        printf("language 2 is rejected \n");
    }

    if (language3(x) == q0) {
        printf("language 3 accepts \n");
    }
    else {
        printf("language 3 is rejected \n");
    }
    
    return 0;
}

int language1(int string1) {
    
    int state = 0;
    int input, x;
    int q0, q1, q2, q3;

    while( scanf("%d", &x) != EOF){
	
	if( state == q0 && input == 0){
	    state = q1;
	}
	else if( input == 1 ){
	    state = q0;
	}
	if( state == q1 && input == 0){
	    state = q1;
	}   
	else if( input == 1 ){
	    state = q2;
	}
	
	if( state == q2 && input == 0){
	    state = q3;
	    break;
	}
	else if( input == 1 ){
	    state = q0;
	}
	if (state == q3 && input == 0||1){
	    state = q3;
	    break;
	}
    }

    return state;
}

int language2(int string2) {
    
    int state = 0;
    int input, x;
    int q0, q1, q2, q3;

    while( scanf("%d", &x) != EOF){
	
	if(state == q0 && input == 0){
	    state = q1;
	}
	else if( input == 1){
	    state = q2;
	    break;
	}
	if( state == q1 && input == 0){
	    state = q0;
	}
	else if( input == 1){
	    state = q3;
	}
	if ( state == q2 && input == 0){
	    state = q3;
	}
	else if( input == 1){
	    state = q0;
	}
	if( state == q3 && input == 0){
	    state = q2;
	    break;
	}
	else if(input == 1){
	    state = q1;
	}
    }
    return state;
}

int language3(int string3) {
    
    int state = 0;
    int input, x;
    int q0, q1;

    while( scanf("%d", &x) != EOF){
	
	if (state == q0 && input == 0){
	    state = q1;
	    break;
	}
	else if( input == 1){ 
	    state = q0;
	}
	if (state == q1 && input == 0) {
	    state = q0;
	}
	else if ( input == 1){
	    state = q1;
	    break;
	}
    }
    return state;
}
