 #define mu_assert(message, test) do { if (!(test)) return message; } while (0)
 #define mu_run_test(test) do { char *message = test(); \
                                if (message) return message; } while (0)
static int tests_run = 0; 
static int tests_passed = 0;
