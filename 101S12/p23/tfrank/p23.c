/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 23:fgetc and toupper                */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
#include <ctype.h>

int main (int argc, char*argv[])
{
	int i;
	i = 0;
	char c;
	c = 0;
	FILE *fin;
	fin = fopen( "testdata23", "r");
	i = fgetc(fin);	
	while( i != EOF){
		c = toupper((char)i);
		putchar(c);
		i = fgetc(fin);
	}
	fclose ( fin );

	return 0;
}

