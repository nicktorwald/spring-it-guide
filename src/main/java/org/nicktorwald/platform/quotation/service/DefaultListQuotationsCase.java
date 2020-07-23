package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * The default implementation {@link org.nicktorwald.platform.quotation.service.ListQuotationsCase}
 */
@Service
class DefaultListQuotationsCase implements ListQuotationsCase {

    private final QuotationFetcher quotationFetcher;

    public DefaultListQuotationsCase(QuotationFetcher quotationFetcher) {
        this.quotationFetcher = quotationFetcher;
    }

    @Override
    public Flux<Quotation> listAll(Pageable pageable) {
        return quotationFetcher.fetchAll(pageable);
    }

}
