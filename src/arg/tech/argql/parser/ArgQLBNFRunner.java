package arg.tech.argql.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TreeSet;

import org.antlr.v4.runtime.*;

import arg.tech.argql.translator.*;

public class ArgQLBNFRunner {

	
	public static void main(String[] args) {
		try {
			InputStream in = new FileInputStream("Queries.txt");
			
			ANTLRInputStream input = new ANTLRInputStream(in);
			
			//ANTLRInputStream input = new ANTLRInputStream(System.in);

			ArgQLBNFLexer lexer = new ArgQLBNFLexer(input);

			CommonTokenStream tokens = new CommonTokenStream(lexer);

			
			ArgQLBNFParser parser = new ArgQLBNFParser(tokens);
			//ParseTree tree = parser.query();
			//System.out.println(tree.toStringTree(parser)); // print
															// LISP-style
															// tree
			String query = parser.query().getText();
			System.out.println(query);
			/*HashMap<String, TreeSet<String>> results = SPARQLTranslator.executeQuery(query);
			

			System.out.println("\n\nRESULTS");
			System.out.println("=================");
			System.out.println(results.toString());
			
			SPARQLTranslator.terminate();
			int a = 0;*/
			
		} catch (IOException e) {     
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
