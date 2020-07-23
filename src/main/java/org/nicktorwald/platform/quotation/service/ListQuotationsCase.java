package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

/**
 * Performs an use case like "I'm as a user can list through the all quotations"
 */
public interface ListQuotationsCase {

    /**
     * Lists all quotations.
     *
     * @param pageable paging options
     * @return stream of the found quotations
     */
    Flux<Quotation> listAll(Pageable pageable);

}
