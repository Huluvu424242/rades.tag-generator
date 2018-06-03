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
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import edu.stanford.nlp.ling.TaggedWord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class TagCloudTest {

    public static final List<WordFrequency> WORD_FREQUENCIES = new ArrayList<>();


    @BeforeAll
    public static void setUp() {
        final Path modelFilepath = Paths.get("model/german-fast.tagger");
        final Charset charset = StandardCharsets.UTF_8;
        final String posTagCategories = "NN|NNP|NNS|NNPS";
        final POSTagCounter tagCounter = new POSTagCounter(modelFilepath, charset, posTagCategories);

        final Path textFile = Paths.get("src/test/resources", "Der_Prozess.txt");
        try {
            final Multiset<TaggedWord> keywords = tagCounter.parse(textFile);
            keywords.entrySet().stream().forEach(entry -> {
                if (WORD_FREQUENCIES.size() < 50) {
                    WORD_FREQUENCIES.add(new WordFrequency(entry.getElement().word(), entry.getCount()));
                } else {
                    return;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void createASampleTagCloud() {

        try {
            final Dimension dimension = new Dimension(400, 300);
            final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.build(WORD_FREQUENCIES);
            wordCloud.setPadding(2);
            wordCloud.setBackground(new RectangleBackground(dimension));

            final Path path = Paths.get("src/site/resources/figures/tagCloud.png");
            final File file = path.toFile();
            if (file.exists()) {
                file.delete();
            }

            final FileOutputStream fout = new FileOutputStream(file);
            wordCloud.writeToStream("png",fout);
            fout.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            fail();
        }

    }

}
