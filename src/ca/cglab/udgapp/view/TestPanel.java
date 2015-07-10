package ca.cglab.udgapp.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

import ca.cglab.udgapp.graphmodel.UnitDiskGraph;

@SuppressWarnings("serial")
public class TestPanel extends JPanel
{
	private UnitDiskGraph udg;
	
	public TestPanel(UnitDiskGraph udg)
	{
		this.udg = udg;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        Path2D path = new Path2D.Double();
        g2d.scale(800, 800);
        
        path.moveTo(50, 50);
        path.lineTo(75, 75);
        path.lineTo(50, 100);
        path.lineTo(25, 75);
        
        
        g2d.setColor(Color.red);
        //g2d.fill(path);
        for(int i = 0; i < udg.getAllPaths().size(); i++)
        {
        	g2d.fill(udg.getAllPaths().get(i));
        }
        
        g2d.dispose();
	}
}
