/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignemnt: Argv                 */
/*                                  */
/* Completion time: 15 min          */
/************************************/

#include <stdio.h>

int main(int argc, char* argv[]) {

    int x;

    for(x = 0 ; x < argc ; x++)
	printf("%s\n", argv[x]);

    return 0;
}
