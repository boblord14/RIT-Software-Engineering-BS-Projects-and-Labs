/*
 * Other includes here
 */
//CPP
#include <iostream>
#include "DMVSchedule.h"
#include "Test.h"

using namespace std;


static void test_all_counts_zero() ;
static void test_empty_lines();
static void test_next_ticket() ;
static void test_add_a_customer_to_each_line() ;
static void test_add_second_customer_to_each_line() ;
static void test_add_third_customer_to_each_line() ;
static void checkpoint1() ;
static void test_serve_2_Bs() ;
static void test_serve_an_A_and_a_C() ;
static void checkpoint2() ;
static void test_serve_highest_priority() ;


	Test myTest;//Create Test Manager
	DMV myDMV;//Init DMV and objects

#define test_assert(expr1, expr2) myTest.ValidateResult(expr1, expr2, __FILE__, __LINE__)

int main() {

	test_all_counts_zero() ;
	test_empty_lines();
	test_next_ticket() ;
	test_add_a_customer_to_each_line() ;
	test_add_second_customer_to_each_line() ;
	test_add_third_customer_to_each_line() ;

	checkpoint1() ;

	test_serve_2_Bs() ;
	test_serve_an_A_and_a_C() ;

	checkpoint2() ;

	test_serve_highest_priority() ;

	test_all_counts_zero() ;
	test_empty_lines();
	myTest.Summary();
}


static void test_all_counts_zero() {
	myTest.SetTestGroup("Testing initialization");

	test_assert(0, myDMV.GetCustomerCount(priorityA));

	test_assert(0,  myDMV.GetCustomerCount(priorityB));

	test_assert(0,  myDMV.GetCustomerCount(priorityC));
	
	test_assert(0, myDMV.GetCustomerCount());

}

static void test_empty_lines()
{
	//Testing handling of methods with empty lines
	myTest.SetTestGroup("Testing handling of methods with empty lines");
	test_assert(NO_CUSTOMER, myDMV.ServeNextCustomer(priorityA));
	test_assert(NO_CUSTOMER, myDMV.ServeNextCustomer(priorityB));
	test_assert(NO_CUSTOMER, myDMV.ServeNextCustomer(priorityC));
	test_assert(NO_CUSTOMER, myDMV.ServeNextCustomer());
}

static void test_next_ticket()
{
	myTest.SetTestGroup("Testing Ticket number generation");

	int ticket = -1;
	ticket = myDMV.CreateNewTicket();
	test_assert(1, ticket);
		
	ticket = myDMV.CreateNewTicket();
	test_assert(2, ticket);
	ticket = myDMV.CreateNewTicket();
	//skip one
	ticket = myDMV.CreateNewTicket();
	test_assert(4, ticket);
}

static void test_add_a_customer_to_each_line()
{
	myTest.SetTestGroup("Testing add a customer to each line");

	myDMV.AddNewCustomer(priorityC);//ticket 5
	myDMV.AddNewCustomer(priorityA);//ticket 6
	myDMV.AddNewCustomer(priorityB);//ticket 7

	cout<<"Check for correct tickets at head of each line"<<endl;
	test_assert(true, myDMV.CheckNextCustomer(priorityA)==6 );
	test_assert(true, myDMV.CheckNextCustomer(priorityB)==7 );
	test_assert(true, myDMV.CheckNextCustomer(priorityC)==5 );

	cout<<"Check for correct counts"<<endl;
	test_assert(1, myDMV.GetCustomerCount(priorityA));
	test_assert(1, myDMV.GetCustomerCount(priorityB));
	test_assert(1, myDMV.GetCustomerCount(priorityC));
	test_assert(3, myDMV.GetCustomerCount());
}

static void test_add_second_customer_to_each_line()
{
	myTest.SetTestGroup("Testing add a 2nd customer to each line");

	myDMV.AddNewCustomer(priorityA);//ticket 8
	myDMV.AddNewCustomer(priorityB);//ticket 9
	myDMV.AddNewCustomer(priorityC);//ticket 10

	cout<<"Check for correct tickets at head of each line"<<endl;
	//NOTE: Head of line shouldn't change yet
	test_assert(true,  myDMV.CheckNextCustomer(priorityA)==6 );
	test_assert(true,  myDMV.CheckNextCustomer(priorityB)==7 );
	test_assert(true,  myDMV.CheckNextCustomer(priorityC)==5 );

	cout<<"Check for correct counts"<<endl;
	test_assert(2, myDMV.GetCustomerCount(priorityA));
	test_assert(2, myDMV.GetCustomerCount(priorityB));
	test_assert(2, myDMV.GetCustomerCount(priorityC));
	test_assert(6, myDMV.GetCustomerCount());

}

static void test_add_third_customer_to_each_line()
{
	myTest.SetTestGroup("Testing add a 3rd customer to each line");

	myDMV.AddNewCustomer(priorityC);//ticket 11
	myDMV.AddNewCustomer(priorityB);//ticket 12
	myDMV.AddNewCustomer(priorityA);//ticket 13

	cout<<"Check for correct tickets at head of each line"<<endl;
	test_assert(true,  myDMV.CheckNextCustomer(priorityA)==6 );
	test_assert(true, myDMV.CheckNextCustomer(priorityB)==7 );
	test_assert(true, myDMV.CheckNextCustomer(priorityC)==5 );

	cout<<"Check for correct counts"<<endl;
	test_assert(3, myDMV.GetCustomerCount(priorityA));
	test_assert(3, myDMV.GetCustomerCount(priorityB));
	test_assert(3, myDMV.GetCustomerCount(priorityC));
	test_assert(9, myDMV.GetCustomerCount());

}


static void checkpoint1() {
	printf("\n** CHECKPOINT #1 **\n") ;
	printf("At this point each line should have three customers with\n") ;
	printf("the following tickets in the order shown.\n") ;

	printf("Priority A line = 6 8 13\n") ;
	printf("Priority B line = 7 9 12\n") ;
	printf("Priority C line = 5 10 11\n") ;
	printf("\n");
}

static void test_serve_2_Bs() {
	myTest.SetTestGroup("Testing serve two B customers");

	test_assert(7, myDMV.ServeNextCustomer(priorityB));
	test_assert(9, myDMV.ServeNextCustomer(priorityB));

	cout<<"Check for correct counts"<<endl;
	test_assert(3, myDMV.GetCustomerCount(priorityA));
	test_assert(1, myDMV.GetCustomerCount(priorityB));
	test_assert(3, myDMV.GetCustomerCount(priorityC));
	test_assert(7, myDMV.GetCustomerCount());

	test_assert(12, myDMV.CheckNextCustomer(priorityB));//This should be head of B line

}

static void test_serve_an_A_and_a_C() {
	myTest.SetTestGroup("Testing serve an 'A' and a 'C' customer");

	test_assert(6, myDMV.ServeNextCustomer(priorityA));
	test_assert(5, myDMV.ServeNextCustomer(priorityC));


	cout<<"Check for correct counts"<<endl;
	test_assert(2, myDMV.GetCustomerCount(priorityA));
	test_assert(1, myDMV.GetCustomerCount(priorityB));
	test_assert(2, myDMV.GetCustomerCount(priorityC));
	test_assert(5, myDMV.GetCustomerCount());

	test_assert(8, myDMV.CheckNextCustomer(priorityA));//This should be head of A line
	test_assert(10, myDMV.CheckNextCustomer(priorityC));//This should be head of C line

}

static void checkpoint2() {
	printf("\n** CHECKPOINT #2 **\n") ;
	printf("At this point each line should have\n") ;
	printf("the following tickets in the order shown.\n") ;

	printf("Priority A line =  8 13\n") ;
	printf("Priority B line = 12\n") ;
	printf("Priority C line =  10 11\n") ;
	printf("\n");
}

static void test_serve_highest_priority() {
	myTest.SetTestGroup("Testing Serve highest priority customers");

	test_assert(8, myDMV.ServeNextCustomer());
	test_assert(13, myDMV.ServeNextCustomer());
	test_assert(12, myDMV.ServeNextCustomer());
	test_assert(10, myDMV.ServeNextCustomer());
	test_assert(11, myDMV.ServeNextCustomer());
}
