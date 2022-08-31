"""
CSAPX Lab 1: Secret Messages

A program that encodes/decodes a message by applying a set of transformation operations.
The transformation operations are:
    shift - Sa[,n] changes letter at index a by moving it n letters fwd in the alphabet. A negative
        value for n shifts the letter backward in the alphabet.
    rotate - R[n] rotates the string n positions to the right. A negative value for n rotates the string
        to the left.
    duplicate - Da[,n] follows character at index a with n copies of itself.

All indices numbers (the subscript parameters) are 0-based.

author: Ethan Patterson
"""


# the following 4 operations are roughly identical in general structure,
# they take the op param and compute what it means on the string param, then returns whatever the new string is
# any major differences are the result of making decrypts work

def shiftOp(pos, mag, string):
    # converts character to ascii, subtracts 64 to remove offset, casts to int, adds, adds offset+converts to
    # character, then restructures/returns the new string
    # decrypt check if letter drops below 26, so it'll wrap around the other way
    letter = int((ord(string[pos]) - 64))
    letter += mag
    letter %= 26
    if letter <= 0:
        letter += 26
    letter = chr(letter + 64)
    return string[:pos] + letter + string[(pos + 1):]


def rotateOp(pos, string):
    # takes characters from the back with negative pos, puts them in the front
    # decrypt version uses negative pos so multiplying that by -1 gives a positive which reverses the process
    mov = pos * -1
    return string[mov:] + string[:mov]


def duplicateOp(pos, mag, string):
    # if statement decides between encrypt and decrypt with +/- mag, decrypt mag is set to - in decryptFormatting
    # takes the start of the string with pos, multiplies the duplicate char by mag, tacks on the original char and end
    # decrypt version takes the start(no dupe char), then the end but shifted by mag to where it should be
    if mag > 0:
        return string[:pos] + (string[pos] * mag) + string[pos:]
    else:
        return string[:pos] + string[pos + (-1 * mag):]


def tradeOp(pos, mag, string):
    # returns string but sliced up and manipulated using pos and mag positions
    # if statement makes sure the end of the string gets added in properly without going out of bounds
    # no difference in decrypt, it just does the same thing but the other way around automatically

    newString = string[:pos] + string[mag] + string[(pos+1):mag] + string[pos]

    if (len(string)-1) >= (mag+1):
        newString += string[(mag+1):]

    return newString


def transform(opList, rawString):
    # finalString here is used over and over in the called ops and is what gets changed by them and eventually printed
    finalString = rawString
    for op in opList:
        # uses find(',') to split the string into pos(position) and mag(magnitude)
        # pos is before the comma and mag is after
        # if no values for pos or mag uses a default setting(1)
        # how pos and mag are used varies(on practically everything but shift),
        # but im keeping the naming scheme for convenience
        split = op.find(',')
        if split != -1:
            pos = int(op[1:split])
            mag = int(op[(split + 1):])
        elif op[1:] != "":
            pos = int(op[1:])
            mag = 1
        else:
            pos = 1
            mag = 1

        # big ifelse to read the first part of the op string and determine what op to call
        if op[0] == "S":
            finalString = shiftOp(pos, mag, finalString)
        elif op[0] == "R":
            finalString = rotateOp(pos, finalString)
        elif op[0] == "D":
            finalString = duplicateOp(pos, mag, finalString)
        elif op[0] == "T":
            finalString = tradeOp(pos, mag, finalString)
        else:
            # unnecessary error catcher but I prefer to have one
            print(op + " is an invalid operator, check over your inputs and try again")
            break
    print(finalString)


def decryptFormatting(decryptList):
    # function necessary to modify list of ops and prep for decrypting
    # lots of annoying garbage to convert in order to get the main encryption functions to play nice here
    # starts with creating an empty list which will hold decryptList but backwards in order to undo encryption nicely
    modList = [str] * len(decryptList)
    i = (len(decryptList)-1)
    for op in decryptList:
        split = op.find(',')
        if op[0] == "R" and op[1:] != "":
            # easy convert to negative this way
            quickMaths = -1 * (int(op[1:]))
            modList[i] = op[0] + str(quickMaths)
        elif op[0] == "R":
            # if r has no params as part of the op, gives it one(that works properly)
            modList[i] = op[0] + "-1" + op[1:]
        elif (op[0] == "D" and op[2:] != "") or (op[0] == "S" and op[2:] != ""):
            # sets mag to negative, which is picked up by the function(for d) and handled natively(for s)
            quickMaths = -1 * (int(op[(split + 1):]))
            modList[i] = op[:(split + 1)] + str(quickMaths)
        elif op[0] == "D" or op[0] == "S":
            # same as before but gives it a ",-1" if it doesn't have a comma
            modList[i] = (op + ",-1")
        elif op[0] == "T":
            # fine as is since the letters just get swapped again in the main func without any hassle
            modList[i] = op
        else:
            # unnecessary error catcher but I prefer to have one
            print(op + " is an invalid operator, check over your inputs and try again")
            break
        i -= 1
    return modList


def encryptStart():
    # just grabs the user input, puts the ops into a nice list format, then tosses it off to transform
    initialString = input('Enter the message: ')
    transformOps = input('Enter the encrypting transformation operations: ')
    print("Generating output ...")
    opListFormat = transformOps.split(';')
    transform(opListFormat, initialString)


def decryptStart():
    # much harder, same function but calls decryptFormatting on opListFormat before it goes to transform
    initialString = input('Enter the message: ')
    transformOps = input('Enter the encrypting transformation operations: ')
    print("Generating output ...")
    opListFormat = transformOps.split(';')
    transform(decryptFormatting(opListFormat), initialString)


def main() -> None:
    # generic intro
    print("Welcome to Secret Messages!")
    while True:
        initialCommand = input('What do you want to do: (E)ncrypt, (D)ecrypt or (Q)uit? ')
        if initialCommand == "E":
            encryptStart()
        elif initialCommand == "D":
            decryptStart()
        elif initialCommand == "Q":
            print("Goodbye!")
            break
        else:
            # unnecessary error catcher but I prefer to have one
            print("Invalid task, please try again")


if __name__ == '__main__':
    main()
