package com.navikenz.codingexercises.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NERModel {
	
	public static Map<String, String> classifyNamedEntity(List<String> sentences) throws ClassCastException, ClassNotFoundException, IOException {
		
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		Map<String, String> entityMap = new HashMap<>(); 
		
		sentences.stream().forEach(sentence -> {
			String rawText = classifier.classifyToString(sentence, "tsv", false);
			Map<String, String> eMap = Arrays.asList(rawText.split("\n"))
				.stream()
				.map(row -> row.split("\t"))
				.filter(cell -> cell.length == 2 && !cell[1].trim().equals("O"))
				.collect(Collectors.toMap(e -> e[0], e -> e[1]));
			
			entityMap.putAll(eMap);
		});
		
		return entityMap;
	}
	
	public static List<String> sentenceSplitter(String text) {
	    List<String> sentences = new ArrayList<>();
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    CoreDocument doc = new CoreDocument(text);
	    pipeline.annotate(doc);
	    
	    for (CoreSentence sent : doc.sentences()) {
	    	sentences.add(sent.text());
	    }
	    return sentences;
	}
}
