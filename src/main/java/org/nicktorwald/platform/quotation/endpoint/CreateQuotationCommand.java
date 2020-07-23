package org.nicktorwald.platform.quotation.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes a command to create a new quotation.
 */
record CreateQuotationCommand(@JsonProperty("text") String text) {

    public static final int MAX_QUOTATION_TEXT_LENGTH = 140;
    public static final String TEXT_OUT_OF_LENGTH_REASON = "The quotation text cannot be empty or greater than "
            + MAX_QUOTATION_TEXT_LENGTH + "symbols";

    public void requireValid() {
        if (text == null || text.isBlank() || text.trim().length() > MAX_QUOTATION_TEXT_LENGTH) {
            throw new IllegalStateException(TEXT_OUT_OF_LENGTH_REASON);
        }
        ;
    }

}
