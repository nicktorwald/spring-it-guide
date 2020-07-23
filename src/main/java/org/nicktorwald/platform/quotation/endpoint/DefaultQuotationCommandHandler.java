package org.nicktorwald.platform.quotation.endpoint;

import java.net.URI;
import java.util.Map;

import org.nicktorwald.platform.quotation.service.CreateQuotationCase;
import org.nicktorwald.platform.util.WebUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

/**
 * The default implementation to process quotation commands.
 *
 * @see org.nicktorwald.platform.quotation.endpoint.CreateQuotationCommand
 * @see org.nicktorwald.platform.quotation.service.CreateQuotationCase
 */
@Component
class DefaultQuotationCommandHandler implements QuotationCommandHandler {

    private final CreateQuotationCase createQuotationCase;

    DefaultQuotationCommandHandler(CreateQuotationCase createQuotationCase) {
        this.createQuotationCase = createQuotationCase;
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CreateQuotationCommand.class)
                .doOnNext(CreateQuotationCommand::requireValid)
                .flatMap(command -> createQuotationCase.createOne(command.text()))
                .map(quotation -> new QuotationView(quotation.qid(), quotation.text()))
                .flatMap(quotation -> ServerResponse.created(makeLocation(request, quotation))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(quotation)
                )
                .onErrorMap(error -> new ServerWebInputException(error.getMessage()));
    }

    private URI makeLocation(ServerRequest request, QuotationView quotation) {
        return WebUtils.buildRelativeUri(request.path() + "/{qid}", Map.of("qid", quotation.qid()));
    }

}
