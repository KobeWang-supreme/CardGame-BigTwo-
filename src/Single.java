/**
 * This class is used to model a hand of Single in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his hand. It provides concrete implementations of
 * two abstract methods in the superclass and overrides several methods for its use.
 * This class also provides a static method to check whether a given hand is
 * a valid Single.
 * @author Wang Kesheng
 */
public class Single extends Hand{
    /**
     * This constructor constructs a Single object with specified player and list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Single(CardGamePlayer player, CardList cards){
        super(player, cards);
        this.type = "Single";
    }
    // Private variable storing the type of this hand in String
    private String type;
    /**
     * This method is used to return the type of this hand
     * @return the type of this hand
     */
    @Override
    public String getType() {
        return this.type;
    }
    /**
     * This static method is used to check whether a given hand is a valid Single hand.
     * @param hand the hand given to be checked validity
     * @return the boolean value to specify whether the given hand is a valid Single in Big Two Game.
     */
    public static boolean isValid(CardList hand) {
        if (hand.size() == 1){
            Card card = hand.getCard(0);
            if (card.getSuit() > -1 && card.getSuit() < 4 && card.getRank() > -1 && card.getRank() < 13){
                return true;
            }
        }
        return false;
    }
    /**
     * This method is used to check if this is a valid Single.
     * @return a boolean value indicating whether this hand is a valid Single
     */
    @Override
    public boolean isValid() {
        return Single.isValid(this);
    }
}
