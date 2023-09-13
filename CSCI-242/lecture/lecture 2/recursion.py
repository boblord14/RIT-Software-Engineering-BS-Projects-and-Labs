def gcd(num, divisor):
    result = (num % divisor)
    if result == 0:
        return divisor
    return gcd(divisor, result)


def fib(n) -> int:
    if n < 2:
        return n
    return fib(n - 2) + fib(n - 1)


def loopBlastoff(num):
    for i in range(num, 0, -1):
        print(i)
    print("Blastoff!")


def recBlastoff(num):
    if num > 0:
        print(num)
        recBlastoff(num - 1)
    else:
        print("Blastoff!")


if __name__ == '__main__':
    # loopBlastoff(5)
    # recBlastoff(5)
    # print(gcd(1220, 516))
    # print(fib(30))
    pass
