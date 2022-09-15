package com.example.dividend.scraper;

import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final long START_TIME = 86400; // 60(초) * 60(분) * 24(시)

    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    @Override
    public ScrapedResult scrap(Company company) {
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company); //scrapResult 생성시점에 company 인자 전달

        try {
            //long start = 0;
            long now = System.currentTimeMillis() / 1000; //현재 시간을 ms으로받아 1000을 나누어 초단위로 받음

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            //해당 속성을 가진 요소가 하나가 아닐 수 있음 Elements 사용
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDivs.get(0);

            //html 데이터 다듬기
            Element tbody = tableEle.children().get(1); //tbody가져오기

            List<Dividend> dividends = new ArrayList<>(); //스크랩 결과 담기
            for (Element e : tbody.children()) { //tbody의 Dividend 요소만 가져오기
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }
                //공백을 기준하여 년월일 구분하여 다듬기
                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]); //enum 을통해 int로 가져옴
                int day = Integer.valueOf(splits[1].replace(",", "")); //replace로 반점 제거
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {//enum 결과값이 -1 인 경우
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                //정상적으로 month를 받아온 경우 Dividend List에 넣어주기
                dividends.add(new Dividend(LocalDateTime.of(year, month, day, 0, 0),dividend));
//                        .builder()
//                        .date(LocalDateTime.of(year, month, day, 0, 0))
//                        .dividend(dividend)
//                        .build());

                //System.out.println(year + "/" + month + "/ " + day + " -> " + dividend);
            }
            scrapResult.setDividends(dividends); //scrapResult에 dividends리스트 전달

        } catch (IOException e) {
            e.printStackTrace();
        }

        return scrapResult;
    }

    @Override
    //ticker에 해당하는 회사의 메타정보를 스크래핑하여 반환
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL,ticker,ticker);

        try {
            Document document = Jsoup.connect(url).get();
            //h1태그에서 회사명 스크래핑
            Element titleEle =  document.getElementsByTag("h1").get(0);
            //회사명을 깔끔하게 가져오기위한 작업
            String title = titleEle.text().split(" - ")[1].trim();
            //" - " 문자열을 기준으로 나눔 , abc - def - xxc => 3개의 아이템을 가진 배열반환 trim으로 앞뒤 공백 제거
            // , 결과 [1]번째인 def

            return new Company(ticker,title);
//
//                    .builder()
//                    .ticker(ticker)
//                    .name(title)
//                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
