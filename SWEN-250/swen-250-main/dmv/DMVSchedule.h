/***
 * Functions for the DMV customer scheduling and service application.
 * Interface file.
 ***/
#ifndef DMV_SCHEDULE_H
#define DMV_SCHEDULE_H

#include "ServiceLine.h"
#define NLINES (3)

class DMV
{
	public:
	DMV();//Constructor; Will initialize all data
	~DMV(); //Destructor; Clean up data
	int GetCustomerCount(t_priority the_priority);//Get # of customers in the specified line
	int GetCustomerCount();//Get total number of customers in all lines

	int ServeNextCustomer();//Serve the next HIGHEST PRIORITY customer.  return the ticket# served
	int ServeNextCustomer(t_priority the_priority); //Serve the next customer of the specified priority.  return the ticket# served
	
	int CreateNewTicket();//Create a new ticket number. Ticket #s start at 1, and increment by one
	void AddNewCustomer(t_priority the_priority);//Add customer to specified line.  

	int CheckNextCustomer(t_priority the_priority);//Get ticket # of the customer @ the head of the requested line
	private:
	int ticketNumber;
	int PriorityToIndex(t_priority the_priority);//Takes a priority enum and returns the array index
	ServiceLine* serviceLines[NLINES];
};



#endif