import java.util.ArrayList;
/**
 * This class is used to model a hand of cards. It is a subclass of the
 * class CardList. It stores the information of the player who plays his
 * hand. It also includes the methods for accessing the player of his
 * hand, checking whether if it beats a specified hand.
 * This class also provides a protocol of static method used to be overridden by
 * its subclasses to determine whether any  arbitrary given hand is a valid hand
 * of certain type.
 * @author Wang Kesheng
 */

public abstract class Hand extends CardList{
    /**
     * This constructor constructs objects from
     * the subclasses of the class Hand, with specified player and list.
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        this.beaten = new ArrayList<String>();
        this.beaten.add("Pass");
        if (cards != null) {
            this.numOfCards = cards.size();
            for (int i = 0; i < cards.size(); i++) {
                this.addCard(cards.getCard(i));
            }
        }
        else{
            this.numOfCards = 0;
        }
    }
    // Private instance variable holding the number of cards in one hand
    private int numOfCards;
    //storing the types of hand can be beaten
    public ArrayList<String> beaten;
    /**
     * This method is used to get the number of cards of this hand.
     * @return the number of cards of this hand
     */
    public int getNumOfCards(){
        return this.numOfCards;
    }
    // Private instance variable holding the information of the player
    private CardGamePlayer player;
    /**
     * This method is used to get the player of this hand.
     * @return the player of this hand
     */
    public CardGamePlayer getPlayer(){
        return this.player;
    }
    /**
     * This method is used to get the top card of this hand.
     * @return the top card of this hand
     */
    public Card getTopCard(){
        this.sort();
        return this.getCard(0);
    }
    /**
     * This method is used for checking whether this hand beats a specified hand.
     * This method firstly checks for the match of number of cards between two hands,
     * if unmatched, returns false. Then this method returns true if the top card, as
     * obtained from the "getTopCard()" method, in this hand is strictly greater than
     * that in the other hand, and returns false otherwise. Finally, this hand will
     * consider whether the hand given, though of a different type can also be beaten
     * by the current hand.
     * @param hand a specified hand used to be compared by this hand
     * @return the boolean value specifying whether the given hand can be beaten
     * 			by this hand
     */
    public boolean beats(Hand hand){
        if (hand == null){
            return true;
        }
        if (this.getNumOfCards() != hand.getNumOfCards()){
            return false;
        }
        if (this.getType() == hand.getType()){
            if (this.getTopCard().compareTo(hand.getTopCard()) > 0){
                return true;
            }
            else{
                return false;
            }
        }
        if (this.beaten.contains(hand.getType())){
            return true;
        }
        return false;


    }
    /**
     * This method is used to check if it is a valid hand.
     * This is an abstract method designed to be overridden.
     * @return a boolean value indicating whether this hand is a valid hand
     */
    public abstract boolean isValid();   // to be decided
    /**
     * This method is used to check if a given hand is a valid hand.
     * And this is a static method.
     * @param hand the hand given to be checked validity
     * @return false
     */
    public static boolean isValid(CardList hand){
        return false;
    }
    /**
     * This method is used to return a string specifying the type of this hand.
     * This is an abstract method designed to be overridden.
     * @return the string specifying the type of this hand
     */
    public abstract String getType();

}
