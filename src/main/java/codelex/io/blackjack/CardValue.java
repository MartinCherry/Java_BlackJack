package codelex.io.blackjack;

public enum CardValue {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("JACK", 10),
    QUEEN("QUEEN", 10),
    KING("KING", 10),
    ACE("ACE", 11);

    private final String stringValue;

    private final int blackJackValue;

    CardValue(String stringValue, int blackJackValue) {
        this.stringValue = stringValue;
        this.blackJackValue = blackJackValue;
    }

    public static CardValue getCardValueByString(String value) {
        for (CardValue cv : values()) {
            if (cv.stringValue.equals(value)) {
                return cv;
            }
        }
        return null;
    }

    public int getBlackJackValue() {
        return blackJackValue;
    }

}
