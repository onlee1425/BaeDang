package com.example.dividend.service;

import com.example.dividend.Exception.impl.NoCompanyException;
import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.constants.CacheKey;
import com.example.dividend.persist.CompanyRepository;
import com.example.dividend.persist.DividendRepository;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FinanceService {

    public final CompanyRepository companyRepository;
    public final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName" , value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);

        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        // 2. 조회된 회사 ID로 배당금 정보를 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환

//        List<Dividend> dividends = new ArrayList<>();
//        for (var entity : dividendEntities){
//            dividends.add(Dividend.builder()
//                            .date(entity.getDate())
//                            .dividend(entity.getDividend())
//                            .build());
//        }

        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(),e.getDividend()))
//                        Dividend.builder()
//                        .date(e.getDate())
//                        .dividend(e.getDividend())
//                        .build())
                .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(),company.getName()),
//                .builder()
//                .ticker(company.getTicker())
//                .name(company.getName())
//                .build(),
                dividends);
    }

}
