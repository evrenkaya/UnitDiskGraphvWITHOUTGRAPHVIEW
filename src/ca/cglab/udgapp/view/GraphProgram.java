package ca.cglab.udgapp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import ca.cglab.udgapp.graphmodel.*;

// Class that handles the view for the program
public class GraphProgram
{
	private static final String TITLE = "Unit Disk Square";
	
	private static final String UNICODE_EPSILON = "\u03B5";
	
	private static final String INSTRUCTIONS = 
	"<html>"+
	"<h2><u>Summary</u></h2>"+
	"<div>This program stores a unit disk graph in memory and allows it to be manipulated.</div>"+
	"<div>Once the program starts, all the vertices are distributed uniformly within the unit square.</div>"+
	"<div>This means that each vertex has x,y coordinates that are between 0 and 1.</div>"+
	"<div>(0 - inclusive, 1 - exclusive)</div>"+
	"<br>"+
	"<div>Note: If program is unresponsive after update is pressed, quit by going to Help -> Quit.</div>"+
	"<br>"+
	"<br>"+
	"<h3>Variables: </h3>"+
	"<ul>"+
	"<li><i>n</i> - Number of vertices</li>"+
	"<li><i>|E|</i> - Number of edges</li>"+
	"<li><i>r</i> - Radius/Distance threshold</li>"+
	"<li><i>k</i> - Least number of vertices to check for in connected components</li>"+
	"</ul>"+
	"<div><i>r</i> is calculated as follows: </div>"+
	"<div><font color='blue'>r = 1 / n ^ (a / b) +" + UNICODE_EPSILON + "</font></div>"+
	"<br>"+
	"<div>Update button calculates the new value of <i>r</i> and shows the new number of edges.</div>"+
	"<div>Note: This does not change the vertices in any way.</div>"+
	"<br>"+
	"<br>"+
	"<div><i>Program Creator: Evren Kaya</i></div>"+
	"<div><i>Creation Date: June - July 6, 2015</i></div>"+
	"</html>";
	
	private String listOfAllComponents;
	
	private JFrame frame;
	
	private final int startingVertexCount = 1000;
	private final UnitDiskGraph udg = new UnitDiskGraph(startingVertexCount);
	
	// exponentNumerator is displayed as 'a'
	private float exponentNumerator = 1;
	// exponentDenominator is displayed as 'b'
	private float exponentDenominator = 1;
	private float exponent = exponentNumerator / (float) exponentDenominator;
	// epsilon is normally a very small number
	private float epsilon = 0f;
	
	// JComponents
	private final JLabel instructionsLabel = new JLabel(INSTRUCTIONS);
	private final JLabel allComponentsLabel = new JLabel();
	private JLabel numVerticesLabel;
	private JLabel numEdgesLabel;
	private JLabel numFreeEdgesLabel;
	private JLabel numIntersectingEdgesLabel;
	private JLabel maxVertexDistanceLabel;
	private JLabel numComponentsWithAtLeastKVerticesLabel;
	
	private JTextField exponentNumeratorTextField;
	private JTextField exponentDenominatorTextField;
	private JTextField epsilonTextField;
	
	private JTextField kTextField;
	
	public void createAndShowGUI()
	{
		frame = new JFrame(TITLE);

		udg.createNewRandomVertices();
		udg.setMaxDistance(getMaxDistance());
		udg.createNewConnectedEdges();
		udg.determineIntersectingEdges();
		
		BreadthFirstSearch bfs = new BreadthFirstSearch(udg);
		bfs.determineAllConnectedComponents();
		
		frame.add(createControlPanel(bfs), BorderLayout.NORTH);
		frame.add(createListOfAllComponentsPanel(bfs));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setJMenuBar(createJMenuBar());
		frame.pack();
		frame.setVisible(true);
	}

	private JPanel createControlPanel(BreadthFirstSearch bfs)
	{
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		controlPanel.setBorder(BorderFactory.createLineBorder(Color.green, 2));
		Font font = new Font("arial", Font.PLAIN, 18);
		
		JPanel leftSidePanel = new JPanel(new GridLayout(3, 1));
		int initialNumComponents = bfs.numConnectedComponentsWithAtLeastKVertices((int)exponentNumerator);
		numComponentsWithAtLeastKVerticesLabel = new JLabel("# Components with >= k vertices: " + initialNumComponents);
		numComponentsWithAtLeastKVerticesLabel.setFont(font);
		
		int numFreeEdges = udg.getNumFreeEdges();
		int numIntersectingEdges = udg.getNumIntersectingEdges();
		numIntersectingEdgesLabel = new JLabel("# Intersecting edges: " + numIntersectingEdges);
		numFreeEdgesLabel = new JLabel("# Free edges: " + numFreeEdges);
		numIntersectingEdgesLabel.setFont(font);
		numFreeEdgesLabel.setFont(font);
		
		leftSidePanel.add(numComponentsWithAtLeastKVerticesLabel);
		leftSidePanel.add(numFreeEdgesLabel);
		leftSidePanel.add(numIntersectingEdgesLabel);
		
		JPanel informationPanel = new JPanel(new GridLayout(3, 1));
		numVerticesLabel = new JLabel("n = " + Integer.toString(startingVertexCount));
		numEdgesLabel = new JLabel("|E| = " + Integer.toString(udg.getNumEdges()));
		maxVertexDistanceLabel = new JLabel("r = " + Float.toString(getMaxDistance()));
		
		numVerticesLabel.setFont(font);
		numEdgesLabel.setFont(font);
		maxVertexDistanceLabel.setFont(font);
		
		informationPanel.add(numVerticesLabel);
		informationPanel.add(numEdgesLabel);
		informationPanel.add(maxVertexDistanceLabel);
		
		JPanel entryPanel = new JPanel(new GridLayout(3, 2));
		JLabel exponentNumeratorLabel = new JLabel("a = ");
		JLabel exponentDenominatorLabel = new JLabel("b = ");
		JLabel epsilonLabel = new JLabel(UNICODE_EPSILON + " = ");
		
		exponentNumeratorLabel.setFont(font);
		exponentDenominatorLabel.setFont(font);
		epsilonLabel.setFont(font);
		
		exponentNumeratorTextField = new JTextField(Float.toString(exponentNumerator));
		exponentDenominatorTextField = new JTextField(Float.toString(exponentDenominator));
		epsilonTextField = new JTextField(Float.toString(epsilon));

		entryPanel.add(exponentNumeratorLabel);
		entryPanel.add(exponentNumeratorTextField);
		entryPanel.add(exponentDenominatorLabel);
		entryPanel.add(exponentDenominatorTextField);
		entryPanel.add(epsilonLabel);
		entryPanel.add(epsilonTextField);
		
		JPanel kPanel = new JPanel(new GridLayout(1, 2));
		JLabel kLabel = new JLabel("k = ");
		
		kLabel.setFont(font);
		
		kTextField = new JTextField(exponentNumeratorTextField.getText());
		
		kPanel.add(kLabel);
		kPanel.add(kTextField);
		
		JButton updateButton = new JButton("Update");
		updateButton.setFont(font);
		updateButton.addActionListener(new GraphUpdateAction());
		
		controlPanel.add(leftSidePanel);
		controlPanel.add(informationPanel);
		controlPanel.add(entryPanel);
		controlPanel.add(kPanel);
		controlPanel.add(updateButton);
		
		return controlPanel;
	}
	
	public JPanel createListOfAllComponentsPanel(BreadthFirstSearch bfs)
	{
		JPanel panel = new JPanel();
		updateListOfAllComponents(bfs);
		allComponentsLabel.setText(listOfAllComponents);
		panel.add(allComponentsLabel);
		
		return panel;
	}
	
	private float getMaxDistance()
	{
		return (float) (1 / (Math.pow(udg.getNumVertices(), exponent + epsilon)));
	}

	public void updateEverything()
	{
		// Use a SwingWorker thread to do all the updating work on the graph
		// so that the GUI does not freeze.
		// This also allows for the user to quit the program by going to Help -> Quit
		// because the GUI won't be frozen.
		new GraphUpdater(new BreadthFirstSearch(udg)).execute();
	}
	
	public void updateListOfAllComponents(BreadthFirstSearch bfs)
	{
		listOfAllComponents = "<html><h3><font color='green'># Components of size: </font></h3> <div>";
		for(int i = 1; i < 6; i++)
		{
			int numComponents = bfs.numConnectedComponentsWithKVertices(i);
			listOfAllComponents += Integer.toString(i) + ": " + Integer.toString(numComponents) + "</div> <div></div><div>";
		}
		int numVerticesInLargestComponent = bfs.getNumVerticesinLargestComponent();
		int numLargestComponent = bfs.numConnectedComponentsWithKVertices(numVerticesInLargestComponent);
		listOfAllComponents += ".</div><div>.</div><div>.</div><div></div><div>";
		listOfAllComponents += "Largest (" + Integer.toString(numVerticesInLargestComponent) + ")" + ": " + Integer.toString(numLargestComponent) + "</div>";
		listOfAllComponents += "</html>";
	}
	
	private void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void setExponentTextFields()
	{
		exponentNumeratorTextField.setText(Float.toString(exponentNumerator));
		exponentDenominatorTextField.setText(Float.toString(exponentDenominator));
		epsilonTextField.setText(Float.toString(epsilon));
	}
	
	private float retrieveEpsilonFromTextFields()
	{
		return Float.parseFloat(epsilonTextField.getText());
	}
	
	private float retrieveExponentFromTextFields()
	{
		float denominator = Float.parseFloat(exponentDenominatorTextField.getText());
		if(denominator == 0) throw new ArithmeticException("Cannot divide by zero");
		
		float numerator = Float.parseFloat(exponentNumeratorTextField.getText());
		return (numerator / (float) denominator);
	}
	
	@SuppressWarnings("serial")
	private JMenuBar createJMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new AbstractAction("About")
        {
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(frame, instructionsLabel, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
        });
        
        // This is a backdoor to quit the program
        // when the update button is taking too long.
        // Do not use the regular exit button because
        // it will freeze the GUI.
        helpMenu.add(new AbstractAction("Quit")
        {
			public void actionPerformed(ActionEvent ae)
			{
				System.exit(0);
			}
        	
        });
        
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new AbstractAction("Number of Vertices")
        {
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					int n = Integer.parseInt(JOptionPane.showInputDialog(frame, "Please enter new value of n: ", Integer.toString(udg.getNumVertices())));
					if(n == 0)
					{
						JOptionPane.showMessageDialog(frame, "Graph cannot contain 0 vertices");
						return;
					}
					udg.setNumVertices(n);
					udg.removeAllVertices();
					udg.createNewRandomVertices();
					updateEverything();
				}
				catch(NumberFormatException ne)
				{
					// Do not show an error message here because it will 
					// show even when the user clicks cancel on the option pane
					//JOptionPane.showMessageDialog(frame, "Please enter integers only");
				}
				
			}
        });
        
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
		return menuBar;
	}
	
	
	// Used as the event handler for the update button
	public final class GraphUpdateAction implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			updateEverything();
		}
	}
	
	// Inner class for a SwingWorker thread that does 
	// time consuming work in the background without locking up the GUI.
	public final class GraphUpdater extends SwingWorker<Void, Void>
	{
		private BreadthFirstSearch bfs;
		private Exception e;
		
		public GraphUpdater(BreadthFirstSearch bfs)
		{
			this.bfs = bfs;
			this.e = null;
		}
		
		protected Void doInBackground() throws Exception
		{
			// Set the mouse cursor to wait to show user that the updating is still going on
			try
			{
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				exponent = retrieveExponentFromTextFields();
				epsilon = retrieveEpsilonFromTextFields();
				
				udg.setMaxDistance(getMaxDistance());
				udg.removeAllEdges();
				udg.createNewConnectedEdges();
				udg.determineIntersectingEdges();
				
				bfs.determineAllConnectedComponents();
			}
			catch(Exception e)
			{
				this.e = e;
			}
			
			return null;
		}
		
		protected void done()
		{
			if(e == null)
			{
				int numVertices = udg.getNumVertices();
				numVerticesLabel.setText("n = " + numVertices);
				
				int numComponents = bfs.numConnectedComponentsWithAtLeastKVertices((int)Float.parseFloat(kTextField.getText()));
				numComponentsWithAtLeastKVerticesLabel.setText("# Components with >= k vertices: " + numComponents);
				
				numEdgesLabel.setText("|E| = " + Integer.toString(udg.getNumEdges()));
				maxVertexDistanceLabel.setText("r = " + Float.toString(getMaxDistance()));
				
				int numIntersectingEdges = udg.getNumIntersectingEdges();
				int numFreeEdges = udg.getNumFreeEdges();
				numIntersectingEdgesLabel.setText("# Intersecting edges: " + numIntersectingEdges);
				numFreeEdgesLabel.setText("# Free edges: " + numFreeEdges);
				
				updateListOfAllComponents(bfs);
				allComponentsLabel.setText(listOfAllComponents);
			}
			else
			{
				if(e instanceof NumberFormatException)
				{
					setExponentTextFields();
					showErrorMessage("Please enter real numbers only.");
				}
				else if(e instanceof ArithmeticException)
				{
					setExponentTextFields();
					showErrorMessage("Cannot divide by zero.");
				}
			}
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
	}
}
