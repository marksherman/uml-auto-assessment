/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: The fscanf Function  */
/*                                  */
/* Completion Time: 30 Min          */
/************************************/

#include <stdio.h>

int main(){

    int x;
    
    FILE* testdata4;

    testdata4 = fopen("testdata4", "r");
    
    fscanf( testdata4, "%d", &x);

    printf( "How Many NBA Championships did Michael Jordan Win As A Member Of The Chicago Bulls:%d\n" );

    fclose( testdata4 );

    return 0;
}
