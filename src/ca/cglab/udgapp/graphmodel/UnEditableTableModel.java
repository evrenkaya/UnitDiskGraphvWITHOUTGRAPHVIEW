package ca.cglab.udgapp.graphmodel;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class UnEditableTableModel extends DefaultTableModel
{
	public UnEditableTableModel(Object[] columns, int numRows)
	{
		super(columns, numRows);
	}
	
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
