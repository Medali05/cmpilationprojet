import java.io.*;
import java.util.*;

public class AnalyseurLexical {
    
    private static boolean erreur = false;
    
    public static List<Token> analyser(String chemin) {
        List<Token> tokens = new ArrayList<>();
        erreur = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            int car;
            String lexeme = "";
            int ligne = 1;
            int colonne = 1;
            int debutColonne = 1;
            boolean ignoreDirective = false;  
            boolean ignoreCommentaireLigne = false;
            boolean ignoreCommentaireBloc = false;

            while ((car = br.read()) != -1) {
                char c = (char) car;
                
                // Gestion des lignes/colonnes
                if (c == '\n') {
                    ligne++;
                    colonne = 1;
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne-1));
                        lexeme = "";
                    }
                    continue;
                } else {
                    colonne++;
                }

                // Ignorer directives #include
                if (!ignoreDirective && c == '#') {
                    ignoreDirective = true;
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne));
                        lexeme = "";
                    }
                    continue;
                }
                if (ignoreDirective) {
                    if (c == '\n' || c == '\r') ignoreDirective = false;
                    continue;
                }

                // Début commentaires
                if (!ignoreCommentaireLigne && !ignoreCommentaireBloc) {
                    if (c == '/') {
                        br.mark(1);
                        int next = br.read();
                        if (next == '/') {
                            ignoreCommentaireLigne = true;
                            continue;
                        } else if (next == '*') {
                            ignoreCommentaireBloc = true;
                            continue;
                        } else {
                            br.reset();
                        }
                    }
                }

                // Ignorer commentaires
                if (ignoreCommentaireLigne) {
                    if (c == '\n' || c == '\r') ignoreCommentaireLigne = false;
                    continue;
                }

                if (ignoreCommentaireBloc) {
                    if (c == '*') {
                        br.mark(1);
                        int next = br.read();
                        if (next == '/') ignoreCommentaireBloc = false;
                        else br.reset();
                    }
                    continue;
                }

                // Début d'une chaîne
                if (c == '"') {
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne));
                        lexeme = "";
                    }
                    StringBuilder chaine = new StringBuilder("\"");
                    while ((car = br.read()) != -1) {
                        char next = (char) car;
                        colonne++;
                        chaine.append(next);
                        if (next == '"') break;
                        if (next == '\n') {
                            ligne++;
                            colonne = 1;
                        }
                    }
                    tokens.add(new Token(chaine.toString(), "CHAINE", ligne, colonne));
                    continue;
                }

                // Fin lexème : espace ou tab
                if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne));
                        lexeme = "";
                    }
                    continue;
                }

                // Séparateur
                if (LexicalUtils.estSeparateur("" + c)) {
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne));
                        lexeme = "";
                    }
                    tokens.add(new Token("" + c, "SEPARATEUR", ligne, colonne-1));
                    continue;
                }

                // Opérateur
                if (LexicalUtils.estOperateur("" + c)) {
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne));
                        lexeme = "";
                    }
                    tokens.add(new Token("" + c, "OPERATEUR", ligne, colonne-1));
                    continue;
                }

                // Caractère
                if (LexicalUtils.estcaractere("" + c)) {
                    if (lexeme.length() > 0) {
                        tokens.add(creerToken(lexeme, debutColonne, ligne));
                        lexeme = "";
                    }
                    tokens.add(new Token("" + c, "CARACTERE", ligne, colonne-1));
                    continue;
                }

                // Début d'un nouveau lexème
                if (lexeme.length() == 0) {
                    debutColonne = colonne - 1;
                }

                // Ajouter au lexème courant
                lexeme = lexeme + c;
            }

            // Dernier lexème
            if (lexeme.length() > 0) {
                tokens.add(creerToken(lexeme, debutColonne, ligne));
            }

        } catch (IOException e) {
            System.out.println("Erreur lecture fichier : " + e.getMessage());
        }

        return tokens;
    }

    private static Token creerToken(String lexeme, int colonne, int ligne) {
        String type;
        
        if (LexicalUtils.estMotCle(lexeme)) {
            type = "MOT_CLE";
        } else if (LexicalUtils.estIdentificateur(lexeme)) {
            type = "IDENTIFICATEUR";
        } else if (LexicalUtils.estNombre(lexeme)) {
            type = "NOMBRE";
        } else if (LexicalUtils.estcaractere(lexeme)) {
            type = "CARACTERE";
        } else if (lexeme.length() >= 2 && lexeme.charAt(0) == '"' && lexeme.charAt(lexeme.length()-1) == '"') {
            type = "CHAINE";
        } else if (LexicalUtils.estSeparateur(lexeme)) {
            type = "SEPARATEUR";
        } else if (LexicalUtils.estOperateur(lexeme)) {
            type = "OPERATEUR";
        } else {
            type = "ERREUR";
            erreur = true;
        }
        
        return new Token(lexeme, type, ligne, colonne);
    }
    
    public static boolean aErreurLexicale() {
        return erreur;
    }
}