package com.game.guessbill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

public class ExpressionUtils {
	public static Map<String, Integer> MAIN_MATH_OPERATIONS;

	private static void initOperations() {
		MAIN_MATH_OPERATIONS = new HashMap<String, Integer>();
		MAIN_MATH_OPERATIONS.put("*", 1);
		MAIN_MATH_OPERATIONS.put("/", 1);
		MAIN_MATH_OPERATIONS.put("+", 2);
		MAIN_MATH_OPERATIONS.put("-", 2);
	}

	// из инфик�?ной нотации в обратную поль�?кую
	public static String sortingStation(String expression, String leftBracket, String rightBracket) {
		initOperations();
		if (expression == null || expression.length() == 0) {
			throw new IllegalStateException("Expression isn't specified.");
		}
		if (MAIN_MATH_OPERATIONS == null || MAIN_MATH_OPERATIONS.isEmpty()) {
			throw new IllegalStateException("Operations aren't specified.");
		}
		// Выходна�? �?трока, разбита�? на "�?имволы" - операции и операнды..
		List<String> out = new ArrayList<String>();
		// Стек операций.
		Stack<String> stack = new Stack<String>();

		// Удаление пробелов из выражени�?.
		expression = expression.replace(" ", "");

		// Множе�?тво "�?имволов", не �?вл�?ющих�?�? операндами (операции и �?кобки).
		Set<String> operationSymbols = new HashSet<String>(MAIN_MATH_OPERATIONS.keySet());
		operationSymbols.add(leftBracket);
		operationSymbols.add(rightBracket);

		// Индек�?, на котором закончил�?�? разбор �?троки на прошлой итерации.
		int index = 0;
		// Признак необходимо�?ти пои�?ка �?ледующего �?лемента.
		boolean findNext = true;
		while (findNext) {
			int nextOperationIndex = expression.length();
			String nextOperation = "";
			// Пои�?к �?ледующего оператора или �?кобки.
			for (String operation : operationSymbols) {
				int i = expression.indexOf(operation, index);
				if (i >= 0 && i < nextOperationIndex) {
					nextOperation = operation;
					nextOperationIndex = i;
				}
			}
			// Оператор не найден.
			if (nextOperationIndex == expression.length()) {
				findNext = false;
			} else {
				// Е�?ли оператору или �?кобке предше�?твует операнд, добавл�?ем его
				// в выходную �?троку.
				if (index != nextOperationIndex) {
					out.add(expression.substring(index, nextOperationIndex));
				}
				// Обработка операторов и �?кобок.
				// Открывающа�? �?кобка.
				if (nextOperation.equals(leftBracket)) {
					stack.push(nextOperation);
				}
				// Закрывающа�? �?кобка.
				else if (nextOperation.equals(rightBracket)) {
					while (!stack.peek().equals(leftBracket)) {
						out.add(stack.pop());
						if (stack.empty()) {
							throw new IllegalArgumentException("Unmatched brackets");
						}
					}
					stack.pop();
				}
				// Операци�?.
				else {
					while (!stack.empty() && !stack.peek().equals(leftBracket)
							&& (MAIN_MATH_OPERATIONS.get(nextOperation) >= MAIN_MATH_OPERATIONS.get(stack.peek()))) {
						out.add(stack.pop());
					}
					stack.push(nextOperation);
				}
				index = nextOperationIndex + nextOperation.length();
			}
		}
		// Добавление в выходную �?троку операндов по�?ле по�?леднего операнда.
		if (index != expression.length()) {
			out.add(expression.substring(index));
		}
		// Пробразование выходного �?пи�?ка к выходной �?троке.
		while (!stack.empty()) {
			out.add(stack.pop());
		}
		StringBuffer result = new StringBuffer();
		if (!out.isEmpty()) {
			result.append(out.remove(0));
		}
		while (!out.isEmpty()) {
			result.append(" ").append(out.remove(0));
		}

		return result.toString();
	}

	public static String sortingStation(String expression) {
		return sortingStation(expression, "(", ")");
	}

	// вычи�?ление
	public static String calculateExpression(String expression) {
		StringTokenizer tokenizer = new StringTokenizer(expression, " ");
		Stack<BigDecimal> stack = new Stack<BigDecimal>();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			// Операнд.
			if (!MAIN_MATH_OPERATIONS.keySet().contains(token)) {
				stack.push(new BigDecimal(token));
			} else {
				BigDecimal operand2 = stack.pop();
				if (operand2.equals(new BigDecimal(0))) {
					return "Error";
				}
				BigDecimal operand1 = stack.empty() ? BigDecimal.ZERO : stack.pop();
				if (token.equals("*")) {
					stack.push(operand1.multiply(operand2));
				} else if (token.equals("/")) {
					if (operand1.compareTo(operand2) == -1) {
						stack.push(operand1.divide(operand2, 3, RoundingMode.HALF_UP));
					} else {
						stack.push(operand1.divide(operand2,2,RoundingMode.HALF_UP));
					}
				} else if (token.equals("+")) {
					stack.push(operand1.add(operand2));
				} else if (token.equals("-")) {
					stack.push(operand1.subtract(operand2));
				}
			}
		}
		if (stack.size() != 1) {
			throw new IllegalArgumentException("Expression syntax error.");
		}
		return stack.pop().toString();
	}

	/**
	 * Закрытый кон�?труктор кла�?�?а.
	 */
	private ExpressionUtils() {
	}
}