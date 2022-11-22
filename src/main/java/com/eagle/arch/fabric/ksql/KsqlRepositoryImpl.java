package com.eagle.arch.fabric.ksql;

import com.eagle.arch.fabric.controller.FabricEventResponse;
import com.eagle.arch.fabric.exceptions.JsonDeserializeException;
import com.eagle.arch.fabric.ksql.dto.KsqlQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class KsqlRepositoryImpl implements KsqlRepository {

    private final WebClient webClient;

    private final String baseQuery = "Select * from dna_fabric_event_view ";

    @Override
    public List<FabricEventResponse> getAll() {
        String query = baseQuery + ";";
        String result = getQueryResult(query);
        log.info("query result for query {} is {}", query, result);
        return deserialize(result);
    }

    @Override
    public Optional<FabricEventResponse> get(String queryId) {
        // todo check the query injection
        String query = baseQuery + String.format("where id = '%s';", queryId);
        String result = getQueryResult(baseQuery + String.format("where id = '%s';", queryId));
        //log.info("query result for query {} is {}", query, result);
        List<FabricEventResponse> response = deserialize(result);
        if (response.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.get(0));
    }


    // todo Not able to find a way to automatically convert string to json, so doing it manually for now
    private String getQueryResult(String sql) {
        KsqlQuery query = new KsqlQuery(sql);
        Mono<String> response = webClient.post()
                .uri("/query")
                .header("Content-Type", "application/vnd.ksql.v1+json")
                .header("charset", "utf-8")
                .header("Accept", "application/vnd.ksql.v1+json")
                .body(Mono.just(query), KsqlQuery.class)
                .retrieve()
                .bodyToMono(String.class);
        return response.block();
    }

    // todo will find better way to deserialize for now some hardcoding
    private List<FabricEventResponse> deserialize(String jsonStr) {
        try {
            JSONArray array = new JSONArray((jsonStr));
            //        JSONObject headers = array.getJSONObject(0);
            List<FabricEventResponse> response = new ArrayList<>();
            if (array.length() == 1) {
                return response;
            }
            for (int index = 1; index < array.length(); ++index) {
                JSONObject row = array.getJSONObject(index);
                response.add(deserializeColumn(row.getJSONObject("row").getJSONArray("columns")));
            }
            return response;
        } catch (JSONException ex) {
            log.error("Error occured when while evaluating json {}", jsonStr);
            throw new JsonDeserializeException("exception occured" + ex);
        }
    }


    public FabricEventResponse deserializeColumn(JSONArray columns) throws JSONException {
        return new FabricEventResponse(getStr(columns, 0), getStr(columns, 1),
                getStr(columns, 2), getStr(columns, 3),
                getStr(columns, 4), getStr(columns, 5));
    }

    private String getStr(JSONArray columns, int index) throws JSONException {
        if (columns.length() <= index) {
            return "";
        }
        return columns.getString(index);
    }
}
