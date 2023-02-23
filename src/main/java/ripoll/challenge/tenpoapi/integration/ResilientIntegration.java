package ripoll.challenge.tenpoapi.integration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResilientIntegration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResilientIntegration.class);

    protected final CircuitBreaker circuitBreaker;

    protected final Retry retry;

    protected ResilientIntegration(CircuitBreaker circuitBreaker, Retry retry) {
        this.circuitBreaker = circuitBreaker;
        this.retry = retry;
    }

    protected <T> Option<T> optionCall(CheckedFunction0<T> supplier) {
        return Option.of(call(supplier));
    }

    protected <T> T call(CheckedFunction0<T> supplier) {
        return tryCall(supplier)
                .recover(it -> {
                    LOGGER.error("Couldn't call integration", it);
                    return null;
                }).get();
    }

    protected <T> Try<T> tryCall(CheckedFunction0<T> supplier) {
        return Try.of(CircuitBreaker.decorateCheckedFunction(circuitBreaker, supplier));
    }

}