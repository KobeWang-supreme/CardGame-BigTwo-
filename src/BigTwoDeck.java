/**
 * This class is used to model a deck of cards used in a Big Two card
 * game. This class is a subclass of the Deck class. It holds the Information
 * of a Big Two card and overrides the "initialize()" method in order to create
 * a deck of Big Two cards.
 * @author Wang Kesheng
 */
public class BigTwoDeck extends Deck{
    /**
     * This method if for initializing a deck of Big Two cards. It will remove
     * all cards from the deck, create 52 Big two cards and add them to the deck.
     */
    @Override
    public void initialize() {
        //remove the current deck
        this.removeAllCards();
        //fill the current deck with new cards
        for (int i = 0;i < 4; i++){
            for (int j = 0;j < 13; j++){
                BigTwoCard card = new BigTwoCard(i,j);
                this.addCard(card);
            }
        }
        //shuffle the current deck
        this.shuffle();
    }
}
