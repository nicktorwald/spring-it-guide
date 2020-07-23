package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Provides a capability to fetch quotations by criteria.
 */
public interface QuotationFetcher {

    /**
     * Lists existing quotations.
     *
     * @param pageable paging options
     * @return stream of the found quotations
     */
    Flux<Quotation> fetchAll(Pageable pageable);

    /**
     * Retrieves a quotation with ID provided.
     *
     * @param qid target quotation ID
     * @return found quotation, if any
     */
    Mono<Quotation> fetchByQid(String qid);

}
