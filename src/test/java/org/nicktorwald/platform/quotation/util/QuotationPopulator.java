package org.nicktorwald.platform.quotation.util;

import java.util.Collection;

import org.nicktorwald.platform.quotation.service.domain.Quotation;

/**
 * Used for test purposes to configure quotation fetchers.
 */
public interface QuotationPopulator {

    /**
     * Applies a new collection of quotations to this fetcher.
     *
     * @param quotations quotations to be applied
     */
    void populate(Collection<Quotation> quotations);

    /**
     * Removes all populated quotations.
     */
    void clear();

}
