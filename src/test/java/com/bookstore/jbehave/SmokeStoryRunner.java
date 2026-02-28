package com.bookstore.jbehave;

import com.bookstore.jbehave.steps.SmokeTestSteps;
import com.bookstore.jbehave.config.TestConfig;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        // Create Spring context and get the step bean so autowiring works (matches UserRegistrationStoryRunner)
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("test");
        ctx.register(TestConfig.class);
        ctx.refresh();

        SmokeTestSteps steps = ctx.getBean(SmokeTestSteps.class);
        return new InstanceStepsFactory(configuration(), steps);
    }

    @Override
    protected List<String> storyPaths() {
        return Arrays.asList("com/bookstore/jbehave/stories/smoke_tests.story");
    }
}