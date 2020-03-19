package calculator;

import java.util.HashMap;
import java.util.Set;

public class SetUp {
	
	public static String[] splitEquation(String equation) {
		String retval = "";
		int length = equation.length();
		
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(equation.charAt(i)) && !Character.isLetter(equation.charAt(i)) && equation.charAt(i) != '.')
				retval += "," + equation.charAt(i) + ",";
			else 
				retval += equation.charAt(i);
		
		return retval.split(",");
	}
	
	public static String[] splitOrganizedEquation(String equation) {
		String retval = "";
		int length = equation.length();
		
		for (int i = 0; i < length; i++)
			if (equation.charAt(i) == '+' || equation.charAt(i) == '-')
				retval += "," + equation.charAt(i);
			else 
				retval += equation.charAt(i);
		
		return retval.split(",");
	}
	
	// PEMDAS
	public static HashMap<String, Integer> createOperationPriority() {
		HashMap<String, Integer> priority = new HashMap<String, Integer>();
		priority.put("^", 3);
		priority.put("*", 2);
		priority.put("/", 2);
		priority.put("+", 1);
		priority.put("-", 1);
		
		return priority;
	}
	
	private static void seperateNumberfromVar(HashMap<String, Double> lt, String n) {
		if (Character.isDigit(n.charAt(0)) || Character.isLetter(n.charAt(0)))
			n ="+" + n;
		
		int len = n.length();
		for (int i = 1; i < len; i++)
			if (Character.isLetter(n.charAt(i))) {
				String variable = n.substring(i);
				
				Double prevCoefficient = lt.get(variable);
				Double currentCoefficient = Double.parseDouble("" + n.charAt(0) + 1.0);
				
				if (i > 1)
					currentCoefficient = Double.parseDouble(n.substring(0,i));
				
				if (prevCoefficient != null)
					lt.put(variable, prevCoefficient + currentCoefficient);
				else
					lt.put(variable, currentCoefficient);
				return;
			}
		
		Double coefficient = lt.get("");
		
		if (coefficient != null) {
			lt.put("", coefficient + Double.parseDouble(n));
			return;
		}
		
		lt.put("", Double.parseDouble(n));
	}
	
	public static String combineLikeTerms(String equation) {
		HashMap<String, Double> likeTerms = new HashMap<String, Double>();
		String [] splitEq = splitOrganizedEquation(equation);
		
		for (int i = 0; i < splitEq.length; i++)
			seperateNumberfromVar(likeTerms, splitEq[i]);
		
		
		Set<String> keys = likeTerms.keySet();
		String retval ="";
		
		for (String k : keys) {
			Double n = likeTerms.get(k);
			
			if (n >= 0)
				retval = retval + "+" + n + k;
			else
				retval = retval + n + k;
		}
			
		return retval;
	}
	
	public static void main(String [] args) {
		System.out.println(combineLikeTerms("2+7x+3x^2+5-x-9+3x^2"));
	}

}
