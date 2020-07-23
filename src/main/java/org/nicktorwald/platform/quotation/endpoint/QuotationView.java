package org.nicktorwald.platform.quotation.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Outcome quotation view representation.
 */
public record QuotationView(@JsonProperty("qid") String qid, @JsonProperty("text") String text) {

}
