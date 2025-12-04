public class Token {
    public final String lexeme;
    public final String type;
    public final int ligne;
    public final int colonne;
    
    public Token(String lexeme, String type, int ligne, int colonne) {
        this.lexeme = lexeme;
        this.type = type;
        this.ligne = ligne;
        this.colonne = colonne;
    }
    
    @Override
    public String toString() {
        return lexeme + " --> " + type;
    }
}