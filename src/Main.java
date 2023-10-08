import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Converter {
    private static final Map<Character, Integer> romanToArabicMap = new HashMap<>();

    static {
        romanToArabicMap.put('I', 1);
        romanToArabicMap.put('V', 5);
        romanToArabicMap.put('X', 10);
        romanToArabicMap.put('L', 50);
        romanToArabicMap.put('C', 100);
    }

    static int romanToArabic(String s) {
        int result = 0;
        int i = 0;

        while (i < s.length()) {
            if (i < s.length() - 1 && romanToArabicMap.get(s.charAt(i)) < romanToArabicMap.get(s.charAt(i + 1))) {
                result += romanToArabicMap.get(s.charAt(i + 1)) - romanToArabicMap.get(s.charAt(i));
                i += 2;
            } else {
                result += romanToArabicMap.get(s.charAt(i));
                i++;
            }
        }

        return result;
    }

    static String arabicToRoman(int num) {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("Число должно быть в пределах от 1 до 3999.");
        }

        StringBuilder result = new StringBuilder();
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        int i = 0;
        while (num > 0) {
            if (num >= values[i]) {
                result.append(symbols[i]);
                num -= values[i];
            } else {
                i++;
            }
        }

        return result.toString();
    }
}

public class Main {

    public static int calculate(int num1, int num2, char operator) {
        int result;
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    throw new ArithmeticException("Недопустимая операция");
                }
                break;
            default:
                throw new IllegalArgumentException("Недопустимая операция");
        }
        return result;
    }

    public static boolean isValidInput(int num) {
        return num >= 1 && num <= 10;
    }

    public static String calc(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        boolean isArabic = tokens[0].matches("[0-9]+") && tokens[2].matches("[0-9]+");
        boolean isRoman = tokens[0].matches("[IVXLCDM]+") && tokens[2].matches("[IVXLCDM]+");

        if (!(isArabic || isRoman)) {
            throw new IllegalArgumentException("используются одновременно разные системы счисления");
        }

        try {
            int num1;
            int num2;

            if (isArabic) {
                num1 = Integer.parseInt(tokens[0]);
                if (!isValidInput(num1)) {
                    throw new IllegalArgumentException("числа должны быть от 1 до 10 включительно.");
                }

                num2 = Integer.parseInt(tokens[2]);
                if (!isValidInput(num2)) {
                    throw new IllegalArgumentException("числа должны быть от 1 до 10 включительно.");
                }
            } else {
                num1 = Converter.romanToArabic(tokens[0]);
                num2 = Converter.romanToArabic(tokens[2]);
            }

            char operator = tokens[1].charAt(0);

            return "Результат: " + (isArabic ? calculate(num1, num2, operator) : Converter.arabicToRoman(calculate(num1, num2, operator)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("неверный формат чисел. Введите корректные числа.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("введите арифметическое выражение, например 1 + 2:");

        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("строка не является математической операцией")) {
                    break;
                }

                String result = calc(input);
                System.out.println(result);
            } catch (IllegalArgumentException e) {
                System.out.println("throws Exception: " + e.getMessage());
                break;
            }
        }

        scanner.close();
    }
}