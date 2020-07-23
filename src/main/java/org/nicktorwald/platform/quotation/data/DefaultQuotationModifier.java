package org.nicktorwald.platform.quotation.data;

import org.nicktorwald.platform.quotation.service.QuotationModifier;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * The default quotation modifier that uses a configured persistent
 * repository.
 */
@Component
class DefaultQuotationModifier implements QuotationModifier {

    private final CommandQuotationRepository commandQuotationRepository;

    DefaultQuotationModifier(CommandQuotationRepository commandQuotationRepository) {
        this.commandQuotationRepository = commandQuotationRepository;
    }

    @Override
    public Mono<Quotation> save(Quotation quotation) {
        return commandQuotationRepository.save(new QuotationEntity(null, quotation.qid(), quotation.text()))
                .map(record -> Quotation.of(record.qid(), record.text()));
    }

}
