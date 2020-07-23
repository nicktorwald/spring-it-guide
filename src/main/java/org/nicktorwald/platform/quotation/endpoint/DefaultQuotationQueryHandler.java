package org.nicktorwald.platform.quotation.endpoint;

import org.nicktorwald.platform.quotation.service.FindQuotationCase;
import org.nicktorwald.platform.quotation.service.ListQuotationsCase;
import org.nicktorwald.platform.util.WebUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The default implementation to process quotation queries.
 *
 * @see org.nicktorwald.platform.quotation.endpoint.CreateQuotationCommand
 * @see org.nicktorwald.platform.quotation.service.CreateQuotationCase
 */
@Component
class DefaultQuotationQueryHandler implements QuotationQueryHandler {

    private final ListQuotationsCase findAllQuotationCase;
    private final FindQuotationCase findQuotationCase;

    DefaultQuotationQueryHandler(ListQuotationsCase findAllQuotationCase,
                                 FindQuotationCase findQuotationCase) {
        this.findAllQuotationCase = findAllQuotationCase;
        this.findQuotationCase = findQuotationCase;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest request) {
        var pageable = WebUtils.extractPageable(request);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(findAllQuotationCase.listAll(pageable).map(quotation -> new QuotationView(quotation.qid(), quotation.text())), QuotationView.class);
    }

    @Override
    public Mono<ServerResponse> getOneByQid(ServerRequest request) {
        var qid = request.pathVariable("qid");
        return findQuotationCase.findByQid(qid)
                .map(quotation -> new QuotationView(quotation.qid(), quotation.text()))
                .flatMap(quotation -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(quotation)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
