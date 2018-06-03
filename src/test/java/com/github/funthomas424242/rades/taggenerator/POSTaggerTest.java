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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class POSTaggerTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(POSTaggerTest.class);

    @Test
    public void tag() {
        final Path path = Paths.get("model/german-fast.tagger");
        final POSTagger posTagger = new POSTagger(path);
        try {
            final Path textPath = Paths.get("src/test/resources", "Der_Prozess.txt");
            final byte[] encoded = Files.readAllBytes(textPath);
            final String text = new String(encoded, StandardCharsets.UTF_8).substring(341,490);

            final List<TokenDescriptionAccessor> tokens = posTagger.createTokenDescriptions(text);
            assertEquals("Jemand", tokens.get(0).getWort());
            assertEquals("NE", tokens.get(0).getWortArt());
            assertEquals("verleumdet", tokens.get(4).getWort());
            assertEquals("VVPP", tokens.get(4).getWortArt());
            assertEquals("Köchin", tokens.get(23).getWort());
            assertEquals("NN", tokens.get(23).getWortArt());
            tokens.forEach(token -> LOGGER.debug(token.getWort() + "_" + token.getWortArt()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }
}

