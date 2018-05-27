package com.github.funthomas424242.rades.taggenerator;


import com.google.common.base.Stopwatch;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class POSTagCounter {

    protected final Path modelFilepath;

    protected final Charset charset;

    public POSTagCounter(final Path modelFilepath, Charset charset) {
        this.modelFilepath = modelFilepath;
        this.charset = charset;
    }

    static class ValueComparator implements Comparator<TaggedWord> {

        Map<TaggedWord, Integer> base;

        public ValueComparator(Map<TaggedWord, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(TaggedWord a, TaggedWord b) {
            if (base.get(a) >= base.get(b)) {
                return 1;
            } else {
                return -1;
            } // returning 0 would merge keys
        }
    }

    static class TaggedWordCounter implements Comparable<TaggedWordCounter> {

        private final TaggedWord word;
        private int count = 0;

        public TaggedWordCounter(final TaggedWord word) {
            this.word = word;
        }

        public int getCount() {
            return count;
        }

        public TaggedWord getWord() {
            return word;
        }

        public void increment() {
            count++;
        }

        public int compareTo(TaggedWordCounter o) {
            if (getCount() > o.getCount()) {
                return 1;
            } else {
                return -1;
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((word == null) ? 0 : word.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TaggedWordCounter other = (TaggedWordCounter) obj;
            if (word == null) {
                if (other.word != null)
                    return false;
            } else if (!word.equals(other.word))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "TaggedWordCounter [word=" + word + ", count=" + count + "]";
        }
    }

    public void parse(final Path textFilePath) throws Exception {
        final String model = modelFilepath.toString();


        final MaxentTagger tagger = new MaxentTagger(model);
        final TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(
                new CoreLabelTokenFactory(), "untokenizable=noneKeep");
        final BufferedReader r = new BufferedReader(new InputStreamReader(
                new FileInputStream(textFilePath.toAbsolutePath().toString()), this.charset));
        final PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out,
                this.charset));

        final DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(r);
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
        final TreeMultiset<TaggedWord> nounSet = TreeMultiset.create();
        final TreeMultiset<TaggedWord> properNounSet = TreeMultiset.create();
        final Stopwatch watch = new Stopwatch();
        watch.start();
        for (List<HasWord> sentence : documentPreprocessor) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            for (TaggedWord word : tSentence) {
                if (word.tag().matches("NN|NNP|NNS|NNPS")) {
                    nounSet.add(word);
                }
                if (word.tag().matches("NNP|NNPS")) {
                    properNounSet.add(word);
                }
            }
        }
        pw.println("Processing time: " + watch.toString());
        watch.reset();
        watch.start();

        int max = 10;
        int count = 0;
        Multiset<TaggedWord> sortedSet = Multisets.copyHighestCountFirst(nounSet);
        pw.println("Sorting time: " + watch.toString());
        Iterator<Entry<TaggedWord>> iterator = sortedSet.entrySet().iterator();
        while (count < max && iterator.hasNext()) {
            Entry<TaggedWord> item = iterator.next();
            pw.println(item.getElement().value() + " - " + item.getCount());
            count++;
        }
//		for (Entry<TaggedWord> item: Multisets.copyHighestCountFirst(tree).entrySet()) {
//			pw.println(item.getElement().value() + " - " + item.getCount());
//		}

        watch.reset();
        watch.start();
        count = 0;
        sortedSet = Multisets.copyHighestCountFirst(properNounSet);
        pw.println("Sorting time: " + watch.toString());
        iterator = sortedSet.entrySet().iterator();
        while (count < max && iterator.hasNext()) {
            Entry<TaggedWord> item = iterator.next();
            pw.println(item.getElement().value() + " - " + item.getCount());
            count++;
        }

        pw.close();
    }

}






