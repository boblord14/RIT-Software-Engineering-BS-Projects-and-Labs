
int find_word(linked_list *p_list,char *word)

{
  int str_comparison;
  int return_value;
  node *temp_char;
  
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

