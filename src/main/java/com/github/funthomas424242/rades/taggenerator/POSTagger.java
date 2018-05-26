package com.github.funthomas424242.rades.taggenerator;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.IOException;

public class POSTagger {


    public void tag(String text) throws IOException, ClassNotFoundException {

        MaxentTagger maxentTagger = new MaxentTagger("german-fast.tagger");;
        String tag = maxentTagger.tagString(text);
        String[] eachTag = tag.split("\\s+");
        System.out.println("Word      " + "Standford tag");
        System.out.println("----------------------------------");
        for(int i = 0; i< eachTag.length; i++) {
            System.out.println(eachTag[i].split("_")[0] +"           "+ eachTag[i].split("_")[1]);
        }

    }
}