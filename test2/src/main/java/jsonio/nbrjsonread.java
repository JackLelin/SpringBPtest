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

    		JSONObject obj1 =  nbrjsonwrite.concept_node_nbr_toJSON (concept_nodes_nbr);
    		JSONObject obj2 =  nbrjsonwrite.undirected_edges_toJSON (undirected_edges);
    		JSONObject obj3 =  nbrjsonwrite.directed_edges_toJSON (directed_edges);
    		
    		FileWriter file1 = new FileWriter("concept_node_nbr1.txt");
    		FileWriter file2 = new FileWriter("undirected_edges1.txt");
    		FileWriter file3 = new FileWriter("directed_edges1.txt");

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
