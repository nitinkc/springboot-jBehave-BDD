package com.bookstore.jbehave;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class VerifyStoryResourceTest {

    @Test
    public void storyResourceShouldBeOnTestClasspath() {
        String path = "com/bookstore/jbehave/stories/component_tests.story";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl.getResourceAsStream(path);
        System.out.println("Checking resource on classpath: " + path + " => " + (in != null));
        Assert.assertNotNull("Story resource should be available on the test classpath: " + path, in);
    }
}

