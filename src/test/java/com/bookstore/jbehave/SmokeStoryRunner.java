package com.bookstore.jbehave;

import com.bookstore.jbehave.steps.SmokeTestSteps;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Arrays;
import java.util.List;

public class SmokeStoryRunner extends JUnitStories {

    @Override
    public Configuration configuration() {
        return new org.jbehave.core.configuration.MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats());
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new SmokeTestSteps());
    }

    @Override
    protected List<String> storyPaths() {
        return Arrays.asList("com/bookstore/jbehave/stories/smoke_tests.story");
    }
}