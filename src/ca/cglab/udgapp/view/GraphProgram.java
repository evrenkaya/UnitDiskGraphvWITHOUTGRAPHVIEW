package ca.cglab.udgapp.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import ca.cglab.udgapp.graphmodel.BreadthFirstSearch;
import ca.cglab.udgapp.graphmodel.UnEditableTableModel;
import ca.cglab.udgapp.graphmodel.UnitDiskGraph;
import static ca.cglab.udgapp.view.GraphProgramConstants.*;


public class GraphProgram
{
	public static final boolean DEBUG = false;
	
	////// Begin Variables //////
	private JFrame frame;
	
	private int numVertices = 50;
	private final UnitDiskGraph udg = new UnitDiskGraph(numVertices);
	
	// exponentNumerator is displayed as 'a'
	private float a = 1;
	// exponentDenominator is displayed as 'b'
	private float b = 1f;
	private float exponent = a / (float) b;
	// epsilon is normally a very small number
	private float epsilon = 0;
	private int k = 1;
	
	// North Panel GUI components
	private JLabel numVerticesLabel;
	private JLabel kLabel;
	private JLabel aLabel;
	private JLabel bLabel;
	private JLabel epsilonLabel;
	private JLabel radiusLabel;
	
	private JTextField numVerticesTextField;
	private JTextField kTextField;
	
	private JTextField aTextField;
	private JTextField bTextField;
	private JTextField epsilonTextField;
	
	private JButton updateButton;
	
	// East Panel GUI components
	private JLabel componentsInformationTitle;

	private DefaultTableModel tableModel;
	private JTable table;
	
	// West Panel GUI components
	private JLabel edgeInformationTitle;
	private JLabel totalEdgesLabel;
	private JLabel intersectingEdgesLabel;
	private JLabel freeEdgesLabel;
	private JLabel superFreeEdgesLabel;
	////// End Variables //////
	
	private float getMaxDistance()
	{
		return (float) (1 / (Math.pow(numVertices, exponent + epsilon)));
	}
	
	public void updateEverything()
	{
		// Use a SwingWorker thread to do all the updating work on the graph so that the GUI does not freeze.
		// This also allows for the user to quit the program by going to Help -> Quit
		// because the GUI won't be frozen.
		new GraphUpdater(new BreadthFirstSearch(udg)).execute();
	}
	
	private void showErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void setTextFields()
	{
		aTextField.setText(Float.toString(a));
		bTextField.setText(Float.toString(b));
		epsilonTextField.setText(Float.toString(epsilon));
		numVerticesTextField.setText(Integer.toString(numVertices));
		kTextField.setText(Integer.toString(k));
	}
	
	private float retrieveEpsilonFromTextFields()
	{
		return Float.parseFloat(epsilonTextField.getText());
	}
	
	private float retrieveExponentFromTextFields()
	{
		float denominator = Float.parseFloat(bTextField.getText());
		if(denominator == 0) throw new ArithmeticException("Cannot divide by zero");
		
		float numerator = Float.parseFloat(aTextField.getText());
		a = numerator;
		b = denominator;
		return (numerator / (float) denominator);
	}
	
	private int retrieveNumVerticesFromTextField()
	{
		return Integer.parseInt(numVerticesTextField.getText());
	}
	
	private int retrieveKFromTextField()
	{
		return Integer.parseInt(kTextField.getText());
	}
	
	public void createAndShowGUI()
	{
		frame = new JFrame(PROGRAM_TITLE);
		JPanel mainPanel = (JPanel)frame.getContentPane();
		
		udg.createNewRandomVertices();
		udg.setMaxDistance(getMaxDistance());
		udg.createNewConnectedEdges();
		udg.determineIntersectingEdges();
		
		BreadthFirstSearch bfs = new BreadthFirstSearch(udg);
		bfs.determineAllConnectedComponents();

		if(DEBUG)
		{
			TestPanel panel = new TestPanel(udg);
			panel.setPreferredSize(new Dimension(1200, 900));
			mainPanel.add(panel);
			
		}
		else
		{
			mainPanel.add(createNorthPanel(), BorderLayout.NORTH);
			mainPanel.add(createEastPanel(bfs), BorderLayout.EAST);
			mainPanel.add(createWestPanel(), BorderLayout.WEST);
			mainPanel.add(createSouthPanel(), BorderLayout.SOUTH);
			mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		
		frame.setVisible(true);
	}
	
	private JPanel createNorthPanel()
	{
		final int insets = 40;
		EmptyBorder emptyBorder = new EmptyBorder(insets, insets, insets, insets);
		final Dimension prefTextFieldSize = new Dimension(50, 30);
		JPanel northPanel = new JPanel();
		
		JPanel leftPanel = new JPanel(new GridLayout(2, 2, 0, 30));
		leftPanel.setBorder(emptyBorder);
		
		numVerticesLabel = new JLabel("n = ");
		kLabel = new JLabel("k = ");
		
		numVerticesTextField = new JTextField(Integer.toString(numVertices));
		kTextField = new JTextField(Integer.toString(k));
		
		numVerticesLabel.setFont(COMMON_LABEL_FONT);
		kLabel.setFont(COMMON_LABEL_FONT);
		
		numVerticesTextField.setFont(TEXT_FIELD_FONT);
		numVerticesTextField.setPreferredSize(prefTextFieldSize);
		kTextField.setFont(TEXT_FIELD_FONT);
		kTextField.setPreferredSize(prefTextFieldSize);
		
		leftPanel.add(numVerticesLabel);
		leftPanel.add(numVerticesTextField);
		leftPanel.add(kLabel);
		leftPanel.add(kTextField);
		
		JPanel middlePanel = new JPanel(new GridLayout(3, 2, 0, 30));
		middlePanel.setBorder(emptyBorder);
		
		aLabel = new JLabel("a = ");
		bLabel = new JLabel("b = ");
		epsilonLabel = new JLabel(UNICODE_EPSILON + " = ");
		
		aTextField = new JTextField(Float.toString(a));
		bTextField = new JTextField(Float.toString(b));
		epsilonTextField = new JTextField(Float.toString(epsilon));
		
		aLabel.setFont(COMMON_LABEL_FONT);
		bLabel.setFont(COMMON_LABEL_FONT);
		epsilonLabel.setFont(COMMON_LABEL_FONT);
		
		
		aTextField.setFont(TEXT_FIELD_FONT);
		aTextField.setPreferredSize(prefTextFieldSize);
		bTextField.setFont(TEXT_FIELD_FONT);
		bTextField.setPreferredSize(prefTextFieldSize);
		epsilonTextField.setFont(TEXT_FIELD_FONT);
		epsilonTextField.setPreferredSize(prefTextFieldSize);
		
		middlePanel.add(aLabel);
		middlePanel.add(aTextField);
		middlePanel.add(bLabel);
		middlePanel.add(bTextField);
		middlePanel.add(epsilonLabel);
		middlePanel.add(epsilonTextField);
		
		JPanel rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.setBorder(emptyBorder);
		
		updateButton = new JButton("Update Graph");
		updateButton.setFont(COMMON_LABEL_FONT);
		updateButton.addActionListener(new GraphUpdateAction());
		
		radiusLabel = new JLabel("r = " + getMaxDistance());
		radiusLabel.setPreferredSize(new Dimension(200, 40));
		radiusLabel.setFont(COMMON_LABEL_FONT);
		
		rightPanel.add(radiusLabel);
		rightPanel.add(updateButton);
		
		northPanel.add(leftPanel);
		northPanel.add(middlePanel);
		northPanel.add(rightPanel);
		
		return northPanel;
	}
	
	private JPanel createEastPanel(BreadthFirstSearch bfs)
	{
		JPanel eastPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(eastPanel, BoxLayout.Y_AXIS);
		eastPanel.setLayout(boxLayout);
		
		componentsInformationTitle = new JLabel("Connected Components");
		componentsInformationTitle.setFont(TITLE_FONT);
		componentsInformationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		String[] columns = { "# Vertices", "# Components" };
		
		tableModel = new UnEditableTableModel(columns, 7);
		
		for(int i = 0; i < 5; i++)
		{
			String numVertices = Integer.toString(i + 1);
			tableModel.setValueAt(numVertices, i, 0);
			String numComponents = Integer.toString(bfs.numConnectedComponentsWithKVertices(i + 1));
			tableModel.setValueAt(numComponents, i, 1);
		}
		
		int numVerticesInLargestComponent = bfs.getNumVerticesinLargestComponent();
		int numLargestComponents = bfs.numConnectedComponentsWithAtLeastKVertices(numVerticesInLargestComponent);
		
		tableModel.setValueAt("Largest(" + Integer.toString(numVerticesInLargestComponent) + ")", 5, 0);
		tableModel.setValueAt(Integer.toString(numLargestComponents), 5, 1);
		
		tableModel.setValueAt(">= k", 6, 0);
		tableModel.setValueAt(Integer.toString(bfs.numConnectedComponentsWithAtLeastKVertices(k)), 6, 1);

		table = new JTable(tableModel);
		table.setFont(COMMON_LABEL_FONT);
		table.setRowHeight(24);
		table.getTableHeader().setFont(COMMON_LABEL_FONT);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(String.class, centerRenderer);
		
		for(int i = 0; i < 2; i++)
		{
	       table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	    }
		table.setPreferredScrollableViewportSize(new Dimension(320, 200));
		
		eastPanel.add(componentsInformationTitle);
		eastPanel.add(Box.createVerticalStrut(20));
		eastPanel.add(new JScrollPane(table));
		
		return eastPanel;
	}
	
	private JPanel createWestPanel()
	{
		final int insets = 20;
		EmptyBorder emptyBorder = new EmptyBorder(insets, insets, insets, insets);
		
		JPanel westPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
		westPanel.setLayout(boxLayout);
		
		int totalEdges = udg.getNumEdges();
		int intersectingEdges = udg.getNumIntersectingEdges();
		int freeEdges = udg.getNumFreeEdges();
		
		edgeInformationTitle = new JLabel("Edges");
		totalEdgesLabel = new JLabel("Total: " + totalEdges);
		intersectingEdgesLabel = new JLabel("Intersecting: " + intersectingEdges);
		freeEdgesLabel = new JLabel("Free: " + freeEdges);
		superFreeEdgesLabel = new JLabel("Super Free: " + Integer.toString(udg.getNumSuperFreeEdges()));
		
		edgeInformationTitle.setFont(TITLE_FONT);
		totalEdgesLabel.setFont(COMMON_LABEL_FONT);
		intersectingEdgesLabel.setFont(COMMON_LABEL_FONT);
		freeEdgesLabel.setFont(COMMON_LABEL_FONT);
		superFreeEdgesLabel.setFont(COMMON_LABEL_FONT);
		
		totalEdgesLabel.setBorder(emptyBorder);
		intersectingEdgesLabel.setBorder(emptyBorder);
		freeEdgesLabel.setBorder(emptyBorder);
		superFreeEdgesLabel.setBorder(emptyBorder);
		
		westPanel.add(edgeInformationTitle);
		westPanel.add(totalEdgesLabel);
		westPanel.add(intersectingEdgesLabel);
		westPanel.add(freeEdgesLabel);
		westPanel.add(superFreeEdgesLabel);

		return westPanel;
	}
	
	private JPanel createSouthPanel()
	{
		JPanel southPanel = new JPanel();
		return southPanel;
	}
	
	private JPanel createCenterPanel()
	{
		JPanel centerPanel = new JPanel();
		return centerPanel;
	}
	
	@SuppressWarnings({"serial" })
	private JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new AbstractAction("About")
        {
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, INSTRUCTIONS_LABEL, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
        });
        
        // This is a backdoor to quit the program when the update button is taking too long.
        // Do not use the regular exit button because it will freeze the GUI.
        helpMenu.add(new AbstractAction("Quit")
        {
			public void actionPerformed(ActionEvent ae)
			{
				System.exit(0);
			}
        	
        });
        
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
				
				numVertices = retrieveNumVerticesFromTextField();
				k = retrieveKFromTextField();
				exponent = retrieveExponentFromTextFields();
				epsilon = retrieveEpsilonFromTextFields();
				
				udg.removeAllVertices();
				udg.setNumVertices(numVertices);
				udg.createNewRandomVertices();
				udg.setMaxDistance(getMaxDistance());
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
				totalEdgesLabel.setText("Total: " + Integer.toString(udg.getNumEdges()));
				intersectingEdgesLabel.setText("Intersecting: " + Integer.toString(udg.getNumIntersectingEdges()));
				freeEdgesLabel.setText("Free: " + Integer.toString(udg.getNumFreeEdges()));
				superFreeEdgesLabel.setText("Super Free: " + Integer.toString(udg.getNumSuperFreeEdges()));
				
				radiusLabel.setText("r = " + Float.toString(getMaxDistance()));
				
				for(int i = 0; i < 5; i++)
				{
					String numComponents = Integer.toString(bfs.numConnectedComponentsWithKVertices(i + 1));
					tableModel.setValueAt(numComponents, i, 1);
				}
				
				int numVerticesInLargestComponent = bfs.getNumVerticesinLargestComponent();
				int numLargestComponents = bfs.numConnectedComponentsWithAtLeastKVertices(numVerticesInLargestComponent);
				
				tableModel.setValueAt("Largest(" + Integer.toString(numVerticesInLargestComponent) + ")", 5, 0);
				tableModel.setValueAt(Integer.toString(numLargestComponents), 5, 1);
				
				tableModel.setValueAt(Integer.toString(bfs.numConnectedComponentsWithAtLeastKVertices(k)), 6, 1);
			}
			else
			{
				if(e instanceof NumberFormatException)
				{
					setTextFields();
					showErrorMessage("Please enter correct number formats.");
				}
				else if(e instanceof ArithmeticException)
				{
					setTextFields();
					showErrorMessage("Cannot divide by zero.");
				}
			}
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
	}
	
}
