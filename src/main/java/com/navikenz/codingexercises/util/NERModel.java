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
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier, props);
		
		Map<String, String> entityMap = new HashMap<>(); 
		
		sentences.stream().forEach(sentence -> {
			String rawText = classifier.classifyToString(sentence, "tabbedEntities", false);
			System.out.println(rawText);
			Map<String, String> eMap = Arrays.asList(rawText.split("\n"))
				.stream()
				.map(row -> row.split("\t"))
				.filter(cell -> cell.length > 2 && !cell[0].trim().equals("") && !cell[1].trim().equals(""))
				.collect(Collectors.toMap(e -> e[0], e -> e[1]));
			
			entityMap.putAll(eMap);
		});
		
		return entityMap;
	}
	
	public static List<String> sentenceSplitter(String text) {
	    List<String> sentences = new ArrayList<>();
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    CoreDocument doc = new CoreDocument(text);
	    pipeline.annotate(doc);
	    
	    for (CoreSentence sent : doc.sentences()) {
	    	sentences.add(sent.text());
	    }
	    return sentences;
	}
	
	public static void main(String[] args) throws ClassCastException, ClassNotFoundException, IOException {
		List<String> sentences = new ArrayList<>();
		sentences.add("Working as a chef, would love to have her own restaurant one day, has spent 2 years at Javu and Alessandros, sister of Ross Geller, married to Chandler Bing, was Rachel Greene's roommate for a long time.");
		System.out.println(classifyNamedEntity(sentences));
	}
}
