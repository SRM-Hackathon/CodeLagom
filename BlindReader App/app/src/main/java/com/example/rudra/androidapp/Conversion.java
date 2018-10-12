package com.example.rudra.androidapp;

public class Conversion {
    public static String conversion(char ch)
    {
        String letter;
        switch (ch)
        {
            case 'a':
            case 'A':
                letter = "100000";
                break;
            case 'b':
            case 'B':
                letter = "101000";
                break;
            case 'c':
            case 'C':
                letter = "110000";
                break;
            case 'd':
            case 'D':
                letter = "110100";
                break;
            case 'e':
            case 'E':
                letter = "100100";
                break;
            case 'f':
            case 'F':
                letter = "111000";
                break;
            case 'g':
            case 'G':
                letter = "111100";
                break;
            case 'h':
            case 'H':
                letter = "101100";
                break;
            case 'i':
            case 'I':
                letter = "011000";
                break;
            case 'j':
            case 'J':
                letter = "011100";
                break;
            case 'k':
            case 'K':
                letter = "100010";
                break;
            case 'l':
            case 'L':
                letter = "101010";
                break;
            case 'm':
            case 'M':
                letter = "110010";
                break;
            case 'n':
            case 'N':
                letter = "110110";
                break;
            case 'o':
            case 'O':
                letter = "100110";
                break;
            case 'p':
            case 'P':
                letter = "111010";
                break;
            case 'q':
            case 'Q':
                letter = "111110";
                break;
            case 'r':
            case 'R':
                letter = "101110";
                break;
            case 's':
            case 'S':
                letter = "011010";
                break;
            case 't':
            case 'T':
                letter = "011110";
                break;
            case 'u':
            case 'U':
                letter = "100011";
                break;
            case 'v':
            case 'V':
                letter = "101011";
                break;
            case 'w':
            case 'W':
                letter = "011101";
                break;
            case 'x':
            case 'X':
                letter = "110011";
                break;
            case 'y':
            case 'Y':
                letter = "110111";
                break;
            case 'z':
            case 'Z':
                letter = "100111";
                break;
            case '0':
                letter = "010110";
                break;
            case '1':
                letter = "100000";
                break;
            case '2':
                letter = "110000";
                break;
            case '3':
                letter = "100100";
                break;
            case '4':
                letter = "100110";
                break;
            case '5':
                letter = "100010";
                break;
            case '6':
                letter = "110100";
                break;
            case '7':
                letter = "110110";
                break;
            case '8':
                letter = "110010";
                break;
            case '9':
                letter = "010100";
                break;
            case '*':
                letter = "001010";
                break;
            case ',':
                letter = "010000";
                break;
            case ';':
                letter = "011000";
                break;
            case ':':
                letter = "010010";
                break;
            case '.':
                letter = "010011";
                break;
            case '!':
                letter = "011010";
                break;
            case '(':
                letter = "011011";
                break;
            case ')':
                letter = "011011";
                break;
            case '?':
                letter = "011001";
                break;
            case '"':
                letter = "001011";
                break;
            case '#':
                letter = "001111";
                break;
            case '-':
                letter = "001001";
                break;
            case ' ':
                letter = "000000";
                break;
            default:
                letter="haha lol";
        }

        return letter;

    }
}

