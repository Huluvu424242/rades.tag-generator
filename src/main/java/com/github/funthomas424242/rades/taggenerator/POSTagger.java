package com.github.funthomas424242.rades.taggenerator;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class POSTagger {


    public List<TokenDescriptionAccessor> createTokenDescriptions(String text) throws IOException, ClassNotFoundException {

        final MaxentTagger maxentTagger = new MaxentTagger("german-fast.tagger");
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