/* Martin Kibusi*/
/* p4 The fscan Function*/


#include <stdio.h>

main(){
  FILE *fin;
  int testdata4;
  
    fin = fopen("testdata4", "r");
   
    fscanf(fin,"%d",&testdata4);
    
    printf("Here is the file content %d \n ", testdata4); 
   
    return 0;


}
