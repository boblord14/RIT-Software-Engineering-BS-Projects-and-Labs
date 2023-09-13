#include <string>


class RegexTests
{
    public:
        bool TestNumberMatch(std::string input);

        bool TestStartsWith(std::string input, std::string match);

        bool TestEndsWith(std::string input, std::string match);

        bool TestStartsAndEndsWith(std::string input, std::string start, std::string end);
        
        bool TestEmailPattern(std::string input);
};