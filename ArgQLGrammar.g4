/**
 * Define a grammar called ArgQLBNF
 */
 grammar ArgQLGrammar;

 @header {
	import java.util.*;
	import arg.tech.argql.translator.sparql.*;
	import arg.tech.argql.results.*;
	import arg.tech.argql.patterns.*;
	import arg.tech.argql.filters.*;
	import arg.tech.utils.Enums.*;
}

 @members {
	/*StringBuilder sparqlBuilder = new StringBuilder();
	String rdfPrefix = "rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	String aifPrefix = "aif:<http://www.arg.dundee.ac.uk/aif#>";*/
}

 query [boolean optimized] returns [String sparqlQuery] @init {$sparqlQuery = "";
		SPARQLTranslator.initialize(optimized);
	}
 @after {
	//	SPARQLTranslator.terminate();
		$sparqlQuery = SPARQLTranslator.generateSPARQLQuery(optimized);
		
	}
 :
 	MATCH
 
 	(
 		dialogue_pattern {
 			DialoguePattern dp = $dialogue_pattern.value;
 			SPARQLTranslator.addDialoguePattern(dp);
 		}
 		(
 			',' dialogue_pattern {
 				DialoguePattern dp1 = $dialogue_pattern.value;
 				SPARQLTranslator.addDialoguePattern(dp1);
 			}
 		)*
 	)
 	(RETURN returnValue (',' returnValue)*)?
 	/*{
		sparqlBuilder.append("}");
	}*/

 ;

returnValue: 
VARIABLE {
			SPARQLTranslator.addVarReturnvalue($VARIABLE.text);
		}
		| PATH {String var1, var2; }
		'(' VARIABLE { var1 = $VARIABLE.text;}',' 
		  VARIABLE { var2 = $VARIABLE.text; }')' {
			SPARQLTranslator.addPathReturnValue(var1, var2); 
		  };


 dialogue_pattern returns[DialoguePattern value]
 @init {
	ArgPattern ap1;
	PathPatternCollection pathcollection = new PathPatternCollection();
 	PathPatternCollection newPathCollection = new PathPatternCollection();
 	
}
 :
 	argpattern
 	{
			ap1 = $argpattern.value;
		}

 	(
 		pathpattern
 		{
				//System.out.println("Paths: ");
				//System.out.println($pathpattern.value.toString());
				newPathCollection = $pathpattern.value;
			}

 		argpattern
 		{
				ArgPattern ap2 = $argpattern.value;
				newPathCollection.updateFirst(ap1);
				newPathCollection.updateLast(ap2);
				
								
			    pathcollection = PathPatternCollection.merge(pathcollection, newPathCollection);
			//	sparqlBuilder.append(ap2.getSparqlRepresentation());
						
				
			/* 				
				for(int j=0; j < pathcollection.getPathSet().size(); j++){
					PathPattern path = (PathPattern) pathcollection.getPathSet().get(j);
					
					if((j + 1) < pathcollection.getPathSet().size())
						sparqlBuilder.append("{\n");
				
					
					for(int i=0; i < path.getRelSequence().size(); i++) {
						RelationPattern relPattern = path.getRelSequence().get(i);
						
						if(i!=path.getRelSequence().size()-1)
						{
							sparqlBuilder.append(relPattern.getAp2().getSparqlRepresentation());
						}
							
						sparqlBuilder.append(relPattern.getSparqlRepresentation());
					}
					 
					
					if((j + 1) < pathcollection.getPathSet().size()){
						sparqlBuilder.append("}\n");	
						sparqlBuilder.append("UNION\n");
					}
				}
				 
				if(pathcollection != null){
		 			System.out.println(pathcollection.toString());  
		 		}*/
				ap1 = ap2;
				
			}

 	)* 
 	{
 		DialoguePattern dp = null;
 		if(pathcollection.getPathSet().isEmpty()) {
 			dp = new DialoguePattern(ap1);
 		} else{
 			dp = new DialoguePattern(pathcollection); 
 		}
 		  
 		 $value = dp;
 	}
 ;

 argpattern returns [ArgPattern value]
 @init { 
	String raVar = SPARQLTranslator.generateRAVariable();
	ArgPattern arg_pattern = new ArgPattern();
	arg_pattern.setRaVariable(raVar);
}
 @after {
	//SPARQLTranslator.addArgPattern(arg_pattern);
	$value = arg_pattern;
	
}
 : 
   (
   	VARIABLE
 	{
			if(SPARQLTranslator.addVar($VARIABLE.text, VariableType.ARGUMENT)){
				arg_pattern = SPARQLTranslator.argPatternVariable($VARIABLE.text, raVar);
				//sparqlBuilder.append(arg_pattern.getSparqlRepresentation());
			}
			
		}

 	|
 	(
 		VARIABLE ':'
 		{
			if(SPARQLTranslator.addVar($VARIABLE.text, VariableType.ARGUMENT))
				arg_pattern.setArgVar($VARIABLE.text);
			
		}

 	)? '<' premisePattern [arg_pattern]
 	{
					PremisePattern prempattern = $premisePattern.value;
					
					arg_pattern.setPremisePattern(prempattern);
							}

 	',' conclusionPattern [arg_pattern]
 	{
					ConclusionPattern conclPattern = $conclusionPattern.value;
					arg_pattern.setConclusionPattern(conclPattern);
					}

 	'>'
 	{
								
				}
			
	)
	(argFilters {
		Metadata meta = $argFilters.value;
		arg_pattern.setMetadata(meta);
	})?
 ;

 premisePattern [ArgPattern arg_pattern] returns [PremisePattern value]
 @init {
	PremisePattern premPattern = null;
}
 :
 	propositionSet
 	{ 
			ArrayList<PropositionPattern> propositions = $propositionSet.value;
			premPattern = SPARQLTranslator.premisePropSet(propositions, arg_pattern.getRaVariable());
			$value = premPattern;
		}

 	| VARIABLE
 	{
		if(SPARQLTranslator.addVar($VARIABLE.text, VariableType.PREMISE)){
				premPattern = SPARQLTranslator.premiseVar($VARIABLE.text, arg_pattern.getRaVariable());				
			}
		}
 	(
 		premiseFilters [premPattern, arg_pattern.getRaVariable()] {
 			premPattern.setFilter($premiseFilters.value);
 		}
 	)? {
 		$value = premPattern;
 	}
 	| EMPTYSET { 
 		premPattern = SPARQLTranslator.premiseEmpty();
 		$value = premPattern; 
 	}
 	
 ;

 conclusionPattern [ArgPattern ap] returns [ConclusionPattern value]
 :
 	VARIABLE
 	{
		if(SPARQLTranslator.addVar($VARIABLE.text, VariableType.CONCLUSION)){
			ConclusionPattern conclPattern = SPARQLTranslator.conclVarPattern($VARIABLE.text);
			conclPattern.setArgPattern(ap);
			$value = conclPattern;	
		}
	} 
 	| TEXT
 	{
		ConclusionPattern conclPattern = SPARQLTranslator.conclPropositionPattern($TEXT.text);
		conclPattern.setArgPattern(ap);
		//sparqlBuilder.append(conclPattern.getSparqlRepresentation());
		$value = conclPattern;
	}
 ;

argFilters returns [Metadata value]
@init {
	Metadata meta = new Metadata();
}

@after{
	$value = meta;
}:
	'['
		af
		{
			meta.addSimpleEntry($af.value);
		} 
		(',' af{
			meta.addSimpleEntry($af.value);
		})*
	']';

af returns [AbstractMap.SimpleEntry<String, String> value]: 
	SCHEME DOTS TEXT{
 		String scheme = $TEXT.text;
 		$value = new AbstractMap.SimpleEntry<String, String>("sch", scheme);
	} |
	  metadata {
	  	$value = $metadata.value;
	  };

metadata returns [AbstractMap.SimpleEntry<String, String> value]:
 TIMESTAMP DOTS TEXT {
 	String timestamp = $TEXT.text;
 	$value = new AbstractMap.SimpleEntry<String, String>("tm", timestamp);
 }
 | 
 AUTHOR DOTS TEXT
;

premiseFilters [PremisePattern premPattern, String raVar] returns [Filter value]
 :
 	'['
 	(
 		inclusionFilter [premPattern, raVar] {
 			$value = $inclusionFilter.value;
 		}
 		| joinFilter [premPattern, raVar] {
 			$value = $joinFilter.value;
 		}
 	) ']'
 ;

 inclusionFilter [PremisePattern premPattern, String raVar] returns [InclusionFilter value]
 :
 	'/'
 	(
 		propositionSet
 		{
			$value = new InclusionFilter(premPattern, $propositionSet.value, raVar);
		}

 		| VARIABLE
 		{ 
			ArgPattern ap2 = SPARQLTranslator.premVarExists($VARIABLE.text);
			if(ap2 != null){
				$value = new InclusionFilter(premPattern, ap2, raVar);
			}						
		}

 	)
 ;

 joinFilter [PremisePattern premPattern, String raVar] returns[JoinFilter value]
 :
 	'.'
 	(
 		propositionSet
 		{
			$value =  new JoinFilter(premPattern, $propositionSet.value, raVar); 	
		}

 		| VARIABLE
 		{
			ArgPattern ap2 = SPARQLTranslator.premVarExists($VARIABLE.text);
			if(ap2 != null){
				$value = new JoinFilter(premPattern, ap2, raVar);
			} 
		}

 	)
 ;


 propositionSet returns [ArrayList<PropositionPattern> value]
 @init {ArrayList<PropositionPattern> propSet = new ArrayList<PropositionPattern>();}
 @after { $value = propSet; }
 :
 	'{' TEXT
 	{
			PropositionPattern prop = SPARQLTranslator.newPropositionPattern($TEXT.text);
			propSet.add(prop);
			//sparqlBuilder.append(prop.getSparqlRepresentation());
		}

 	(
 		',' TEXT
 		{
				PropositionPattern prop2 = SPARQLTranslator.newPropositionPattern($TEXT.text);
				propSet.add(prop2);
			//	sparqlBuilder.append(prop2.getSparqlRepresentation());
				}

 	)* '}'
 ;

 /*//argVar : VARIABLE;
//argFilters: aboutFilter;

//aboutFilter: ABOUT topicPattern;

//topicPattern: VARIABLE | STRINGLITERAL;*.
 
/*propositionpattern : 
		VARIABLE  | 
		STRINGLITERAL ;
*/
/*pathpattern returns[ArrayList<PathPattern> value]

@after{
	
}: 
			relation { 	
				System.out.println("Relation Type: " +$relation.text);
			 	if(paths.isEmpty()) {
					System.out.println("Empty path list");
				//	PathPattern path = new Pathpattern();
					//path.addToPath($relation.value);
				} else {
					for(PathPattern path : paths) {
					//	path.addToPath($relation.value);
					}
				}
				$value = paths;
			}
				
			  
			| 
			
			pathpattern
			 '/' 
			 	 pathpattern
			 	 |
			 '('pathpattern{
			 	
			   } ')' '*' INT {
			   	
			   	
			 	}
			 | 
			 
			 '(' pathpattern ')' '+' INT;
*/
 pathpattern returns [PathPatternCollection value]
 @init{
 	PathPatternCollection leftCollection;
 }
 @after{
 	$value = leftCollection;
 }
 : //[PathPatternCollection pathcollection] returns [PathPatternCollection value] : 
 	pp
 	{
//	pathcollection = $pp.value;
		leftCollection = $pp.value;
	}

 	(
 		'/' pp
 		{
 			
			PathPatternCollection rightCollection= $pp.value;
 			
 			String raVar = SPARQLTranslator.generateRAVariable();
			ArgPattern newAP = SPARQLTranslator.argPatternVariable("_", raVar);
			
			leftCollection.updateLast(newAP);
			rightCollection.updateFirst(newAP);		
								
			leftCollection = PathPatternCollection.merge(leftCollection, rightCollection);
		}

 	)*
 ;

 pp returns [PathPatternCollection value]
 :
 	relation
 	{
			PathPatternCollection pathcollection = new PathPatternCollection();
			PathPattern path = new PathPattern();
			path.addToPath($relation.value);
			pathcollection.addToCollection(path);
			$value = pathcollection;
			/* 
			 	if(pathcollection.getPathSet().isEmpty()) {
					PathPattern path = new PathPattern();
					path.addToPath($relation.value);
					pathcollection.addToCollection(path);
				} else {
					for(PathPattern path : pathcollection.getPathSet()) {
						path.addToPath($relation.value);
					//	System.out.println("Path Collection: "+pathcollection.toString());
					
					}
				}
				$value = pathcollection;*/
		}

 	| '(' pathpattern')'
 	(
 		'*' INT
 		{
   		 				PathPatternCollection pathcollection = $pathpattern.value;
   		 			 	PathPatternCollection newCollection = new PathPatternCollection();
   		 			 	
   		 				int num = Integer.parseInt($INT.text);
   		 				for(PathPattern pathpattern : pathcollection.getPathSet()){
   		 					newCollection.addToCollection(PathPattern.multiplyPathWith(pathpattern, num));
   		 				}	
   		 				$value = newCollection;
   					 }

 		| '+' INT
 		{
   						PathPatternCollection pathcollection = $pathpattern.value;
   						PathPatternCollection newCollection = new PathPatternCollection();
   						int num = Integer.parseInt($INT.text);
   						
   						for(int i=0; i < pathcollection.getPathSet().size(); i++){
   							PathPattern base = PathPattern.deepCopy(pathcollection.getPathSet().get(i));
   						//	PathPattern newPath = new PathPattern();
   							for(int j=0; j<num; j++){	
   								if(j==0) {
   									newCollection.addToCollection(base);
   					//				newPath.relSequence.addAll(base.relSequence);
   								} else{
   									int prevPathPosition = newCollection.getPathSet().size()-1;
   									PathPattern prevPath = newCollection.getPathSet().get(prevPathPosition);
   									
   								//	ArrayList<RelationPattern> prevRelSequence = new ArrayList<RelationPattern>(newCollection.getPathSet().get(prevPathPosition).getRelSequence());
   							//		PathPattern prevPath = new PathPattern();
   								//	prevPath.setRelSequence(prevRelSequence);
   									PathPattern newPath = PathPattern.appendPathPattern(prevPath, base);
   									
   									newCollection.addToCollection(newPath);
   								}
   							}
   						}
   						
   						
   						/*for(int i=1; i<=num; i++){
   							for(int j=0; j < pathcollection.getPathSet().size(); j++){
   							 	PathPattern pathpattern = pathcollection.getPathSet().get(j);
   							
   								PathPattern newPath = PathPattern.multiplyPathWith(pathpattern, i); 
   		 						newCollection.addToCollection(newPath);
   		 					}
   						}*/
   						$value = newCollection;
   					 }

 	)
 ;

 relation returns [RelationPattern value]
 :
 	(
 		ATTACK
 		{
	 	//$value = "attack";
	 		$value = (AttackPattern)SPARQLTranslator.attack();
	 	}

 		| REBUT
 		{
	    	$value = (RebutPattern)SPARQLTranslator.rebut();
	    	//$value = "rebut";
	 	}

 		| UNDERCUT
 		{
		 $value = (UndercutPattern)SPARQLTranslator.undercut();
		// $value = "undercut";
	 	}
	 	
 		| UNDERMINE
 		{
		 $value = (UnderminePattern)SPARQLTranslator.undermine();
		// $value = "undercut";
	 	}
 		| SUPPORT
 		{
	 	$value = (SupportPattern)SPARQLTranslator.support();
	 	// $value = "support";
	 	}

 		| ENDORSE
 		{
	 	$value  = (EndorsePattern)SPARQLTranslator.endorse();
	 	//$value = "endorse";
	 	}

 		| BACK
 		{
		$value  = (BackPattern)SPARQLTranslator.backing();
	 	//$value = "back";
	 	}

 	)
 ;

 MATCH
 :
 	(
 		'M'
 		| 'm'
 	)
 	(
 		'A'
 		| 'a'
 	)
 	(
 		'T'
 		| 't'
 	)
 	(
 		'C'
 		| 'c'
 	)
 	(
 		'H'
 		| 'h'
 	)
 ;
 
 PATH: ('P' | 'p')('A' | 'a')('T' | 't')('H' | 'h');

 RETURN
 :
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'E'
 		| 'e'
 	)
 	(
 		'T'
 		| 't'
 	)
 	(
 		'U'
 		| 'u'
 	)
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'N'
 		| 'n'
 	)
 ;

 ATTACK
 :
 	(
 		'A'
 		| 'a'
 	)
 	(
 		'T'
 		| 't'
 	)
 	(
 		'T'
 		| 't'
 	)
 	(
 		'A'
 		| 'a'
 	)
 	(
 		'C'
 		| 'c'
 	)
 	(
 		'K'
 		| 'k'
 	)
 ;

 REBUT
 :
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'E'
 		| 'e'
 	)
 	(
 		'B'
 		| 'b'
 	)
 	(
 		'U'
 		| 'u'
 	)
 	(
 		'T'
 		| 't'
 	)
 ;

 UNDERCUT
 :
 	(
 		'U'
 		| 'u'
 	)
 	(
 		'N'
 		| 'n'
 	)
 	(
 		'D'
 		| 'd'
 	)
 	(
 		'E'
 		| 'e'
 	)
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'C'
 		| 'c'
 	)
 	(
 		'U'
 		| 'u'
 	)
 	(
 		'T'
 		| 't'
 	)
 ;
 
 UNDERMINE
 :
 	(
 		'U'
 		| 'u'
 	)
 	(
 		'N'
 		| 'n'
 	)
 	(
 		'D'
 		| 'd'
 	)
 	(
 		'E'
 		| 'e'
 	)
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'M'
 		| 'm'
 	)
 	(
 		'I'
 		| 'i'
 	)
 	(
 		'N'
 		| 'n'
 	)
 	(
 		'E'
 		| 'e'
 	)
 ;

 SUPPORT
 :
 	(
 		'S'
 		| 's'
 	)
 	(
 		'U'
 		| 'u'
 	)
 	(
 		'P'
 		| 'p'
 	)
 	(
 		'P'
 		| 'p'
 	)
 	(
 		'O'
 		| 'o'
 	)
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'T'
 		| 't'
 	)
 ;

 ENDORSE
 :
 	(
 		'E'
 		| 'e'
 	)
 	(
 		'N'
 		| 'n'
 	)
 	(
 		'D'
 		| 'd'
 	)
 	(
 		'O'
 		| 'o'
 	)
 	(
 		'R'
 		| 'r'
 	)
 	(
 		'S'
 		| 's'
 	)
 	(
 		'E'
 		| 'e'
 	)
 ;

 BACK
 :
 	(
 		'B'
 		| 'b'
 	)
 	(
 		'A'
 		| 'a'
 	)
 	(
 		'C'
 		| 'c'
 	)
 	(
 		'K'
 		| 'k'
 	)
 ;

 VARIABLE
 :
 	'?'
 	(
 		'a' .. 'z'
 		| 'A' .. 'Z'
 	)
 	(
 		'a' .. 'z'
 		| 'A' .. 'Z'
 		| '_'
 		| '0' .. '9'
 	)*
 ;
 
EMPTYSET: '_';

TIMESTAMP: ('t'|'T')('m'|'M');

AUTHOR: ('A'|'a')('U'|'u')('T'|'t')('H'|'h');

SCHEME: ('S'|'s')('C'|'c')('H'|'h');

DOTS: ':';

 TEXT
 :
 	'"' .*? '"'
 ;
 //STRINGLITERAL: '"'.*?'"';
 //'"' (options {greedy=false;} : ~('\u0022' |'\u005C' | '\u000A' | '\u000D') | ECHAR)* '"'; 

 //ECHAR: '\\' ('t' | 'b' | 'n' | 'r' | 'f' | '\\' | '"' | '\'');

 INT
 :
 	'0' .. '9'+
 ;

 WS
 :
 	(
 		' '
 		| '\t'
 		| '\n'
 		| '\r'
 	)+
 	{skip();}

 ;

 //[ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines


