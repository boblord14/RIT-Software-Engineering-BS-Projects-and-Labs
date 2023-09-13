
node * create_node(char *word)

{
  int word_size;
  node *new_node;
  size_t strlen_word;
  word_entry *actual_word;
  
  new_node = (node *)calloc(1,0x20);
  strlen_word = strlen(word);
  word_size = (int)strlen_word + 1;
  actual_word = (word_entry *)malloc((long)word_size);
  (new_node->one_word).unique_word = (char *)actual_word;
  strncpy((new_node->one_word).unique_word,word,(long)word_size);
  (new_node->one_word).word_count = 1;
  return new_node;
}

