package arg.tech.argql.translator.json;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.stringtemplate.v4.compiler.STParser.andConditional_return;
import org.stringtemplate.v4.compiler.STParser.ifstat_return;

import com.github.andrewoma.dexx.collection.Pair;

import arg.tech.argql.filters.InclusionFilter;
import arg.tech.argql.filters.Metadata;
import arg.tech.argql.patterns.ArgPattern;
import arg.tech.argql.patterns.ConclusionPattern;
import arg.tech.argql.patterns.PremisePattern;
import arg.tech.argql.patterns.PropositionPattern;
import arg.tech.argql.results.Argument;
import arg.tech.utils.Enums.PremiseType;

import org.json.simple.JSONObject;

public class JSONTranslator {

	private static ArrayList<Edge> edges;
	private static ArrayList<Node> iNodes;
	private static ArrayList<Node> raNodes;
	private static ArrayList<Node> caNodes;
	private static ArrayList<ArgPattern> argPatterns;
	private static List<Relation> relations;
	private static int propVarCnt = 1;
	private static int conclVarCnt = 1;

	/*
	 * private JSONArray getIncommingNodes(Node node) { ArrayList<Node>
	 * incomingNodesArray = new ArrayList<Node>(); for (Edge edge : edges) {
	 * if(edge) }
	 * 
	 * if(edge.get("toID").toString().compareTo(node.get("nodeID").toString())==0) {
	 * String inNodeIDString = edge.get("fromID").toString();
	 * 
	 * JSONObject inNode = findNode(inNodeIDString); incomingNodesArray.add(inNode);
	 * }
	 * 
	 * return incomingNodesArray; }
	 */

	private static String createPropVar() {
		return "?pr" + propVarCnt++;
	}

	private static String createConclVar() {
		return "?concl" + conclVarCnt++;
	}

	private static PremisePattern createPremisePattern(Node node) {
		ArrayList<Edge> incomingEdges = node.getIncomingEdges();
		PremisePattern premisePattern = null;
		ArrayList<PropositionPattern> props = new ArrayList<PropositionPattern>();

		for (Edge edge : incomingEdges) {
			if (edge.getFromNode().getType().compareTo("I") == 0) {
				if (edge.getFromNode().getText().compareTo("") == 0 && incomingEdges.size() == 1) {
					PropositionPattern propVar = new PropositionPattern("", createPropVar(), null, true);
					propVar.setId(edge.getFromNode().getNodeID());
					premisePattern = new PremisePattern(propVar, "", PremiseType.VARIABLE);

				} else if (edge.getFromNode().getText().compareTo("") != 0) {

					PropositionPattern prop = new PropositionPattern("", edge.getFromNode().getText(), null, false);
					prop.setId(edge.getFromNode().getNodeID());
					props.add(prop);
					PropositionPattern propVar = new PropositionPattern("", createPropVar(), null, true);

					premisePattern = new PremisePattern(propVar, "", PremiseType.VARIABLE);
				}
			}
		}

		if (!props.isEmpty()) {
			InclusionFilter inclusionFilter = new InclusionFilter(premisePattern, props, "");
			premisePattern.setFilter(inclusionFilter);
		}

		return premisePattern;
	}

	private static ConclusionPattern createConclusionPattern(String id, String conclTextString) {

		ConclusionPattern conclusionPattern = new ConclusionPattern();
		PropositionPattern prop;

		if (conclTextString.compareTo("") != 0) {
			// is proposition
			prop = new PropositionPattern("", conclTextString, null, false);
			prop.setId(id);
			conclusionPattern.setPropPattern(prop);
			// conclusionPattern.setRaVar("");
			conclusionPattern.setVariable(false);
		} else {
			prop = new PropositionPattern("", createConclVar(), null, true);
			prop.setId(id);
			conclusionPattern.setPropPattern(prop);
			// conclusionPattern.setRaVar("");
			conclusionPattern.setVariable(true);
		}
		return conclusionPattern;
	} 

	private static ConclusionPattern processConclusion(Node node) {
		ArrayList<Edge> outgoinEdges = node.getOutgoingEdges();
		ConclusionPattern conclp = null;

		if (outgoinEdges.get(0).getToNode().getType().compareTo("I") == 0) {
			String conclTextString = outgoinEdges.get(0).getToNode().getText();
			String conclIDString = outgoinEdges.get(0).getToNode().getNodeID();
			conclp = createConclusionPattern(conclIDString, conclTextString);

		}
		return conclp;
	}

	private static Metadata createMetadata(Node node) {

		ArrayList<SimpleEntry<String, String>> data = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
		if (node.getScheme().compareTo("") != 0) {
			SimpleEntry<String, String> entry = new SimpleEntry<String, String>("sch", node.getScheme());
			data.add(entry);
		}
		if (node.getTimestamp().compareTo("") != 0) {
			SimpleEntry<String, String> entry = new SimpleEntry<String, String>("tm", node.getTimestamp());
			data.add(entry);
		}

		Metadata metadata = new Metadata(data);

		return metadata;
	}

	private static boolean comesfromRA(ArrayList<Edge> edgeList) {
		for (Edge edge : edgeList) {
			if (edge.getFromNode().getType().compareTo("RA") == 0) {
				return true;
			}
		}
		return false;
	}

	private static boolean goesToRA(ArrayList<Edge> edgeList) {
		for (Edge edge : edgeList) {
			if (edge.getToNode().getType().compareTo("RA") == 0) {
				return true;
			}
		}
		return false;
	}

	private static void identifyArgPatterns() {

		for (Node node : raNodes) {
			ArgPattern argPattern = new ArgPattern();
			PremisePattern premisePattern = createPremisePattern(node);
			ConclusionPattern conclusionPattern = processConclusion(node);
			argPattern.setId(node.getNodeID());
			argPattern.setArgVar("");
			argPattern.setRaVariable("");
			argPattern.setPremisePattern(premisePattern);
			argPattern.setConclusionPattern(conclusionPattern);

			Metadata metadata = createMetadata(node);
			argPattern.setMetadata(metadata);
			argPatterns.add(argPattern);
			//System.out.println(argPattern.toArgQLString());

		}

		for (Node node : iNodes) {
			if (!comesfromRA(node.getIncomingEdges()) && !goesToRA(node.getOutgoingEdges())) {
				ConclusionPattern conclusionPattern = createConclusionPattern(node.getNodeID(), node.getText());

				PremisePattern premisePattern = new PremisePattern(PremiseType.EMPTY);
				ArgPattern argPattern = new ArgPattern();
				argPattern.setId(node.getNodeID());
				argPattern.setArgVar("");
				argPattern.setRaVariable("");
				argPattern.setPremisePattern(premisePattern);
				argPattern.setConclusionPattern(conclusionPattern);
				argPattern.setMetadata(null);
				argPatterns.add(argPattern);
				//System.out.println(argPattern.toArgQLString());

			}
		}
	}

	private static Node findNode(String id) {
		for (Node node : iNodes) {
			if (node.getNodeID().compareTo(id) == 0) {
				return node;
			}
		}
		for (Node node : caNodes) {
			if (node.getNodeID().compareTo(id) == 0) {
				return node;
			}
		}
		for (Node node : raNodes) {
			if (node.getNodeID().compareTo(id) == 0) {
				return node;
			}
		}
		return null;
	}

	private static Pair<ArgPattern, String> findAPbyInode(String propID, String currentNodeID) {

		for (ArgPattern argPattern : argPatterns) {
			if (argPattern.getId().compareTo(currentNodeID) == 0)
				continue;

			if (argPattern.getPremisePattern().getType() == PremiseType.VARIABLE) {
				if (argPattern.getPremisePattern().getPropPattern().getId().compareTo(propID) == 0) {
					Pair<ArgPattern, String> result = new Pair<ArgPattern, String>(argPattern, "premise");
					return result;
				}

				if (argPattern.getPremisePattern().getFilter() != null) {
					for (PropositionPattern prop : argPattern.getPremisePattern().getFilter().getPropset()) {
						if (prop.getId().compareTo(propID) == 0) {
							Pair<ArgPattern, String> result = new Pair<ArgPattern, String>(argPattern, "premise");
							return result;
						}
					}
				}
			}

			if (argPattern.getConclusionPattern().getPropPattern().getId().compareTo(propID) == 0) {
				Pair<ArgPattern, String> result = new Pair<ArgPattern, String>(argPattern, "conclusion");
				return result;
			}
			
			if (argPattern.getId().compareTo(propID) == 0) {
				Pair<ArgPattern, String> result = new Pair<ArgPattern, String>(argPattern, "argument");
				return result;
			}

		}
		return null;
	}



	private static void addRelation(Relation relation) {
		boolean added = false;
		boolean exists = false;

		try {

			if (relations.isEmpty()) {
				relations.add(relation);
				
			} else {
				
				for (Relation rel : relations) {

					if (rel.getFromAP().getId().compareTo(relation.getToAP().getId()) == 0) {
						int pos = relations.indexOf(rel);
						relations.add(pos, relation);
						added = true;
					} else if (rel.getToAP().getId().compareTo(relation.getFromAP().getArgVar()) == 0) {
						int pos = relations.indexOf(rel);
						relations.add(pos + 1, relation);
						added = true;
					} else {
						relations.add(relation);
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private static void identifyAttackPatterns() throws Exception {
		for (Node node : caNodes) {

			if (node.getIncomingEdges().size() != 1 || node.getOutgoingEdges().size() != 1) {
				throw new Exception("Invalid AIF");
			} else {
				String inNodeID = node.getIncomingEdges().get(0).getFromNode().getNodeID();
				String outNodeID = node.getOutgoingEdges().get(0).getToNode().getNodeID();

				Pair<ArgPattern, String> fromPair = findAPbyInode(inNodeID, node.getNodeID());
				Pair<ArgPattern, String> toPair = findAPbyInode(outNodeID, node.getNodeID());

				ArgPattern fromAP = fromPair.component1();
				ArgPattern toAP = toPair.component1();

				if (fromPair.component2().compareTo("conclusion") == 0) {
					if (toPair.component2().compareTo("premise") == 0) {
						Relation relation = new Relation(fromAP, toAP, "undermine");
						addRelation(relation);
					} else if (toPair.component2().compareTo("conclusion") == 0) {
						Relation relation = new Relation(fromAP, toAP, "rebut");
						addRelation(relation);
					} else if (toPair.component2().compareTo("argument") == 0) {
						Relation relation = new Relation(fromAP, toAP, "undercut");
						addRelation(relation);
					}
				}
			}
		}

	}

	private static ArgPattern findAPbyID(String id) {
		for (ArgPattern argPattern : argPatterns) {
			if (argPattern.getId().compareTo(id) == 0) {
				return argPattern;
			}
		}
		return null;
	}

	private static void identifySupportPatterns() {

		for (Node node : raNodes) {
			if (node.getIncomingEdges().isEmpty() || node.getOutgoingEdges().isEmpty()) {
				System.out.println("RA node: " + node.getNodeID() + " is invalid");
			} else {
				ArrayList<Edge> inNodes = node.getIncomingEdges();
				String outNodeID = node.getOutgoingEdges().get(0).getToNode().getNodeID();

				Pair<ArgPattern, String> toPair = findAPbyInode(outNodeID, node.getNodeID());

				if (toPair != null && toPair.component1().getId().compareTo(node.getNodeID()) != 0
						&& toPair.component2().compareTo("premise") == 0) {

					ArgPattern fromAP = findAPbyID(node.getNodeID());
					ArgPattern toAP = toPair.component1();

					Relation relation = new Relation(fromAP, toAP, "back");
					addRelation(relation);

				}
			}
		}
	}

	private static void parseJSON(JSONObject jsonObj) throws Exception {

		JSONArray nodesJSON = (JSONArray) jsonObj.get("nodes");
		JSONArray edgesJSON = (JSONArray) jsonObj.get("edges");

		Iterator<JSONObject> iterator = nodesJSON.iterator();
		while (iterator.hasNext()) {
			JSONObject obj = iterator.next();
			String nodeIDString = "";
			if (obj.get("nodeID") != null) {
				nodeIDString = obj.get("nodeID").toString();
			}
			String textString = obj.get("text").toString();
			String typeString = obj.get("type").toString();
			String schemeString = "";
			if (obj.get("scheme") != null)
				schemeString = obj.get("scheme").toString();

			String timestampString = "";
			if (obj.get("timestamp") != null)
				timestampString = obj.get("timestamp").toString();

			Node node = new Node(nodeIDString, textString, typeString, schemeString, timestampString);
			if (typeString.compareTo("I") == 0)
				iNodes.add(node);
			if (typeString.compareTo("RA") == 0)
				raNodes.add(node);
			if (typeString.compareTo("CA") == 0)
				caNodes.add(node);
		}

		Iterator<JSONObject> iter = edgesJSON.iterator();
		while (iter.hasNext()) {
			JSONObject obj = iter.next();
			String edgeIDString = "";
			if (obj.get("edgeID") != null)
				edgeIDString = obj.get("edgeID").toString();
			Node fromNode = findNode(obj.get("fromID").toString());
			Node toNode = findNode(obj.get("toID").toString());

			if (fromNode != null && toNode != null) {
				Edge edge = new Edge(edgeIDString, fromNode, toNode);
				fromNode.addOutgoingEdge(edge);
				toNode.addIncomingEdge(edge);
				edges.add(edge);
			} else {
				throw new Exception("Invalid AIF");
			}
		}
	}

	private static void printGraph() {
		System.out.println("INODES");
		for (Node node : iNodes) {
			System.out.println(node.toString());
		}
		System.out.println("\nRANODES");
		for (Node node : raNodes) {
			System.out.println(node.toString());
		}
		System.out.println("\nCANODES");
		for (Node node : caNodes) {
			System.out.println(node.toString());
		}
		System.out.println("\nEDGES");
		for (Edge edge : edges) {
			System.out.println(edge.toString());
		}
	}

	private static boolean apInRelation(ArgPattern ap) {
		for (Relation relation : relations) {
			if (relation.getFromAP().getId().compareTo(ap.getId()) == 0
					|| relation.getToAP().getId().compareTo(ap.getId()) == 0) {
				return true;
			}
		}
		return false;
	}

	private static String getArgQLquery() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("match ");

		for (ArgPattern argPattern : argPatterns) {
			if (!apInRelation(argPattern)) {
				stringBuilder.append(argPattern.toArgQLString() + ", ");
			}
		}

		for(int i=0; i<relations.size(); i++ ) {
			Relation relation = relations.get(i);
			stringBuilder.append(relation.getFromAP().toArgQLString());
			stringBuilder.append(" " + relation.getType() + " ");

			
			if((i+1 < relations.size() && relations.get(i+1).getFromAP().getId().compareTo(relation.getToAP().getId()) != 0)
					|| i+1 == relations.size()) {
				stringBuilder.append(relation.getToAP().toArgQLString() + ", ");
			}
			
			
		}

		String queryString = stringBuilder.toString();
		if (queryString.endsWith(", ")) {
			queryString = queryString.substring(0, queryString.length() - 2);
		}
		return queryString;
	}

	public static String createArgQLQuery(String filename) throws Exception {

		Object obj;
		iNodes = new ArrayList<Node>();
		raNodes = new ArrayList<Node>();
		caNodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		argPatterns = new ArrayList<ArgPattern>();
		relations = new LinkedList<Relation>();

		try {
			obj = new JSONParser().parse(new FileReader(filename));

			JSONObject jsonObj = (JSONObject) obj;

			parseJSON(jsonObj);
			// printGraph();
			identifyArgPatterns();
			identifyAttackPatterns();
			identifySupportPatterns();

			for (Relation relation : relations) {
				System.out.println("-> " + relation.toString());
			}

			return (getArgQLquery());

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
