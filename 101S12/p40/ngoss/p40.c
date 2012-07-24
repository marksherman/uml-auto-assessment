/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 040: Multiple Deterministic Finite State Machines*/
/*                                                          */
/* Approximate completion time: 030 minutes                 */
/************************************************************/


#include <stdio.h>

int main(int argc, char* argv[])
{
    int  state_L1 = 0, state_L2 = 0, state_L3 = 0;
    char input;

    printf("Please enter a string of characters:\n");

    while(scanf("%c", &input) != EOF)
    {
	switch(state_L1)
	{
	case 0: 
	    if(input == '0')
		state_L1 = 1;
	    else
		state_L1 = 0;
	    break;
	case 1:
	    if(input == '0')
		state_L1 = 1;
	    else
		state_L1 = 2;
	    break;
	case 2:
	    if(input == '0')
		state_L1 = 3;
	    else
		state_L1 = 0;
	    break;
	case 3:
	    break;
	}

	switch(state_L2)
	{
	case 0:
	    if(input == '0')
		state_L2 = 1;
	    else
		state_L2 = 2;
	    break;
	case 1:
	    if(input == '0')
		state_L2 = 0;
	    else
		state_L2 = 3;
	    break;
	case 2:
	    if(input == '0')
		state_L2 = 3;
	    else
		state_L2 = 0;
	    break;
	case 3:
	    if(input == '0')
		state_L2 = 2;
	    else
		state_L2 = 1;
	    break;
	}

	switch(state_L3)
	{
	case -1:
	    break;
	case 0:
	    if(input == '0')
		state_L3 = 1;
	    else
		state_L3 = -1;
	    break;
	case 1:
	    if(input == '0' || input == '1')
		state_L3 = 2;
	    break;
	case 2:
	    if(input == '0' || input == '1')
		state_L3 = 1;
	    break;
	}
    }

    printf("\nLanguage 1 ");
    if(state_L1 == 3)
	printf("accepts.\n");
    else
	printf("rejects.\n");

    printf("Language 2 ");
    if(state_L2 == 2)
	printf("accepts.\n");
    else
	printf("rejects.\n");

    printf("Language 3 ");
    if(state_L3 == 1)
	printf("accepts.\n");
    else
	printf("rejects.\n");

    return 0;
}
