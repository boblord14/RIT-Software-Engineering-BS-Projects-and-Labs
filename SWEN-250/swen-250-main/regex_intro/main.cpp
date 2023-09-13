#include <iostream>
#include <string>
#include <cassert>
#include "regextests.h"

using namespace std;

int testsRun = 0;
int testsPassed = 0;
int testsFailed = 0;
bool print_test_result(const bool assertion, int testNumber, int line) 
{
    std::string file = __FILE__;//Unused for now, since it's always this file
    if(assertion == false)
    {
        cout << "Test " << to_string(testNumber) << " failed at line:" << line<<endl;
        testsFailed++;
    }
    else
    {
        cout<<"Test "<< to_string(testNumber) << " passed"<<endl;
        testsPassed++;
    }
    testsRun++;
    return assertion;
}

int main(int argc, char** argv)
{
    RegexTests* regex_tests = new RegexTests();
    int testNumber = 0;
    string msg = "";
    cout<<"***Test numbers only***"<<endl;
    bool result = regex_tests->TestNumberMatch("01235");//true
    print_test_result(result == true, ++testNumber, __LINE__);
    result = regex_tests->TestNumberMatch("0123a5");//false
    print_test_result(result == false,++testNumber, __LINE__);
    result = regex_tests->TestNumberMatch("0 1235");//false
    print_test_result(result == false,++testNumber, __LINE__);

    cout<<"***Test starts with***"<<endl;
    result = regex_tests->TestStartsWith("abcxyz", "ab");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsWith("01-02-2023", "01");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsWith("0 1235", "0");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsWith("=sum(a2:b5", "=sum");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsWith("sum(a2:b5", "=sum");//false
    print_test_result(result == false,++testNumber, __LINE__);
    result = regex_tests->TestStartsWith("In the beginning", "Inxs");//false
    print_test_result(result == false,++testNumber, __LINE__);

    cout<<"***Test ends with***"<<endl;
    result = regex_tests->TestEndsWith("abcxyz","yz");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEndsWith("01-02-2023", "23");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEndsWith("0 1235", "5");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEndsWith("I'll be back", "back");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEndsWith("0 123", "5");//false
    print_test_result(result == false,++testNumber, __LINE__);
    result = regex_tests->TestEndsWith("=sum(a2:b5)", "b5");//false
    print_test_result(result == false,++testNumber, __LINE__);


    cout<<"***Test starts AND ends with***"<<endl;
    result = regex_tests->TestStartsAndEndsWith("abcxyz","a","yz");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsAndEndsWith("01-02-2023", "01","23");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsAndEndsWith("It was a dark and stormy night", "It", "night");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsAndEndsWith("Meet the new boss, same as the old boss", "Meet", "boss");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestStartsAndEndsWith("It was a dark and stormy night", "It", "nite");//false
    print_test_result(result == false,++testNumber, __LINE__);
    result = regex_tests->TestStartsAndEndsWith("Meet the new boss, same as the old boss", "Meet", "box");//true
    print_test_result(result == false,++testNumber, __LINE__);

    cout<<"***Test email pattern***"<<endl;
    result = regex_tests->TestEmailPattern("johndoe@domainsample.com");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEmailPattern("john.doe@domainsample.net");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEmailPattern("john-doe@domainsample.com");//true
    print_test_result(result == true,++testNumber, __LINE__);
    result = regex_tests->TestEmailPattern("johndoe@domainsample.scam");//false
    print_test_result(result == false,++testNumber, __LINE__);
    result = regex_tests->TestEmailPattern("@domainsample.com");//false
    print_test_result(result == false,++testNumber, __LINE__);
    result = regex_tests->TestEmailPattern("jon.com");//false
    print_test_result(result == false,++testNumber, __LINE__);

    cout<<"****Summary****"<<endl;
    cout<<"Tests Run:"<<testsRun<<endl;
    cout<<"Tests passed:"<<testsPassed<<endl;
    cout<<"Tests failed:"<<testsFailed<<endl;
    cout<<"***************"<<endl;
    delete regex_tests;
}
