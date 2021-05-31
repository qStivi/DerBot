package qStivi.sportBet.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import qStivi.sportBet.objects.Result;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

public class CrawlerResult {

    private static String pattern1 = "<(.*?)\\>";

    private static ArrayList<String> teams = new ArrayList<String>();
    private static ArrayList<String> finalScores = new ArrayList<String>();
    private static ArrayList<Integer> matchIDS = new ArrayList<Integer>();
    private static String url = "https://livescore.bet3000.com/de/handball/deutschland";

    public static void saveInResults(ArrayList<Result> results) {
        crawlNames(url, new ArrayList<String>());
        crawlFinalScore(url, new ArrayList<String>());
        crawlMatchID(url, new ArrayList<String>());
        int c1 = 0;
        int c2 = 0;
        while (c1 < teams.size()) {
            results.add(new Result(teams.get(c1), teams.get(c1 + 1), finalScores.get(c2), matchIDS.get(c2)));
            c1 += 2;
            c2++;
        }
        matchIDS.clear();
        teams.clear();
        finalScores.clear();
    }

    private static void crawlFinalScore(String url, ArrayList<String> visited) {
        Document doc = request(url, visited);

        if (doc != null) {
            for (Element score : doc.select("[data-tournament-id=\"95\"] > [class=\"hidden-xs score no-wrap text-center  text-center\"]")) {
                String finalScore = score.toString();
                finalScore = finalScore.replaceAll(pattern1, "");
                finalScore = cleanForCommand(finalScore);
                if (!finalScore.startsWith("-")) {
                    finalScores.add(finalScore);
                } else {
                    finalScores.add("-:-");
                }
            }
        }
    }

    private static void crawlNames(String url, ArrayList<String> visited) {
        Document doc = request(url, visited);

        if (doc != null) {
            for (Element name : doc.select("[data-tournament-id=\"95\"] > [class=\"hidden-xs teams text-center\"]")) {
                String team = name.toString();
                team = team.replaceAll(pattern1, "");
                team = cleanForCommand(team);
                if (allReadyIn(team) == false && !team.startsWith("<")) {
                    teams.add(team);
                }
            }
        }
    }

    private static void crawlMatchID(String url, ArrayList<String> visited) {
        Document doc = request(url, visited);
        if (doc != null) {
            for (Element s : doc.select("[data-tournament-id=\"95\"]")) {
                String status = s.attr("data-match-id");
                ;
                status = cleanForCommand(status);
                matchIDS.add(Integer.parseInt(status));
            }
        }
    }

    public static boolean isWinner(String team) {
        boolean condition = false;
        boolean condition2 = isFinished(url, new ArrayList<String>(), team);
        ArrayList<Result> results = new ArrayList<Result>();
        saveInResults(results);
        for (int i = 0; i < results.size(); i++) {
            String score = results.get(i).getFinalScore();
            String[] scores;
            scores = score.split(" : ");
            int score1 = Integer.parseInt(scores[0]);
            int score2 = Integer.parseInt(scores[1]);
            if (condition2 && results.get(i).getTeam1().equals(team) && score1 > score2) {
                condition = true;
            }
            if (condition2 && results.get(i).getTeam2().equals(team) && score1 > score2) {
                condition = true;
            }
        }
        return condition;
    }

    public static boolean isFinished(String url, ArrayList<String> visited, String team) {
        Document doc = request(url, visited);
        Boolean condition = false;
        int matchid = 0;
        ArrayList<Result> results = new ArrayList<Result>();
        saveInResults(results);
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getTeam1().equals(team) || results.get(i).getTeam2().equals(team)) {
                matchid = results.get(i).getMatchID();
            }
        }
        if (doc != null) {
            for (Element s : doc.select("[data-match-id=\"" + String.valueOf(matchid) + "\"] > [data-type=\"status\"]")) {
                String status = s.toString();
                status = status.replaceAll(pattern1, "");
                status = cleanForCommand(status);
                if (status.equals("Beendet")) {
                    condition = true;
                }
            }
        }
        return condition;
    }

    private static boolean isNumber(String s) {
        boolean condition = false;
        if (s.matches("[0-9]")) {
            condition = true;
        }
        return condition;
    }


    private static boolean allReadyIn(String team) {
        boolean a = false;
        for (int i = 0; i < teams.size(); i++) {
            if (team == teams.get(i)) {
                a = true;
            }
        }
        return a;
    }

    public static String cleanForCommand(String str) {
        str = str.strip();
        str = Normalizer.normalize(str, Normalizer.Form.NFKD);
        str = str.replaceAll("^ü^ö[^a-z0-9A-Z -]", ""); // Remove all non valid chars
        str = str.replaceAll("[ \\t]+", " ").trim(); // convert multiple spaces into one space
        return str;
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
