package org.nicktorwald.platform.quotation;

import java.net.URI;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nicktorwald.platform.quotation.endpoint.QuotationView;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.nicktorwald.platform.quotation.util.QuotationPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

/**
 * @see org.nicktorwald.platform.quotation.service.ListQuotationsCase
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FindQuotationCaseIT {

    private final QuotationPopulator fetcherPopulator;
    private final WebTestClient webClient;

    @Autowired
    public FindQuotationCaseIT(QuotationPopulator fetcherPopulator,
                               WebTestClient webClient) {
        this.fetcherPopulator = fetcherPopulator;
        this.webClient = webClient;
    }

    @AfterEach
    public void tearDown() {
        fetcherPopulator.clear();
    }

    @Test
    @DisplayName("gets no result when there are no quotations")
    void testEmptyQuotationList() {
        // given
        fetcherPopulator.populate(List.of());
        var notExistingQid = "SOpV6YZRKm1u";

        // when
        var result = webClient.get()
                .uri(builder -> toParticularQuotation(builder, notExistingQid))
                .exchange();

        // then
        result.expectStatus().isNotFound();
    }

    @Test
    @DisplayName("gets a quotation by its QID")
    void testPartialQuotationList() {
        // given
        var existingQuotation =  Quotation.of("yhvsuu2-y0Pj", "If you play with fire, you’ll get burned.");
        fetcherPopulator.populate(List.of(existingQuotation));

        // when
        var result = webClient.get()
                .uri(builder -> toParticularQuotation(builder, existingQuotation.qid()))
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBody(QuotationView.class).value(v -> Quotation.of(v.qid(), v.text()), Matchers.is(existingQuotation));
    }

    @Test
    @DisplayName("gets no quotation by not-existing QID")
    void testPartialQuotationList2() {
        // given
        var notExistingQid = "gH0cAQaNzw4M";
        fetcherPopulator.populate(List.of(
                Quotation.of("yhvsuu2-y0Pj", "If you play with fire, you’ll get burned."),
                Quotation.of("uZFgyVLcr924", "If you want something done right, you have to do it yourself."),
                Quotation.of("Sn2h1Tyg7Y4N", "Keep your friends close, and your enemies closer.")
        ));

        // when
        var result = webClient.get()
                .uri(builder -> toParticularQuotation(builder, notExistingQid))
                .exchange();

        // then
        result.expectStatus().isNotFound();
    }

    private URI toParticularQuotation(UriBuilder builder, String qid) {
        return builder
                .path("quotations/{qid}")
                .build(qid);
    }
}
