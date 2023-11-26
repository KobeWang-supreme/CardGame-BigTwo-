import java.util.ArrayList;
import java.util.Comparator;
/**
 * This class is used to model a hand of Straight in Big Two Game.
 * It is a subclass of the class Hand. It stores the information of
 * the player who plays his hand. It provides concrete implementations of
 * two abstract methods in the superclass and overrides several methods for its use.
 * This class also provides a static method to check whether a given hand is a
 * valid Straight.
 * @author Wang Kesheng
 */
public class Straight extends Hand{
    /**
     * This constructor constructs a Straight object with specified player and list
     * of cards.
     * @param player The player who plays his hand
     * @param cards the list cards that the player plays
     */
    public Straight(CardGamePlayer player,CardList cards){
        super(player, cards);
        this.type = "Straight";
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
     * This method is used to check if this is a valid Straight.
     * @return a boolean value indicating whether this is a valid Straight
     */
    @Override
    public boolean isValid() {
        return Straight.isValid(this);
    }
    /**
     * This static method is used to check whether a given hand is a valid Straight hand.
     * @param hand a given hand to be checked validity
     * @return the boolean value to specify whether the given hand is a valid Straight in Big Two Game.
     */
    public static boolean isValid(CardList hand){
        if (hand.size() == 5){
            ArrayList<Integer> RankOfCards = new ArrayList<Integer>(hand.size());
            for (int i = 0; i < 5; i++){
                Card card = hand.getCard(i);
                if (!(card.getSuit() > -1 && card.getSuit() < 4 && card.getRank() > -1 && card.getRank() < 13)){
                    return false;
                }
                if (!RankOfCards.contains(card.getRank())){
                    RankOfCards.add(card.getRank());
                }
            }
            RankOfCards.sort(new Comparator<Integer>() {
                public int compare(Integer o1, Integer o2) {
                    return (o1 + 11)%13 - (o2 + 11)%13;
                }
            });
            if (RankOfCards.size() == 5){
                if ((RankOfCards.get(4)+11)%13 -(RankOfCards.get(0)+11)%13 == 4){
                    return true;
                }
            }
        }
        return false;
    }
}
