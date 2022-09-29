package com.jhonatapers.labirinto.util.grafo;

import java.util.HashSet;
import java.util.Set;
import java.lang.Float;


public class Nodo {
	
	public Coord coord;
	
	public Set<Nodo> vizinhos;
	public Nodo chamante;
	public boolean foiVisitado;
	public boolean noCaminho;
	
	public float custo;
	public Nodo(Coord input) {
		vizinhos= new HashSet<Nodo>();
		foiVisitado=false;
		noCaminho=false;
		chamante=null;
		custo=Float.MAX_VALUE;
		coord = new Coord(input);
  }
	
	public void reset()
	{
		foiVisitado=false;
		noCaminho=false;
		chamante=null;
		custo=Float.MAX_VALUE;
	}
	
	public void conecta(Nodo outroNodo)
	{
		if (!vizinhos.contains(outroNodo))
		{
			vizinhos.add(outroNodo);
		}
		
		if(!outroNodo.vizinhos.contains(this))
		{
			outroNodo.vizinhos.add(this);
		}
		
	}
	
	public boolean equals(Object O)
	{
		
		if (!(O instanceof Nodo))
		{
			return false;
		}
		
		
		Nodo outroNodo = (Nodo) O;
		
		if (coord.equals(outroNodo.coord) )
		{
			return true;
		}
		
		return false;
	}

}
