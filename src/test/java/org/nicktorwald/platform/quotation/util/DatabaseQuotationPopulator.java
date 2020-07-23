package org.nicktorwald.platform.quotation.util;

import java.util.Collection;

import org.nicktorwald.platform.quotation.data.QuotationEntity;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.test.StepVerifier;

/**
 * Populates quotations using the configured datasource.
 */
@Component
public class DatabaseQuotationPopulator implements QuotationPopulator {

    private final DatabaseClient databaseClient;

    public DatabaseQuotationPopulator(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void populate(Collection<Quotation> quotations) {
        quotations.stream()
                .map(q -> new QuotationEntity(null, q.qid(), q.text()))
                .forEach(record -> {
                            databaseClient.insert()
                                    .into(QuotationEntity.class)
                                    .using(record)
                                    .then()
                                    .as(StepVerifier::create)
                                    .verifyComplete();
                        }
                );
    }

    @Override
    public void clear() {
        databaseClient.delete()
                .from(QuotationEntity.class)
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

}
