/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 40:Multiple Deterministic Finite    */
/*           State Machine                    */
/*Approximate completion time: 120 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

#define STATE0 0
#define STATE1 1
#define STATE2 2
#define STATE3 3

int language_1 (int c);
int language_2 (int c);
int language_3 (int c);

int main (int argc, char*argv[])
{
	int c;
	int L1, L2, L3;
	printf("Please enter a string of characters:\n");
	c = getchar();

	while(c != EOF){				
		L1 = language_1(c);
		L2 = language_2(c);
		L3 = language_3(c);
		c = getchar();
	}
	if(L1 == STATE3)
		printf("\nLanguage 1 accepts\n");
	else
		printf("\nLanguage 1 rejects\n");
	if(L2 == STATE2)
		printf("Language 2 accepts\n");
	else
		printf("Language 2 rejects\n");
	if(L3 == STATE1)
		printf("Language 3 accepts\n");
	else
		printf("Language 3 rejects\n");
	return 0;
}

int language_1 (int x)
{
	static int cur_state = STATE0;

	int c = x - '0';
	switch (cur_state){
	case STATE0:
		if(c == 0){
			cur_state = STATE1;
		}
		else if(c == 1){
			cur_state = STATE0;
		}
		break;
	case STATE1:
		if( c == 0){
			cur_state = STATE1;
		}
		else if( c == 1){
			cur_state = STATE2;
		}
		break;
	case STATE2:
		if( c == 0){
			cur_state = STATE3;
		}
		else if ( c == 1){
			cur_state = STATE0;
		}
		break;
	case STATE3:
		break;
	}
	return cur_state;
}

int language_2(int x){
	static int cur_state = STATE0;

	int c = x - '0';

	switch(cur_state){
	case STATE0:
		if( c == 0){
			cur_state = STATE1;
		}
		else if(c == 1){
			cur_state = STATE2;
		}
		break;
	case STATE1:
		if(c == 0) {
			cur_state = STATE0;
		}
		else if (c == 1){
			cur_state = STATE3;
		}
		break;
	case STATE2:
		if( c == 0) {
			cur_state = STATE3;
		}
		else if (c == 1) {
			cur_state = STATE0;
		}
		break;
	case STATE3:
		if( c == 0) {
			cur_state = STATE2;
		}
		else if( c == 1) {
			cur_state = STATE1;
		}
		break;
	}
	return cur_state;
}

int language_3( int x){
	static int cur_state = STATE0;
	int c = x - '0';
	
	switch(cur_state){
	case STATE0:
		if(c == 0){
			cur_state = STATE1;
		}
		else if( c == 1){
			cur_state = STATE3;
		}
		break;
	case STATE1:
		if(c == 0 || c == 1){ 
			cur_state = STATE2;
		}
		break;
		
	case STATE2:
		if( c == 0 || c == 1){
			cur_state = STATE1;
		}
		break;
	case STATE3:
		break;	
	}
	
	return cur_state;
}
