package calculator;

import java.util.HashMap;
import java.util.Stack;

public class AlgebraicSolver {
	 String leftHandSide;
	 String rightHandSide;
	
	public AlgebraicSolver(String equation) {
		equation = equation.replaceAll(" ", "");
		int equalSignPos = equation.indexOf("=");
		int varPos = equation.indexOf("x");
		
		if (varPos < equalSignPos) {
			this.leftHandSide = equation.substring(0, equalSignPos);
			this.rightHandSide = equation.substring(equalSignPos + 1);
		} else {
			this.leftHandSide = equation.substring(equalSignPos + 1);
			this.rightHandSide = equation.substring(0, equalSignPos);
		}
	}
	
	public String solve() {	
		String organizedLHS = SetUp.combineLikeTerms(organizeEquation(leftHandSide));
		String [] lhs = SetUp.splitOrganizedEquation(organizedLHS);
		double rhs = solveNormalEquation(rightHandSide);
		
		double coefficient = 1;
		double order = 1;
	
		for (int i = 0; i < lhs.length; i++) {
			String temp = lhs[i];
			
			if (temp.contentEquals(""))
				continue;
			
			int varPos = containsVariable(temp);
			System.out.println(temp + " " + varPos);
			if (varPos != -1) {
				coefficient = Double.parseDouble(temp.substring(0, varPos));
				
				if (varPos < temp.length() - 1)
					order = Double.parseDouble(temp.substring(varPos+2));
				continue;
			}
	
			double flippedNumber = -1 * Double.parseDouble(temp);
			
			rhs += flippedNumber;
		}
		
		rhs = rhs / coefficient;
		
		rhs = Math.pow(rhs, 1 / order);
		return "x = " + rhs; 
	}
	
	public String organizeEquation(String equation) {
		HashMap<String, Integer> priority = SetUp.createOperationPriority();
		String retval = "";
		String [] seperatedEq = SetUp.splitEquation(equation);
		
		for (String str : seperatedEq)
			System.out.println(str);
		
		Stack<Double> n = new Stack<Double>();
		Stack<String> op = new Stack<String>();
		
		String var = "";
		
		for (int i = 0; i < seperatedEq.length; i++) {
			String temp = seperatedEq[i];
			
			int varPos = containsVariable(temp);
			
			if (varPos != -1) {
				
				if (varPos == 0)
					n.push(1.0);
				else
					n.push(Double.parseDouble(temp.substring(0, varPos)));
				var = temp.substring(varPos);
				
				continue;
			}
			
			if (isNumber(temp)) {
				n.push(Double.parseDouble(temp));
				continue;
			}
			
			if (priority.get(temp) == 1) {
				while (!op.isEmpty())
					performStackOperations(n, op);
				
				retval += n.pop() + var + temp;
				var = "";
				continue;
			}
			
			if (op.isEmpty() || priority.get(temp) > priority.get(op.peek())) {
				op.push(temp);
				continue;
			}
			
			int tempPriority = priority.get(temp);
			
			while (!op.isEmpty() && tempPriority <= priority.get(op.peek())) 
				performStackOperations(n, op);
			
			op.push(temp);
		}
		
		while (!op.isEmpty()) 
			performStackOperations(n, op);
		
		retval += n.pop() + var;
		
		return retval;
	}
	
	
	public double solveNormalEquation(String equation) {
		HashMap<String, Integer> priority = SetUp.createOperationPriority();
		String [] seperatedEquation = SetUp.splitEquation(equation);
		
		Stack<Double> n = new Stack<Double>();
		Stack<String> op = new Stack<String>();
		
		for (int i = 0; i < seperatedEquation.length; i++) {
			String temp = seperatedEquation[i];
			
			if (temp.length() == 0)
				continue;
			
			if (isNumber(temp)) {
				n.push(Double.parseDouble(temp));
				continue;
			}
			
			if (temp.equals("(")) {
				op.push(temp);
				continue;
			}
			
			if (temp.equals(")")) {
				while (!op.isEmpty() && !op.peek().equals("(")) 
					performStackOperations(n, op);
				
				op.pop();
				continue;
			}
			
			if (op.isEmpty() || op.peek().equals("(") || priority.get(temp) > priority.get(op.peek())) {
				op.push(temp);
				continue;
			}
			
			int tempPriority = priority.get(temp);
			
			while (!op.isEmpty() && !op.peek().equals("(") && tempPriority <= priority.get(op.peek())) 
				performStackOperations(n, op);
	
			op.push(temp);
		}
		
		while (!op.isEmpty()) 
			performStackOperations(n, op);
		
		return n.pop();
	}
	
	private void performStackOperations(Stack<Double> n, Stack<String> op) {
		double y = n.pop();
		double x = n.pop();
		
		n.push(doOperation(x, y, op.pop()));
	}
	
	private Double doOperation(double x, double y, String op) {
		if (op.equals("^"))
			return Math.pow(x, y);
		else if (op.equals("*"))
			return x * y;
		else if (op.equals("/"))
			return x / y;
		else if (op.equals("+"))
			return x + y;
		else if (op.equals("-"))
			return x - y;
		return null;
	}
	
	private int containsVariable(String n) {
		int length = n.length();
		
		for (int i = 0; i < length; i++)
			if (Character.isLetter(n.charAt(i))) return i;
		return -1;
	}
	
	private boolean isNumber(String n) {
		int length = n.length();
		
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(n.charAt(i)) && n.charAt(i) != '.') return false;
		return true;
	}
}
