

#include <stdlib.h>
#include <stdio.h>

#include <string.h>
#include "linked_list_functions.h"
#include "unit_tests.h"


struct node *create_node( char *word )
{
    int word_size;
    struct node *new_node;
    size_t strlen_word;
    struct word_entry *actual_word;

    new_node = calloc(1,0x20);
    strlen_word = strlen(word);
    word_size = (int)strlen_word + 1;
    actual_word = malloc((long)word_size);
    (new_node->one_word).unique_word = (char *)actual_word;
    strncpy((new_node->one_word).unique_word,word,(long)word_size);
    (new_node->one_word).word_count = 1;
    return new_node;
}

int add_node_at_head( struct linked_list *p_list, char *word )
{
    struct node *new_node;
    int return_value;

    return_value = 0;
    if (((p_list != NULL) && (word != NULL)) && (*word != '\0')) {
        return_value = 1;
        new_node = create_node(word);
        p_list->p_current = new_node;
        if (p_list->p_head == NULL) {
            p_list->p_tail = new_node;
        }
        else {
            p_list->p_head->p_previous = new_node;
            new_node->p_next = p_list->p_head;
        }
        p_list->p_head = new_node;
    }
    return return_value;
}


int clear_linked_list( struct linked_list *p_list )
{
    int return_value;
    struct node *current_node;
    struct node *next_node;

    return_value = 0;
    if (p_list != NULL) {
        current_node = p_list->p_head;
        while (current_node != NULL) {
            free((current_node->one_word).unique_word);
            next_node = current_node->p_next;
            free(current_node);
            return_value = return_value + 1;
            current_node = next_node;
        }
    }
    p_list->p_head = NULL;
    p_list->p_tail = NULL;
    p_list->p_current = NULL;
    return return_value;
}


int add_node_after_current( struct linked_list *p_list, char *word )
{
    struct node *new_node;
    int return_value;
    struct node *after_node;
    struct node *before_node;

    if (p_list->p_current == NULL) {
        return_value = add_node_at_head(p_list,word);
    }
    else {
        new_node = create_node(word);
        before_node = p_list->p_current;
        after_node = p_list->p_current->p_next;
        new_node->p_next = after_node;
        new_node->p_previous = before_node;
        before_node->p_next = new_node;
        p_list->p_current = new_node;
        if (after_node == NULL) {
            p_list->p_tail = new_node;
        }
        else {
            after_node->p_previous = new_node;
        }
        return_value = 1;
    }
    return return_value;
}

int find_word( struct linked_list *p_list, char *word )
{
    int str_comparison;
    int return_value;
    struct node *temp_char;

    return_value = -1;
    if ((((p_list != NULL) && (p_list->p_head != NULL)) && (word != NULL)) && (*word != '\0')) {
        for (temp_char = p_list->p_head; temp_char != NULL; temp_char = temp_char->p_next) {
            str_comparison = strcmp((temp_char->one_word).unique_word,word);
            if (str_comparison == 0) {
                return_value = 1;
                p_list->p_current = temp_char;
            }
            else if (0 < str_comparison) {
                return_value = 0;
                p_list->p_current = temp_char->p_previous;
            }
            if (-1 < return_value) break;
        }
        if (temp_char == NULL) {
            p_list->p_current = p_list->p_tail;
            return_value = 0;
        }
    }
    return return_value;
}

