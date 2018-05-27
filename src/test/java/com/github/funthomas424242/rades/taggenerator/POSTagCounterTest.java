package com.github.funthomas424242.rades.taggenerator;

import org.junit.jupiter.api.Test;
import sun.nio.cs.UTF_32;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.fail;

class POSTagCounterTest {

    @Test
    void parse() {

        final Path modelFilepath = Paths.get("model/german-fast.tagger");
        final Charset charset = StandardCharsets.UTF_8;
        final POSTagCounter tagCounter = new POSTagCounter(modelFilepath,charset);

        final Path textFile = Paths.get("src/main/resources","Der_Prozess.txt");
        System.out.println("Textfile: "+textFile.toAbsolutePath().toString());
        try {
            tagCounter.parse(textFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }



    }
}