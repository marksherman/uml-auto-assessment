/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignement: fgetc and toupper   */
/*                                  */
/* Completion time: 30 Min          */
/************************************/

#include <stdio.h>
#include <ctype.h>

int main ( int argc, char* argv[]) {

    FILE *testdata23;
    char c;

    testdata23 = fopen ("testdata23", "r");

    while ((c = fgetc (testdata23)) != EOF) putchar(toupper(c)); {
    }

    fclose(testdata23);

    return 0;
}
