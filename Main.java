package Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

	
	public static void main(String[] args) {

		Graph g = new ListGraph(10, false);
		
		// 0: Graz
		// 1: Wien
		// 2: Klagenfurt
		// 3: Salzburg
		// 4: Innschbruck
		// 5: St. Pölten
		// 6: Linz
		
		g.addEdge(0, 1, 200);
		g.addEdge(0, 2, 130);
		g.addEdge(2, 3, 230);
		g.addEdge(1, 6, 150);
		g.addEdge(6, 3, 140);
		g.addEdge(3, 4, 200);
		g.addEdge(2, 4, 200);
		g.addEdge(0, 4, 1000);
		
		//findByTiefenSucheRekursiv(g, 0, 4);
		
		// findByTiefenSuche(g, 0, 4);
		findByBreitenSuche(g, 0, 4);
		
		//findByBreitenSuche(g, 0, 4);
		//dijkstra(g, 0, 4);
		
		
	}
	
	private static void findByTiefenSucheRekursiv(Graph g, int von, int nach) {
		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		
		// pred[5] = 0
		// Wir besuchen 5 über 0
		
		_findByTiefenSucheRekursiv(g, von, nach, visited, pred);
		
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
	}
	
	private static boolean _findByTiefenSucheRekursiv(
			Graph g, int current, int nach, 
			boolean[] visited, int[] pred) {
		
		boolean ok;
		
		if (current == nach) return true;
		
		visited[current] = true;
		
		List<WeightedEdge> nachbarn = g.getEdges(current);
				
		for(WeightedEdge n: nachbarn) {
			int next = n.vertex;
			if (!visited[next]) {
				pred[next] = current;
				ok = _findByTiefenSucheRekursiv(g, next, nach, visited, pred);
				if (ok) return true;
			}
		}
		
		return false;
	}
	
	
	private static void findByTiefenSuche(
					Graph g, int von, int nach) {
		
		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		
		for(int i=0; i<pred.length; i++) {
			pred[i] = -1;
		}
		
		Stack<Integer> stack = new Stack<Integer>(); 
		
		// pred[5] = 0
		// Wir besuchen 5 über 0
		stack.push(von);
		
		while( !stack.isEmpty() ) {
			
			int current = stack.pop();
			
			visited[current] = true;
			
			if (current == nach) {
				break;
			}
			
			List<WeightedEdge> nachbarn = g.getEdges(current);
			for(WeightedEdge n: nachbarn) {
				if (!visited[n.vertex]) {
					pred[n.vertex] = current;
					stack.push(n.vertex);
				}
			}
		
		}
		
		
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
		
		
	}
	

	
	
	
	
	private static void findByBreitenSuche(
			Graph g, int von, int nach) {
		
		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		
		for(int i=0; i<pred.length; i++) {
			pred[i] = -1;
		}
		
		ArrayDeque<Integer> queue = new ArrayDeque<Integer>();
		
		// pred[5] = 0
		// Wir besuchen 5 über 0
		queue.add(von);
		
		OUTER: while( !queue.isEmpty() ) {
			
			int current = queue.poll();
			
			visited[current] = true;
			
			if (current == nach) {
				break;
			}
			
			List<WeightedEdge> nachbarn = g.getEdges(current);
			for(WeightedEdge n: nachbarn) {
				if (!visited[n.vertex]) {
					pred[n.vertex] = current;
					if (n.vertex == nach) {
						break OUTER;
					}
					queue.add(n.vertex);
				}
			}
		
		}
		
		
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}


	}

	
	private static ArrayList<Integer> predToWay(int[] pred, int from, int to) {
		
		ArrayList<Integer> way = new ArrayList<Integer>(); 
		
		int i = to;
		while (i != from) {
			way.add(0, i);
			i = pred[i];
		}
		way.add(0, from);
		
		return way;
	}
	
	// Variante mit Heap für lichte Graphen
	private static void dijkstra(Graph g, int von, int nach) {
		
		int[] pred = new int[g.numVertices()];
		int[] dist = new int[g.numVertices()];
		boolean[] visited = new boolean[g.numVertices()];
		
	
		VertexHeap heap = new VertexHeap(g.numVertices());
		for(int i=0; i<dist.length; i++) {
			dist[i] = 99999;
			heap.insert(new WeightedEdge(i, 99999));
			pred[i] = -1;
		}
	
		dist[von] = 0;
		heap.setPriority(0, 0);
		
		while(!heap.isEmpty()) {
			
			WeightedEdge cur = heap.remove();
			
			if (cur.vertex == nach) break;
			
			List<WeightedEdge> nachbarn = g.getEdges(cur.vertex);
			
			for(WeightedEdge nachbar: nachbarn) {
				int distBisHier = cur.weight;
				int distZumNachbar = nachbar.weight;
				
				int distInsg = distBisHier + distZumNachbar;
				
				if (distInsg < dist[nachbar.vertex] ) {
					
					dist[nachbar.vertex] = distInsg;
					heap.setPriority(nachbar.vertex, distInsg);
					
					pred[nachbar.vertex] = cur.vertex;
				}
			}
		}
		
		// pred ausgeben
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
		
		
		// Way ausgeben
		System.out.println();
		ArrayList<Integer> way = predToWay(pred, von, nach);
		for(int vertexNumber: way) {
			System.out.print(vertexNumber + " ");
		}
		System.out.println();
		
	}
	
	

}
