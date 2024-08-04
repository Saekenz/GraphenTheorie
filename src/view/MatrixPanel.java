package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import model.Matrix;
import model.MatrixException;

public class MatrixPanel extends JPanel
{
	private static final long serialVersionUID = -1762062309144972876L;
	private Matrix hilfMatrix;
	private boolean modifyDiagonal;
	private TitledBorder border;
	private JButton[][] buttons = new JButton[15][15];
	private int[][] werte = new int[15][15];
	private Color selectedColor = Color.WHITE;
	private final Color notSelectedColor = Color.WHITE;
	private final Color disabledColor = Color.LIGHT_GRAY;
	private final Color lockedColor = Color.GRAY;
	
	public MatrixPanel(String title, Matrix matrix, boolean modifiable, boolean modifyDiagonal) throws MatrixException
	{
		if(title == null || title.isEmpty())
		{
			throw new MatrixException("Der uebergebene Titel war NULL!");
		}
		if(matrix == null)
		{
			throw new MatrixException("Die uebergebene Matrix war NULL!");
		}
		
		border = new TitledBorder(title);
		setBorder(border);
		setLayout(new GridLayout(16, 16));
		this.modifyDiagonal = modifyDiagonal;
		
		add(new JLabel());
		for(int col = 0; col < buttons[0].length; col++)
		{
			add(new JLabel(String.format("%02d", col + 1)));
		}
		for(int row = 0; row < buttons.length; row++)
		{
			add(new JLabel(String.format("%02d", row + 1)));
			for(int col = 0; col < buttons[row].length; col++)
			{
				add(buttons[row][col] = new JButton());
				buttons[row][col].setBorder(new EtchedBorder());
				
				final int ro = row;
				final int co = col;
				
				
				if(modifiable)
				{
					buttons[row][col].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							werte[ro][co] = (werte[ro][co] + 1) % 2;
							werte[co][ro] = (werte[co][ro] + 1) % 2;
							buttons[ro][co].setBackground(werte[ro][co] == 0 ? notSelectedColor : selectedColor);
							buttons[ro][co].setText("<html>" + werte[ro][co] + "</html>");
							buttons[co][ro].setBackground(werte[ro][co] == 0 ? notSelectedColor : selectedColor);
							buttons[co][ro].setText("<html>" + werte[ro][co] + "</html>");
						}
					});
				}	
			}
		}
		hilfMatrix = matrix;
		update(true);
	}
	
	public void update(boolean update) throws MatrixException
	{
		if(update)
		{
			werte = new int[15][15];
		}
		for(int row = 0; row < buttons.length; row++)
		{
			for(int col = 0; col < buttons[row].length; col++)
			{
				if(row == col && !modifyDiagonal)
				{
					setEnabled(false);
				}
				if(row < hilfMatrix.getDimension() && col < hilfMatrix.getDimension())
				{
					if(update)
					{
						werte[row][col] = hilfMatrix.getWert(row, col);
					}
					if(row == col && !modifyDiagonal)
					{
						buttons[row][col].setBackground(lockedColor);
						buttons[row][col].setText("<html><font color = white>0</font/></html>");
					}
					else
					{
						buttons[row][col].setEnabled(true);
						buttons[row][col].setBackground(werte[row][col] == 0 ? notSelectedColor : selectedColor);
						if(werte[row][col] == Integer.MIN_VALUE)
						{
							buttons[row][col].setText("<html>&infin;</html>");
							buttons[row][col].setBackground(notSelectedColor);
						}
						else
							if(werte[row][col] > 99)
							{
								buttons[row][col].setText("<html>##</html>");
								buttons[row][col].setBackground(selectedColor);
							}
							else
								buttons[row][col].setText(werte[row][col]+"");
					}
				}
				else
				{
					buttons[row][col].setEnabled(false);
					buttons[row][col].setBackground(disabledColor);
					buttons[row][col].setText("");
				}
				boolean enable = 
						row != col &&
						col < hilfMatrix.getDimension() &&
						row < hilfMatrix.getDimension();
				buttons[row][col].setEnabled(enable);
			}
		}
	}
	
	public void updateMatrix(Matrix matrix, boolean update) throws MatrixException
	{
		if(matrix == null)
			throw new MatrixException("Die uebergebene Matrix war NULL!");
		
		hilfMatrix = matrix;
		update(update);
	}
	
	public void updateSize(int size) throws MatrixException
	{
		if(size > 0 && size <= 15)
			updateMatrix(new Matrix(size), false);
	}
	
	public Matrix getMatrix() throws MatrixException
	{
		int size = 0;
		for(int i = 0; i < buttons.length && buttons[i][i].getBackground() == lockedColor; size = ++i)
		{
			
		}
		Matrix matrix = new Matrix(size);
		for(int row = 0; row < size; row++)
		{
			for(int col = 0; col < size; col++)
				matrix.setWert(row, col, werte[row][col]);
		}
		return matrix;
	}
	
	public void setSelectedColor(Color col) throws MatrixException
	{
		this.selectedColor = col;
		update(false);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
