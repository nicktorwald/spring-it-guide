package org.nicktorwald.platform.quotation.util;

import org.nicktorwald.platform.quotation.service.QuotationModifier;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * A fake in-memory quotation modifier.
 */
class InMemoryQuotationModifier implements QuotationModifier {

    @Override
    public Mono<Quotation> save(Quotation quotation) {
        return Mono.just(quotation);
    }

}
