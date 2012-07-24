/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Palindrome detector  */
/*                                  */
/* Completion time: HOURS & HOURS   */
/************************************/

#include <stdio.h>
#include <string.h>

void Spaces(char *the_string);

int main(int argc, char* argv[]) {

    char the_string[20];
    int start_char, end_char;
    int is_pal;

    printf("Please Enter A Phrase:\n");
    scanf("%s", the_string[20]);

    fgets(the_string, 20, stdin);
    Spaces(the_string);

    end_char = strlen(the_string) - 1;
    start_char = 0;
    is_pal = 1;

    while( (start_char != end_char) && (end_char > start_char) ) {
	if( tolower(the_string[start_char]) != tolower(the_string[end_char]) ) {
	    
	    is_pal = 0;
	    
	    break;
	}
	start_char++;
	end_char--;
    }
    if( is_pal ) {
	printf("%s is a Palindrome\n", the_string);
    }
    else {
	printf("%s is Not a Palindrome\n", the_string);
    }
    
    return 0;
}

void Spaces(char *the_string) {

    int i = 0, j = 0;

    for(; the_string[i] != '\0'; i++ ) {
	
	if( isalpha(the_string[i]) ) {
	    the_string[j] = the_string[i];
	    j++;
	}
    }
    
    the_string[j] = '\0';
}
