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
            e.printStackTrace();
            fail();
        }
    }
}

