package org.nicktorwald.platform.quotation.service;

import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The default implementation {@link org.nicktorwald.platform.quotation.service.FindQuotationCase}
 */
@Service
class DefaultFindQuotationCase implements FindQuotationCase {

    private final QuotationFetcher quotationFetcher;

    public DefaultFindQuotationCase(QuotationFetcher quotationFetcher) {
        this.quotationFetcher = quotationFetcher;
    }

    @Override
    public Mono<Quotation> findByQid(String qid) {
        return quotationFetcher.fetchByQid(qid);
    }

}
