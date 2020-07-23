package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The default implementation {@link org.nicktorwald.platform.quotation.service.CreateQuotationCase}
 */
@Service
class DefaultCreateQuotationCase implements CreateQuotationCase {

    private final QuotationModifier quotationModifier;

    DefaultCreateQuotationCase(QuotationModifier quotationModifier) {
        this.quotationModifier = quotationModifier;
    }

    @Override
    public Mono<Quotation> createOne(String text) {
        return Mono.just(Quotation.of(text))
                .flatMap(quotationModifier::save);
    }

}
