#include<stdio.h>
#include<stdlib.h>

int main()
{
	printf("Fahrenheit-Celsius\n");
	for(int i=0; i<=300;i+=20){
	        float j = (float)(5*(i-32))/9;
		printf("\t%d\t%.1f\n",i,j); 
	}
	return 0;

}
