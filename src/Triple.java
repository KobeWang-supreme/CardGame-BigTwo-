/**
 * This class is used to model a hand of Triple in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his hand. It provides concrete implementations of
 * two abstract methods in the superclass and overrides several methods for its use.
 * This class also provides a static method to check whether a given hand is a
 * valid Triple.
 * @author Wang Kesheng
 */
public class Triple extends Hand{
    // Private variable storing the type of this hand in String
    private String type;
    /**
     * This constructor constructs a Triple object with specified player and list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Triple(CardGamePlayer player,CardList cards){
        super(player, cards);
        this.type = "Triple";
    }
    /**
     * This method is used to return the type of this hand
     * @return the type of this hand
     */
    @Override
    public String getType() {
        return this.type;
    }
    /**
     * This method is used to check if this is a valid Triple.
     * @return a boolean value indicating whether this hand is a valid Pair
     */
    public boolean isValid() {
        return Triple.isValid(this);
    }
    /**
     * This static method is used to check whether a given hand is a valid Triple hand.
     * @param hand the hand given to be checked validity
     * @return the boolean value to specify whether the given hand is a valid Triple in Big Two Game.
     */
    public static boolean isValid(CardList hand){
        if (hand.size() == 3){
            for (int i = 0; i < 3; i++){
                Card card = hand.getCard(i);
                if (!(card.getSuit() > -1 && card.getSuit() < 4 && card.getRank() > -1 && card.getRank() < 13)){
                    return false;
                }
            }
            if(!(hand.getCard(0).getRank() == hand.getCard(1).getRank() && hand.getCard(1).getRank() == hand.getCard(2).getRank())){
                return false;
            }
            return true;
        }
        return false;
    }
}
