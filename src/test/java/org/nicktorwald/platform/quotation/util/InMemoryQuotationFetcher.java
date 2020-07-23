package org.nicktorwald.platform.quotation.util;

import java.util.Collection;
import java.util.Collections;

import org.nicktorwald.platform.quotation.service.QuotationFetcher;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A fake in-memory quotation fetcher.
 */
class InMemoryQuotationFetcher implements QuotationFetcher, QuotationPopulator {

    private Collection<Quotation> quotations;

    @Override
    public Flux<Quotation> fetchAll(Pageable pageable) {
        return Flux.fromIterable(quotations)
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    @Override
    public Mono<Quotation> fetchByQid(String qid) {
        return Flux.fromIterable(quotations)
                .filter(quotation -> quotation.qid().equals(qid))
                .next();
    }

    @Override
    public void populate(Collection<Quotation> quotations) {
        this.quotations = Collections.unmodifiableCollection(quotations);
    }

    @Override
    public void clear() {
        quotations = Collections.emptyList();
    }

}
