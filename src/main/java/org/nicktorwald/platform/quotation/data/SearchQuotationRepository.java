package org.nicktorwald.platform.quotation.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * It serves queries for quotations.
 */
interface SearchQuotationRepository extends Repository<QuotationEntity, Long> {

    /**
     * Finds a particular quotation by ID.
     *
     * @param qid target quotation ID
     * @return found quotation, if any
     */
    Mono<QuotationEntity> findByQid(String qid);

    /**
     * Lists all quotations.
     *
     * @param pageable paging options
     * @return list of quotations
     */
    Flux<QuotationEntity> findAllBy(Pageable pageable);

}
