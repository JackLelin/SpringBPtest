package jsonio;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class nbrjsonread 
{
	
	public static HashMap<String,HashMap<String,HashSet<String>>> to_concept_node_nbr(JSONObject nbr) 
	{
		HashMap<String,HashMap<String,HashSet<String>>> concept_nodes_nbr =  new HashMap<String,HashMap<String,HashSet<String>>>();

        JSONArray all_nodes = (JSONArray)nbr.get("nodes");
        for (Object node : all_nodes)
        {
            JSONObject _node_nbr = (JSONObject) nbr.get((String)node);

            JSONArray pro_nbr = (JSONArray)_node_nbr.get("problem");
            JSONArray con_nbr = (JSONArray)_node_nbr.get("concept");

            HashSet<String> problem_node = new HashSet<String>();
            HashSet<String> concept_node = new HashSet<String>();
            HashMap<String,HashSet<String>> node_nbr = new HashMap<String,HashSet<String>>();

            for(Object problem : pro_nbr)
				problem_node.add((String)problem);		
			node_nbr.put("problem",problem_node);

			for(Object concept : con_nbr)
				concept_node.add((String)concept);
			node_nbr.put("concept",concept_node);

 			concept_nodes_nbr.put((String)node,node_nbr);
        }
        return concept_nodes_nbr;
	}

	public static HashMap<HashSet<String>,Double[][]> to_undirected_edges(JSONObject undir_edge)
	{
		HashMap<HashSet<String>,Double[][]> undirected_edges =  new HashMap<HashSet<String>,Double[][]>();

        JSONArray all_edges = (JSONArray)undir_edge.get("edges");
        for (Object edge : all_edges)
        {
            JSONArray _edge = (JSONArray) undir_edge.get((String)edge);

            Double[][] potential = new Double[2][2];

            int i = 0;
            for (Object a : _edge)
            {
            	int j = 0;
                for (Object b : (JSONArray)a)
                {
                	potential[i][j] = (double)b;
                    j++;
                }
                i ++;
            }
            String[] parts = ((String)edge).split("#");
            HashSet<String> edge_node = new HashSet<String>();
            edge_node.add(parts[0]);
            edge_node.add(parts[1]);
 			undirected_edges.put(edge_node,potential);
        }

        return undirected_edges;
	}

	public static HashMap<String,Double[]>  to_directed_edges(JSONObject dir_edge)
	{
		HashMap<String,Double[]> directed_edges =  new HashMap<String,Double[]>();

        JSONArray all_edges = (JSONArray)dir_edge.get("edges");
        for (Object edge : all_edges)
        {
            JSONArray _edge = (JSONArray) dir_edge.get((String)edge);

            Double[] potential = new Double[2];

            int i = 0;
            for (Object a : _edge)
            {
            	potential[i] = (double)a;
                i++;
            }

 			directed_edges.put((String)edge,potential);
        }

        return directed_edges;

	}

	
	public static void readtest()
	{

		JSONParser parser = new JSONParser();
        try
        {
            JSONObject a = (JSONObject) parser.parse(new FileReader("concept_node_nbr.txt"));
            JSONObject b = (JSONObject) parser.parse(new FileReader("undirected_edges.txt"));
            JSONObject c = (JSONObject) parser.parse(new FileReader("directed_edges.txt"));


            HashMap<String,HashMap<String,HashSet<String>>> concept_nodes_nbr = to_concept_node_nbr(a);
    		HashMap<HashSet<String>,Double[][]> undirected_edges = to_undirected_edges(b);
    		HashMap<String,Double[]>  directed_edges = to_directed_edges(c);

            HashMap<String,String> problem_nodes_nbr = null;
            HashMap<String,Integer> observation = new HashMap<String,Integer>();


            HashMap<String,Double> concept_nodes_marginal = new HashMap<String,Double> ();
            HashMap<HashSet<String>,Double[][]> undirected_edges_marginal = new HashMap<HashSet<String>,Double[][]>();
            
            BliefPropagation bptest = new BliefPropagation();
            
            observation.put("a",1);
            observation.put("ab",1);
            observation.put("abc",0);
            
            bptest.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
                    undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
            bptest.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
                    undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
            
            
            
            
            System.out.println("concept_nodes_marginal");
            for (HashMap.Entry<String,Double> node : concept_nodes_marginal.entrySet())
            {
                System.out.print(node.getKey() +":"+ node.getValue() + ",");
            }
            System.out.println("");
            
            System.out.println("undirected_edges_marginal");
            for (HashMap.Entry<HashSet<String>,Double[][]> node : undirected_edges_marginal.entrySet())
            {
                System.out.print("{"+node.getKey() +",");
                System.out.print("}:");
                
                System.out.print("[");
                for (Double[] d : node.getValue())
                {
                    System.out.print("[");
                    for (double i : d)
                    {
                        System.out.print(i + ",");
                    }
                    System.out.print("]");
                }
                System.out.println("]");
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		
	}
	
	public static void main(String[] acg) 
	{
		readtest();
	}
}
