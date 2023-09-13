#include <iostream>

using namespace std;
class Test
{
    public:
    Test();
    Test(int testNumber);
    void SetTestGroup(string group);
    bool ValidateResult(int expected, int actual, string file="", int line=-1);
    bool ValidateResult(string expected, string actual, string file="", int line=-1);
    int GetTotalTests() { return totalTests;};
    int GetFailedTests() { return failedTests; };
    int GetPassedTests() { return passedTests; };
    void Summary();
    private:
    int currentTest;
    int totalTests;
    int passedTests;
    int failedTests;
    string testGroupName;
};