package com.bookstore.jbehave;

import com.bookstore.jbehave.config.TestConfig;
import com.bookstore.jbehave.steps.ComponentTestSteps;
import com.bookstore.jbehave.steps.SmokeTestSteps;
import com.bookstore.jbehave.steps.UserRegistrationSteps;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * JBehave runner for integration tests stories.
 */
@Slf4j
public class IntegrationStoryRunner extends JUnitStories {

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(Format.CONSOLE, Format.TXT, Format.HTML));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("test");
        ctx.register(TestConfig.class);
        ctx.refresh();

        List<Object> stepBeans = new ArrayList<>();

        try {
            stepBeans.add(ctx.getBean(ComponentTestSteps.class));
            log.debug("Successfully loaded ComponentTestSteps bean");
        } catch (Exception e) {
            log.info("ComponentTestSteps bean not available - skipping. Reason: {}", e.getMessage());
        }
        try {
            stepBeans.add(ctx.getBean(UserRegistrationSteps.class));
            log.debug("Successfully loaded UserRegistrationSteps bean");
        } catch (Exception e) {
            log.info("UserRegistrationSteps bean not available - skipping. Reason: {}", e.getMessage());
        }
        try {
            stepBeans.add(ctx.getBean(SmokeTestSteps.class));
            log.debug("Successfully loaded SmokeTestSteps bean");
        } catch (Exception e) {
            log.info("SmokeTestSteps bean not available - skipping. Reason: {}", e.getMessage());
        }

        log.info("IntegrationStoryRunner initialized with {} step bean(s)", stepBeans.size());
        return new InstanceStepsFactory(configuration(), stepBeans.toArray());
    }

    @Override
    protected List<String> storyPaths() {
        return List.of("com/bookstore/jbehave/stories/integration_tests.story");
    }
}
