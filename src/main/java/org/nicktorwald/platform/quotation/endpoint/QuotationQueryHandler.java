package org.nicktorwald.platform.quotation.endpoint;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Query-related requests handler specification.
 */
public interface QuotationQueryHandler {

    /**
     * Handles a request for listing quotations.
     *
     * @param request input request
     * @return output response
     */
    Mono<ServerResponse> listAll(ServerRequest request);

    /**
     * Handles a request for getting one quotation.
     *
     * @param request input request
     * @return output response
     */
    Mono<ServerResponse> getOneByQid(ServerRequest request);

}
