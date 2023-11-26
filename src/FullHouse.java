import java.util.ArrayList;
/**
 * This class is used to model a hand of Full House in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his / her hand. It provides concrete implementations of
 * two abstract methods in the superclass and overrides several methods for its use.
 * This class also provides a static method for checking whether a given hand is
 * a Full House.
 * @author Wang Kesheng
 */
public class FullHouse extends Hand{
    /**
     * This constructor constructs a FullHouse object with specified player and list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public FullHouse(CardGamePlayer player, CardList cards) {
        //call superclass' constructor
        super(player, cards);
        //store the types this type can beat
        this.type = "FullHouse";
        this.beaten.add("Straight");
        this.beaten.add("Flush");
    }
    //a private variable storing the type of this hand in String
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
     * This method is used to check if this is a valid FullHouse.
     * @return a boolean value indicating whether this hand is a valid FullHouse
     */
    @Override
    public boolean isValid() {
        return FullHouse.isValid(this);
    }
    /**
     * This static method is used to check whether the given hand is a valid FullHouse hand.
     * @param hand one hand to be checked validity
     * @return the boolean value to indicate whether the given hand is a valid FullHouse in Big Two Game.
     */
    public static boolean isValid(CardList hand){
        //first check the number of this hand
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
            if ((firList.size()==3&&secList.size()==2)||(firList.size()==2&&secList.size()==3)){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    /**
     * This method is used to retrieve the top card of this Full House.
     * @return the top card of this hand
     */
    @Override
    public Card getTopCard() {
        //create two lists to hold cards according to the rank
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
        if (firstList.size() == 3){
            firstList.sort();
            return firstList.getCard(0);
        }
        else{
            secondList.sort();
            return secondList.getCard(0);
        }
    }
}
