package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Matrix
{
	private int[][] matrix;
	public final static int DEFAULT_SIZE = 9;
	
	
	public Matrix(int size) throws MatrixException
	{
			setDimension(size);
	}
	
	public Matrix() throws MatrixException
	{	//Default Gr��e 9
		setDimension(DEFAULT_SIZE);
	}
	

//----------------------------------Getter/Setter----------------------------------------	
	
	public int getDimension()
	{
		return matrix.length;
	}
	
	public void setDimension(int dim) throws MatrixException
	{
		if(dim > 2 && dim <=15)
			matrix = new int[dim][dim];
		else
			throw new MatrixException("Ungueltige Knotenanzahl!(Mindestens 3x3, Hoechstens 15x15)");
	}
	
//----------------------------------------------------------------------------------------
	
	public int getWert(int row, int col) throws MatrixException 
	{
		if(row < 0 || row >= matrix.length)
			throw new MatrixException("Ungueltiger Zeilenindex!(Wert muss zwischen 0 & " +((int)matrix.length-1)+ " liegen!)");
		if(col <0 || col >= matrix.length)
			throw new MatrixException("Ungueltiger Spaltenindex!(Wert muss zwischen 0 & " +((int)matrix.length-1)+ " liegen!)");
		return matrix[row][col];
	}
	
	public void setWert(int row, int col, int wert) throws MatrixException
	{
		if(row < 0 || row >= matrix.length)
			throw new MatrixException("Ungueltiger Zeilenindex!(Wert muss zwischen 0 & " +((int)matrix.length-1)+ " liegen!)");
		if(col <0 || col >= matrix.length)
			throw new MatrixException("Ungueltiger Spaltenindex!(Wert muss zwischen 0 & " +((int)matrix.length-1)+ " liegen!)");
		matrix[row][col] = wert;
	}
	
//----------------------- Matrix Multiplikation ------------------------------------------
	
	public Matrix multiplyMatrix(Matrix multMatrix) throws MatrixException
	{
		//nur Matritzen der gleichen Groesse werden hier multipliziert
		if(multMatrix.getDimension() != getDimension())
		{
			throw new MatrixException("Um die Matrizen zu multiplizieren muessen sie gleich gross sein!");
		}

		Matrix ergebnisMatrix = new Matrix(getDimension());
		//die ergebnisMatrix wird fuer jede Zeile von links nach rechts aufgebaut
		for(int row = 0; row < ergebnisMatrix.getDimension(); row++)
		{
			for(int col = 0; col < ergebnisMatrix.matrix[row].length; col++)
			{
				for(int otherCol = 0; otherCol < ergebnisMatrix.getDimension(); otherCol++)
					ergebnisMatrix.matrix[row][col] += matrix[row][otherCol] * multMatrix.matrix[otherCol][col];
			}
		}
		return ergebnisMatrix;	 
	}

//---------------------------------------------------------------------------------------------------
	
	public Matrix(Matrix source) throws MatrixException 
	{
		//Matrix kopieren
		this(source.getDimension());
		for(int row = 0; row < source.getDimension(); row ++)
		{
			for(int col = 0; col < source.getDimension(); col++)
			{
				setWert(row, col, source.getWert(row, col));
			}
		}
	}
	
	public Matrix potenz(int p) throws MatrixException
	{
		//Matrix wird p mal multipliziert (fuer Weg - bzw. Distanzmatrix Berechnung)
		if(p < 1)
		{
			throw new MatrixException("Potenz muss mindestens 1 sein! (�bergeben: " +p);
		}
		
		Matrix pMatrix = new Matrix(this);
		for(int i = 1; i < p; i++)
		{
			pMatrix = pMatrix.multiplyMatrix(this);
		}
		return pMatrix;
	}
	
//------------------ Knotengrade berechnen ------------------------------------------------------------------
	
	public int getKnotengrad(int knoten) throws MatrixException
	{
		//Algorithmus: Knotengrad ist die Summe einer Zeile der Adjazenzmatrix
		int grad = 0;
		for(int i = 0; i < getDimension(); i++)
		{
			grad += getWert(knoten, i);
		}
		return grad;
	}
	
	public int[] getKnotengrade() throws MatrixException
	{
		int grade[] = new int[getDimension()];
		for(int i = 0; i < getDimension(); i++)
		{
			grade[i] = getKnotengrad(i);
		}
		return grade;	 
	}
	
//---------------------- Wegmatrix und Distanzmatrix ---------------------------------------------------------------	
	
	public Matrix wegMatrix() throws MatrixException 
	{
		//Algorithmus: Diagonale 1 setzen, die Adjazenzmatrix wird Dimension-1 mal multipliziert
		//sobald ein Wert groesser 0 in der Adjazenzmatrix zu finden ist wird an der selben Stelle
		//der Wegmatrix der Wert auf 1 gesetzt. Beendet wenn alle Felder 1 sind oder keine Aenderungen mehr passieren
		Matrix wMatrix = new Matrix(getDimension());
			for(int row = 0; row < wMatrix.getDimension(); row++)
			{
				for(int col = 0; col < wMatrix.getDimension(); col++)
				{
					if(col == row)
					{
						//Diagonale ueberall 1 setzen
						wMatrix.setWert(row, col, 1);
					}
				}
			}
			for(int potenz = 1; potenz < wMatrix.getDimension(); potenz++)
			{
				boolean geaendert = false;
				boolean fertig = true;
				//Die Adjazenzmatrix wird "potenz" mal multipliziert und in potenzMatrix gespeichert
				Matrix potenzMatrix = potenz(potenz);
				
				for(int row = 0; row < wMatrix.getDimension(); row++)
				{
					for(int col = 0; col < wMatrix.getDimension(); col++)
					{
						if(wMatrix.getWert(row, col) == 0)
						{
							//solange es 0er in der Wegmatrix sind
							//-> weiter berechnen
							fertig = false;
						}
					}
				}
				for(int row = 0; row < wMatrix.getDimension(); row++)
				{
					for(int col = 0; col < wMatrix.getDimension(); col++)
					{
						if(potenzMatrix.getWert(row, col) > 0)
						{	
							//Wenn in der Potenzmatrix ein anderer Wert als 0 steht
							//existiert ein Weg -> Wert in Wegmatrix auf 1 setzen
							wMatrix.setWert(row, col, 1);
							geaendert = true;
						}
					}
				}
				if(!geaendert) //wenn keine Aenderungen mehr passieren kann beendet werden
					break;
				if(fertig) //wenn der Wert 0 nicht mehr vorkommt kann beendet werden
					break;
			}
		return wMatrix;
	}
	
	
	
	public Matrix distanzMatrix() throws MatrixException
	{
		//Algorithmus: Diagonale 0 setzen, die Adjazenzmatrix wird Dimension-1 mal multipliziert
		//sobald ein Wert groesser 0 in der Adjazenzmatrix zu finden ist wird an der selben Stelle
		//der Distanz Matrix der Wert auf die Potenz der Adjazenzmatrix gesetzt. Beendet wenn
		//kein "undendlich" mehr vorhanden oder keine Aenderungen mehr passieren
		Matrix dMatrix = new Matrix(getDimension());
		for(int row = 0; row < dMatrix.getDimension(); row++)
		{
			for(int col = 0; col < dMatrix.getDimension(); col++)
			{
				if(col == row)
				{
					//Diagonale ueberall 0 setzen
					dMatrix.setWert(row, col, 0);
				}
				else
					//Integer.MIN_VALUE entspricht -2147483648
					//und wird hier statt "unendlich verwendet"
					//Integer.MAX_VALUE kann nicht verwendet werden da sonst der Algorithmus
					//um die Exzentrizitaeten zu berechnen nicht mehr funktioniert
					dMatrix.setWert(row, col, Integer.MIN_VALUE);
			}
		}
		for(int potenz = 1; potenz < dMatrix.getDimension(); potenz++)
		{
			//Der laengste Weg kann hoechstens der Laenge bzw. Breite der Matrix - 1 entsprechen
			boolean geaendert = false; //Um zu ueberpruefen ob noch Aenderungen passieren
			boolean fertig = true; //Um zu erkennen wann die Berechnungen beendet sind
			//Die Adjazenzmatrix wird "potenz" mal multipliziert und in potenzMatrix gespeichert
			Matrix potenzMatrix = potenz(potenz);
			
			//Distanzmatrix nach dem Wert "unendlich" durchsuchen um herauszufinden
			//ob die Berechnungen fortgesetzt werden muessen
			for(int row = 0; row < dMatrix.getDimension(); row++)
			{
				for(int col = 0; col < dMatrix.getDimension(); col++)
				{
					if(dMatrix.getWert(row, col) == Integer.MIN_VALUE)
					{
						//so lange in der Distanzmatrix noch "unendlich" vorkommt
						//muss weiter gerechnet werden
						fertig = false;
					}
				}
			}
			for(int row = 0; row < dMatrix.getDimension(); row++)
			{
				for(int col = 0; col < dMatrix.getDimension(); col++)
				{
					if(potenzMatrix.getWert(row, col) > 0 && dMatrix.getWert(row, col) == Integer.MIN_VALUE)
					{	
						//Wenn in der Potenzmatrix ein Wert steht der gr��er als Null ist und in der Distanzmatrix
						//an dieser Stelle der Wert "unendlich" steht, wird "unendlich" durch die Potenz der Adjazenzmatrix ersetzt
						dMatrix.setWert(row, col, potenz);
						geaendert = true;
					}
				}
			}
			if(!geaendert) //wenn keine Aenderungen mehr passieren kann beendet werden
				break;
			if(fertig) //wenn der Wert "unendlich" nicht mehr vorkommt kann beendet werden
				break;
		}		
		return dMatrix;
	}
	
//------------------------------ Ausgabe Methoden --------------------------------------------------------	
	
	public int[] exzentrizitaeten() throws MatrixException
	{
		//Algorithmus: Jede Reihe der Distanzmatrix durchgehen und 
		//jeweils das Maximum der Zeile in einem Array speichern.
		//Hier muss keine List verwendet werden weil die Groesse
		//des Arrays der Laenge bzw. der Breite der Distanzmatrix entspricht
		Matrix distanzMatrix = distanzMatrix();
		int[] exz = new int[distanzMatrix.getDimension()];
		for(int row = 0; row < distanzMatrix.getDimension(); row++)
		{
			int max = 0; //Fuer jede Zeile wird ein Maximum Wert gespeichert
			for(int col = 0; col < distanzMatrix.getDimension(); col++)
			{
				if(distanzMatrix.getWert(row, col) > max)
					max = distanzMatrix.getWert(row, col);
			}
			exz[row] = max; //Das Maximum der Zeile wird in das Array gespeichert
		}
		return exz;
	}
	
	public int radius() throws MatrixException
	{
		//Algorithmus: Das Exzentrizitaeten Array wird nach dem
		//kleinsten Wert durchsucht
		int[] exz = exzentrizitaeten();
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < exz.length; i++)
		{
			if(exz[i] < min)
				min = exz[i];
		}
		return min;
	}
	
	public int durchmesser() throws MatrixException
	{
		//Algorithmus: Das Exzentrizitaeten Array wird nach dem
		//groessten Wert durchsucht
		int[] exz = exzentrizitaeten();
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < exz.length; i++)
		{
			if(exz[i] > max)
				max = exz[i];
		}
		return max;
	}
	
	public ArrayList<Integer> zentrum() throws MatrixException
	{
		//Algorithmus: Das Exzentrizitaeten Array nach allen Werten
		//durchsuchen die dem Radius entsprechen
		//List weil Groesse nicht bekannt
		ArrayList<Integer> zentrum = new ArrayList<>();
		int[] exz = exzentrizitaeten();
		int radius = radius(); 
		
		for(int i = 0; i < exzentrizitaeten().length; i++)
		{
			if(exz[i] == radius)
				zentrum.add(i+1);
		}
		return zentrum;
	}
	
	public boolean zusammenhaengend() throws MatrixException
	{
		//Wenn in der Weg-Matrix ueberall 1er stehen -> Graph zusammenhaengend!
		Matrix wegMatrix = wegMatrix();
		for(int row = 0; row < wegMatrix.getDimension(); row++)
		{
			for(int col = 0; col < wegMatrix.getDimension(); col++)
			{
				if(wegMatrix.getWert(row, col) != 1)
					return false;
			}
		}
		return true;
	}
	
	public ArrayList<ArrayList<Integer>> komponenten(Matrix wegMatrix) throws MatrixException
	{
		ArrayList<ArrayList<Integer>> komponenten = new ArrayList<>();
		for(int row = 0; row < wegMatrix.getDimension(); row++)
		{
			ArrayList<Integer> neueKomponente = new ArrayList<>();
			for(int col = 0; col < wegMatrix.getDimension(); col++)
			{
				if(wegMatrix.getWert(row, col) == 1)
					neueKomponente.add(col+1);
			}
			if(!neueKomponente.isEmpty()) //Nur speichern wenn das Komponenten Array nicht leer ist
			{	
				boolean speichern = true;
				Iterator<ArrayList<Integer>> it = komponenten.iterator();
				while(it.hasNext()) //Ueberpruefen ob diese Komponente bereits existiert
				{
					ArrayList<Integer> komp = it.next();
					if(komp.containsAll(neueKomponente))
					{	
						//wenn bereits eine Komponente mit denselben Knoten existiert
						//wird die neue nicht gespeichert und die Schleife abgebrochen
						speichern = false;
						break;
					}
					if(neueKomponente.containsAll(komp))
					{
						//wenn die neue Komponente alle Knoten (und mehr) als eine bereits
						//bestehende Kompenente besitzt wird die bestehende geloescht
						it.remove();
					}
				}
				if(speichern)
				{
					komponenten.add(neueKomponente); //neue Komponente hinzufuegen
				}
			}
		}
		return komponenten;
	}
	
	public ArrayList<ArrayList<Integer>> komponenten() throws MatrixException
	{
		return komponenten(wegMatrix());
	}
	
	public ArrayList<Integer> artikulationen() throws MatrixException
	{
		//Algorithmus: Jeden Knoten einzeln loeschen und ueberpruefen, ob nach dem
		//Loeschen mehr Komponenten existieren als davor.
		int anzKomponenten = komponenten().size();
		ArrayList<Integer> artikulationen = new ArrayList<>();
		for(int knoten = 0; knoten < getDimension(); knoten++)
		{
			Matrix hilf = new Matrix(this); //Hilfsmatrix initialisieren um Knoten loeschen zu koennen
			if(getKnotengrad(knoten) >= 2) //nur Knoten ab Grad 2 koennen Artikulationen sein
			{
				hilf.delKnoten(knoten);
				ArrayList<ArrayList<Integer>> neuAnzKomp = komponenten(hilf.wegMatrix());
				if(neuAnzKomp.size()-1 > anzKomponenten) //Wenn nach dem loeschen mehr Komponenten als zuvor -> Artikulation
					artikulationen.add(knoten + 1);
			}	
		}
		return artikulationen; 
	}
	 
	
	public ArrayList<ArrayList<Integer>> bruecken() throws MatrixException
	{
		//Algorithmus: Jede Kante einzeln loeschen und ueberpruefen, ob nach dem
		//Loeschen mehr Komponenten existieren als davor.
		int anzKomponenten = komponenten().size();
		ArrayList<ArrayList<Integer>> bruecken = new ArrayList<>();
		Matrix hilf = new Matrix(this); //Hilfsmatrix initialisieren um Kanten loeschen zu koennen
		for(int row = 0; row < hilf.getDimension(); row++)
		{
			for(int col = 0; col < hilf.getDimension(); col++)
			{
				if(hilf.getWert(row, col) == 1)
				{
					hilf.delKante(row, col); //Wenn eine Kante gefunden wurde wird diese geloescht
					if(komponenten(hilf.wegMatrix()).size() > anzKomponenten)
					{	//Wenn mehr Komponenten:
						ArrayList<Integer> bruecke = new ArrayList<>();
						if(col > row)
						{
							bruecke.add(row + 1);
							bruecke.add(col + 1);
						}
						if(row > col) //Sorgt dafuer, dass bei den Kanten immer die
						{             //kleinere Zahl zuerest in das Array aufgenommen wird.
							bruecke.add(col + 1);
							bruecke.add(row + 1);
						}
						if(!bruecken.contains(bruecke)) //Wenn diese Kante noch nicht im Bruecken Array vorhanden ist
						{                               //-> speichern
							bruecken.add(bruecke);
						}
					}
					hilf.addKante(row, col);
				}
			}
		}
		return bruecken;
	}

	
//------------------------ Hilfs - Methoden ----------------------------------------------------------------
	
	
	public void addKante(int knoten1,int knoten2) throws MatrixException
	{
		setWert(knoten1, knoten2, 1);
		setWert(knoten2, knoten1, 1);
	}
	
	public void delKante(int knoten1, int knoten2) throws MatrixException
	{
		setWert(knoten1, knoten2, 0);
		setWert(knoten2, knoten1, 0);
	}
	
		
	public void delKnoten(int knoten) throws MatrixException
	{
		for(int i = 0; i < getDimension(); i++)
		{
			setWert(knoten, i, 0);
			setWert(i, knoten, 0);
		}
	}

	
//-------------------------------------------------------------------------------	
	
	public void print() //nur zu Testzwecken
	{
		for(int i=0;i<matrix.length;i++)
		{
		    for(int j=0;j<matrix.length;j++) 
		    {
		        System.out.print(matrix[i][j]+" ");
		    }
		    System.out.println("");
		}
	}






}


