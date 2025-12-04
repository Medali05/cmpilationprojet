public class LexicalUtils {
    private static final String[] SEPARATEURS = {
        "(", ")", "{", "}", "[", "]",
        ";", ",", ":", ".", "#"
    };

    private static final String[] OPERATEURS = {
        "+", "-", "*", "/", "%", "++", "--",
        "==", "!=", ">=", "<=", ">", "<",
        "&&", "||", "!", "&", "|", "^", "~",
        "<<", ">>", "=", "+=", "-=", "*=", "/=", "%="
    };

    private static final String[] MOTS_CLES = {
        "main","auto","break","case","char","const","continue",
        "do","double","else","float","for","if","int",
        "return","switch","void","while","mohamed_ali","bool","lebsir"
    };
    
    private static final char[] lettres = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m',
        'n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M',
        'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };

    public static boolean estSeparateur(String t) {
        for (String s : SEPARATEURS) {
            if (s.equals(t)) return true;
        }
        return false;
    }

    public static boolean estcaractere(String c) {
        if (c == null || c.length() != 3) return false;
        if (c.charAt(0) != '\'' || c.charAt(2) != '\'') return false;
        char ch = c.charAt(1);
        for (char lettre : lettres) {
            if (lettre == ch) return true;
        }
        return false;
    }

    public static boolean estLettre(char c) {
        for (char lettre : lettres) {
            if (lettre == c ) return true;
        }
        return false;
    }

    public static boolean estChiffre(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean estOperateur(String t) {
        for (String op : OPERATEURS) {
            if (op.equals(t)) return true;
        }
        return false;
    }

    public static boolean estMotCle(String t) {
        for (String mot : MOTS_CLES) {
            if (mot.equals(t)) return true;
        }
        return false;
    }

    private static int col(char c) {
        if (c == '_') return 0;
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) return 1;
        if (c >= '0' && c <= '9') return 2;
        return 3;
    }

    public static boolean estIdentificateur(String lex) {
        if (lex == null || lex.length() == 0) return false;
        int[][] matrice = {
            {1, 1, -1, -1},
            {1, 1, 1, -1}
        };
        int etat = 0;
        lex = lex + "#";
        int i = 0;
        while (lex.charAt(i) != '#' && matrice[etat][col(lex.charAt(i))] != -1) {
            etat = matrice[etat][col(lex.charAt(i))];
            i++;
        }
        return lex.charAt(i) == '#' && etat == 1 && i == lex.length() - 1;
    }

    public static boolean estNombre(String lex) {
        if (lex == null || lex.length() == 0) return false;
        for (int i = 0; i < lex.length(); i++) {
            char c = lex.charAt(i);
            if (!estChiffre(c)) return false;
        }
        return true;
    }
}