# written by Ethan Patterson, lab 2 for apcsx

import turtle
import math


# generic recursive function to draw the initial shape thats needed for the antenna designs later on, for strat1 only
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


# recursive function that handles drawing the antenna part, and prints the length total
def strat1(n, v):
    strat1total = 0
    turtle.left(45)
    for i in range(4):
        strat1total += draw_side(n, v)
        turtle.left(90)
    turtle.right(45)
    return str(strat1total)


def strat2(n, v):
    if v == 1:
        # boils down to "go forward half the length of diagonal, turn to the right angle, do square, go back to center"
        halfDiag = ((math.sqrt(2 * (n * n))) / 2)  # a bit complicated with parenthesis spam but less effort overall
        turtle.penup()
        turtle.forward(halfDiag)
        turtle.pendown()
        turtle.right(135)
        for i in range(4):
            turtle.forward(n)
            turtle.right(90)
        turtle.left(135)
        turtle.penup()
        turtle.backward(halfDiag)
        return n * 4  # returning actual total value
    else:
        strat2total = 0
        # boils down to "do strat2 in center, then do it again on the corners(halfDiag*2 for their center)"
        halfDiag = ((math.sqrt(2 * ((n / 3) * (n / 3)))) / 2)  # even more annoyingly written but its still easier
        strat2total += float(strat2(n / 3, v - 1))
        for i in range(4):
            turtle.forward(halfDiag * 2)
            strat2total += float(strat2(n / 3, v - 1))
            turtle.backward(halfDiag * 2)
            turtle.right(90)
        return str(strat2total)


# main to deal with speed and keeping window open once done, plus actually calling them
if __name__ == '__main__':
    while True:  # inputs for side and depth with idiot catchers if something like a letter gets input
        n = input("Please select a side length: ")
        try:  # try/except to confirm if a value is actually a float by casting it to float and managing error if not
            if float(n) > 0:
                break
            else:
                raise ValueError  # intentionally call that error if negative, easy way to simplify things and save time
        except ValueError:
            print("Value must be a positive float. You entered '" + n + "'")

    while True:
        v = input("Please select a fractal depth, note these get very big very quickly and may take a while to run: ")
        if v.isdigit():  # easy test for a positive int
            break
        else:
            print("Value must be a positive integer. You entered '" + v + "'")

    turtle.speed(20)  # I dont know the max speed so heres a big number so it goes fast
    turtle.left(90)  # no clue why the turtle starts pointing right, it just does for me
    print("Length from first method is " + strat1(float(n), int(v)))  # first strat call plus print length
    input("Press enter to continue")  # wait and clear screen once good
    turtle.clearscreen()
    print("Length from second method is " + strat2(float(n), int(v)))  # same deal as strat1 call but with strat2
    turtle.mainloop()
