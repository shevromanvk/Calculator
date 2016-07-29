
package com.company;

import com.company.tokens.DoubleValue;
import com.company.tokens.Operator;
import com.company.tokens.Token;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 30.05.2016.
 */
public class ExpressionCalculator {

    String calculate(List<Token> tokens) throws CalculatorException {
        ArrayList<Token> list = new ArrayList<>(tokens);

        if (list.size() == 1 && list.get(0) instanceof Operator) {
            throw new CalculatorException("Only operator");
        }

        for (int k = 0; k < tokens.size() - 1; k++) {
            Token token2 = list.get(k);
            Token token3 = list.get(k + 1);
            if (token2 instanceof Operator && token3 instanceof Operator) {
                String operator2 = ((Operator) token2).getOperator();
                String operator3 = ((Operator) token3).getOperator();
                if (operator2.equals("(") && operator3.equals(")")) {
                    throw new CalculatorException("Empty brackets");
                }
            }
        }

        for (int k = 0; k < tokens.size() - 1; k++) {
            Token token2 = list.get(k);
            Token token3 = list.get(k + 1);
            if (token2 instanceof Operator && token3 instanceof DoubleValue) {
                String element = ((Operator) token2).getOperator();
                if (element.equals(")")) {
                    throw new CalculatorException("Number after close bracket.");
                }
            }
        }
        while (list.size() > 1) {

            for (int i = 0; i < list.size(); i += 2) {
                Token token = list.get(i);
                if (token instanceof Operator && ((Operator) token).getOperator().equals("(")) {
                    calculateExpressionInBrackets(list, i);
                }

            }

            boolean foundHighPriorityOperator = false;
            for (int i = 1; i < list.size(); i += 2) {
                String operator = ((Operator) list.get(i)).getOperator();
                if (operator.equals("*") || operator.equals("/")) {
                    double v1 = ((DoubleValue) list.get(i - 1)).getValue();
                    double v2 = ((DoubleValue) list.get(i + 1)).getValue();
                    double val;
                    if (operator.equals("*")) {
                        val = v1 * v2;
                    } else {
                        val = v1 / v2;
                    }
                    list.remove(i - 1);
                    list.remove(i);
                    list.remove(i - 1);
                    list.add(i - 1, new DoubleValue(val));
                    foundHighPriorityOperator = true;
                    break;
                }
            }
            if (foundHighPriorityOperator) {
                continue;
            }
            for (int i = 1; i < list.size(); i += 2) {
                String operator = ((Operator) list.get(i)).getOperator();
                if (operator.equals("+") || operator.equals("-")) {
                    double v1 = ((DoubleValue) list.get(i - 1)).getValue();
                    double v2 = ((DoubleValue) list.get(i + 1)).getValue();
                    double val;
                    if (operator.equals("+")) {
                        val = v1 + v2;
                    } else {
                        val = v1 - v2;
                    }
                    list.remove(i - 1);
                    list.remove(i);
                    list.remove(i - 1);
                    list.add(i - 1, new DoubleValue(val));
                    break;
                }
            }
        }

        String res = String.valueOf(((DoubleValue) list.get(0)).getValue());

        return res;
    }

    private void calculateExpressionInBrackets(ArrayList<Token> list, int positionOfOpenBracket) throws CalculatorException {
        ArrayList<Token> inBrackets = new ArrayList<>();
        int positionOfCloseBracket = 0;
        int openBracketsCounter = 0;
        for (int i = positionOfOpenBracket + 1; i < list.size(); i++) {
            Token token = list.get(i);
            if (token instanceof Operator) {
                String operator = ((Operator) token).getOperator();
                if (operator.equals(")")) {
                    if (openBracketsCounter == 0) {
                        positionOfCloseBracket = i;
                        break;
                    } else {
                        openBracketsCounter--;
                    }
                } else if (operator.equals("(")) {
                    openBracketsCounter++;
                }
            }
            inBrackets.add(list.get(i));
        }
        Token result = new DoubleValue(Double.parseDouble(calculate(inBrackets)));
        list.set(positionOfOpenBracket, result);
        for (int i = positionOfCloseBracket; i > positionOfOpenBracket; i--) {
            list.remove(i);
        }
    }

}
