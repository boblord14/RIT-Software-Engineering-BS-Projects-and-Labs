// Document Analysis project linked list functions
// L. Kiser Feb. 20, 2018

#include <stdlib.h>
#include <stdio.h>

#include <string.h>
#include "linked_list_functions.h"
#include "unit_tests.h"

// Implement this function first to create an initialized linked list node.
// It must allocate space for the passed string in the one_word structure.
// It must copy the passed string into that structure and sets its word count to 1.
// Assume that the passed word points is not NULL and is not an empty string.
// The calling function is responsible for passing a valid string.
// This function returns a pointer to the allocated node.
struct node *create_node( char *word )
{
	struct word_entry new_word;
	new_word.unique_word = malloc(strlen(word) +1 );
	strcpy(new_word.unique_word, word);
	new_word.word_count = 1;
	
	struct node *new_node;
	new_node = malloc(sizeof(new_word) + sizeof (new_node) + sizeof(new_node));
	new_node->one_word = new_word;
	new_node->p_next = NULL;
	new_node->p_previous = NULL;
	
	return new_node ;
}

// Inserts a node at the beginning of the linked list.
// Updates the passed set of pointers as needed based upon the addition that was done.
//
// It checks that p_list is not NULL. Checks that word is not NULL and is not an empty string. 
// Returns 0 for failure if either word test fails.
// Also on failure no change is made to the pointers in p_list.
//
// On success it returns a 1 and updates at least the passed p_head and p_current members of p_list.
// p_head and p_current will always point to the newly added node.
// When adding the first node to an empty list p_tail will also point to this same new node since it is the only node.
//
// Hint: use the create_node function to actually create the node.
// Hint: be sure to maintain both the p_previous and p_next pointers in each node.
int add_node_at_head( struct linked_list *p_list, char *word )
{
    if((p_list==NULL) || (word==NULL) || (word[0]=='\0')){
        return 0;
    }
    p_list->p_current = create_node(word);
    if(p_list->p_head != NULL) {
        p_list->p_current->p_next = p_list->p_head;
        p_list->p_head->p_previous = p_list->p_current;

    } else {
        p_list->p_tail = p_list->p_current;
    }
    p_list->p_head = p_list->p_current;
    return 1;
}

// For the passed linked_list pointer free all of the nodes in the list.
// Be careful to free the space for the string before freeing the node itself.
// Also be careful to save a copy of the pointer to the next item in the list before
// freeing the node.
// Lastly, return the number of nodes freed (which could be zero if p_list indicates an empty list).
int clear_linked_list( struct linked_list *p_list )
{
    int node_count = 0;
    struct node* current_node = p_list->p_head;
    struct node* next_node;
    while(current_node != NULL){
        next_node = current_node->p_next;
        free(current_node->one_word.unique_word);
        free(current_node);
        current_node = next_node;
        node_count++;
    }

    p_list->p_head = NULL;
    p_list->p_current = NULL;
    p_list->p_tail = NULL;

    return node_count ;	// REMOVE THIS and replace with working code

}


// Inserts a node after the current pointer in the linked list.
// Updates the passed set of pointers as needed based upon the addition that was done.
//
// It checks that p_list is not NULL. Checks that word is not NULL and is not an empty string. 
// Returns 0 for failure if either word test fails.
// Also on failure no change is made to the pointers in p_list.
//
// On success it returns a 1 and updates at least the p_current member of p_list. p_current points to the newly added node.
// p_head is updated only if p_current is NULL (an empty list).
// p_tail is updated only if the new node is at the end of the list.
// When adding the first node to an empty list p_tail will also point to this same new node since it is the only node.
//
// Hint: use the create_node function to actually create the node.
// Hint: be sure to maintain both the p_previous and p_next pointers in each node.
// Hint: if this function is called with a p_current that is NULL (meaning the list is empty)
//       use the add_node_at_head function to create the new node.
int add_node_after_current( struct linked_list *p_list, char *word )
{
    if((p_list==NULL) || (word==NULL) || (word[0]=='\0')){
        return 0;
    }
    if(p_list->p_current == NULL){
        return add_node_at_head(p_list, word);
    }

    struct node* old_next = p_list->p_current->p_next;

    p_list->p_current->p_next = create_node(word);
    p_list->p_current->p_next->p_previous = p_list->p_current;

    if(old_next != NULL){
        p_list->p_current->p_next->p_next = old_next;
        old_next->p_previous = p_list->p_current->p_next;
    }
    
    if(p_list->p_tail == p_list->p_current) {
        p_list->p_tail = p_list->p_current->p_next;

    }
    p_list->p_current = p_list->p_current->p_next;
    return 1;
}


// Searches the linked list for the passed word.
// NOTE -- this function REQUIRES that the linked list has been maintained in English language alphabetical order.
// Definition of match: every letter must match exactly including by case.
// e.g. "A" does NOT match "a". "a " does NOT match "a"
//
// If found it sets the current pointer to the matching node.
// If not found it returns a failure and sets the current pointer to the node just before the
// insertion point (by alphabetic order). Note the special case for a new word that goes at the
// beginning of the list. See the examples below.
//
// Hint: use strcmp to determine sorting order.
//
// If it is found the current pointer is set to the node containing the matching word.
// If it is found this function returns a 1 to indicate success.
//
// If it is not found the current pointer is set to the node just before the insertion point.
// If it is not found the function returns a 0 to indicate failure.
// e.g. the list contains the words "apple", "buy", "cat".
//      Searching for "baby" would set current to the node containing "apple".
//      Searching for "acid" would set current to NULL to indicate that "acid" belongs at the head.
//      searching for "zebra" would set current to the node containing "cat".
//
// Tests on p_list and word: p_list, p_list->head, and word must all not be NULL.
// In addition, word must not be an empty string.
// We must have a valid list with at least one node and we must a valid word to match.
// If any of these conditions are violated this function returns a -1 to indicate invalid input.
int find_word( struct linked_list *p_list, char *word )
{
    if((p_list==NULL) || (p_list->p_head==NULL) || (word==NULL) || (word[0]=='\0') ){
        return -1;
    }

    struct node* current_node = p_list->p_head;//yoinked my loop to iterate the list from a previous function

    p_list->p_current = NULL;

    while(current_node != NULL){

        int str_test = strcmp(current_node->one_word.unique_word, word);

        if(str_test == 0){
            p_list->p_current = current_node;
            return 1;
        } else if(str_test<0){
            p_list->p_current = current_node;
        }
        current_node = current_node->p_next;
    }
    return 0;
}
