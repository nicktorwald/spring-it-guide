package org.nicktorwald.platform.quotation;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nicktorwald.platform.quotation.config.TestQuotationConfig;
import org.nicktorwald.platform.quotation.endpoint.QuotationView;
import org.nicktorwald.platform.quotation.service.domain.Quotation;
import org.nicktorwald.platform.quotation.util.QuotationPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

/**
 * @see org.nicktorwald.platform.quotation.service.ListQuotationsCase
 */
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestQuotationConfig.class
)
class ListQuotationsCaseIT {

    private final QuotationPopulator fetcherPopulator;
    private final WebTestClient webClient;

    @Autowired
    public ListQuotationsCaseIT(QuotationPopulator fetcherPopulator,
                                WebTestClient webClient) {
        this.fetcherPopulator = fetcherPopulator;
        this.webClient = webClient;
    }

    @AfterEach
    public void tearDown() {
        fetcherPopulator.clear();
    }

    @Test
    @DisplayName("gets an empty list when there are no quotations created")
    void testEmptyQuotationList() {
        // given
        fetcherPopulator.populate(List.of());
        var pageable = PageRequest.of(0, 10);

        //when
        var result = webClient.get()
                .uri(builder -> toPageableUri(builder, pageable))
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBodyList(QuotationView.class).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("gets a partial list when there are less quotations than the page size")
    void testPartialQuotationList() {
        // given
        var quotationStrings = new String[] {
                "Absence makes the heart grow fonder.",
                "Actions speak louder than words.",
                "All good things must come to an end."
        };
        fetcherPopulator.populate(Arrays.stream(quotationStrings).map(Quotation::of).collect(Collectors.toList()));
        var pageable = PageRequest.of(0, 10);

        // when
        var result = webClient.get()
                .uri("/quotations", pageable)
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBodyList(QuotationView.class).hasSize(3)
                .value(this::toQuotationStrings, Matchers.containsInRelativeOrder(quotationStrings));
    }

    @Test
    @DisplayName("gets a full list when there are same quotations number as the page size")
    void testFullQuotationList() {
        // given
        var quotationStrings = new String[] {
                "A picture is worth a thousand words.",
                "A watched pot never boils.",
                "Beggars can’t be choosers.",
                "Beauty is in the eye of the beholder.",
                "Better late than never."
        };
        fetcherPopulator.populate(Arrays.stream(quotationStrings).map(Quotation::of).collect(Collectors.toList()));
        var pageable = PageRequest.of(0, 5);

        // when
        var result = webClient.get()
                .uri("/quotations", pageable)
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBodyList(QuotationView.class).hasSize(5)
                .value(this::toQuotationStrings, Matchers.containsInRelativeOrder(quotationStrings));
    }

    @Test
    @DisplayName("gets a partial list when there are same quotations number as the page size")
    void testPartialQuotationListWithLastPage() {
        // given
        var quotationStrings = new String[] {
                "Birds of a feather flock together.",
                "Cleanliness is next to godliness.",
                "Don’t bite the hand that feeds you.",
                "Don’t count your chickens before they hatch.",
                "Don’t judge a book by its cover.",
                "Don’t put all of your eggs in one basket."
        };
        var expectedQuotationStrings = new String[] {
                "Don’t put all of your eggs in one basket."
        };
        fetcherPopulator.populate(Arrays.stream(quotationStrings).map(Quotation::of).collect(Collectors.toList()));
        var pageable = PageRequest.of(1, 5);

        // when
        var result = webClient.get()
                .uri(builder -> toPageableUri(builder, pageable))
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBodyList(QuotationView.class).hasSize(1)
                .value(this::toQuotationStrings, Matchers.containsInRelativeOrder(expectedQuotationStrings));;
    }

    @Test
    @DisplayName("gets a full list when there are more quotations that the page number")
    void testFullQuotationListWhenIntermediatePage() {
        // given
        fetcherPopulator.populate(List.of(
                Quotation.of("Don’t put off until tomorrow what you can do today."),
                Quotation.of("Don’t put too many irons in the fire."),
                Quotation.of("Easy come, easy go."),
                Quotation.of("Fortune favors the bold."),
                Quotation.of("God helps those who help themselves.")
        ));
        var pageable = PageRequest.of(2, 5);

        // when
        var result = webClient.get()
                .uri(builder -> toPageableUri(builder, pageable))
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBodyList(QuotationView.class).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("gets an empty list when there are less quotations that the page number")
    void testEmptyQuotationListWhenOutOfLastPage() {
        // given
        var quotationStrings = new String[] {
                "Good things come to those who wait.",
                "Honesty is the best policy.",
                "Hope for the best, prepare for the worst.",
                "If it ain’t broke, don’t fix it.",
                "If you can’t beat ’em, join ’em."
        };
        var expectedQuotationStrings = new String[] {
                "Hope for the best, prepare for the worst.",
                "If it ain’t broke, don’t fix it.",
        };
        fetcherPopulator.populate(Arrays.stream(quotationStrings).map(Quotation::of).collect(Collectors.toList()));
        var pageable = PageRequest.of(1, 2);

        // when
        var result = webClient.get()
                .uri(builder -> toPageableUri(builder, pageable))
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectBodyList(QuotationView.class).hasSize(2)
                .value(this::toQuotationStrings, Matchers.containsInRelativeOrder(expectedQuotationStrings));;;
    }

    private URI toPageableUri(UriBuilder builder, Pageable pageable) {
        return builder
                .path("quotations")
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build();
    }

    private List<String> toQuotationStrings(List<QuotationView> views) {
        return views.stream()
                .map(QuotationView::text)
                .collect(Collectors.toList());
    }
}
