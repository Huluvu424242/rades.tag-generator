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
import edu.stanford.nlp.ling.TaggedWord;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class POSTagCounterTest {

    @Test
    public void parse() {

        final Path modelFilepath = Paths.get("model/german-fast.tagger");
        final Charset charset = StandardCharsets.UTF_8;
        final String posTagCategories = "NN|NNP|NNS|NNPS";
        final POSTagCounter tagCounter = new POSTagCounter(modelFilepath, charset, posTagCategories);

        final Path textFile = Paths.get("src/test/resources", "Der_Prozess.txt");
        try {
            final Multiset<TaggedWord> keywords = tagCounter.parse(textFile);
             // tagCounter.printSortedNounsCount(keywords);
            assertEquals(177,keywords.count(new TaggedWord("Tür","NN")));
            assertEquals(133,keywords.count(new TaggedWord("Hand","NN")));
            assertEquals(125,keywords.count(new TaggedWord("Advokaten","NN")));
            assertEquals(122,keywords.count(new TaggedWord("Mann","NN")));
            assertEquals(118,keywords.count(new TaggedWord("Maler","NN")));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }
}
