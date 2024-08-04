package view;


import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import model.*;

public class AusgabePanel extends JPanel
{	
	private static final long serialVersionUID = 4389777253809643469L;
	private static JTextPane ausgabe;
	
	public AusgabePanel()
	{
		init();
	}
	
	public void init()
	{
		setLayout(new GridLayout());
		ausgabe = new JTextPane();
		ausgabe.setBorder(new TitledBorder("Ausgabe"));
		ausgabe.setContentType("text/html");
		add(ausgabe);
	}
	
	public void leeren()
	{
		ausgabe.setText("");
	}
	
	public void updateAusgabe(Matrix matrix) throws MatrixException
	{
		String text = "<font size = 4 face = Verdana>";
		if(matrix.zusammenhaengend())
		{
			//Nur wenn der Graph zusammenhaengend ist werden Exzentrizitaeten,
			//Zentrum, Radius und Durchmesser ausgegeben da sonst bei den Exzentrizitaeten
			//und dem Radius der Wert "unendlich" vorkommt
			text += "<i>Graph ist zusammenhaengend!</i> <br>";
			text += "<b>Exzentrizitaeten: </b>";
			int[] exzentrizitaeten = matrix.exzentrizitaeten();
			String exz = "(" + exzentrizitaeten[0];
			for(int i = 1; i < exzentrizitaeten.length; i++)
			{
				exz += ", " +exzentrizitaeten[i];
			}
			exz += ")";
			text += exz + "<br>";
			text += "<b>Zentrum: </b>" + matrix.zentrum() + "<br>";
			text += "<b>Radius: </b>" + matrix.radius() + "<br>";
			text += "<b>Durchmesser: </b>" + matrix.durchmesser() + "<br>";
		}
		else
			text += "<i>Graph ist nicht zusammenhaengend!</i>" + "<br>";
		
		//Knotengrade ausgeben
		text += "<b>Knotengrade: </b>";
		int[] knotenGrade = matrix.getKnotengrade();
		String knoGra = "(" + knotenGrade[0];
		for(int i = 1; i < knotenGrade.length; i++)
		{
			knoGra +=", " + knotenGrade[i];
		}
		knoGra += ")";
		text += knoGra + "<br>";
		
		//Komponenten ausgeben
		ArrayList<ArrayList<Integer>> komp = matrix.komponenten();
		text += "<b>Komponenten(" + komp.size() + "): </b>";
		String kompo = "{" + komp.get(0);
		for(int i = 1; i < komp.size(); i++)
		{
			kompo += ", " + komp.get(i);
		}
		kompo += "}";
		text += kompo;
		
		//Artikulationen ausgeben
		ArrayList<Integer> art = matrix.artikulationen();
		text += "<br>" + "<b>Artikulationen("+ art.size() +"): </b>";
		text += art  +"";
		
		//Bruecken ausgeben
		ArrayList<ArrayList<Integer>> bruecken = matrix.bruecken();
		text += "<br>" + "<b>Bruecken(" + bruecken.size() + "): </b>";
		String brueck = "{ }";
		if (matrix.bruecken().size() > 0) 
		{
			brueck = "{" + bruecken.get(0).toString();

			for (int i = 1; i < bruecken.size(); i++) 
			{
				brueck += ", " + bruecken.get(i);
			}
			brueck += "}";
		}
		text += brueck + "<br>";		

		ausgabe.setText(text);
		ausgabe.setCaretPosition(0);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
