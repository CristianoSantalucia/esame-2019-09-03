package it.polito.tdp.food.model;

/*
 * classe Model preimpostata 
 * questo documento è soggetto ai relativi diritti di ©Copyright
 * giugno 2021
 */ 
 
import java.util.*; 
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import it.polito.tdp.food.db.FoodDao;

public class Model
{
	private FoodDao dao;
	private Map<String, Portion> vertici; 
	private Graph<Portion, DefaultWeightedEdge> grafo; 
	
	public Model()
	{
		this.dao = new FoodDao();
	}
	
	public void creaGrafo(int calories)
	{
		// ripulisco mappa e grafo
		this.vertici = new HashMap<>(); 
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); // 
		
		/// vertici 
		this.dao.getVertici(vertici, calories); //riempio la mappa
		Graphs.addAllVertices(this.grafo, this.vertici.values()); 
		
		/// archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(vertici, calories));
		for (Adiacenza a : adiacenze)
		{
//			if(!a.getP1().equals(a.getP2()))
				Graphs.addEdge(this.grafo, a.getP1(), a.getP2(), a.getPeso());
		}
	}
	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public Collection<Portion> getVertici()
	{
		List<Portion> vertici = new ArrayList<>(this.grafo.vertexSet()); 
		vertici.sort((v1,v2)->v1.getPortion_id() - (v2.getPortion_id())); 
		return vertici;
	}
	public Collection<DefaultWeightedEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}
	public String stampaConnessi(Portion partenza)
	{
		String s = ""; 
		List<Portion> list = new ArrayList<>(Graphs.neighborListOf(this.grafo, partenza));
		list.sort((v1,v2)->v1.getPortion_id() - (v2.getPortion_id()));
		for (Portion p : list)
		{
			s += "\n" + p + " - peso:  " + grafo.getEdgeWeight(grafo.getEdge(p, partenza)); 
		}
		return s; 
	}
	
	//RICORSIONE

	private Integer passi; 
	private List<Portion> cammino; 
	public List<Portion> cercaCamminio(Portion partenza, Integer passi)
	{
		this.passi = passi; 
		this.cammino = new ArrayList<>();
		List<Portion> parziale = new ArrayList<>();
		parziale.add(partenza);
		this.cerca(parziale);
		return this.cammino;
	}
	
	public void cerca(List<Portion> parziale)
	{
		if(parziale.size() == this.passi)
		{
			if(calcolaPesoArchi(parziale) > calcolaPesoArchi(this.cammino))
			{
				this.cammino = new ArrayList<>(parziale); 
				return; 
			}
		}
		Portion ultimo = parziale.get(parziale.size()-1); 
		for(Portion p : Graphs.neighborListOf(this.grafo, ultimo))
		{
			if(!parziale.contains(p))
			{
				parziale.add(p);
				this.cerca(parziale);
				parziale.remove(p);
			}
		}
	}
	
	private Double calcolaPesoArchi(List<Portion> parziale)
	{
		Double peso = 0.0;
		if (parziale.size() > 1)
			for (int i = 1; i < parziale.size(); i++)
				peso += grafo.getEdgeWeight(grafo.getEdge(parziale.get(i-1), parziale.get(i))); 
		return peso;
	}
}