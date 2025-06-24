package com.bookstore.jbehave;


import com.bookstore.jbehave.steps.UserRegistrationSteps;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Arrays;
import java.util.List;

public class UserRegistrationStoryRunner extends JUnitStories {

    @Override
    public Configuration configuration() {
        return new org.jbehave.core.configuration.MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withFormats(StoryReporterBuilder.Format.CONSOLE));
    }

    @Override
    public List<> stepsFactory() {
        return new InstanceStepsFactory(configuration(), new UserRegistrationSteps()).createCandidateSteps();
    }

    @Override
    protected List<String> storyPaths() {
        return Arrays.asList("com/bookstore/jbehave/stories/user_registration.story");
    }
}

