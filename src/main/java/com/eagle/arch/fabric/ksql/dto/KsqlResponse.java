package com.eagle.arch.fabric.ksql.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KsqlResponse { // todo figure out schema registry stuffs
    private String queryId;
    private List<String> columnNames = new ArrayList<>();
    private List<String> columnTypes = new ArrayList<>();

}
