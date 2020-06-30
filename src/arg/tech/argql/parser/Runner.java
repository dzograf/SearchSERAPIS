package arg.tech.argql.parser;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import arg.tech.argql.patterns.ArgPattern;
import arg.tech.argql.translator.sparql.SPARQLTranslator;
//import arg.tech.utils.OntologyManager;


public class Runner {

	
	public static void main(String[] args) {
		try {
			
			
			
			InputStream in = new FileInputStream("Queries.txt");
			ANTLRInputStream input = new ANTLRInputStream(in);
			
			//ANTLRInputStream input = new ANTLRInputStream(System.in);

		//	ArgQLBNF2ActionsLexer lexer = new ArgQLBNF2ActionsLexer(input);
			ArgQLGrammarLexer lexer = new ArgQLGrammarLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			
		//	ArgQLBNF2ActionsParser parser = new ArgQLBNF2ActionsParser(tokens);
			ArgQLGrammarParser parser = new ArgQLGrammarParser(tokens);
			System.out.println("\n");
			String sparql = parser.query(false).sparqlQuery;
			
		//	System.out.println("");
		//	System.out.println(sparql);
			
			

			//ResultSet results = OntologyManager.executeSQLQuery(sparql);
		//	String res = SPARQLTranslator.collectResults(results);
	//		System.out.println(res);
			SPARQLTranslator.terminate();
			/*System.out.println("Argument Patterns: ");
			for(ArgPattern ap : SPARQLTranslator.argpatterns)
				System.out.println(ap.toString());*/
		//	ParseTree tree = (ParseTree)parser.argpattern();
		//	System.out.println(tree.toString()); // print
															// LISP-style
			/*try {										// tree
			String query = parser.query().sparqlQuery;
			
			
			System.out.println("\n");
			System.out.println(query);
			} catch (Exception e){
				if(e.getClass().getName() == "RecognitionException") {
					System.out.println(e.getMessage());
				}
			}
			*/
			

			
		} catch (IOException e) {     
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
