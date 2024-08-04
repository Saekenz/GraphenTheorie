package model;
import view.*;
import java.util.Arrays;

import model.Matrix;

public class MatrixTest
{

	public static void main(String[] args)
	{
		try
		{
			Matrix m = new Matrix(7);

			m.addKante(1,4);
			m.addKante(0,2);
			m.addKante(2,3);
			m.addKante(5,6);
			
			System.out.println("Adjazenzmatrix");
			System.out.println();
			m.print();
			System.out.println();

			int[] knotengrade = m.getKnotengrade();
			for(int i = 0; i < knotengrade.length; i++)
				System.out.print(knotengrade[i]);

			System.out.println();
			System.out.println();
			Matrix wegMatrix = m.wegMatrix();
			wegMatrix.print();
			System.out.println();

			Matrix distanzMatrix = m.distanzMatrix();
			distanzMatrix.print();
			System.out.println();

			int[] exzentrizitaeten = m.exzentrizitaeten();
			for(int i = 0; i < knotengrade.length; i++)
				System.out.print(exzentrizitaeten[i]);

			System.out.println();
			System.out.println();
			System.out.println("Durchmesser: " +m.durchmesser() +", Radius: "+ m.radius());
			System.out.println();
			System.out.println(m.zentrum());
			System.out.println();

			if(m.zusammenhaengend())
				System.out.println("Zusammenhaengender Graph!");
			else
				System.out.println("Nicht zusammenhaengender Graph!");

			System.out.println();
			System.out.println(m.komponenten());
			System.out.println();
			System.out.println(m.artikulationen());
			System.out.println();
			System.out.println(m.bruecken());


        }
		catch (MatrixException e)
		{
		System.out.println(e.getMessage());  
		}
			
	}

}
