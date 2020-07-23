package org.nicktorwald.platform.quotation.endpoint;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Command-related requests handler.
 */
public interface QuotationCommandHandler {

    /**
     * Handles a request for creating a new quotation.
     *
     * @param request input request
     * @return output response
     */
    Mono<ServerResponse> create(ServerRequest request);

}
