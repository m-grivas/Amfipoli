package Controller;

import Model.Characters.Coder;
import Model.Characters.Professor;
import Model.Model;
import Model.Tiles.Tile;
import View.View;
import View.TileView;
import Model.Player;
import Model.AreaType;
import View.CharacterView;
import Model.Characters.Character;
import View.StartingScreen;
import Model.TurnTimer;
import View.WinnerScreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class Controller implements ActionListener {

    private Model model;
    private View view;
    private int tilesToPick;
    private boolean usedCharacter;
    private AreaType previousArea;
    private boolean professorUsed;
    private AreaType coderArea;
    private TurnTimer timer;
    private int playerCount;

    public Controller() {
        startGame();
    }


    /**
     * When a button from view is pressed, gets the action event and calls does the corresponding action in model
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "NEW_GAME":
                int playerCount = StartingScreen.askNumberOfPlayers();
                if (playerCount == -1) {
                    log("No input given.");
                    System.exit(0);
                }
                List<String> names = StartingScreen.askPlayerNames(playerCount);
                newGame(playerCount, names);
                break;

            case "LOAD_GAME":
                model = Model.loadGame();
                newGameFromLoad(model);
                break;

            case "SAVE":
                view.dispose();
                model.saveGame();
                System.exit(0);

                break;

            case "END_TURN":
                endTurn();
                break;

            case "DRAW_TILES":
                drawTiles();
                break;

            case "TILE_PRESSED":
                if (model.currentPlayer().getUseCoderNextTurn()) {
                    model.currentPlayer().setCoderTiles(model.currentPlayer().getCoderTiles() - 1);
                    pickTileFromBoard(e);

                    if (model.currentPlayer().getCoderTiles() == 0) {
                        model.currentPlayer().setUseCoderNextTurn(false);
                        tilesToPick = 2;
                        usedCharacter = false;
                        professorUsed = false;
                        view.disableAreas();
                        timer.start();
                        updatePlayerPoints(model.currentPlayer());
                        view.showPlayer(model.currentPlayer());
                    }
                }
                else {
                    tilesToPick--;
                    previousArea = pickTileFromBoard(e);
                    if (tilesToPick == 0) {
                        view.disableAreas();
                        if (!usedCharacter) {
                            view.enableCharacters();
                            view.updateCharacters();
                        }
                    } else if (tilesToPick == 1) {
                        model.getBoard().setAllAreasAvailable(false);
                        model.getBoard().setAreaAvailability(previousArea, true);
                        view.updateBoard();
                    }


                    if (professorUsed) {
                        model.getBoard().setAreaAvailability(previousArea, false);
                        view.updateBoard();
                    }
                }
                break;

            case "CHARACTER_PRESSED":

                CharacterView viewSource = (CharacterView) e.getSource();
                Character characterSource = viewSource.getCorrespondingCharacter();

                tilesToPick = model.currentPlayer().useCharacter(characterSource, previousArea, model.getBoard());
                view.setUsedCharacter(model.currentPlayer(), characterSource);
                view.updateBoard();

                if (characterSource instanceof Professor) {
                    professorUsed = true;
                }
                else if (characterSource instanceof Coder) {
                    view.disableAreas();
                }

                usedCharacter = true;
                view.disableCharacters();
                log("Character pressed: :" + characterSource.toString());
        }
    }


    /**
     * Displays the starting screen
     */
    public void startGame() {
        new StartingScreen(this);
    }


    /**
     * Initializes the new game after the user gave the number of players and their names
     * @param playerCount the number of the players
     * @param names the names of the players
     */
    public void newGame(int playerCount, List<String> names) {


        this.model = new Model(playerCount, names);
        this.view = new View(model.getBoard(), model.getPlayersList(), this);
        this.playerCount = playerCount;

        timer = new TurnTimer(
                30,
                seconds -> view.setDisplayTime(seconds),
                this::endTurn
        );

        view.addButtonsListener(this);

        tilesToPick = 2;
        usedCharacter = false;
        previousArea = null;
        coderArea = null;

        view.disableAreas();
        view.disableCharacters();
        timer.start();
        updatePlayerPoints(model.currentPlayer());
        view.showPlayer(model.currentPlayer());
    }

    /**
     * Updates the view based on the loaded model
     * @param model the loaded model
     */
    public void newGameFromLoad(Model model) {
        this.model = model;
        this.view = new View(model,  this);

        timer = new TurnTimer(
                30,
                seconds -> view.setDisplayTime(seconds),
                this::endTurn
        );


        view.addButtonsListener(this);

        tilesToPick = 2;
        usedCharacter = false;
        previousArea = null;
        coderArea = null;

        view.disableAreas();
        view.disableCharacters();
        timer.start();
        updatePlayerPoints(model.currentPlayer());
        view.showPlayer(model.currentPlayer());
    }


    /**
     * When player 4 draws tiles from the bag
     */
    public void drawTiles() {log("Draw tiles pressed");
        Tile newTile1 = model.currentPlayer().pickTileFromBag(model.getBag(), model.getBoard());
        if (newTile1 != null)
            view.drawTilesFromBag(newTile1);
        if (model.gameOver()) {
            view.dispose();
            showWinnerScreen(model.calculateScoreboard());
        }


        Tile newTile2 = model.currentPlayer().pickTileFromBag(model.getBag(), model.getBoard());
        if (newTile2 != null)
            view.drawTilesFromBag(newTile2);
        if (model.gameOver()) {
            view.dispose();
            showWinnerScreen(model.calculateScoreboard());
        }

        Tile newTile3 = model.currentPlayer().pickTileFromBag(model.getBag(), model.getBoard());
        if (newTile3 != null)
            view.drawTilesFromBag(newTile3);
        if (model.gameOver()) {
            view.dispose();
            showWinnerScreen(model.calculateScoreboard());
        }

        Tile newTile4 = model.currentPlayer().pickTileFromBag(model.getBag(), model.getBoard());
        if (newTile4 != null)
            view.drawTilesFromBag(newTile4);
        if (model.gameOver()) {
            view.dispose();
            showWinnerScreen(model.calculateScoreboard());
        }

        view.enableAreas();
    }


    /**
     * When player presses the End Turn button, end his turn and move to the next player
     */
    public void endTurn() {
        timer.stop();
        log("End turn pressed");
        Player nextPlayer = model.nextPlayer();

        if (model.currentPlayer().getUseCoderNextTurn()) { //if coder was activated at the previous turn
            timer.start();
            updatePlayerPoints(nextPlayer);
            view.showPlayer(nextPlayer);
            //sets the coder only the coder area available
            model.getBoard().setAllAreasAvailable(false);
            model.getBoard().setAreaAvailability(model.currentPlayer().getCoderArea(), true);
            view.updateBoard();
        }
        else {
            tilesToPick = 2;
            usedCharacter = false;
            professorUsed = false;
            view.disableAreas();
            timer.start();
            updatePlayerPoints(nextPlayer);
            view.showPlayer(nextPlayer);
        }
    }

    /**
     * When player presses a board area, select tiles from this area
     * @param e the action event to get the tile source from
     * @return the area type the tile was picked from
     */
    public AreaType pickTileFromBoard(ActionEvent e) {
        TileView buttonSource = (TileView) e.getSource();
        Tile source = (Tile) buttonSource.getTileSource();
        log("Tile pressed" + source.toString());

        model.currentPlayer().pickTileFromBoard(model.getBoard(), source);
        view.removeTileFromBoard(buttonSource);
        log("removed tile from board");
        view.addTileToInventory(source, model.currentPlayer());
        log("added tile to " + model.currentPlayer().getName() + "'s inventory");

        return buttonSource.getCorrespondingArea();
    }

    /**
     * Update the players points at the playerPanel of View
     * @param player the player whose points to update
     */
    public void updatePlayerPoints(Player player) {
        view.setPointsLabel(model.calculatePlayerPoints(player));
    }

    public void showWinnerScreen(List<Map.Entry<Player, Integer>> scoreboard) {
        new WinnerScreen(scoreboard, playerCount);
    }


    /**
     * Prints a message to the console to keep game log
     * @param message the log message
     */
    private void log(String message) {
        System.out.println(message);
    }
}
