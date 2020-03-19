package calculator;

import javax.swing.*;
import java.awt.event.*;

public class CalculatorGUI extends JFrame {
	private JLabel title;
	private JTextField input;
	private JButton solveButton;
	private JTextField output;
	
	public CalculatorGUI() {
		super("Math Calculator");
		setLayout(null);
		Handler handler = new Handler();
		
		title = new JLabel("ENTER AN EQUATION AND PRESS SOLVE");
		title.setBounds(50, 10, 375, 50);
		add(title);
		
		input = new JTextField();
		input.setBounds(50, 50, 375, 30);
		input.addActionListener(handler);
		add(input);
		
		solveButton = new JButton("Solve!");
		solveButton.setBounds(50, 90, 70, 25);
		add(solveButton);
		
		output = new JTextField();
		output.setEditable(false);
		output.setBounds(50, 150, 375, 30);
		add(output);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,500);
		this.setVisible(true);
	}
	
	private class Handler implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			String equation = event.getActionCommand();
			AlgebraicSolver solver = new AlgebraicSolver(equation);
			output.setText("" + solver.solve());
		}
		
	}

}
