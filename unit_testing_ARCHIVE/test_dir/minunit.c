 #include <stdio.h>
 #include "minunit.h"
 

 int tests_run = 0;
 
  
int product(int x, int y) {
  int z;
  z = x*y;
  return z;
}
 
 static char * test_return_number() { 
    int x; 
    x = product(2,3); 
    mu_assert("error, 6 is supposed to be returned, dumbass!", x == 6); 
    return 0; 
  } 

 static char * all_tests() {
     mu_run_test(test_return_number);
     return 0;
 }
 
 int main(int argc, char **argv) {
     char *result = all_tests();
     if (result != 0) {
         printf("%s\n", result);
     }
     else {
         printf("ALL TESTS PASSED\n");
     }
     printf("Tests run: %d\n", tests_run);
 
     return result != 0;
 }


