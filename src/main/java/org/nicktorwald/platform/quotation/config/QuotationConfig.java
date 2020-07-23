package org.nicktorwald.platform.quotation.config;

import org.nicktorwald.platform.quotation.endpoint.QuotationCommandHandler;
import org.nicktorwald.platform.quotation.endpoint.QuotationQueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * The common quotation service configuration.
 */
@Configuration
@EnableWebFlux
@EnableR2dbcRepositories
class QuotationConfig {

    private final QuotationCommandHandler quotationCommandHandler;
    private final QuotationQueryHandler quotationQueryHandler;

    QuotationConfig(QuotationCommandHandler quotationCommandHandler,
                    QuotationQueryHandler quotationQueryHandler) {
        this.quotationCommandHandler = quotationCommandHandler;
        this.quotationQueryHandler = quotationQueryHandler;
    }

    @Bean
    RouterFunction<?> quotationRoutes() {
        return RouterFunctions.route()
                .path("/quotations", builder ->
                        builder.POST("/", applicationJsonContent(), quotationCommandHandler::create)
                                .GET("/", acceptJson(), quotationQueryHandler::listAll)
                                .GET("/{qid}", acceptJson(), quotationQueryHandler::getOneByQid)
                )
                .build();
    }

    private RequestPredicate acceptJson() {
        return RequestPredicates.accept(MediaType.APPLICATION_JSON);
    }

    private RequestPredicate applicationJsonContent() {
        return RequestPredicates.contentType(MediaType.APPLICATION_JSON)
                .and(acceptJson());
    }


}
