import java.util.ArrayList;
/**
 * This class is used to model a hand of Quad in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his hand. It provides concrete implementation of
 * two abstract methods in the superclass and overrides several methods for its use.
 * This class also provides a static method for checking whether a given hand
 * is a valid Quad.
 * @author Wang Kesheng
 */
public class Quad extends Hand{
    // Private variable storing the type of this hand in String
    private String type;
    /**
     * This constructor constructs a Quad object with specified player and the list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = "Quad";
        // Storing the type(s) of hand FullHouse can beat
        this.beaten.add("Straight");
        this.beaten.add("Flush");
        this.beaten.add("FullHouse");
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
     * This method is used to check if it is a valid Quad.
     * @return a boolean value indicating whether this hand is a valid Quad.
     */
    @Override
    public boolean isValid() {
        return Quad.isValid(this);
    }
    /**
     * This static method is used to check whether the given hand is a valid Quad hand.
     * @param hand a given hand to be checked validity
     * @return the boolean value to indicate whether the given hand is a valid
     * Quad in Big Two Game.
     */
    public static boolean isValid(CardList hand){
        if (hand.size() == 5){
            ArrayList<Card> firList = new ArrayList<Card>();
            ArrayList<Card> secList = new ArrayList<Card>();
            int rank1 = hand.getCard(0).getRank(),rank2 = -1;
            for (int i = 0; i < 5 ; i++){
                Card card = hand.getCard(i);
                if (!(card.getSuit() > -1 && card.getSuit() < 4 && card.getRank() > -1 && card.getRank() < 13)) {
                    return false;
                }
            }
            for (int i = 0; i < 5; i++) {
                if (hand.getCard(i).getRank() != rank1){
                    rank2 = hand.getCard(i).getRank();
                    break;
                }
            }
            for (int i = 0; i < 5; i++) {
                if (hand.getCard(i).getRank() == rank1){
                    firList.add(hand.getCard(i));
                }
                if (hand.getCard(i).getRank() == rank2){
                    secList.add(hand.getCard(i));
                }
            }
            if ((firList.size()==4&&secList.size()==1)||(firList.size()==1&&secList.size()==4)){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    /**
     * This method is used to get the top card of this Quad.
     * @return the top card of this hand
     */
    @Override
    public Card getTopCard() {
        // Create two lists to hold cards according to rank
        CardList firstList = new CardList();
        CardList secondList = new CardList();
        int rank1 = this.getCard(0).getRank(), rank2 = -1;
        for (int i = 0; i < 5; i++) {
            if (this.getCard(i).getRank() != rank1){
                rank2 = this.getCard(i).getRank();
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (this.getCard(i).getRank() == rank1){
                firstList.addCard(this.getCard(i));
            }
            if (this.getCard(i).getRank() == rank2){
                secondList.addCard(this.getCard(i));
            }
        }
        if (firstList.size() == 4){
            firstList.sort();
            return firstList.getCard(0);
        }
        else{
            secondList.sort();
            return secondList.getCard(0);
        }
    }
}
