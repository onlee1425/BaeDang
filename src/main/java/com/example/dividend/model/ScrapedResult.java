package com.example.dividend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapedResult {

    private Company company;

    private List<Dividend> dividends;
}
