import turtle


def init():
    turtle.left(90)
    turtle.pensize(3)
    turtle.speed(1000)


def draw_tree_1(len):
    turtle.color('blue')
    turtle.forward(len)
    turtle.backward(len)


def draw_tree_2(len):
    turtle.color('red')
    turtle.forward(len)
    turtle.left(45)
    draw_tree_1(len / 2)
    turtle.right(90)
    draw_tree_1(len / 2)
    turtle.left(45)
    turtle.penup()
    turtle.backward(len)
    turtle.pendown()


def draw_tree_3(len):
    turtle.color('purple')
    turtle.forward(len)
    turtle.left(45)
    draw_tree_2(len / 2)
    turtle.right(90)
    draw_tree_2(len / 2)
    turtle.left(45)
    turtle.penup()
    turtle.backward(len)
    turtle.pendown()


def draw_tree_rec(len, seg):
    total = 0
    if seg == 0:
        pass
    else:
        turtle.forward(len)
        turtle.left(45)
        total += draw_tree_rec(len / 2, seg - 1)
        turtle.right(90)
        total += draw_tree_rec(len / 2, seg - 1)
        turtle.left(45)
        turtle.backward(len)
    return total #doesnt return right cba to fix


def main():
    init()
    print(draw_tree_rec(200, 5))
    turtle.mainloop()


if __name__ == '__main__':
    main()
