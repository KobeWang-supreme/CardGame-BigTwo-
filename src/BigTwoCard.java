/**
 * This class is used to model the a card used in a Big Two card
 * game. This class is a subclass of the Card class. It holds the Information
 * of a Big Two card and overrides the compareTo() method in order to return
 * the ordering of the cards used in the Big Two game.
 *
 * @author Wang Kesheng
 */
public class BigTwoCard extends Card{
    /**
     * The constructor of the BigTwoCards class, constructing
     * a Big Two card with specified suit and rank.
     * @param suit
     *				an integer between 0 and 3 representing the suit of the card
     *				0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
     * @param rank
     *				an integer between 0 and 12 representing the rank of the card
     *				0 = 'A', 1 = '2', 2 = '3', ..., 11 = 'Q', 12 = 'K'
     */
    public BigTwoCard(int suit , int rank){
        super(suit,rank);
    }
    /**
     * The method for comparing this Big Two card with another Big Two card using the
     * rules specified in the Big Two game. This method returns a negative, zero, or a
     * positive integer to present this card is less than, equal to or greater than
     * the card given.
     * @param card Another card to be compared with this card.
     * @return A negative integer, zero or a positive integer if this card is less
     * than, equal to or greater than the given card
     */
    @Override
    public int compareTo(Card card) {
        int thisSuit = this.getSuit(), thisRank = this.getRank();
        if (thisRank == card.getRank()) {
            return thisSuit - card.getSuit();
        }
        else{
            return (thisRank + 11) % 13- (card.getRank() + 11) % 13;
        }
    }
}
