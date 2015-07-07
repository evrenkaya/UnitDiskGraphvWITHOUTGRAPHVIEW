package ca.cglab.udgapp;

import javax.swing.SwingUtilities;

import ca.cglab.udgapp.view.GraphProgram;

public class GraphProgramLauncher
{

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new GraphProgram().createAndShowGUI();
			}
		});
	}

}
