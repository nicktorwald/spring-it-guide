package org.nicktorwald.platform.quotation.data;

import org.nicktorwald.platform.quotation.service.QuotationFetcher;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The default quotation fetcher that uses a configured persistent
 * repository.
 */
@Component
class DefaultQuotationFetcher implements QuotationFetcher {

    private final SearchQuotationRepository searchQuotationRepository;

    DefaultQuotationFetcher(SearchQuotationRepository searchQuotationRepository) {
        this.searchQuotationRepository = searchQuotationRepository;
    }

    @Override
    public Flux<Quotation> fetchAll(Pageable pageable) {
        return searchQuotationRepository.findAllBy(pageable)
                .map(record -> Quotation.of(record.qid(), record.text()));
    }

    @Override
    public Mono<Quotation> fetchByQid(String qid) {
        return searchQuotationRepository.findByQid(qid)
                .map(record -> Quotation.of(record.qid(), record.text()));
    }
}
