/**
 * This class is used to model a hand of Flush in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his hand. It provides the concrete implementation
 * of two abstract methods in the superclass and overrides several methods.
 * This class also provides a static method for checking whether a given hand
 * is a valid Flush.
 * A Flush is five Big Two Cards from the same suit.
 * @author Wang Kesheng
 */
public class Flush extends Hand{
    /**
     * This constructor constructs a Flush object with specified player and the list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Flush(CardGamePlayer player, CardList cards){
        super(player, cards);
        this.type = "Flush";
        // storing the types that can be beaten by this type
        this.beaten.add("Straight");
    }
    //a private variable storing the type of this hand
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
     * This method is used to check if this is a valid Flush.
     * @return a boolean value indicating whether this hand is a valid Flush
     */
    @Override
    public boolean isValid() {
        return Flush.isValid(this);
    }
    /**
     * This static method is used to check whether a given hand is a valid Flush hand.
     * @param hand a hand given to be checked validity
     * @return the boolean value to specify whether the given hand is a valid Flush in Big Two Game.
     */
    public static boolean isValid(CardList hand){
        if (hand.size() == 5){
            int ty = hand.getCard(0).getSuit();
            for ( int i = 0; i < 5; i++){
                if(!(hand.getCard(i).getSuit()>-1&&hand.getCard(i).getSuit()<4&&hand.getCard(i).getRank()>-1&&hand.getCard(i).getRank()<13&&hand.getCard(i).getSuit()==ty  )){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /**
     * This method is used for checking whether this hand can beat a specified hand.
     * This method firstly checks for the match of number of cards between two hands,
     * if unmatched, returns false. Then this method returns true if the top card, as
     * obtained from the getTopCard() method, in this hand is strictly greater than
     * that in the other hand, and returns false otherwise. Finally, this method will
     * consider whether the hand given, though of a different type can also be beaten
     * by the current hand.
     * @param hand a specified hand used to be compared by this hand
     * @return the boolean value indicating whether the given hand can be beaten
     * 			by this hand
     */
    @Override
    public boolean beats(Hand hand) {
        if(hand == null)
        {
            return true;
        }
        if(this.getNumOfCards() != hand.getNumOfCards())
        {
            return false;
        } else if (this.getType() == hand.getType()) {
            if (this.getTopCard().getSuit() - hand.getTopCard().getSuit() > 0) {
                return true;
            }
            if (this.getTopCard().getSuit() - hand.getTopCard().getSuit() == 0) {
                if (this.getTopCard().compareTo(hand.getTopCard()) > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            if (this.getTopCard().getSuit() - hand.getTopCard().getSuit() < 0){
                return false;
            }
        } else if (this.beaten.contains(hand.getType())) {
            return true;
        }

        return false;
    }
}
