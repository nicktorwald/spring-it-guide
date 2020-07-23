package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import reactor.core.publisher.Mono;

/**
 * Performs an use case like "I'm as a user can create a new quotation"
 */
public interface CreateQuotationCase {

    /**
     * Creates a new quotation using the text specified.
     *
     * @param text quotation text
     * @return created quotation
     */
    Mono<Quotation> createOne(String text);

}
