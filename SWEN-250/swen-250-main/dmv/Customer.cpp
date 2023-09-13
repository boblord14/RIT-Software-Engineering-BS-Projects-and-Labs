#include "Customer.h"

//This is already implemented for you.  Do not change
Customer::Customer(int ticket)//Create the object with the assigned ticket number
{
	ticket_number = ticket;
	pNextCustomer = nullptr;
}

//Return the ticket # assigned to this customer
//This is already implemented for you.  Do not change
int Customer::GetTicketNumber()
{
	return ticket_number;
}
