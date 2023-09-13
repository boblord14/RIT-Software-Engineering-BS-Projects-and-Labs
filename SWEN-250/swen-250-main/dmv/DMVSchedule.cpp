/***
 * Methods for the DMV customer scheduling and service application.
 * Interface file.
 ***/
#include <iostream>
#include "DMVSchedule.h"
using namespace std;

//Constructor - initialize data members
DMV::DMV()
{
    this->serviceLines[0] = new ServiceLine(priorityA);
    this->serviceLines[1] = new ServiceLine(priorityB);
    this->serviceLines[2] = new ServiceLine(priorityC);
	this->ticketNumber = 0;
//I feel weird about manually doing the servicelines but I dont see a way to pull enums by index in cpp so this is just how it goes i guess.

}

//Clean up data
DMV::~DMV()
{
for(int i = 0; i<NLINES; i++){
    delete serviceLines[i];
}

}

//Takes a priority enum and returns the array index
//This is implemented for you.  Do not change
int DMV::PriorityToIndex(t_priority the_priority)
{
	return ((int)the_priority -1);
}

//Create a new ticket - increments the next ticket number by one
int DMV::CreateNewTicket()
{
	ticketNumber++;
	return ticketNumber;

}

//Get the customer count for ALL the service lines. Return the total
//In line is empty, return NO_CUSTOMER
int DMV::GetCustomerCount()
{
    int customer_count = 0;
	for(int i = 0; i<NLINES; i++){
        customer_count += serviceLines[i]->GetCustomerCount();
    }
    if(customer_count == 0){
        return NO_CUSTOMER;
    } else return customer_count;

}

//Get the customer count for the specified service line.  
//If the line is empty, return NO_CUSTOMER
int DMV::GetCustomerCount(enum t_priority priority)
{
    int customer_count = serviceLines[PriorityToIndex(priority)]->GetCustomerCount();
    if(customer_count == 0){
        return NO_CUSTOMER;
    } else return customer_count;

}


//Serve the next HIGHEST PRIORITY customer.  return the ticket# served
//Remember that priorityA is the highest, priorityC is the lowest
//If no customers in any line, return NO_CUSTOMER
int DMV::ServeNextCustomer()
{
    for(int i = 0; i<NLINES; i++){
        if(GetCustomerCount(serviceLines[i]->GetPriority()) != NO_CUSTOMER){
            return serviceLines[i]->ServeCustomer();
        }
    }
    return NO_CUSTOMER;
}

//Serve the next customer of the specified priority. return the ticket# served
//If no customers in the line, return NO_CUSTOMER
int DMV::ServeNextCustomer(t_priority the_priority)
{
    if(GetCustomerCount(serviceLines[PriorityToIndex(the_priority)]->GetPriority()) != NO_CUSTOMER){
        return serviceLines[PriorityToIndex(the_priority)]->ServeCustomer();
    } else {
        return NO_CUSTOMER;
    }

}

//Add customer to specified line.
void DMV::AddNewCustomer(t_priority the_priority)
{
	serviceLines[PriorityToIndex(the_priority)]->AddNewCustomer(CreateNewTicket());

} 

//Get ticket # of the customer at the head of the requested line
//If no customers, return NO_CUSTOMER
int DMV::CheckNextCustomer(t_priority the_priority)
{
    if(GetCustomerCount(the_priority) == NO_CUSTOMER) return NO_CUSTOMER;
    return serviceLines[PriorityToIndex(the_priority)]->GetNextCustomer()->GetTicketNumber();

}



