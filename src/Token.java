public class Token {
    private int tokenType;  // The type of the token (e.g., 1 for "program", 31 for integer, etc.)
    private String tokenValue;  // The actual value of the token (e.g., "123" for integers or "X" for identifiers)
    
    public Token(int tokenType, String tokenValue) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }

    public int getTokenType() {
        return tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public int getVal() {
        if (tokenType == 31) {  // If the token is an integer
            return Integer.parseInt(tokenValue);  // Convert and return the integer value
        } else {
            throw new IllegalStateException("Current token is not an integer.");
        }
    }

    public String getIDName() {
        if (tokenType == 32) {  // If the token is an identifier
            return tokenValue;  // Return the identifier name
        } else {
            throw new IllegalStateException("Current token is not an identifier.");
        }
    }
}
