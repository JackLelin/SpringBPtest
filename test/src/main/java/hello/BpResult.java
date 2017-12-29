package hello;

import java.util.HashMap;
import java.util.HashSet;


public class BpResult 
{
	HashMap<String,Double> concept_nodes_marginal = new HashMap<String,Double> ();
	HashMap<HashSet<String>,Double[][]> undirected_edges_marginal = new HashMap<HashSet<String>,Double[][]>();

	public HashMap<String,Double> get_concept_nodes_marginal() {
        return concept_nodes_marginal;
    }

    public HashMap<HashSet<String>,Double[][]> get_undirected_edges_marginal() {
        return undirected_edges_marginal;
    }
	
	public BpResult( int module)
	{
		HashMap<String,Integer> observation = new HashMap<String,Integer>();
		HashMap<String,HashMap<String,HashSet<String>>> concept_nodes_nbr =  new HashMap<String,HashMap<String,HashSet<String>>>();
		HashMap<HashSet<String>,Double[][]> undirected_edges = new HashMap<HashSet<String>,Double[][]>();
		HashMap<String,String> problem_nodes_nbr = null;
		HashMap<String,Double[]> directed_edges = new HashMap<String,Double[]> ();

		if ( module == 1)
		{
			observation.put("a",1);
			observation.put("ab",1);
			observation.put("abc",0);
		
			HashMap<String,HashSet<String>> c_a = new HashMap<String,HashSet<String>>();
				HashSet<String> c_a_p1 = new HashSet<String>();
				c_a_p1.add("a");
				HashSet<String> c_a_c1 = new HashSet<String>();
				c_a_c1.add("a^b");
				c_a_c1.add("a^b^c");
				
			c_a.put("problem", c_a_p1);
			c_a.put("concept",c_a_c1);
			
			HashMap<String,HashSet<String>> c_ab = new HashMap<String,HashSet<String>>();
				HashSet<String> c_ab_p = new HashSet<String>();
				c_ab_p.add("ab");
				HashSet<String> c_ab_c = new HashSet<String>();
				c_ab_c.add("a");
				c_ab_c.add("a^b^c");
		
			c_ab.put("problem", c_ab_p);
			c_ab.put("concept", c_ab_c);
			
			HashMap<String,HashSet<String>> c_abc = new HashMap<String,HashSet<String>>();
				HashSet<String> c_abc_p = new HashSet<String>();
				c_abc_p.add("abc");
				HashSet<String> c_abc_c = new HashSet<String>();
				c_abc_c.add("a");
				c_abc_c.add("a^b");
	
			c_abc.put("problem", c_abc_p);
			c_abc.put("concept", c_abc_c);
			
			concept_nodes_nbr.put("a", c_a);
			concept_nodes_nbr.put("a^b", c_ab);
			concept_nodes_nbr.put("a^b^c", c_abc);
	        
			
				HashSet<String> e_ab_a = new HashSet<String>();
				e_ab_a.add("a^b");
				e_ab_a.add("a");
				Double[][] e_ab_a_p = new Double[][] {{0.2597402597402597,0.1974025974025976},{0.3246753246753247,0.38961038961038963}};
				
				HashSet<String> e_ab_abc = new HashSet<String>();
				e_ab_abc.add("a^b");
				e_ab_abc.add("a^b^c");
				Double[][] e_ab_abc_p = new Double[][] {{0.2597402597402597,0.1974025974025976},{0.3246753246753247,0.38961038961038963}};
				
				HashSet<String> e_a_abc = new HashSet<String>();
				e_a_abc.add("a");
				e_a_abc.add("a^b^c");
				Double[][] e_a_abc_p = new Double[][] {{0.2597402597402597,0.1974025974025976},{0.3246753246753247,0.38961038961038963}};
			undirected_edges.put(e_ab_a,e_ab_a_p);
			undirected_edges.put(e_ab_abc,e_ab_abc_p);
			undirected_edges.put(e_a_abc,e_a_abc_p);
			
			directed_edges.put("a", new Double[] {0.1,0.9});
			directed_edges.put("ab", new Double[] {0.1,0.9});
			directed_edges.put("abc", new Double[] {0.1,0.9});

		}
		
		if ( module == 2)
		{
			observation.put("a",1);
			observation.put("ab",1);
		
			HashMap<String,HashSet<String>> c_a = new HashMap<String,HashSet<String>>();
				HashSet<String> c_a_p1 = new HashSet<String>();
				c_a_p1.add("a");
				HashSet<String> c_a_c1 = new HashSet<String>();
				c_a_c1.add("a^b");
				
			c_a.put("problem", c_a_p1);
			c_a.put("concept",c_a_c1);
			
			HashMap<String,HashSet<String>> c_ab = new HashMap<String,HashSet<String>>();
				HashSet<String> c_ab_p = new HashSet<String>();
				c_ab_p.add("ab");
				HashSet<String> c_ab_c = new HashSet<String>();
				c_ab_c.add("a");
		
			c_ab.put("problem", c_ab_p);
			c_ab.put("concept", c_ab_c);

			concept_nodes_nbr.put("a", c_a);
			concept_nodes_nbr.put("a^b", c_ab);
	        
			
				HashSet<String> e_ab_a = new HashSet<String>();
				e_ab_a.add("a^b");
				e_ab_a.add("a");
				Double[][] e_ab_a_p = new Double[][] {{0.43,0.01},{0.35,0.21}};
				
			undirected_edges.put(e_ab_a,e_ab_a_p);

			
			directed_edges.put("a", new Double[] {0.1,0.9});
			directed_edges.put("ab", new Double[] {0.1,0.9});


		}
		
		HashMap<String,Double> concept_nodes_marginal = new HashMap<String,Double> ();
		HashMap<HashSet<String>,Double[][]> undirected_edges_marginal = new HashMap<HashSet<String>,Double[][]>();
		
		BliefPropagation bptest = new BliefPropagation();
		
		bptest.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
				undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
		bptest.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
				undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
		
		
		
		
		// System.out.println("concept_nodes_marginal");
		// for (HashMap.Entry<String,Double> node : concept_nodes_marginal.entrySet())
		// {
		// 	System.out.print(node.getKey() +":"+ node.getValue() + ",");
		// }
		// System.out.println("");
		
		// System.out.println("undirected_edges_marginal");
		// for (HashMap.Entry<HashSet<String>,Double[][]> node : undirected_edges_marginal.entrySet())
		// {
		// 	System.out.print("{"+node.getKey() +",");
		// 	System.out.print("}:");
			
		// 	System.out.print("[");
		// 	for (Double[] d : node.getValue())
		// 	{
		// 		System.out.print("[");
		// 		for (double a : d)
		// 		{
		// 			System.out.print(a + ",");
		// 		}
		// 		System.out.print("]");
		// 	}
		// 	System.out.println("]");
		// }
		
	}
	
	// public static void main(String[] acg)
	// {
	// 	bptest();
	// }
}
