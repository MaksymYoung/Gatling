package scenarios;

import static config.BaseHelpers.*;
import static config.SessionKeys.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import api.Navigation;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

public class UserFlow {

    /**
     * @param chairs Percentage of users adding chairs
     * @param cart   Percentage of users proceeding to checkout
     * @return ScenarioBuilder for the e-commerce user journey
     */
    public static ScenarioBuilder userJourney(double chairs, double cart) {
        return scenario("E-commerce End-to-End Journey")
                .exec(flushHttpCache())
                .exec(flushCookieJar())
                .exitBlockOnFail().on(
                        exec(
                                page(Navigation.navigateToHomePage("001_HomePage")),
                                page(Navigation.navigateToCategory("002_Category", TABLES)),
                                page(Navigation.navigateToProduct("003_Tables", TABLES)),
                                page(Navigation.addProductToCart("004_Tables_", TABLES_QUANTITY)),
                                randomSwitch().on(
                                        percent(chairs).then(
                                                exec(
                                                        page(Navigation.navigateToCategory("005_Category", CHAIRS)),
                                                        page(Navigation.navigateToProduct("006_Chairs", CHAIRS)),
                                                        page(Navigation.addProductToCart("007_Chairs_", CHAIRS_QUANTITY))
                                                )
                                        )
                                ),
                                randomSwitch().on(
                                        percent(cart).then(
                                                exec(
                                                        page(Navigation.navigateToCart("008_Cart", CART)),
                                                        page(Navigation.navigateToCheckout("009_Checkout", CHECKOUT)),
                                                        page(Navigation.chooseCountry("010_Choosing_Country")),
                                                        page(Navigation.placeOrder("011_Place_Order", CHECKOUT))
                                                )
                                        )
                                )
                        )
                );
    }

    private static ChainBuilder page(ChainBuilder request) {
        return request.pause(THINK_TIME);
    }
}
