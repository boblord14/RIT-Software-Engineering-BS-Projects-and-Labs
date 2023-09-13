#include "regextests.h"
#include <string>
#include <regex>

using namespace std;

//This method will check to see if the provided string 
//contains numbers ONLY
//Returns true if there are only numbers, else returns false;
bool RegexTests::TestNumberMatch(string input)
{
    regex numberTest("^[\\d]*$");
    return regex_search(input, numberTest);
}

//This method will check to see if the provided string 
//starts with the string in 'substring'
//Returns true if the condition is met, else returns false
bool RegexTests::TestStartsWith(string input, string substring)
{
    regex subStrStartTest(("^"+substring+"(.*)"));
    return regex_match(input, subStrStartTest);
}

//This method will check to see if the provided string 
//ends with the string in 'substring'
//Returns true if the condition is met, else returns false
bool RegexTests::TestEndsWith(string input, string substring)
{
    regex subStrEndTest(("(.*)"+substring+"$"));
    return regex_match(input, subStrEndTest);
}

//This method will check to see if the provided string 
//starts with the string in 'start', and ends with the string in 'end'
//Returns true if the condition is met, else returns false
bool RegexTests::TestStartsAndEndsWith(string input, string start, string end)
{
    return TestStartsWith(input, start) && TestEndsWith(input, end);
}

//This method will check to see if the provided string 
//is a valid email patterns
//The rules are: 
//- alphanumerics followed by a '@', 
//- then more alphanumerics, folowed by a '.', 
//- and then 2-3 letters
//Note: We are using a simplified version of email validation
//Returns true if the condition is met, else returns false
bool RegexTests::TestEmailPattern(string input)
{
    regex emailTest(("[A-z0-9._%-]+@[A-z0-9.-]+\\.[A-z]{3}"));
    return regex_match(input, emailTest);
}



