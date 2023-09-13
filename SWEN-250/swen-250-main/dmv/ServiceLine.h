#include "Customer.h"

using namespace std;

enum t_priority  {priorityA=1, priorityB=2, priorityC=3};

/*
 * Value returned when no customers are in the desired line(s).
 */
#define NO_CUSTOMER (0)


class ServiceLine {
	public:
	ServiceLine(enum t_priority);//Constructor; Initialize everything
	~ServiceLine(); //Destructor; Clean up data
	void AddNewCustomer( int ticketNumber);//Add a customer to this line with the given ticket#
	int ServeCustomer();//Serve the next customer in the line.  Return the ticket# 
	Customer* GetNextCustomer();//Return a pointer to the customer at the head of the line
	int GetCustomerCount();//Return a count of all customers in the line
	enum t_priority GetPriority();//Return the priority of this line
	protected:
	enum t_priority priority ;//The priority of this line
	Customer *p_head_of_line;//The pointer to the beginning of the queue
} ;
