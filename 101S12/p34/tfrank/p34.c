/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 34:Palindrome Detector              */
/*                                            */
/*Approximate completion time: 40 minutes     */
/**********************************************/

#include <stdio.h>
#include <string.h>

int is_palindrome( char* the_string, int start_char, int end_char);

int main (int argc, char*argv[])
{
	int start_char, end_char = 0;
	char the_string[21];

	printf("Enter a string\n");
	scanf("%s", &the_string[0]);
	end_char = strlen(the_string);

	if (is_palindrome( the_string, start_char, end_char) == 1)
		printf("The string is a palindrome\n");
	else
		printf("The string is not a palindrome\n");

	return 0;
}

int is_palindrome( char* the_string, int start_char, int end_char){

	if( start_char == end_char || start_char == end_char - 1)
		return 1;
	else if( the_string[start_char] != the_string[end_char - 1])
		return 0;
	else
		return	is_palindrome( the_string, start_char + 1, end_char - 1);
}
