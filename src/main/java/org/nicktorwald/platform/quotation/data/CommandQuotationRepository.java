package org.nicktorwald.platform.quotation.data;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

/**
 * It serves persistent changes for quotations.
 */
interface CommandQuotationRepository extends Repository<QuotationEntity, Long> {

    /**
     * Inserts a new quotation or updates the existing one.
     *
     * @param quotation target entity
     * @return updated entity
     */
    Mono<QuotationEntity> save(QuotationEntity quotation);

}
