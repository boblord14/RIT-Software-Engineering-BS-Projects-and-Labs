
int add_node_at_head(linked_list *p_list,char *word)

{
  node *new_node;
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

