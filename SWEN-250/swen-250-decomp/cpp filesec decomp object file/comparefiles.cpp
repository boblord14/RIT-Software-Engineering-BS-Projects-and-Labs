
/* CompareFiles(char*, char*) */

int CompareFiles(char *file1,char *file2)

{
  int filechar1;
  int filechar2;
  FILE *openfile1;
  FILE *openfile2;
  char chartyping1;
  char chartyping2;
  
  openfile1 = fopen(file1,"r");
  openfile2 = fopen(file2,"r");
  if (openfile1 == NULL) {
    printf("Cannot open %s for reading ",file1);
    filechar1 = 1;
  }
  else if (openfile2 == NULL) {
    printf("Cannot open %s for reading ",file2);
    filechar1 = 1;
  }
  else {
    filechar1 = getc(openfile1);
    filechar2 = getc(openfile2);
    chartyping2 = (char)filechar2;
    chartyping1 = (char)filechar1;
    while (((chartyping1 != -1 && (chartyping2 != -1)) && (chartyping1 == chartyping2))) {
      filechar1 = getc(openfile1);
      filechar2 = getc(openfile2);
      chartyping2 = (char)filechar2;
      chartyping1 = (char)filechar1;
    }
    if (chartyping1 == chartyping2) {
      printf("Files %s and %s are identical \n",file1,file2);
    }
    else if (chartyping1 != chartyping2) {
      printf("Files %s and %s are Not identical \n",file1,file2);
    }
    fclose(openfile1);
    fclose(openfile2);
    filechar1 = 0;
  }
  return filechar1;
}

