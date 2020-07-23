package org.nicktorwald.platform.util;

import java.net.URI;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A convenient set misc methods related to the Web layer.
 */
public class WebUtils {

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Extracts paging properties from the request.
     * If it extracts nothing the default paging options will be
     * constructed.
     *
     * @param request target request
     * @return parsed pageable
     * @see #DEFAULT_PAGE_NUMBER
     * @see #DEFAULT_PAGE_SIZE
     */
    public static Pageable extractPageable(ServerRequest request) {
        var page = extractNumberFromQuery(request, "page", DEFAULT_PAGE_NUMBER);
        var size = extractNumberFromQuery(request, "size", DEFAULT_PAGE_SIZE);
        return PageRequest.of(page, size);
    }

    /**
     * Builds a relative URI based on variables provided.
     *
     * @param relativeUri relative URI pattern
     * @param params      URI variables to be expanded
     * @return constructed URI
     */
    public static URI buildRelativeUri(String relativeUri, Map<String, Object> params) {
        return UriComponentsBuilder
                .fromPath(relativeUri)
                .buildAndExpand(params)
                .encode()
                .toUri();
    }

    private static Integer extractNumberFromQuery(ServerRequest request, String name, int defaultValue) {
        return request.queryParam(name)
                .map(Integer::valueOf)
                .orElse(defaultValue);
    }

}
