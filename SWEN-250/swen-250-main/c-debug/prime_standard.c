
#include <stdio.h>

int primes[1028]; 	//primes is the array that stores what numbers will be prime. Size is an issue with numbers in the thousands and it will segfault with numbers that are too big. 
int upper_limit;	// user input, program finds primes less than or equal to this number

void check_prime(int test_num, int primes[]) {
 
  /* check_prime's job is to take an input in test_num, check if it's prime, and put a 1 in primes[test_num] if it is, and a 0 otherwise.
   *
   * The result of the function just gets added to the pre-existing primes array, so this function returns void. 
   * */

  int divisor; 		 			 //test variable to divide(well, mod in this case) test_num by and check if it is an actual divisor
 
  divisor  = 2;					 // 1 doesn't count for the purpose of primes
  
  /* Actual case testing. The case in the while makes sure we're not doing the same thing multiple times
   *
   * Then come the actual checks. In order to not be a prime, divisor must be a prime and test_num mod divisor must be 0(cleanly divides into it).
   * If true, we set primes to the right case and break the while. 
   * If not, divisor increments until the while case is invalid. 
   *
   * If test_num never triggers both the above cases while it exhausts the while loop, its prime. 
   *
   * */
  while (divisor*divisor <= test_num) {		 
    if (primes[divisor] == 1){
      if (test_num % divisor == 0)  {
        primes[test_num] = 0;
        return;
      } 
    } 
    divisor++;
  } 

  primes[test_num] = 1;

} 

int main() {

  /* Main function. Reads user input for upper_limit, automatically sets 1 and 2 to being primes(as thats always the case), 
   * and uses a for loop to run check_prime on every odd number between 3 and the upper limit. Every odd number since
   * even numbers always can be divided by 2 and are therefore not prime by default. 
   *
   * Numbers just get printed out in the console if they are indeed prime and below the upper limit. 
   *
   * */
  printf("Enter upper bound:\n");
  scanf("%d", &upper_limit);
  primes[1] = 1;
  primes[2] = 1;
  for (int i = 3; i <= upper_limit; i += 2) {
    check_prime(i, primes);
    if (primes[i]) {
      printf("%d is a prime\n", i);
    } 
  }
  return 0;
} 
