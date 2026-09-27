package simulations;

import static assertions.AssertionsMap.ASSERT_MAPPING;
import static config.BaseHelpers.*;
import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.Simulation;
import scenarios.UserFlow;

import java.util.List;

public class PerfTestSimulation extends Simulation {
    {
        // Run command:
        // .\mvnw.cmd gatling:test "-Dgatling.simulationClass=simulations.PerfTestSimulation" -DLOAD_MODEL=open
        // .\mvnw.cmd gatling:test "-Dgatling.simulationClass=simulations.PerfTestSimulation" -DLOAD_MODEL=open -DASSERTION_TYPE=ecommerceOrder
        // .\mvnw.cmd gatling:test "-Dgatling.simulationClass=simulations.PerfTestSimulation" -DLOAD_MODEL=closed
        // .\mvnw.cmd gatling:test "-Dgatling.simulationClass=simulations.PerfTestSimulation" -DLOAD_MODEL=closed -DASSERTION_TYPE=ecommerceOrder
        // Opinion: preflight requests are not required for this scenario because the flow sends same-origin backend
        // form/AJAX requests; OPTIONS calls would only be needed if the browser performed CORS preflight checks.

        setUp(
                injection()
        )
                .normalPausesWithPercentageDuration(THINK_TIME_STD_DEV_PERCENT)
                .protocols(HTTP_PROTOCOL)
                .assertions(assertions());
    }

    private PopulationBuilder injection() {
        if ("closed".equalsIgnoreCase(LOAD_MODEL)) {
            return UserFlow.userJourney(CHAIRS_PERCENTAGE, CART_PERCENTAGE)
                    .injectClosed(rampConcurrentUsers(0).to(CONCURRENT_USERS).during(RAMP_UP_PERIOD));
        }

        return UserFlow.userJourney(CHAIRS_PERCENTAGE, CART_PERCENTAGE)
                .injectOpen(rampUsers(OPEN_USERS).during(RAMP_UP_PERIOD));
    }

    private Assertion[] assertions() {
        List<Assertion> selectedAssertions = ASSERT_MAPPING.get(ASSERTION_TYPE);
        if (selectedAssertions == null) {
            throw new IllegalArgumentException(
                    "Unsupported ASSERTION_TYPE '" + ASSERTION_TYPE + "'. Supported values: " + ASSERT_MAPPING.keySet()
            );
        }

        return selectedAssertions.toArray(new Assertion[0]);
    }
}
