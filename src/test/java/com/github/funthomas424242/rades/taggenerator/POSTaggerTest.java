package com.github.funthomas424242.rades.taggenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class POSTaggerTest {

    @Test
    public void tag() {
        final Path path = Paths.get("model/german-fast.tagger");
        final POSTagger posTagger = new POSTagger(path);
        final List<TokenDescriptionAccessor> tokens;
        try {
            tokens = posTagger.createTokenDescriptions("In diesem Programm geht es um die Programmiersprache Java. Außerdem wollen " +
                    "wir uns mit Sprachanalyse befassen. Die Sprache wird in Token zerlegt und es wird die" +
                    "Wortform (Verb, Substantiv, ...) bestimmt. Dabei werden die Stopp Wörter ignoriert." +
                    "Anschließend wird durchgezählt. Die 5 Wörter mit den höchsten Zahlen sollten sich " +
                    "zum automatischen Taggen des Textes als Schlagwörter eignen.");
            assertEquals("In", tokens.get(0).getWort());
            assertEquals("APPR", tokens.get(0).getWortArt());
            assertEquals("Programm", tokens.get(2).getWort());
            assertEquals("NN", tokens.get(2).getWortArt());
            assertEquals("geht", tokens.get(3).getWort());
            assertEquals("VVFIN", tokens.get(3).getWortArt());
        } catch (IOException | ClassNotFoundException e) {
            fail();
        }
    }
}

