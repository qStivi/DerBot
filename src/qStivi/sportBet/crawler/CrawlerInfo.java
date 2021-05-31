package qStivi.sportBet.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import qStivi.sportBet.objects.Match;

import java.io.IOException;
import java.util.ArrayList;

public class CrawlerInfo {
    private static String pattern1 = "<(.*?)\\>";

    private static ArrayList<String> dates = new ArrayList<String>();
    private static ArrayList<String> teams = new ArrayList<String>();
    private static ArrayList<Double> rates = new ArrayList<Double>();
    private static String url = "https://www.bet3000.de/de/events/1891-bundesliga-manner";


    public static void saveInMatches(ArrayList<Match> matches) {
        crawlRates(url, new ArrayList<String>());
        crawlNames(url, new ArrayList<String>());
        crawlDates(url, new ArrayList<String>());
        int c1 = 0;
        int c2 = 0;
        int c3 = 0;
        while (c1 < teams.size() && c3 < dates.size()) {
            matches.add(new Match(teams.get(c1), teams.get(c1 + 1), rates.get(c2), rates.get(c2 + 1), rates.get(c2 + 2), dates.get(c3)));
            c1 += 2;
            c2 += 3;
            c3++;
        }
        teams.clear();
        rates.clear();
        dates.clear();
    }

    private static void crawlDates(String url, ArrayList<String> visited) {
        Document doc = request(url, visited);

        if (doc != null) {
            for (Element date : doc.select(".date")) {
                String dateValue = date.toString();
                dateValue = dateValue.replaceAll(pattern1, "");
                dates.add(dateValue);
            }
        }
    }

    private static void crawlRates(String url, ArrayList<String> visited) {
        Document doc = request(url, visited);

        if (doc != null) {
            for (Element überprüfen : doc.select("[class=\"prediction-label \"]")) {
                String ü = überprüfen.toString().replaceAll(pattern1, "");
                if (ü != "X" || ü != "1" || ü != "2") {
                    for (Element rate : doc.select(".odds")) {
                        String rateValue = rate.toString();
                        rateValue = rateValue.replaceAll(pattern1, "");
                        rates.add(Double.valueOf(rateValue));
                    }
                }
            }
        } else {
            rates.add(0.0);
        }
    }

    private static void crawlNames(String url, ArrayList<String> visited) {
        Document doc = request(url, visited);

        if (doc != null) {
            for (Element name : doc.select("span[itemprop*=\"name\"]")) {
                String team = name.toString();
                team = team.replaceAll(pattern1, "");
                teams.add(team);
            }
        }
    }

    private static Document request(String url, ArrayList<String> v) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
                v.add(url);
                return doc;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
