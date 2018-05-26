package com.github.funthomas424242.rades.taggenerator;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TagGeneratorTest {

    @Test
    public void tag() throws IOException, ClassNotFoundException {
        TagGenerator tagging = new TagGenerator();
        tagging.tag("If you have several test classes, you can combine them into a test suite.");
    }
}

