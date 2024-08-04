package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Matrix;
import model.MatrixException;

@SuppressWarnings("serial")
public class Hauptfenster extends JFrame
{
	private JMenuBar menuBar;
	private JButton zuruecksetzen, neuBerechnen;
	private JPanel matrixPanel, buttonPanel; 
	private JSlider slider;
	
	private Matrix matrixDefault;
	private MatrixPanel adjazenz;
	private MatrixPanel weg;
	private MatrixPanel distanz;
	private AusgabePanel ausgabePanel;
	
	
	public Hauptfenster()
	{
		try
		{
			init();
			initComponents();
			addComponents();
			setVisible(true);
		} catch (MatrixException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void init()
	{
		setTitle("Graphentheorie");
		setSize(950,740);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(2,1));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initComponents() throws MatrixException
	{
		matrixDefault = new Matrix();
		menuBar = new JMenuBar();
		slider = new JSlider(3, 15, Matrix.DEFAULT_SIZE);
		slider.setBorder(new TitledBorder("Wie viele Knoten?"));
		slider.setOpaque(false);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.addChangeListener(new ChangeListener()
		{
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				try
				{
					Matrix adjazenzMatrix = adjazenz.getMatrix();
					adjazenz.updateMatrix(new Matrix(slider.getValue()), false);
					weg.updateMatrix(adjazenzMatrix.wegMatrix(), true);
					distanz.updateMatrix(adjazenzMatrix.distanzMatrix(), true);
				} catch (MatrixException e1)
				{
					System.out.println(e1.getMessage());
				}
			}
		});
		
		zuruecksetzen = new JButton("Zuruecksetzen");
		zuruecksetzen.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Matrix leereMatrix = new Matrix();
					adjazenz.updateMatrix(leereMatrix, true);
					slider.setValue(leereMatrix.getDimension());
					distanz.updateMatrix(leereMatrix.distanzMatrix(), true);
					weg.updateMatrix(leereMatrix.wegMatrix(), true);
					ausgabePanel.leeren();
				} catch (MatrixException e1)
				{
					System.out.println(e1.getMessage());
				}
			}
		});
				
		neuBerechnen = new JButton("Berechnen");
		neuBerechnen.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Matrix adjazenzMatrix = adjazenz.getMatrix();
					weg.updateMatrix(adjazenzMatrix.wegMatrix(), true);
					distanz.updateMatrix(adjazenzMatrix.distanzMatrix(), true);
					ausgabePanel.updateAusgabe(adjazenzMatrix);
				} catch (MatrixException m)
				{
					System.out.println(m.getMessage());
				}
			}
		});
		ausgabePanel = new AusgabePanel();
		matrixPanel = new JPanel(new GridLayout());
		buttonPanel = new JPanel();
		adjazenz = new MatrixPanel("Adjazenzmatrix",matrixDefault,true,false);
		adjazenz.setSelectedColor(Color.LIGHT_GRAY); 
		weg = new MatrixPanel("Wegmatrix",matrixDefault.wegMatrix(),false,true);
		distanz = new MatrixPanel("Distanzmatrix",matrixDefault.distanzMatrix(),false,true);
	}
	
	private void addComponents() throws MatrixException
	{
		setJMenuBar(menuBar);
		buttonPanel.add(neuBerechnen);
		buttonPanel.add(zuruecksetzen);
		
		menuBar.setLayout(new GridLayout(1,2));
		menuBar.add(slider);
		menuBar.add(buttonPanel);
		
		matrixPanel.add(adjazenz);
		matrixPanel.add(weg);
		matrixPanel.add(distanz);	
		add(matrixPanel);
		add(ausgabePanel);
	}
	
	
	public static void main(String[] args)
	{
		new Hauptfenster(); 
	}

}
