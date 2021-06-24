package qStivi.sportBet.objects;

import qStivi.sportBet.Emote;

public class Result {

    private final String team1;
    private final String team2;
    private final String finalScore;
    private final int matchID;

    public Result(String team1, String team2, String finalScore, int matchID) {
        this.team1 = team1;
        this.team2 = team2;
        this.finalScore = finalScore;
        this.matchID = matchID;
    }

    public static String getActualTeam(String team) {
        String s = "error";
        switch (team) {
            case "thw kiel":
                s = "Kiel";
                break;

            case "sg flensburg":
                s = "SG Flensburg";
                break;

            case "rhein neckar":
                s = "Rhein Neckar";
                break;

            case "sc magdeburg":
                s = "Magdeburg";
                break;

            case "gppingen":
                s = "Göppingen";
                break;

            case "fchse berlin":
                s = "Füchse Berlin";
                break;

            case "mt melsungen":
                s = "Melsungen";
                break;

            case "hsg wetzlar":
                s = "Wetzlar";
                break;

            case "tbv lemgo":
                s = "TBV Lemgo";
                break;

            case "sc leipzig":
                s = "SC Leipzig";
                break;

            case "bergischer hc":
                s = "Bergischer HC";
                break;

            case "hc erlangen":
                s = "Erlangen";
                break;

            case "tvb stuttgart":
                s = "TVB Stuttgart";
                break;

            case "tsv hannover":
                s = "TSV Hannover";
                break;

            case "tsv minden":
                s = "TSV Minden";
                break;

            case "hbw balingen":
                s = "HBW Balingen";
                break;

            case "ludwigshafen":
                s = "Ludwigshafen";
                break;

            case "nordhorn lingen":
                s = "Nordhorn Lingen";
                break;

            case "tusem essen":
                s = "Essen";
                break;

            case "hsc coburg":
                s = "HSC Coburg";
                break;
        }
        return s;
    }

    public String getTeam1() {
        return this.team1;
    }

    public String getTeam2() {
        return this.team2;
    }

    public int getMatchID() {
        return this.matchID;
    }

    public String getFinalScore() {
        return this.finalScore;
    }

    public String getEmote(String team) {
        String s = "error";
        switch (team) {
            case "Kiel":
                s = Emote.Kiel;
                break;

            case "SG Flensburg":
                s = Emote.SGFlensburg;
                break;

            case "Rhein Neckar":
                s = Emote.RNL;
                break;

            case "Magdeburg":
                s = Emote.SCMagdeburg;
                break;

            case "Göppingen":
                s = Emote.FrischAufGppingen;
                break;

            case "Füchse Berlin":
                s = Emote.FchseBerlin;
                break;

            case "Melsungen":
                s = Emote.MTMelsungen;
                break;

            case "Wetzlar":
                s = Emote.HSGWetzlar;
                break;

            case "TBV Lemgo":
                s = Emote.TBVLemgo;
                break;

            case "SC Leipzig":
                s = Emote.SCLeipzig;
                break;

            case "Bergischer HC":
                s = Emote.BergischerHC;
                break;

            case "Erlangen":
                s = Emote.HCErlangen;
                break;

            case "TVB Stuttgart":
                s = Emote.TVBStuttgart;
                break;

            case "TSV Hannover":
                s = Emote.Hannover;
                break;

            case "TSV Minden":
                s = Emote.GWDMinden;
                break;

            case "HBW Balingen":
                s = Emote.Balingen;
                break;

            case "Ludwigshafen":
                s = Emote.Eulen;
                break;

            case "Nordhorn Lingen":
                s = Emote.HSGNordhornLingen;
                break;

            case "Essen":
                s = Emote.TUSEMEssen;
                break;

            case "HSC Coburg":
                s = Emote.HSCCoburg;
                break;
        }
        return s;
    }

    public String getTeams() {
        return getEmote(team1) + team1 + "  :  " + getEmote(team2) + team2;
    }

    public String toString() {
        String value;
        value = "Ergebnis: " + finalScore;
        return value;
    }
}
