/**
 * This class is used to model a hand of Pair in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his hand. It provides concrete implementations of
 * two abstract methods in the superclass and overrides several methods.
 * This class also provides a static method for checking whether a given hand
 * is a valid Pair.
 * @author Wang Kesheng
 */
public class Pair extends Hand{
    /**
     * This constructor constructs a Pair object with specified player and list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards the player plays
     */
    public Pair(CardGamePlayer player, CardList cards){
        super(player, cards);
        this.type = "Pair";
    }
    // a private variable storing the type of this hand in String
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
     * This method is used to check if it is a valid Pair.
     * @return a boolean value indicating whether this hand is a valid Pair
     */
    @Override
    public boolean isValid() {
        return Pair.isValid(this);
    }
    /**
     * This static method is used to check whether a given hand is a valid Pair hand.
     * @param hand the hand given to be checked validity
     * @return the boolean value to specify whether the given hand is a valid Pair in Big Two Game.
     */
    public static boolean isValid(CardList hand){
        if (hand.size() == 2){
            for (int i = 0; i < 2; i++){
                Card card = hand.getCard(i);
                if (!(card.getSuit() > -1 && card.getSuit() < 4 && card.getRank() > -1 && card.getRank() < 13)){
                    return false;
                }
            }
            if (hand.getCard(0).getRank() != hand.getCard(1).getRank()){
                return false;
            }
            return true;
        }
        return false;
    }
    /**
     * This method is used to retrieve the top card of this Pair.
     * @return the top card of this hand
     */
    @Override
    public Card getTopCard() {
        if (this.getCard(0).getSuit() > this.getCard(1).getSuit()){
            return this.getCard(0);
        }
        else {
            return this.getCard(1);
        }


    }
}
