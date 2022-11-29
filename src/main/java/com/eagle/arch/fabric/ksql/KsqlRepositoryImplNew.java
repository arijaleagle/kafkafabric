package com.eagle.arch.fabric.ksql;

import com.eagle.arch.fabric.controller.FabricEventResponse;
import com.eagle.arch.fabric.exceptions.KsqlException;
import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
@Qualifier("ksqlClient")
public class KsqlRepositoryImplNew implements KsqlRepository {

    private final Client client;

    private final String baseQuery = "Select * from dna_fabric_event_view ";

    @Override
    public List<FabricEventResponse> getAll() {
        String query = baseQuery + " LIMIT 100;";
        List<FabricEventResponse> result = getQueryResult(query);
        log.info("query result for query {} is {}", query, result);
        return result;
    }

    @Override
    public Optional<FabricEventResponse> get(String queryId) {
        // todo check the query injection
        String query = baseQuery + String.format("where id = '%s';", queryId);
        List<FabricEventResponse> response = getQueryResult(query);
        //log.info("query result for query {} is {}", query, result);
        if (response.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.get(0));
    }


    private List<FabricEventResponse> getQueryResult(String sql) {
        BatchedQueryResult response = client.executeQuery(sql);
        List<Row> rows;
        try {
            rows = response.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occured when while evaluating response {}", response);
            throw new KsqlException("Exception getting response");
        }
        return rows.stream().map(this::deserializeRow).collect(Collectors.toList());
    }

    public FabricEventResponse deserializeRow(Row row) {
        return new FabricEventResponse(getStr(row, "ID"), getStr(row, "INPUTQUERY"),
                getStr(row, "TIMESTAMP"), getStr(row, "STORAGEID"),
                getStr(row, "STATUS"), getStr(row, "ERRORMESSAGE"));
    }

    private String getStr(Row row, String columnName) {
        try {
            return row.getString(columnName);
        } catch( IllegalArgumentException ex) {
           // log.info("No value found for index {} for row {}", columnName, row);
            return "";
        }
    }
}

