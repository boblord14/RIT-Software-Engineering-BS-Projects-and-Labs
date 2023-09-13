
int clear_linked_list(linked_list *p_list)

{
  int return_value;
  node *current_node;
  node *next_node;
  
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

