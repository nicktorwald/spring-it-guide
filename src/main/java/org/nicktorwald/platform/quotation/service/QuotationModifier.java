package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import reactor.core.publisher.Mono;

/**
 * Provides a capability to modify quotations.
 */
public interface QuotationModifier {

    /**
     * Applies changes for the quotation.
     *
     * @param quotation target quotation
     * @return saved quotation
     */
    Mono<Quotation> save(Quotation quotation);

}
