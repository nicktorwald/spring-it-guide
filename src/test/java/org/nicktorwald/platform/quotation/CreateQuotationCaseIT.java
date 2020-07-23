package org.nicktorwald.platform.quotation;

import java.net.URI;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nicktorwald.platform.quotation.endpoint.QuotationView;
import org.nicktorwald.platform.quotation.util.QuotationPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

/**
 * @see org.nicktorwald.platform.quotation.service.ListQuotationsCase
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateQuotationCaseIT {

    private final QuotationPopulator fetcherPopulator;
    private final WebTestClient webClient;

    @Autowired
    public CreateQuotationCaseIT(QuotationPopulator fetcherPopulator,
                                 WebTestClient webClient) {
        this.fetcherPopulator = fetcherPopulator;
        this.webClient = webClient;
    }

    @AfterEach
    public void tearDown() {
        fetcherPopulator.clear();
    }

    @Test
    @DisplayName("creates a new quotation")
    void testCreateNewQuotation() {
        // given
        var quotationText = "Knowledge is power.";
        var quotationBody = Map.of(
                "text", quotationText
        );

        // when
        var result = webClient.post()
                .uri(this::toCreateQuotation)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(quotationBody)
                .exchange();

        // then
        result.expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody(QuotationView.class)
                    .value(QuotationView::text, Matchers.is(quotationText))
                    .value(QuotationView::qid, Matchers.not(Matchers.emptyOrNullString()));
    }

    @Test
    @DisplayName("cannot create a blank quotation")
    void testFailToCreateBlankQuotation() {
        // given
        var quotationText = "   ";
        var quotationBody = Map.of(
                "text", quotationText
        );

        // when
        var result = webClient.post()
                .uri(this::toCreateQuotation)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(quotationBody)
                .exchange();

        // then
        result.expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("cannot create a too long quotation")
    void testFailToCreateTooLongQuotation() {
        // given
        var quotationText = "If someone whom I don’t like doesn’t like someone else whom I don’t like, we can act" +
                " like friends and unite against the other person (common in war)";
        var quotationBody = Map.of(
                "text", quotationText
        );

        // when
        var result = webClient.post()
                .uri(this::toCreateQuotation)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(quotationBody)
                .exchange();

        // then
        result.expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("cannot create a wrong composed quotation")
    void testFailToCreateWrongComposedQuotation() {
        // given
        var quotationText = "Laughter is the best medicine.";
        var quotationBody = Map.of(
                "text-field", quotationText
        );

        // when
        var result = webClient.post()
                .uri(this::toCreateQuotation)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(quotationBody)
                .exchange();

        // then
        result.expectStatus().isBadRequest();
    }

    private URI toCreateQuotation(UriBuilder builder) {
        return builder.path("quotations").build();
    }
}
