package ca.cglab.udgapp.view;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

public class GraphProgramConstants
{
	////Begin Static Variables ////
	public static final String PROGRAM_TITLE = "Unit Disk Square";
	
	public static final String UNICODE_EPSILON = "\u03B5";
	
	public static final String INSTRUCTIONS = 
	"<html>"+
	"<h2><u>Summary</u></h2>"+
	"<div>This program stores a unit disk graph in memory and allows it to be manipulated.</div>"+
	"<div>Once the program starts, all the vertices are distributed uniformly within the unit square.</div>"+
	"<div>Each vertex has x,y coordinates between 0 and 1. (0 - inclusive, 1 - exclusive)</div>"+
	"<div>Two vertices are connected by an edge if the distance between them is at most the radius <i>r</i>.</div>"+
	"<br>"+
	"<div>Note: If program is unresponsive after update is pressed, quit by going to Help -> Quit.</div>"+
	"<br>"+
	"<br>"+
	"<h3>Variables: </h3>"+
	"<ul>"+
	"<li><i>n</i> - Number of vertices</li>"+
	"<li><i>k</i> - Least number of vertices to check for in connected components</li>"+
	"<li><i>a</i> - Used in calculating <i>r</i></li>"+
	"<li><i>b</i> - Used in calculating <i>r</i></li>"+
	"<li><i>" + UNICODE_EPSILON + "</i> - Used in calculating <i>r</i></li>"+
	"<li><i>r</i> - Radius/Distance threshold</li>"+
	"</ul>"+
	"<div><i>r</i> is calculated as follows: </div>"+
	"<div><font color='blue'>r = 1 / n pow ((a / b) +" + UNICODE_EPSILON + "))</font></div>"+
	"<br>"+
	"<div>The update button creates a new set of <i>n</i> random vertices,</div>"+
	"<div>calculates the new value of <i>r</i>, creates the new edges and then shows new statistics.</div>"+
	"<br>"+
	"<br>"+
	"<div><i>Program Creator: Evren Kaya</i></div>"+
	"<div><i>Creation Date: June - July, 2015</i></div>"+
	"</html>";
	
	public static final JLabel INSTRUCTIONS_LABEL = new JLabel(INSTRUCTIONS);

	
	public static final Font TITLE_FONT;
	
	static
	{
		Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
		fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		TITLE_FONT = new Font("arial", Font.BOLD, 20).deriveFont(fontAttributes);
	}
	
	public static final Font COMMON_LABEL_FONT = new Font("arial", Font.ITALIC, 20);
	
	public static final Font TEXT_FIELD_FONT = new Font("arial", Font.PLAIN, 14);
	
	static
	{
		INSTRUCTIONS_LABEL.setFont(TEXT_FIELD_FONT);
	}
	//// End Static Variables ////
	
	private GraphProgramConstants() {}
}
