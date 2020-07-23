package org.nicktorwald.platform.quotation.service.domain;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * Describes an quotation as a domain object.
 */
public record Quotation(String qid, String text) {

    private static final SecureRandom BYTES_GENERATOR = new SecureRandom();
    private static final int CODE_LENGTH_IN_BYTES = 9;

    public Quotation {
        Objects.requireNonNull(qid, "QID cannot be null");
        Objects.requireNonNull(text, "Quotation text cannot be null");
    }

    public Quotation withText(String text) {
        return new Quotation(this.qid, text);
    }

    public static Quotation of(String text) {
        return of(generateQid(), text);
    }

    public static Quotation of(String qid, String text) {
        return new Quotation(qid, text);
    }

    private static String generateQid() {
        var bytes = new byte[CODE_LENGTH_IN_BYTES];
        BYTES_GENERATOR.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
