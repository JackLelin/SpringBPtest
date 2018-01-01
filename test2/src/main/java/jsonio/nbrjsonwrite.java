package jsonio;

import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class nbrjsonwrite 
{
	
	public static JSONObject concept_node_nbr_toJSON(HashMap<String,HashMap<String,HashSet<String>>> concept_nodes_nbr) 
	{
		JSONObject jsonobj = new JSONObject();

		for (HashMap.Entry<String,HashMap<String,HashSet<String>>> node : concept_nodes_nbr.entrySet())
		{
			JSONObject node_nbr = new JSONObject();
			JSONArray concept_node = new JSONArray();
			JSONArray problem_node = new JSONArray();

			for(String problem : node.getValue().get("problem"))
			{
				problem_node.add(problem);
			}
			node_nbr.put("problem",problem_node);

			for(String concept : node.getValue().get("concept"))
			{
				concept_node.add(concept);
			}
			node_nbr.put("concept",concept_node);

			jsonobj.put(node.getKey(),node_nbr);	

		}
		return jsonobj;
	}

	public static JSONObject undirected_edges_toJSON(HashMap<HashSet<String>,Double[][]> undirected_edges)
	{
		JSONObject obj = new JSONObject();

		for (HashMap.Entry<HashSet<String>,Double[][]> edge : undirected_edges.entrySet())
		{
			String node_name = new String();
			int i =0 ;
			for(String node : edge.getKey())
			{
				if ( i == 0)
				{
					node_name = node_name + node;
					i = 1;					
				}
				else
					node_name = node_name + "#" + node;

			}
			JSONArray pot0 = new JSONArray();
			pot0.add(edge.getValue()[0][0].doubleValue());
			pot0.add(edge.getValue()[0][1].doubleValue());

			JSONArray pot1 = new JSONArray();
			pot1.add(edge.getValue()[1][0].doubleValue());
			pot1.add(edge.getValue()[1][1].doubleValue());
			JSONArray pot = new JSONArray();
			pot.add(pot0); pot.add(pot1);
			obj.put(node_name,pot);
		}

		return obj;
	}

	public static JSONObject directed_edges_toJSON(HashMap<String,Double[]> directed_edges)
	{
		JSONObject obj = new JSONObject();

		for (HashMap.Entry<String,Double[]> edge : directed_edges.entrySet())
		{
			JSONArray pot = new JSONArray();
			pot.add(edge.getValue()[0].doubleValue()); pot.add(edge.getValue()[1].doubleValue());
			obj.put(edge.getKey(),pot);
		}

		return obj;
	}

	
	public static void bptest() throws IOException 
	{
		int module = 1;
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

		JSONObject obj1 =  concept_node_nbr_toJSON (concept_nodes_nbr);
		JSONObject obj2 =  undirected_edges_toJSON (undirected_edges);
		JSONObject obj3 =  directed_edges_toJSON (directed_edges);
		
		FileWriter file1 = new FileWriter("jsonfile1.txt");
		FileWriter file2 = new FileWriter("jsonfile2.txt");
		FileWriter file3 = new FileWriter("jsonfile3.txt");

		file1.write(obj1.toJSONString());
		file1.flush();
		file1.close();
		System.out.println("Successfully Copied JSON Object to File...");
		System.out.println("\nJSON Object: " + obj1);
		file2.write(obj2.toJSONString());
		file2.flush();
		file2.close();
		System.out.println("Successfully Copied JSON Object to File...");
		System.out.println("\nJSON Object: " + obj2);
		file3.write(obj3.toJSONString());
		file3.flush();
		file3.close();
		System.out.println("Successfully Copied JSON Object to File...");
		System.out.println("\nJSON Object: " + obj3);
		// HashMap<String,Double> concept_nodes_marginal = new ;HashMap<String,Double> ();
		// HashMap<HashSet<String>,Double[][]> undirected_edges_marginal = new HashMap<HashSet<String>,Double[][]>();
		
		// BliefPropagation bptest = new BliefPropagation();
		
		// bptest.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
		// 		undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
		// bptest.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
		// 		undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
		
		
		
		
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
	
	public static void main(String[] acg) 
	{
		try {bptest();}
		catch (IOException e) {
            e.printStackTrace();
        }
	}
}
