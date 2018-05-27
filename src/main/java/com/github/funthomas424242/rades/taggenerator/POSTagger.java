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

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class POSTagger {

    final Path modelFilepath;

    public POSTagger(final Path modelFilepath){
        this.modelFilepath=modelFilepath;
    }


    public List<TokenDescriptionAccessor> createTokenDescriptions(String text) throws IOException, ClassNotFoundException {

        final MaxentTagger maxentTagger = new MaxentTagger(modelFilepath.toString());
        ;
        final String tagLine = maxentTagger.tagString(text);
        final String[] tagDescription = tagLine.split("\\s+");
        final List<TokenDescriptionAccessor> tokens = Arrays.stream(tagDescription)
                .map(s -> {
                    final String[] tokenDescription = s.split("_");
                    return new TokenDescriptionBuilder()
                            .withWort(tokenDescription[0])
                            .withWortArt(tokenDescription[1])
                            .build(TokenDescriptionAccessor.class);
                })
                .collect(Collectors.toList());
        return tokens;
    }
}
