package com.fcosta.enterprise_api.infrastructure.external.cep;

import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;
import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.ZipCodeGatewayPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.Normalizer;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CepLaGatewayAdapter implements ZipCodeGatewayPort {

    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile(
            "<strong>CEP:</strong>\\s*([^<]+)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern STREET_PATTERN = Pattern.compile(
            "<strong>Endereço:</strong>\\s*([^<]+)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern NEIGHBORHOOD_PATTERN = Pattern.compile(
            "<strong>Bairro:</strong>\\s*([^<]+)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern CITY_STATE_PATTERN = Pattern.compile(
            "<strong>Cidade:</strong>\\s*([^<]+?)\\s*-\\s*([A-Z]{2})",
            Pattern.CASE_INSENSITIVE
    );

    private final WebClient webClient;
    private final String baseUrl;

    public CepLaGatewayAdapter(
            WebClient cepLaWebClient,
            @Value("${external.cep-la.base-url}") String baseUrl
    ) {
        this.webClient = cepLaWebClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public ZipCodeInfo findByZipCode(String zipCode) {
        String normalizedZipCode = normalizeOnlyNumbers(zipCode);

        if (normalizedZipCode == null || normalizedZipCode.length() != 8) {
            throw new ApplicationException("Zip code must have 8 digits");
        }

        try {
            String html = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/")
                            .queryParam("cep", normalizedZipCode)
                            .build()
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> {
                                throw new ApplicationException("Zip code not found");
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                throw new ApplicationException("Zip code service unavailable");
                            }
                    )
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(10));

            return parseHtmlResponse(html, normalizedZipCode);

        } catch (ApplicationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ApplicationException(
                    "Unable to validate zip code: " + exception.getMessage()
            );
        }
    }

    private ZipCodeInfo parseHtmlResponse(String html, String requestedZipCode) {
        if (html == null || html.isBlank()) {
            throw new ApplicationException("Zip code not found");
        }

        String foundZipCode = extract(ZIP_CODE_PATTERN, html, 1);
        String street = extract(STREET_PATTERN, html, 1);
        String neighborhood = extract(NEIGHBORHOOD_PATTERN, html, 1);

        Matcher cityStateMatcher = CITY_STATE_PATTERN.matcher(html);

        if (!cityStateMatcher.find()) {
            throw new ApplicationException("Zip code not found");
        }

        String city = cleanHtmlText(cityStateMatcher.group(1));
        String state = cleanHtmlText(cityStateMatcher.group(2)).toUpperCase();

        String normalizedFoundZipCode = normalizeOnlyNumbers(foundZipCode);

        if (!requestedZipCode.equals(normalizedFoundZipCode)) {
            throw new ApplicationException("Zip code not found");
        }

        return new ZipCodeInfo(
                normalizedFoundZipCode,
                cleanHtmlText(street),
                cleanHtmlText(neighborhood),
                city,
                state
        );
    }

    private String extract(Pattern pattern, String html, int group) {
        Matcher matcher = pattern.matcher(html);

        if (!matcher.find()) {
            return null;
        }

        return cleanHtmlText(matcher.group(group));
    }

    private String cleanHtmlText(String value) {
        if (value == null) {
            return null;
        }

        return value
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&#8211;", "-")
                .replace("&#8212;", "-")
                .trim();
    }

    private String normalizeOnlyNumbers(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
