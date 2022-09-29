package com.jhonatapers.labirinto.util.grafo;

public class Coord {
	
	
	public int _linha;
	public int _coluna;

	public Coord(int linha, int coluna) {
		_linha=linha;
		_coluna=coluna;
	}
	
	public Coord(Coord coord) {
		_linha=coord._linha;
		_coluna=coord._coluna;
	}
	
	public boolean equals(Object O)
	{	
		
		if (!(O instanceof Coord))
		{
			return false;
		}
		
		
		Coord coordComparada = (Coord) O;
		
		if (coordComparada._linha==_linha && coordComparada._coluna ==_coluna )
		{
			return true;
		}
		
		return false;
	}

}
