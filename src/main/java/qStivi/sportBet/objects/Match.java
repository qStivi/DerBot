package main.java.qStivi.sportBet.objects;

import main.java.qStivi.sportBet.Emote;

public class Match {

    private final String team1;
    private final String team2;
    private final double winRateTeam1;
    private final double winRateTeam2;
    private final double drawRate;
    private final String date;

    public Match(String team1, String team2, double winRateTeam1, double drawRate, double winRateTeam2, String date) {
        this.team1 = team1;
        this.team2 = team2;
        this.winRateTeam1 = winRateTeam1;
        this.winRateTeam2 = winRateTeam2;
        this.drawRate = drawRate;
        this.date = date;
    }

    public String getTeam1() {
        return this.team1;
    }


    public String getTeam2() {
        return this.team2;
    }

    public double getWinRateTeam1() {
        return this.winRateTeam1;
    }

    public double getWinRateTeam2() {
        return this.winRateTeam2;
    }

    public String getEmote(String team) {
        String s = "error";
        switch (team) {
            case "THW Kiel":
                s = Emote.Kiel;
                break;

            case "SG Flensburg":
                s = Emote.SGFlensburg;
                break;

            case "Rhein Neckar":
                s = Emote.RNL;
                break;

            case "SC Magdeburg":
                s = Emote.SCMagdeburg;
                break;

            case "Göppingen":
                s = Emote.FrischAufGppingen;
                break;

            case "Füchse Berlin":
                s = Emote.FchseBerlin;
                break;

            case "MT Melsungen":
                s = Emote.MTMelsungen;
                break;

            case "HSG Wetzlar":
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

            case "HC Erlangen":
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

            case "TuSEM Essen":
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
        value = "Tag " + date + ": " + winRateTeam1 + "  " + drawRate + "  " + winRateTeam2;
        return value;
    }
}
