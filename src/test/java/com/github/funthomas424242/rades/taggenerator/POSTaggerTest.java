package com.github.funthomas424242.rades.taggenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class POSTaggerTest {

    @Test
    public void tag() throws IOException, ClassNotFoundException {
        POSTagger tagging = new POSTagger();
        tagging.tag("In diesem Programm geht es um die Programmiersprache Java. Außerdem wollen " +
                "wir uns mit Sprachanalyse befassen. Die Sprache wird in Token zerlegt und es wird die" +
                "Wortform (Verb, Substantiv, ...) bestimmt. Dabei werden die Stopp Wörter ignoriert." +
                "Anschließend wird durchgezählt. Die 5 Wörter mit den höchsten Zahlen sollten sich " +
                "zum automatischen Taggen des Textes als Schlagwörter eignen.");
    }
}

