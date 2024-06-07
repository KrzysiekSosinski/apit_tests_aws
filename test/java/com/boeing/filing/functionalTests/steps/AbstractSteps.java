package com.boeing.filing.functionalTests.steps;

import static com.boeing.filing.functionalTests.steps.TestContext.CONTEXT;

public abstract class AbstractSteps {

    public TestContext testContext() {
        return CONTEXT;
    }
}
