/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 034: Palindrome Detector                         */
/*                                                          */
/* Approximate completion time: 025 minutes                 */
/************************************************************/

#include <stdio.h>
#include <string.h>


int is_palindrome( char* the_string , int start_char , int end_char );


int main(int argc, char* argv[])
{
    char the_string[20];
    int length, retval;

    printf("Input a string (max 20 characters): ");

    scanf("%s", the_string);
    
    length = strlen(the_string);
    
    retval = is_palindrome(the_string, 0, length - 1);

    if(retval == 1)
	printf("%s is a palindrome.\n", the_string);
    else
	printf("%s is not a palindrome.\n", the_string);

    return 0;
}


int is_palindrome( char* the_string , int start_char , int end_char )
{
    if(the_string[start_char] == the_string[end_char])
    {
	start_char++;
	end_char--;
	if(start_char - end_char > 1)
	    return is_palindrome(the_string, start_char, end_char);
	else
	    return 1;
    }
    else
	return 0;
}
