import java.util.ArrayList;
/**
 * The BigTwo class is used to model a Big Two card game.
 * It stores the information of the deck of cards, list of players,
 * list of hands played on the table, the index of the current player,
 * and a console for providing the user interface.
 * @author Wang Kesheng
 */
public class BigTwo implements CardGame{
    // a variable to count the number of pass
    private int passNum = 0;
    // a variable to store the number of players
    private int numOfPlayers = 4;
    // a deck of cards
    private Deck deck;
    // A list of players
    private ArrayList<CardGamePlayer> playerList;
    // A list of hands played on the table
    private ArrayList<Hand> handsOnTable;
    // the index of the current player
    private int currentPlayerIdx;
    // the ui to be used
    private BigTwoGUI ui;
    // a variable indicates whether the game is end
    private boolean endOfGame = false;
    // just to be used in the first round to help judge if the cards played by the first player contains diamond 3
    private int num = 0;
    // a big two game client
    private BigTwoClient client;
    // a variable to store the local player index
    private int localPlayerIndex;

    /**
     *  The constructor for creating a Big Two card game.
     *  Four players will be initialized and stored into
     *  an ArrayList. The table is also created.
     */
    public BigTwo(){
        this.playerList = new ArrayList<CardGamePlayer>();
        CardGamePlayer player1 = new CardGamePlayer();
        player1.setName("Player 0");
        CardGamePlayer player2 = new CardGamePlayer();
        player2.setName("Player 1");
        CardGamePlayer player3 = new CardGamePlayer();
        player3.setName("Player 2");
        CardGamePlayer player4 = new CardGamePlayer();
        player4.setName("Player 3");
        this.playerList.add(player1);
        this.playerList.add(player2);
        this.playerList.add(player3);
        this.playerList.add(player4);
        this.handsOnTable = new ArrayList<Hand>();
        this.ui = new BigTwoGUI(this);
        client = new BigTwoClient(this,ui);//
    }

    /**
     * a method to return the local player index
     * @return local player index
     */
    public int getLocalPlayerIndex() {
        return localPlayerIndex;
    }

    /**
     * a method to set the local player index
     * @param localPlayerIndex
     */
    public void setLocalPlayerIndex(int localPlayerIndex) {
        this.localPlayerIndex = localPlayerIndex;
    }

    /**
     * a method to get the game client
     * @return
     */
    public BigTwoClient getClient(){
        return client;
    }
    /**
     * The accessor for getting the number of players
     * @return the number of players
     */
    public int getNumOfPlayers(){
        return playerList.size();
    }
    /**
     * The accessor method for getting the deck of cards being used.
     *  @return The deck of cards being used
     */
    public Deck getDeck() {
        return this.deck;
    }
    /**
     * The accessor method for getting the list of the players.
     * @return The list of players
     */
    public ArrayList<CardGamePlayer> getPlayerList(){
        return this.playerList;
    }
    /**
     * The accessor method for getting the list of hands played on the table.
     * @return The list of hands played on the table
     */
    public ArrayList<Hand> getHandsOnTable(){
        return this.handsOnTable;
    }
    /**
     * The accessor method for getting the index of the current player.
     * @return The index of current player
     */
    public int getCurrentPlayerIdx(){
        return this.currentPlayerIdx;
    }
    /**
     * a method for starting/restarting the game with a given shuffled deck of cards.
     * The game logic is implemented in this method.
     * @param deck A shuffled deck for starting the game
     */
    public void start(Deck deck){
        ui.disable();
        this.deck = deck;
        for (int i = 0; i < 4; i++) {
            playerList.get(i).removeAllCards();
        }
        //give the cards to players
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                playerList.get(i).addCard(this.deck.getCard(i * 13 + j));
            }
        }
        for (CardGamePlayer player : playerList){
            player.sortCardsInHand();
        }
        int CurIndex = -1;
        // get the first player
        for (int i = 0; i < 4; i++) {
            if (playerList.get(i).getCardsInHand().contains(new Card(0,2))){
                CurIndex = i;
            }
        }
        currentPlayerIdx = CurIndex;
        ui.setActivePlayer(currentPlayerIdx);
        if (this.getCurrentPlayerIdx() == this.getClient().getPlayerID()){
            ui.enable();
        }
        else {
            ui.disable();
        }
        ui.repaint();
        ui.promptActivePlayer();
    }

    /**
     *  a method for sending message to the server(move)
     * @param playerIdx the index of the player who makes the move
     * @param cardIdx   the list of the indices of the cards selected by the player
     */
    public void makeMove(int playerIdx, int[] cardIdx){
        //checkMove(playerIdx,cardIdx);
        CardGameMessage msg = new CardGameMessage(6, client.getPlayerID(), cardIdx);
        client.sendMessage(msg);
    }

    /**
     * a method for checking a move made by a player.
     * This method should be called from the makeMove() method.
     * @param playerIdx the index of the player who makes the move
     * @param cardIdx   the list of the indices of the cards selected by the player
     */
    public void checkMove(int playerIdx, int[] cardIdx){
        CardGamePlayer currentPlayer = this.playerList.get(this.currentPlayerIdx);
        CardList cards = currentPlayer.play(cardIdx);
        Hand hand = BigTwo.composeHand(currentPlayer,cards);
        Hand latestHand = this.handsOnTable.size() == 0 ? null : this.getHandsOnTable().get(this.handsOnTable.size()-1);
        //check if it is valid
        if ((hand == null) || (latestHand == null && hand.getType() == "Pass") || (latestHand != null && !hand.beats(latestHand)) || (num == 0)&& !hand.contains(currentPlayer.getCardsInHand().getCard(0))){
            this.ui.printMsg("Not a legal Move!!!\n");
            this.ui.promptActivePlayer();
            return;
        }
        String message = String.format("{%s} ",hand.getType());
        if (hand.getType() != "Pass"){
            message = message + hand.toString();
            this.passNum = 0;
            currentPlayer.removeCards(cards);
            this.handsOnTable.add(hand);
        }
        else{
            ++this.passNum;
        }
        this.ui.printMsg(message + "\n");
        if ( currentPlayer.getNumOfCards() == 0){
            this.endOfGame = true;
            this.handsOnTable.clear();
            System.out.println("Game ends");
            for(int i = 0; i < numOfPlayers; ++i)
            {
                CardGamePlayer player = this.playerList.get(i);
                int numOfCards = player.getNumOfCards();
                if(numOfCards != 0)
                {
                    this.ui.printMsg(String.format("%n%s has %d cards in hand.", player.getName(), numOfCards));
                } else {
                    this.ui.printMsg(String.format("%n%s won the game.", player.getName()));
                }
            }
            return;
        }
        this.currentPlayerIdx = (this.currentPlayerIdx + 1) % 4;
        num = 1;
        this.ui.setActivePlayer(currentPlayerIdx);
        this.ui.repaint();
        if (this.passNum == 3){
            passNum = 0;
            this.handsOnTable.clear();
        }
        this.ui.promptActivePlayer();
    }
    /**
     * This method is used for checking whether the game ends
     * @return a boolean value indicating whether the game ends
     */
    public boolean endOfGame(){
        return endOfGame;
    }
    /**
     * The method for starting a Big Tww card game.
     * One instance of the game is created, and then is started by a shuffle deck of cards.
     * @param args This argument will not be used.
     */
    public static void main(String[] args){
        BigTwo game = new BigTwo();
        BigTwoDeck newDeck = new BigTwoDeck();
        game.start(newDeck);
    }
    /**
     * The method for returning a valid hand from the list of cards of the player.
     * This method returns null if there is no valid hand. An empty hand, if valid
     * will be returned with type "Pass".
     * @param player the player holding a hand of cards
     * @param cards the cards the player is holding
     * @return a specific hand of cards that is valid for this hand
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards){
        Hand hand = null;
        if (cards == null){
            if(Pass.isValid(cards)){
                hand = new Pass(player,cards);
            }
        }
        else{
            int num = cards.size();
            if(num == 1&&Single.isValid(cards)){
                hand = new Single(player,cards);
            }else if(num == 2&&Pair.isValid(cards)){
                hand = new Pair(player,cards);
            }else if(num == 3&&Triple.isValid(cards)){
                hand = new Triple(player,cards);
            }else if (num == 5){
                if(StraightFlush.isValid(cards)){
                    hand = new StraightFlush(player,cards);
                }else if (Quad.isValid(cards)){
                    hand = new Quad(player,cards);
                }
                else if (FullHouse.isValid(cards)){
                    hand = new FullHouse(player,cards);
                }
                else if (Flush.isValid(cards)){
                    hand = new Flush(player,cards);
                }
                else if (Straight.isValid(cards)){
                    hand = new Straight(player,cards);
                }
            }
        }
        return hand;
    }


}
