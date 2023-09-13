
int add_node_after_current(linked_list *p_list,char *word)

{
  node *new_node;
  int return_value;
  node *after_node;
  node *before_node;
  
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

