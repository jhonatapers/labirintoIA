package com.jhonatapers.labirinto.util.grafo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.lang.Math;

public class Grafo {

	public Nodo raiz;
	private Nodo raizOriginal;
	public Set<Nodo> objetivos;
	public Set<Nodo> caminhos;
	
	public Set<Nodo> solucoesComuns;
	
	
	
	public float custoCaminho;
	public int nodosPercorridos;
	public int profMax;
	public int frontMax;
	
	char[][] labirinto;
	Nodo[][] labirintoGrafo;

	


	public Grafo(char[][] lab)
	{

		labirinto=lab;		
		objetivos = new HashSet<Nodo>();
		caminhos = new HashSet<Nodo>();
		
		
		int largura = lab[0].length;
		int altura= lab.length;
		
		labirintoGrafo = new Nodo[altura+2][largura+2];
		
		for (int i = 0; i < altura; i++) {
			for (int j = 0; j < largura; j++) {
				
				Nodo temp = new Nodo(new Coord(i,j));
				
				switch (lab[i][j]) {
				case 'E':
					raiz = temp;
					caminhos.add(temp);
					break;
				case '0':
					caminhos.add(temp);
					break;
				case 'C':
					objetivos.add(temp);
					break;
				}
				
				labirintoGrafo[i+1][j+1]=temp;
			}
		}
		
		for (int i = 1; i < altura+1; i++) {
			for (int j = 1; j < largura+1; j++) {
				if (caminhos.contains(labirintoGrafo[i][j]) || objetivos.contains(labirintoGrafo[i][j])) 
				{
					if (caminhos.contains(labirintoGrafo[i-1][j]) || objetivos.contains(labirintoGrafo[i-1][j]))
					{
						labirintoGrafo[i][j].conecta(labirintoGrafo[i-1][j]);
					}
					
					if (caminhos.contains(labirintoGrafo[i+1][j]) || objetivos.contains(labirintoGrafo[i+1][j]))
					{
						labirintoGrafo[i][j].conecta(labirintoGrafo[i+1][j]);
					}
					
					if (caminhos.contains(labirintoGrafo[i][j-1])||objetivos.contains(labirintoGrafo[i][j-1]))
					{
						labirintoGrafo[i][j].conecta(labirintoGrafo[i][j-1]);
					}
					
					if (caminhos.contains(labirintoGrafo[i][j+1])||objetivos.contains(labirintoGrafo[i][j+1]))
					{
						labirintoGrafo[i][j].conecta(labirintoGrafo[i][j+1]);
					}
				}
				
				
			
			}
		}
		
		raizOriginal=raiz;
		
	}
	
	public void reset()
	{
		for (int i = 1; i < labirintoGrafo.length-1; i++) {
			for (int j = 1; j < labirintoGrafo[0].length-1; j++) {
				labirintoGrafo[i][j].reset();
				
			}
		}
	}

	
	private class NodoComparator implements Comparator<Nodo>
	{
	    @Override
	    public int compare(Nodo x, Nodo y)
	    {
	        if (x.custo < y.custo)
	        {
	            return -1;
	        }
	        if (x.custo > y.custo)
	        {
	            return 1;
	        }
	        return 0;
	    }
	}
	
	
	public void AEstrela()
	{
		
		custoCaminho=0;
		nodosPercorridos=0;
		profMax=0;
		frontMax=0;
		
		System.out.println("Sequencia de A* (Um para cada comida):");
		System.out.println("Início[" +raiz.coord._linha +"][" +raiz.coord._coluna+"] -> Destino[" 
		+objetivos.iterator().next().coord._linha +"][" + +objetivos.iterator().next().coord._coluna +"]" );

		Comparator<Nodo> comparador = new NodoComparator();
        PriorityQueue<Nodo> fila =  new PriorityQueue<Nodo>(10, comparador);
        
        raiz.custo=0;
        raiz.chamante=null;
        fila.add(raiz);
        

        while(!fila.isEmpty())
        {
			nodosPercorridos++;
			if (fila.size() > frontMax) {
				frontMax = fila.size();
			}
        	
        	
        	Nodo custo = fila.poll();
        	float custoAtual = custo.custo;

        	if (objetivos.contains(custo) )
        	{
        		while(!fila.isEmpty())
        		{
                	Nodo novoAtual = fila.poll();
                	float novoCustoAtual = novoAtual.custo;
                	
                	if (novoCustoAtual<custoAtual)
                	{
                		custo.custo=novoCustoAtual;
                		custo.chamante=novoAtual;
                	}
        		}
        		
        		break;
        	}
        	
        	
			for (Nodo e : custo.vizinhos) {
	        	
				if (e.custo > (float) (custoAtual + 1.0 + funcaoCusto(e))) {
					e.custo = (float) (custoAtual + 1.0 + funcaoCusto(e));
					fila.add(e);
					e.chamante=custo;
				}

			}
        }
        
        Set<Nodo> solucao = new HashSet<Nodo>();
        
		for (Nodo e : objetivos) {
			Nodo temp = e;
			while(temp!=null)
			{
				temp = temp.chamante;
				solucao.add(temp);
				custoCaminho+=1.0;
			}

		}
	
		solucoesComuns=solucao;
		
	}
	
	private float funcaoCusto(Nodo x)
	{
		Iterator<Nodo> it = objetivos.iterator();
		Nodo g = it.next();
		
		return (float)(Math.abs(x.coord._linha-g.coord._linha) + Math.abs(x.coord._coluna-g.coord._coluna));
		
		
	}
	
	public void printAEstrela()
	{
		
		int mapWidth = labirinto[0].length;
		int mapHeight= labirinto.length;
		
		System.out.println();

		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				if (objetivos.contains(labirintoGrafo[i+1][j+1]))
				{
					System.out.print("X");
				}
				else if (labirintoGrafo[i+1][j+1]==raiz)
				{
					System.out.print("E");
				}
				else if (solucoesComuns.contains(labirintoGrafo[i+1][j+1]))
				{
					System.out.print(".");
				}
				else
				{
					System.out.print(labirinto[i][j]);
				}
			}
			System.out.println();
		}
		
		solucoesComuns.clear();
	}

	public void resetRoot() {
		raiz=raizOriginal;
	}
}
