package com.github.funthomas424242.rades.taggenerator;


import com.google.common.collect.Multiset;
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
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class POSTagCounter {

    protected final Path modelFilepath;

    protected final Charset charset;

    protected final String posTagCategories;

    public POSTagCounter(final Path modelFilepath, final Charset charset, final String posTagCategories) {
        this.modelFilepath = modelFilepath;
        this.charset = charset;
        this.posTagCategories = posTagCategories;
    }

    public Multiset<TaggedWord> parse(final Path textFilePath) throws Exception {
        final String model = modelFilepath.toString();

        final MaxentTagger tagger = new MaxentTagger(model);
        final TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(
                new CoreLabelTokenFactory(), "untokenizable=noneKeep");
        final BufferedReader r = new BufferedReader(new InputStreamReader(
                new FileInputStream(textFilePath.toAbsolutePath().toString()), this.charset));

        final DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(r);
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
        final TreeMultiset<TaggedWord> nounSet = TreeMultiset.create();
        for (List<HasWord> sentence : documentPreprocessor) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            for (TaggedWord word : tSentence) {
                if (word.tag().matches("NE|NN|NNP|NNS|NNPS")) {
                    nounSet.add(word);
                }
            }
        }

        final Multiset<TaggedWord> sortedSet = Multisets.copyHighestCountFirst(nounSet);
        return sortedSet;
    }

    public void printSortedNounsCount(final Multiset<TaggedWord> keywords) {
        keywords.entrySet().stream().forEach(item -> {
            System.out.println(item.getElement().value() + " - " + item.getCount());
        });
    }
}






