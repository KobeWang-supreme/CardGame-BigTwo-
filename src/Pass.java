/**
 * This class is used to model an empty hand of Single in Big Two Game.
 * It is a subclass of the class Hand.
 * This class also provides a static method to check whether a given hand
 * is a valid hand.
 * A hand is a valid Pass if and only if it is empty and can be played.
 * This class stores the information of the number of the consecutive Passes
 * that has been constructed. If this number is greater than three, a Pass shall
 * not be constructed according to the Big Two Game Rule.
 * @author Wang Kesheng
 */
public class Pass extends Hand {
    // A private variable storing the type of this hand in String
    private String type;
    /**
     * This constructor construct a Pass object with specified player and list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Pass(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = "Pass";
        this.beaten.add("Single");
        this.beaten.add("Pair");
        this.beaten.add("Triple");
        this.beaten.add("Straight");
        this.beaten.add("Flush");
        this.beaten.add("Quad");
        this.beaten.add("FullHouse");
        this.beaten.add("StraightFlush");
    }
    /**
     * This method is used to check if this is a valid Pass.
     * @return a boolean value indicating whether this hand is a valid Pass
     */
    @Override
    public boolean isValid() {
        return Pass.isValid(this);
    }
    /**
     * This static method is used to check whether a given hand is a valid Pass hand.
     * An empty hand is considered as an invalid Pass iff all other players have
     * used passed before the current player.
     * @param hand the hand given to be checked validity
     * @return the boolean value to indicate whether the given hand is a Pass
     */
    public static boolean isValid(CardList hand){
        if(hand == null || hand.size()==0){
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * This method is used to return the type of this hand
     * @return the type of this hand
     */
    public String getType(){
        return this.type;
    }
    /**
     * This method is used for checking whether this hand beats a specified hand.
     * Since this hand is an empty hand, i.e., pass, this method returns true
     * for any hands valid
     * @param hand a specified hand used to be compared by this hand
     */
    @Override
    public boolean beats(Hand hand) {
        return true;
    }
}
