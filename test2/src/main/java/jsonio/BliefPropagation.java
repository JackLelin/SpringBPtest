package hello;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;

public class BliefPropagation 
{
	int max_BP_round = 100;
	
	BliefPropagation(int N)
	{
		max_BP_round = N;
	}
	BliefPropagation()
	{
		
	}
//	private HashMap<Integer,String> NameMap = new HashMap<Integer,String>();
	
	// HashMap<String,Integer> observation problem:result ;
	private HashMap<String,HashMap<String,HashMap<String,Double>>> past_message_hidden_marginal_given_observed = new HashMap<String,HashMap<String,HashMap<String,Double>>>();

	public void hidden_marginal_given_observed ( 
			//Input
			HashMap<String,Integer> observation, 
			HashMap<String,HashMap<String,HashSet<String>>> concept_nodes_nbr,
			HashMap<String,String> problem_nodes_nbr, //not used
			HashMap<HashSet<String>,Double[][]> undirected_edges, 
			HashMap<String,Double[]> directed_edges,
			// Output
			HashMap<String,Double> concept_nodes_marginal,
			HashMap<HashSet<String>,Double[][]> undirected_edges_marginal)
	{
	    // initialize message

		String observation_string = strMap(observation);
		
		HashMap<String,HashMap<String,Double>> message = new HashMap<String,HashMap<String,Double>>();

		
	    if (	past_message_hidden_marginal_given_observed.containsKey(observation_string ) )
	    	{
	    		message = past_message_hidden_marginal_given_observed.get(observation_string);
//	    		System.out.println("re-use message");
	    	}
	    else
	    {
		    	for (HashMap.Entry<String,HashMap<String,HashSet<String>>> concept_node : concept_nodes_nbr.entrySet()) 
		    	{
		    		HashMap<String,Double> t_msg_node = new HashMap<String,Double>();
		    		HashSet<String> t_nbr_node  = concept_node.getValue().get("concept");
		    		
		    		for(String node_name : t_nbr_node  )
		    			t_msg_node.put(node_name, 1.0);
		    		
		    		message.put(concept_node.getKey(), t_msg_node);
		    		
		    	}
//		    	System.out.println("No generated message, initialzing new message");
	    }
	    
	    //starting BP round
	    for (int iteration = 0; iteration < max_BP_round;iteration ++)
	    {
			HashMap<String,HashMap<String,Double>> message_new = new HashMap<String,HashMap<String,Double>>();
	    		//initialize message for this round
		    	for (HashMap.Entry<String,HashMap<String,HashSet<String>>> concept_node : concept_nodes_nbr.entrySet()) 
		    	{
		    		HashMap<String,Double> t_msg_node = new HashMap<String,Double>();
		    		HashSet<String> t_nbr_node  = concept_node.getValue().get("concept");
		    		
		    		for(String node_name : t_nbr_node  )
		    			t_msg_node.put(node_name, 1.0);
		    		
		    		message_new.put(concept_node.getKey(), t_msg_node);
		    	}		    
	        double sum_difference = 0.0;
	        //compute message
	        for (HashMap.Entry<String,HashMap<String,HashSet<String>>> concept_node : concept_nodes_nbr.entrySet()) 
		    	{
	        		HashSet<String> neighbors  = concept_node.getValue().get("concept");
		    		for(String to_neighbor : neighbors)
		    		{
		    			//read edge params
		    			HashSet<String> undirected_edge = new HashSet<String>();
		    			undirected_edge.add(concept_node.getKey());
		    			undirected_edge.add(to_neighbor);
		    			Double[][] undirected_edge_param = undirected_edges.get(undirected_edge);
		    			int undirected_edge_param_index = compare_two_sets(concept_node.getKey(), to_neighbor);
		    			
		    			//compute node potential
		    			double node_potiential_0 = 1.0;
		    			double node_potiential_1 = 1.0;
		    	        
		    	        for (String problem : concept_nodes_nbr.get(concept_node.getKey()).get("problem"))
		    	        {
		    	        		if(observation.containsKey(problem))
		    	        		{
		    	        			Double[] directed_edges_param = directed_edges.get(problem);
		    	        			if(observation.get(problem) == 0)
		    	        			{
		    	        				node_potiential_0 *= 1.0 - directed_edges_param[0];
		    	        				node_potiential_1 *= 1.0 - directed_edges_param[1];
		    	        			}
		    	        			else
		    	        			{
		    	        				node_potiential_0 *= directed_edges_param[0];
		    	        				node_potiential_1 *= directed_edges_param[1];
		    	        			}
		    	        		}     
		    	        }
		    	        double edge_potiential_a0_b0,edge_potiential_a0_b1,edge_potiential_a1_b0,edge_potiential_a1_b1;
		    	        //compute edge potential
		                if (undirected_edge_param_index == 0)
		                {
		                    edge_potiential_a0_b0 = undirected_edge_param[0][0];
		                    edge_potiential_a0_b1 = undirected_edge_param[0][1];
		                    edge_potiential_a1_b0 = undirected_edge_param[1][0];
		                    edge_potiential_a1_b1 = undirected_edge_param[1][1];
		                }
		                else
		                {
		                    edge_potiential_a0_b0 = undirected_edge_param[0][0];
		                    edge_potiential_a0_b1 = undirected_edge_param[1][0];
		                    edge_potiential_a1_b0 = undirected_edge_param[0][1];
		                    edge_potiential_a1_b1 = undirected_edge_param[1][1];
		                }
		                
		                // compute message product
		                double message_product_0 = 1.0;
		                double message_product_1 = 1.0;
		                for(String from_neighbor : neighbors)
		                {
		                		if (from_neighbor.equals(to_neighbor) == false)
		                		{
		                			double t = message.get(from_neighbor).get(concept_node.getKey());
		                			message_product_0 *= 1 - t;
		                			message_product_1 *= t;
		                		}
		                }
		                //compute unnormalized message
		                double M0 = edge_potiential_a0_b0 * node_potiential_0 * message_product_0 + 
		                     edge_potiential_a1_b0 * node_potiential_1 * message_product_1;
		                double M1 = edge_potiential_a0_b1 * node_potiential_0 * message_product_0 + 
		                     edge_potiential_a1_b1 * node_potiential_1 * message_product_1;
		                double m;
		                if (M0 + M1 == 0)
		                    m = 0.5;
		                else
		                    m = M1/(M0 + M1);
		                message_new.get(concept_node.getKey()).put(to_neighbor, m);
		                
		                double difference = message_new.get(concept_node.getKey()).get(to_neighbor) - message.get(concept_node.getKey()).get(to_neighbor);
		                sum_difference += difference*difference;
	                
		    		}
		    	}
		    message = message_new;
		    if (sum_difference < 1e-10)
	            break;
	    }
	    
	    //save past value of message for this observation
	    past_message_hidden_marginal_given_observed.put(observation_string, message);
	    
//	    printmessage(message);
	    // compute node marginal
	    for(HashMap.Entry<String,HashMap<String,HashSet<String>>> node : concept_nodes_nbr.entrySet())
	    {
	    		concept_nodes_marginal.put(node.getKey(),0.0);
	    }
	    for (HashMap.Entry<String,HashMap<String,HashSet<String>>> concept_node : concept_nodes_nbr.entrySet())
	    {
	    		HashSet<String> neighbors = concept_nodes_nbr.get(concept_node.getKey()).get("concept");
	    		//compute node potential
	    		double node_potiential_x0 = 1.0;
	    		double node_potiential_x1 = 1.0;
	    		for (String problem : concept_nodes_nbr.get(concept_node.getKey()).get("problem"))
	    		{
	    			if ( observation.containsKey(problem))
	    			{
	    				Double[] directed_edges_param = directed_edges.get(problem);
	    				if (observation.get(problem) == 0)
	    				{
	    					node_potiential_x0 *= (1.0 - directed_edges_param[0]);
	    					node_potiential_x1 *= (1.0 - directed_edges_param[1]);
	    				}
	    				else
	    				{
	    					node_potiential_x0 *= directed_edges_param[0];
	    					node_potiential_x1 *= directed_edges_param[1];
	    				}              
	    			}   
	    		}
	    		
	    		double belief_0 = node_potiential_x0;
	    		double belief_1 = node_potiential_x1;
	    		for(String from_neighbor : neighbors)
	    		{
	    			double t = message.get(from_neighbor).get(concept_node.getKey());;
	    			belief_0 *= 1.0 - t;
	    			belief_1 *= t;
	    		}
	    		if (belief_0 + belief_1 == 0)
	    			concept_nodes_marginal.put(concept_node.getKey(), 0.5);
	    		else
	    			concept_nodes_marginal.put(concept_node.getKey(), belief_1 / (belief_0 + belief_1));           
	    }
	    
	    // compute edge marginal
	    for (HashMap.Entry<HashSet<String>,Double[][]> edge : undirected_edges.entrySet())
	    {
	    		undirected_edges_marginal.put(edge.getKey(), new Double[][]{{0.0,0.0},{0.0,0.0}});
	    }
	    for (HashMap.Entry<HashSet<String>,Double[][]> edge : undirected_edges.entrySet())
	    {
	    	
	    
	        String edge_node_a = get_edge_node(edge.getKey(), 0);
	        String edge_node_b = get_edge_node(edge.getKey(), 1);

	        // compute node potential
	        double node_potiential_a0 = 1.0;
	        double node_potiential_a1 = 1.0;
	        double node_potiential_b0 = 1.0;
	        double node_potiential_b1 = 1.0;
	        for(String problem : concept_nodes_nbr.get(edge_node_a).get("problem"))
	        {
	            if (observation.containsKey(problem))
	            {
	                Double[] directed_edges_param = directed_edges.get(problem);
	                if (observation.get(problem) == 0)
	                {
	                    node_potiential_a0 *= 1.0 - directed_edges_param[0];
	                    node_potiential_a1 *= 1.0 - directed_edges_param[1];
	                }
	                else
	                {
	                    node_potiential_a0 *= directed_edges_param[0];
	                    node_potiential_a1 *= directed_edges_param[1];	
	                }
  	            
	             }
      
	        }

	        for(String problem : concept_nodes_nbr.get(edge_node_b).get("problem"))
	        {
	        		if(observation.containsKey(problem))
	        		{
	        			Double[] directed_edges_param = directed_edges.get(problem);
	        			if (observation.get(problem) == 0)
	        			{
	        				node_potiential_b0 *= 1.0 - directed_edges_param[0];
	        				node_potiential_b1 *= 1.0 - directed_edges_param[1];
	        			}
	        			else
	        			{
	        				node_potiential_b0 *= directed_edges_param[0];
	        				node_potiential_b1 *= directed_edges_param[1];
	        			}
	        		}
	        }
	        // compute edge potential
	        HashSet<String> undirected_edge = new HashSet<String>();
	        undirected_edge.add(edge_node_a);
	        undirected_edge.add(edge_node_b);

			Double[][] undirected_edge_param = undirected_edges.get(undirected_edge);

	        int undirected_edge_param_index = compare_two_sets(edge_node_a, edge_node_b);
	        double edge_potiential_a0_b0,edge_potiential_a0_b1,edge_potiential_a1_b0,edge_potiential_a1_b1;
	        if (undirected_edge_param_index == 0)
	        {
	            edge_potiential_a0_b0 = undirected_edge_param[0][0];
	            edge_potiential_a0_b1 = undirected_edge_param[0][1];
	            edge_potiential_a1_b0 = undirected_edge_param[1][0];
	            edge_potiential_a1_b1 = undirected_edge_param[1][1];
	        }

	        else
	        {
	        	// TODO: redundant
	            edge_potiential_a0_b0 = undirected_edge_param[0][0];
	            edge_potiential_a0_b1 = undirected_edge_param[1][0];
	            edge_potiential_a1_b0 = undirected_edge_param[0][1];
	            edge_potiential_a1_b1 = undirected_edge_param[1][1];
	        }
	        
	        double belief_a0_b0 = node_potiential_a0 * node_potiential_b0 * edge_potiential_a0_b0;
	        double belief_a0_b1 = node_potiential_a0 * node_potiential_b1 * edge_potiential_a0_b1;
	        double belief_a1_b0 = node_potiential_a1 * node_potiential_b0 * edge_potiential_a1_b0;
	        double belief_a1_b1 = node_potiential_a1 * node_potiential_b1 * edge_potiential_a1_b1;

	        HashSet<String> neighbors_a = concept_nodes_nbr.get(edge_node_a).get("concept");
	        HashSet<String> neighbors_b = concept_nodes_nbr.get(edge_node_b).get("concept");
	        
	        for (String from_neighbor : neighbors_a)
	        {
	        		if (! from_neighbor.equals(edge_node_b))
	        		{
	        			belief_a0_b0 *= 1-message.get(from_neighbor).get(edge_node_a);
	        			belief_a0_b1 *= 1-message.get(from_neighbor).get(edge_node_a);
	        			belief_a1_b0 *= message.get(from_neighbor).get(edge_node_a);
	        			belief_a1_b1 *= message.get(from_neighbor).get(edge_node_a);
	        		}
	        }
	        for (String from_neighbor : neighbors_b)
	        {
	        		if (! from_neighbor.equals(edge_node_a))
	        		{
	        			belief_a0_b0 *= 1-message.get(from_neighbor).get(edge_node_b);
	        			belief_a0_b1 *= message.get(from_neighbor).get(edge_node_b);
	        			belief_a1_b0 *= 1-message.get(from_neighbor).get(edge_node_b);
	        			belief_a1_b1 *= message.get(from_neighbor).get(edge_node_b);
	        		}
	        }


	        double sum_belief = belief_a0_b0 + belief_a0_b1 + belief_a1_b0 + belief_a1_b1;
	        if (sum_belief == 0)
	        {
	            undirected_edges_marginal.get(edge.getKey())[0][0] = 0.25;
	            undirected_edges_marginal.get(edge.getKey())[0][1] = 0.25;
	            undirected_edges_marginal.get(edge.getKey())[1][0] = 0.25;
	            undirected_edges_marginal.get(edge.getKey())[1][1] = 0.25;
	        }
	        else
	        {
	            undirected_edges_marginal.get(edge.getKey())[0][0] = belief_a0_b0/sum_belief;
	            undirected_edges_marginal.get(edge.getKey())[0][1] = belief_a0_b1/sum_belief;
	            undirected_edges_marginal.get(edge.getKey())[1][0] = belief_a1_b0/sum_belief;
	            undirected_edges_marginal.get(edge.getKey())[1][1] = belief_a1_b1/sum_belief;
	        }
	    }
	    
		return;
	}

	//: hash map to string
	public static String strMap(HashMap<String,Integer> hm)
	{
		Set<HashMap.Entry<String,Integer>> ent = hm.entrySet();
		String[] list = new String[ent.size()];
		int i = 0;
		for (HashMap.Entry<String,Integer> e : ent)
		{
			list[i] = "" + e.getKey() + e.getValue();
			i++;
		}
		Arrays.sort(list);
		String mapstr = new String();
		for (String s : list)
			mapstr = mapstr + s;
		return mapstr;
	}
	//: compare two sets
	public static int compare_two_sets(String a, String b)
	{
		if (a.length() < b.length())
			return 0;
		else if (a.length() > b.length())
			return 1;
		else
		{
			System.out.print("error: comparing two same sets");
			return (Integer) null;
		}
	}
	

	//: get edge node
	public static String get_edge_node(HashSet<String> e, int i)
	{ 
		if (e.size() != 2)
			return null;
		String[] edges = e.toArray(new String[e.size()]);

		String edge_a = edges[0];
		String edge_b = edges[1];
		
		if(edge_a.length() < edge_b.length())
		{
			if (i == 0)
				return edge_a;
			else if (i == 1)
				return edge_b;
			else
			{
				System.out.print("error: no such index");
				return null;
			}
				
		}
		else if (edge_a.length() > edge_b.length())
		{
			if (i == 0)
				return edge_b;
			else if (i == 1)
				return edge_a;
			else
			{
				System.out.print("error: no such index");
				return null;
			}
		}
		System.out.print("error: connected nodes are in the same layer");
		return null;
	}
	public static void printmessage(HashMap<String,HashMap<String,Double>> message)
	{
		for(HashMap.Entry<String,HashMap<String,Double>> from : message.entrySet())
		{
			System.out.print(from.getKey() + ":{");
			for (HashMap.Entry<String,Double> nbr : from.getValue().entrySet())
			{
				System.out.print("["+nbr.getKey() + ":" + nbr.getValue()+"]");
			}
			System.out.print("}\n");
		}
	}
	
}
