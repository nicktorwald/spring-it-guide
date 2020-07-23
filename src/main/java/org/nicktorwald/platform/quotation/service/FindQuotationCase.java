package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import reactor.core.publisher.Mono;

/**
 * Performs an use case like "I'm as a user can obtain a particular quotation"
 */
public interface FindQuotationCase {

    /**
     * Finds a quotation by ID.
     *
     * @param qid target quotation ID
     * @return found quotation, if any
     */
    Mono<Quotation> findByQid(String qid);

}
