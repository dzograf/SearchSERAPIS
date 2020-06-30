package arg.tech.testing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import arg.tech.argql.translator.json.JSONTranslator;
import arg.tech.data.DatabaseManager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openrdf.query.algebra.Load;

import com.mysql.cj.log.Log;

import arg.tech.argql.parser.ArgQLGrammarLexer;
import arg.tech.argql.parser.ArgQLGrammarParser;
import arg.tech.argql.parser.ThrowingErrorListener;
import arg.tech.argql.results.ResultManager;
import arg.tech.argql.translator.sparql.SPARQLTranslator;
//import gr.forth.ics.argql.translator.SymbolTable;


public class Testing {

	public static boolean downloadRDFDataset() {
		String downloadUrlStr = new String("http://www.aifdb.org/rdf/");
		
		try {
			// String rootName = "/data/schemes";
			
		
			   

				File rootDirectory = new File("./data/dbyd");
			   
			   
			// File[] directories = rootDirectory.listFiles();

			File[] files = rootDirectory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".json");
				}
			});
			int i = 0;
			for (File file : files) {
				// System.out.println(i++ + "Name: " + file.getName());
				String withoutExtension = FilenameUtils.removeExtension(file.getName());
				String fileCode = withoutExtension.replaceAll("nodeset", "");
				System.out.println(i++ + " Name: " + fileCode);
				String destination = rootDirectory + "/rdf/" + fileCode + ".rdf";
				File downloaded = new File(destination);
				String downloadUrl = downloadUrlStr + fileCode;
				URL url = new URL(downloadUrl);

				FileUtils.copyURLToFile(url, downloaded);
			}
			System.out.println("Downloaded");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	
	
	private static String translateArgQL(String query, String graph, boolean optimized) {
		InputStream in = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
		String sparql = "";
		//jsonObject = new JSONObject();
		try {
			ANTLRInputStream input = new ANTLRInputStream(in);
			ArgQLGrammarLexer lexer = new ArgQLGrammarLexer(input);
			lexer.removeErrorListeners();
			lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

			CommonTokenStream tokens = new CommonTokenStream(lexer);
			ArgQLGrammarParser parser = new ArgQLGrammarParser(tokens);
			parser.removeErrorListeners();
			parser.addErrorListener(ThrowingErrorListener.INSTANCE);

			sparql = parser.query(optimized).sparqlQuery;
			
			
			
		} catch (ParseCancellationException ex) {
			System.out.println("Syntax Error: " + ex.getMessage());
		//	jsonObject.put("sparql", "");

			//jsonObject.put("results", "Syntax Error\n\n" + ex.getMessage());// jsonArray.toString());
			SPARQLTranslator.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sparql;
	}
	
	
	public static void executeArgQL(String query) {

		String resultsString = "";
		try {
			if (SPARQLTranslator.logicErrorsExist()) {
				for (String error : SPARQLTranslator.getLogicErrors()) {
					resultsString += error;

				}

				System.out.println("Logic error exists: " + resultsString );
			} else if(query.compareTo("")!=0) {


				long execstart = System.currentTimeMillis();

				ResultSet results = DatabaseManager.executeSparqlQuery(query);
				float executionTime = (System.currentTimeMillis() - execstart) ;
				
			//	long collectStartTime = System.currentTimeMillis();

				System.out.println("Query execution...");
		     	resultsString = ResultManager.collectResults(results);
		     	System.out.println("Results:" + resultsString);
				float collectTime = (System.currentTimeMillis() - execstart) ;
				System.out.println("Query execution time: " + executionTime);
				System.out.println("Overall execution time: " + collectTime);
 
				SPARQLTranslator.terminate();
			}


		} 

		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("!!! ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		


}
	private static void consumeAPI(String jsonString) {

		try {

			URL url = new URL("http://tomcat.arg.tech/ArgStructSearch/search/query/exec/");
			
	//		URL url = new URL("http://localhost:8080/ArgStructSearch/search/query/exec/");
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			
			OutputStream os = conn.getOutputStream();
		    os.write(jsonString.getBytes());
		    os.flush();
		    os.close();
			
			
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();

		} catch (Exception e) {
			System.out.println("-->Exception in NetClientGet:- " + e);
		}

	}
	
	
	private static void writeLog(String message){
		try {
		      FileWriter myWriter = new FileWriter("Log.txt");
		      myWriter.write(message + "\n");
		      myWriter.close();
		    } catch (IOException e) {
		      System.out.println("ERROR: Did not write to Log.. ");
		      e.printStackTrace();
		    }

	}
	
	private static void loadEverything() {

		String downloadUrlStr = new String("http://www.aifdb.org/rdf/");
		
		for(int i=8087; i<18520; i++) {
			try {
				// DOWNLOAD 
				String destination = i + ".rdf";
				File downloadFile = new File(destination);
				if(downloadFile.exists()) {
					downloadFile.delete();
				}
				
				String downloadUrl = downloadUrlStr + i;
				URL url = new URL(downloadUrl);

				FileUtils.copyURLToFile(url, downloadFile);

				
				//UPLOAD
				
				DatabaseManager.importFile(destination, "http://"+i);
				
				downloadFile.delete();
				
				
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				writeLog(e.getMessage());
			}
		}
	}
	
	private static void loadFileInVirtuosoService(String nodesetID){
		try {
			URL url = new URL("http://tomcat.arg.tech/ArgStructSearch/search/virtuoso/load/");
			
			
			//URL url = new URL("http://localhost:8080/ArgStructSearch/search/virtuoso/load/");
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			
			OutputStream os = conn.getOutputStream();
		    os.write(nodesetID.getBytes());
		    os.flush();
		    os.close();
			
			
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			} else {
				System.out.println("Loaded");
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();

		} catch (Exception e) {
			System.out.println("-->Exception in NetClientGet:- " + e);
		}
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//downloadRDFDataset();
		try {
			
			loadEverything();
			
			System.out.println("EVERYTHING LOADED");
		//	loadFileInVirtuosoService("16467");
		/*	String jsonString = "{"+
				"\"nodes\": " +
					" [ " +
					   "  {\"nodeID\":1,\"text\":\"\",\"type\":\"I\"}, " +
					    " {\"nodeID\":2,\"text\":\"\",\"type\":\"L\"}, " +
					    " {\"nodeID\":4,\"text\":\"\",\"type\":\"YA\"} "+
					" ],"
					 +
					" \"edges\": " +
					" [ "+
					    " {\"edgeID\":\"e0\", \"fromID\":2, \"toID\":4},"+
					   "  {\"edgeID\":\"e1\", \"fromID\":4,\"toID\":1} " +
					" ], " +
					   " \"locutions\":[]} ";
			
			File file = new File("./files/json/test1.json"); 
			  
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			  
			String st; 
			StringBuilder stringBuilder = new StringBuilder();
			while ((st = br.readLine()) != null) { 
				stringBuilder.append(st);
			}
			  System.out.println(stringBuilder.toString()); 
			
			  
			
			consumeAPI(stringBuilder.toString());*/
			
			
			
		//    DatabaseManager.importRepository("./data/dbyd/rdf/");
		/*	String argqString = JSONTranslator.createArgQLQuery(jsonString);
			
			System.out.println(argqString);
			String sparqlQuery = translateArgQL(argqString, "aif.db", false);
			executeArgQL(sparqlQuery);
			
			System.out.println("Query executed");*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
