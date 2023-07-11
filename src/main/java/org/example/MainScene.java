package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainScene extends JPanel {
    public static final String URL_PLAYERS = "https://www.basketball-reference.com/players/";
    public static final String URL_TEAMS = "https://www.basketball-reference.com/teams/";
    private String[] nbaTeams;
    private JComboBox<String> nbaTeamsComboBox;
    private JComboBox<String> playersComboBox;


    public MainScene(int x, int y, int width, int height) {
        this.setFocusable(true);
        this.requestFocus();
        this.setBounds(x, y, width, height);
        this.setBackground(Color.BLACK);
        this.nbaTeams = getNbaTeams();
        this.nbaTeamsComboBox = new JComboBox(this.nbaTeams);
        this.nbaTeamsComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTeam = (String) nbaTeamsComboBox.getSelectedItem();
                displayTeamStats(selectedTeam);
            }
        });
        ArrayList<String> playerNames = fetchPlayerNames();
        this.playersComboBox = new JComboBox<>(playerNames.toArray(new String[0]));
        this.playersComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedPlayer = (String) playersComboBox.getSelectedItem();
                displayPlayerStats(selectedPlayer);
            }
        });
        this.add(nbaTeamsComboBox);
        this.add(playersComboBox);
    }


    private ArrayList<String> fetchPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        try {
            Document document = Jsoup.connect(URL_PLAYERS).get();
            Elements playerLinks = document.select("div a[href^=\"/players/\"]");
            for (Element playerLink : playerLinks) {
                String playerName = playerLink.text();
                if (!playerName.matches("[A-Z]")) {
                    playerNames.add(playerName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerNames;
    }

    private void displayPlayerStats(String playerName) {
        try {
            Document document = Jsoup.connect("https://www.basketball-reference.com/players/").get();
            Element playerList = document.selectFirst("ul.page_index");
            if (playerList != null) {
                Elements playerLinks = playerList.select("a");

                for (Element playerLink : playerLinks) {
                    String playerFullName = playerLink.text();
                    if (playerFullName.contains(playerName)) {
                        String playerURL = playerLink.attr("href");
                        String playerPageURL = "https://www.basketball-reference.com" + playerURL;

                        Document playerDocument = Jsoup.connect(playerPageURL).get();

                        Element playerStatsElement = playerDocument.getElementsByClass("stats_pullout").first();

                        if (playerStatsElement != null) {
                            Element gamesElement = playerStatsElement.selectFirst("div.p1 span[data-tip='Games']").nextElementSibling();
                            String games = gamesElement != null ? gamesElement.text() : "";

                            Element pointsElement = playerStatsElement.selectFirst("div.p1 span[data-tip='Points']").nextElementSibling();
                            String points = pointsElement != null ? pointsElement.text() : "";

                            Element reboundsElement = playerStatsElement.selectFirst("div.p1 span[data-tip='Total Rebounds']").nextElementSibling();
                            String rebounds = reboundsElement != null ? reboundsElement.text() : "";

                            Element assistsElement = playerStatsElement.selectFirst("div.p1 span[data-tip='Assists']").nextElementSibling();
                            String assists = assistsElement != null ? assistsElement.text() : "";

                            Element fgPercentageElement = playerStatsElement.selectFirst("div.p2 span[data-tip='Field Goal Percentage']").nextElementSibling();
                            String fgPercentage = fgPercentageElement != null ? fgPercentageElement.text() : "";

                            Element threePtPercentageElement = playerStatsElement.selectFirst("div.p2 span[data-tip='3-Point Field Goal Percentage']").nextElementSibling();
                            String threePtPercentage = threePtPercentageElement != null ? threePtPercentageElement.text() : "";

                            Element ftPercentageElement = playerStatsElement.selectFirst("div.p2 span[data-tip='Free Throw Percentage']").nextElementSibling();
                            String ftPercentage = ftPercentageElement != null ? ftPercentageElement.text() : "";

                            Element efgPercentageElement = playerStatsElement.selectFirst("div.p2 span[data-tip^='Effective Field Goal Percentage']").nextElementSibling();
                            String efgPercentage = efgPercentageElement != null ? efgPercentageElement.text() : "";

                            Element perElement = playerStatsElement.selectFirst("div.p3 span[data-tip^='Player Efficiency Rating']").nextElementSibling();
                            String per = perElement != null ? perElement.text() : "";

                            Element wsElement = playerStatsElement.selectFirst("div.p3 span[data-tip^='Win Shares']").nextElementSibling();
                            String ws = wsElement != null ? wsElement.text() : "";

                            // Rest of the code remains the same

                            String playerStats = "Career summary statistics for " + playerName + ":\n"
                                    + "Games: " + games + "\n"
                                    + "Points: " + points + "\n"
                                    + "Total Rebounds: " + rebounds + "\n"
                                    + "Assists: " + assists + "\n"
                                    + "Field Goal Percentage: " + fgPercentage + "\n"
                                    + "3-Point Field Goal Percentage: " + threePtPercentage + "\n"
                                    + "Free Throw Percentage: " + ftPercentage + "\n"
                                    + "Effective Field Goal Percentage: " + efgPercentage + "\n"
                                    + "Player Efficiency Rating: " + per + "\n"
                                    + "Win Shares: " + ws;

                            JOptionPane.showMessageDialog(this, playerStats, "Player Statistics", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Player not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private String[] getNbaTeams() {
        ArrayList<String> getNbaTeams = new ArrayList<>();
        try {
            Document documentTeams = Jsoup.connect(URL_TEAMS).get();
            ArrayList<Element> teams = documentTeams.getElementsByClass("full_table");
            for (Element element : teams) {
                Element title = element.child(0);
                getNbaTeams.add(title.text());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        String[] nbaTeamsString = new String[getNbaTeams.size()];

        for (int i = 0; i < nbaTeamsString.length; i++) {
            nbaTeamsString[i] = getNbaTeams.get(i);
        }
        return nbaTeamsString;
    }

    private void displayTeamStats(String teamName) {
        try {
            Document document = Jsoup.connect(URL_TEAMS).get();
            Element teamRow = null;

            // Find the team row in the HTML table based on the team name
            Elements rows = document.select("tbody tr");
            for (Element row : rows) {
                Element teamNameElement = row.selectFirst("th a");
                if (teamNameElement != null && teamNameElement.text().equals(teamName)) {
                    teamRow = row;
                    break;
                }
            }

            if (teamRow != null) {
                // Extract the team statistics from the row using Element methods
                Element leagueElement = teamRow.selectFirst("td[data-stat=lg_id]");
                String league = leagueElement != null ? leagueElement.text() : "";

                Element yearsActiveElement = teamRow.selectFirst("td[data-stat=years]");
                String yearsActive = yearsActiveElement != null ? yearsActiveElement.text() : "";

                Element totalGamesElement = teamRow.selectFirst("td[data-stat=g]");
                String totalGames = totalGamesElement != null ? totalGamesElement.text() : "";

                Element totalWinsElement = teamRow.selectFirst("td[data-stat=wins]");
                String totalWins = totalWinsElement != null ? totalWinsElement.text() : "";

                Element totalLossesElement = teamRow.selectFirst("td[data-stat=losses]");
                String totalLosses = totalLossesElement != null ? totalLossesElement.text() : "";

                Element winLossPercentageElement = teamRow.selectFirst("td[data-stat=win_loss_pct]");
                String winLossPercentage = winLossPercentageElement != null ? winLossPercentageElement.text() : "";

                Element yearsInPlayoffsElement = teamRow.selectFirst("td[data-stat=years_playoffs]");
                String yearsInPlayoffs = yearsInPlayoffsElement != null ? yearsInPlayoffsElement.text() : "";

                Element divisionChampionshipsElement = teamRow.selectFirst("td[data-stat=years_division_champion]");
                String divisionChampionships = divisionChampionshipsElement != null ? divisionChampionshipsElement.text() : "";

                Element conferenceChampionshipsElement = teamRow.selectFirst("td[data-stat=years_conference_champion]");
                String conferenceChampionships = conferenceChampionshipsElement != null ? conferenceChampionshipsElement.text() : "";

                Element leagueChampionshipsElement = teamRow.selectFirst("td[data-stat=years_league_champion]");
                String leagueChampionships = leagueChampionshipsElement != null ? leagueChampionshipsElement.text() : "";

                // Build the message to display
                String message = "Team statistics for " + teamName + ":\n"
                        + "League: " + league + "\n"
                        + "Years Active: " + yearsActive + "\n"
                        + "Total Games: " + totalGames + "\n"
                        + "Total Wins: " + totalWins + "\n"
                        + "Total Losses: " + totalLosses + "\n"
                        + "Win-Loss Percentage: " + winLossPercentage + "\n"
                        + "Years in Playoffs: " + yearsInPlayoffs + "\n"
                        + "Division Championships: " + divisionChampionships + "\n"
                        + "Conference Championships: " + conferenceChampionships + "\n"
                        + "League Championships: " + leagueChampionships;

                JOptionPane.showMessageDialog(this, message, "Team Statistics", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
