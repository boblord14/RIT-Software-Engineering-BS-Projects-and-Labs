

/*
 * A given customer has a ticket number that uniquely identifies
 * him or her plus a pointer to the next customer in the list (NULL
 * for the last customer in a list).
 */
class Customer
{
	public:
	Customer(int ticket);//Create the object with the assigned ticket number
	int GetTicketNumber();
	Customer* pNextCustomer;
	protected:
	int ticket_number;
};

