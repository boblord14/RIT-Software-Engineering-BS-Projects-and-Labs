import turtle


def draw_side(n, level):
    if level == 1:
        turtle.forward(n)
        return n
    else:
        total = 0
        total += draw_side(n / 3, level - 1)
        turtle.left(90)
        total += draw_side(n / 3, level - 1)
        turtle.right(90)
        total += draw_side(n / 3, level - 1)
        turtle.right(90)
        total += draw_side(n / 3, level - 1)
        turtle.left(90)
        total += draw_side(n / 3, level - 1)
        return total


if __name__ == '__main__':
    n = input("Input a length: ")
    level = input("Input a depth: ")
    turtle.speed(25)
    print("Length of initial side: " + n)
    print("Number of levels: " + level)
    print("Total length is " + str(draw_side(int(n), int(level))))
    turtle.mainloop()
