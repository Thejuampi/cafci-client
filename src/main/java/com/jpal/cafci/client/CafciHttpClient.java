package com.jpal.cafci.client;

import com.jpal.cafci.shared.Impure;
import lombok.SneakyThrows;
import lombok.val;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

public final class CafciHttpClient {

    private final HttpClient client = HttpClient.newBuilder().build();

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // config.apiUrl+'/fondo/'+$scope.fondo.fondoId+'/clase/'+$scope.fondo.claseFondoId+'/ficha'
    // e.g.: https://api.cafci.org.ar/fondo/747/clase/1638/ficha
    @SuppressWarnings("unused")
    public String ficha(String fondoId, String claseId) {
        throw new UnsupportedOperationException();
    }

    // e.g.: https://api.cafci.org.ar/fondo/747/clase/1638/rendimiento/2021-01-27/2021-01-28?step=1
    public String rendimiento(String fund, String fundClass, LocalDate from, LocalDate to) {
        val request = HttpRequest.newBuilder(URI.create(format("https://api.cafci.org.ar/fondo/%s/clase/%s/rendimiento/%s/%s?step=1", fund, fundClass, from.format(fmt), to.format(fmt))))
                .GET()
                .build();

        return send(request).body();
    }

    // e.g.: https://api.cafci.org.ar/fondo?include=clase_fondo
    @Impure(cause = "http io")
    public String fetchFundJson() {
        val request = HttpRequest.newBuilder(URI.create("https://api.cafci.org.ar/fondo?include=clase_fondo&limit=0&estado=1"))
                .GET()
                .build();

        return send(request).body();
    }

    @SneakyThrows
    @Impure(cause = "http io")
    private HttpResponse<String> send(HttpRequest request) {
        val response = client.send(request, ofString());

        if(response.statusCode() != 200)
            throw new RuntimeException("response status is bad. Response=" + response);

        return response;
    }

    private HttpResponse.BodyHandler<String> ofString() {
        return HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
    }

}
