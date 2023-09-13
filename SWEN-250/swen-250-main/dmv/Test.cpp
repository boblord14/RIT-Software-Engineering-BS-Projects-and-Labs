#include <iostream>
#include <string>
#include "Test.h"

Test::Test()
{
    totalTests = passedTests = failedTests = 0;
    currentTest = -1; testGroupName = "no test group";
}

Test::Test(int startTestNumber)
{
    totalTests = passedTests = failedTests = 0;
    currentTest = startTestNumber; testGroupName = "no test group";
}

void Test::SetTestGroup(string group)
{
    testGroupName = group;
    cout<<endl<<"***"<<group<<endl;
}

bool Test::ValidateResult(int expected, int actual, string fileinfo /*= ""*/, int line /*= -1*/)
{
    totalTests++;
    if (expected == actual)
    {
        passedTests++;
        return true;
    }
    else
    {
        failedTests++;
        cout <<  "****"<<testGroupName<<" - Test Failed:"<< " expected " << expected << " - received " << actual <<"; "<<fileinfo<< "; line:"<<line<<endl;
        return false;
    }
}

bool Test::ValidateResult(string expected, string actual, string fileinfo/* =""*/, int line /*= -1*/)
{
    totalTests++;
    if (expected == actual)
    {
        passedTests++;
        return true;
    }
    else
    {
        failedTests++;
        cout <<  "****"<<testGroupName<<" - Test Failed:"<< " expected " << expected << " - received " << actual <<"; "<<fileinfo<< "; line:"<<line<<endl;

        return false;
    }
}

void Test::Summary()
{
    cout<<"Total Tests Run:"<<totalTests<<endl;
    cout<<passedTests<<" Tests passed"<<endl;
    cout<<failedTests<<" Tests failed"<<endl;
}