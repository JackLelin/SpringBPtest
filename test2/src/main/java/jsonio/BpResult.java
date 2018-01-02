package jsonio;

import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BpResult 
{
	private String graphname;
	public BpResult (String s)
	{
		graphname = s;
		concept_nodes_nbr = nbrjsonread.to_concept_node_nbr(nbrjsonread.readJSON(s+"/concept_nodes_nbr.json"));
		undirected_edges = nbrjsonread.to_undirected_edges(nbrjsonread.readJSON(s+"/undirected_edges.json"));
		directed_edges = nbrjsonread.to_directed_edges(nbrjsonread.readJSON(s+"/directed_edges.json"));
	}
	HashMap<String,Double> concept_nodes_marginal = new HashMap<String,Double> ();
	HashMap<HashSet<String>,Double[][]> undirected_edges_marginal = new HashMap<HashSet<String>,Double[][]>();

	HashMap<String,Integer> observation;
	HashMap<String,HashMap<String,HashSet<String>>> concept_nodes_nbr;
	HashMap<HashSet<String>,Double[][]> undirected_edges;
	HashMap<String,Double[]> directed_edges;
	HashMap<String,String> problem_nodes_nbr = null;

	public JSONObject get_concept_nodes_marginal() 
	{
		JSONObject obj = new JSONObject();
		JSONArray con_nodes = new JSONArray();

		for (HashMap.Entry<String,Double> node : concept_nodes_marginal.entrySet())
		{
			obj.put(node.getKey(),node.getValue());
			con_nodes.add(node.getKey());
		}

		obj.put("nodes",con_nodes);
		return obj;
    }

    public JSONObject get_undirected_edges_marginal() 
    {
        return nbrjsonwrite.undirected_edges_toJSON(undirected_edges_marginal);

    }

    public void start(String problems,String result)
    {
    	observation = new HashMap<String,Integer>();
    	String[] parts1 = problems.split(",");
    	String[] parts2 = result.split(",");
		for ( int i = 0; i<parts1.length; i++)
		{
			observation.put(parts1[i],Integer.parseInt(parts2[i]));
		}
		BliefPropagation bp = new BliefPropagation();
		bp.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
				undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
		bp.hidden_marginal_given_observed(observation, concept_nodes_nbr, problem_nodes_nbr,  
				undirected_edges, directed_edges,concept_nodes_marginal,undirected_edges_marginal);
			
    }

    public JSONObject getJSON()
    {
    	JSONObject obj = new JSONObject();
    	obj.put("concept_nodes_marginal",get_concept_nodes_marginal());
    	obj.put("undirected_edges_marginal", get_undirected_edges_marginal());
    	return obj;
    }
	
}
