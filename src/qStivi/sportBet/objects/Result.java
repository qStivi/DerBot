package qStivi.sportBet.objects;
import qStivi.sportBet.Emote;

public class Result {

        private String team1;
        private String team2;
        private String finalScore;
        private int matchID;

        public Result(String team1, String team2, String finalScore, int matchID) {
            this.team1 = team1;
            this.team2 = team2;
            this.finalScore = finalScore;
            this.matchID = matchID;
        }

    public String getTeam1(){
        return this.team1;
    }

    public String getTeam2(){
        return this.team2;
    }

    public int getMatchID(){
        return this.matchID;
    }

    public String getFinalScore(){
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

        public static String getActualTeam(String team){
            String s = "error";
            switch (team) {
                case "THW Kiel":
                    s = "Kiel";
                    break;

                case "SG Flensburg":
                    s = "SG Flensburg";
                    break;

                case "Rhein Neckar":
                    s = "Rhein Neckar";
                    break;

                case "SC Magdeburg":
                    s = "Magdeburg";
                    break;

                case "Göppingen":
                    s = "Göppingen";
                    break;

                case "Füchse Berlin":
                    s = "Füchse Berlin";
                    break;

                case "MT Melsungen":
                    s = "Melsungen";
                    break;

                case "HSG Wetzlar":
                    s = "HSG Wetzlar";
                    break;

                case "TBV Lemgo":
                    s = "TBV Lemgo";
                    break;

                case "SC Leipzig":
                    s = "SC Leipzig";
                    break;

                case "Bergischer HC":
                    s = "Bergischer HC";
                    break;

                case "HC Erlangen":
                    s = "Erlangen";
                    break;

                case "TVB Stuttgart":
                    s = "TVB Stuttgart";
                    break;

                case "TSV Hannover":
                    s = "TSV Hannover";
                    break;

                case "TSV Minden":
                    s = "TSV Minden";
                    break;

                case "HBW Balingen":
                    s = "HBW Balingen";
                    break;

                case "Ludwigshafen":
                    s = "Ludwigshafen";
                    break;

                case "Nordhorn Lingen":
                    s = "Nordhorn Lingen";
                    break;

                case "TuSEM Essen":
                    s = "Essen";
                    break;

                case "HSC Coburg":
                    s = "HSC Coburg";
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
