package com.github.funthomas424242.rades.taggenerator;

/*-
 * #%L
 * rades.tag-generator
 * %%
 * Copyright (C) 2018 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

public class POSTagCounter {

    protected final Logger LOGGER = LoggerFactory.getLogger(POSTagCounter.class);

    protected final Path modelFilepath;

    protected final Charset charset;

    protected final String posTagCategoriesExpression;

    public POSTagCounter(final Path modelFilepath, final Charset charset, final String posTagCategoriesExpression) {
        this.modelFilepath = modelFilepath;
        this.charset = charset;
        this.posTagCategoriesExpression = posTagCategoriesExpression;
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
                if (word.tag().matches(this.posTagCategoriesExpression)) {
                    nounSet.add(word);
                }
            }
        }

        final Multiset<TaggedWord> sortedSet = Multisets.copyHighestCountFirst(nounSet);
        return sortedSet;
    }

    public void logSortedKeywords(final Multiset<TaggedWord> keywords, final int anzahl) {
        final Counter counter = new Counter();
        keywords.entrySet().stream().forEach(item -> {
            if (counter.value < anzahl) {
                LOGGER.debug(item.getElement().value() + " - " + item.getCount());
                counter.value++;
            } else {
                return;
            }
        });
    }

    protected static class Counter {
        public int value = 0;
    }
}






