#include "ServiceLine.h"

//This is already implemented for you.  Do not change
ServiceLine:: ServiceLine(enum t_priority priority)//Constructor; Initialize everything
{
	this->priority = priority;
	p_head_of_line = nullptr;
}

//This is already implemented for you.  Do not change
ServiceLine::~ServiceLine() //Destructor; Clean up data
{
	if (p_head_of_line != nullptr)
	{
		Customer* pObj = p_head_of_line;
		p_head_of_line = p_head_of_line->pNextCustomer;
		delete pObj;
	}
}

//Add a customer to this line with the given ticket#
//This is already implemented for you.  Do not change
void ServiceLine::AddNewCustomer( int ticketNumber)
{
	Customer* newCustomer = new Customer(ticketNumber);
	//If empty, just add, else add to tail
	if (p_head_of_line == nullptr)
	{
		p_head_of_line = newCustomer;
	}
	else
	{
		Customer* pObj = p_head_of_line;
		while (pObj->pNextCustomer != nullptr)
		{
			pObj = pObj->pNextCustomer;
		}
		//pObj is the last node
		pObj->pNextCustomer = newCustomer;//Consider creating customerLine class ... with add, remove etc
	}
}

//Serve the next customer in the line, return the ticket# of the customer served
// AND remove the served customer from the list
//If no customer, return NO_CUSTOMER
//This is already implemented for you.  Do not change
int ServiceLine::ServeCustomer()//Serve the next customer in the line
{
	if (!p_head_of_line)
		return NO_CUSTOMER;
	int ticket = p_head_of_line->GetTicketNumber();
	Customer *pObj = p_head_of_line;
	p_head_of_line = p_head_of_line->pNextCustomer;
	delete pObj;
	return ticket;
}

//Get the count of all customers in the line.  If none, return NO_CUSTOMER
//This is already implemented for you.  Do not change
int ServiceLine::GetCustomerCount()
{
	int count = 0;
	Customer* pObj = p_head_of_line;
	while (pObj != nullptr)
	{
		count++;
		pObj = pObj->pNextCustomer;
	}
	return count;
}

//Return a pointer to the next customer in the line.  
// p_head_of_line should be NULL if no customers
//This is already implemented for you.  Do not change
Customer* ServiceLine::GetNextCustomer()
{
	return p_head_of_line;
}


//Return the priority of this Service Line
//This is already implemented for you.  Do not change
enum t_priority ServiceLine::GetPriority()
{
	return priority; 

}


