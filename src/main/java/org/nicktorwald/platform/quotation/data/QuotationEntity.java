package org.nicktorwald.platform.quotation.data;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Describes a persistent entity for an quotation.
 */
@Table("quotation")
record QuotationEntity(@Id Long id, String qid, String text) {

    public QuotationEntity {
        Objects.requireNonNull(qid, "QID cannot be null");
        Objects.requireNonNull(text, "Quotation text cannot be null");
    }

}
