import java.util.List;

public class AnalyseurSyntaxiqueDoWhile {
    
    private List<Token> tokens;
    private int i;
    private boolean error;
    private boolean erreurDansStructure;
    private Token tokenCourant;
    private int nombreStructures = 0;
    private int nombreErreurs = 0;
    
    public AnalyseurSyntaxiqueDoWhile(List<Token> tokens) {
        this.tokens = tokens;
        this.i = 0;
        this.error = false;
        this.erreurDansStructure = false;
        if (!tokens.isEmpty()) {
            this.tokenCourant = tokens.get(0);
        }
    }
    
    public boolean analyser() {
        // Parcourir tous les tokens pour trouver des structures do-while
        while (i < tokens.size()) {
            if (verifierToken("do", "MOT_CLE")) {
                nombreStructures++;
                System.out.println("\n=== Structure do-while n°" + nombreStructures + " ===");
                
                // Réinitialiser le flag d'erreur pour cette structure
                erreurDansStructure = false;
                
                S();
                
                // Si erreur détectée dans cette structure
                if (erreurDansStructure) {
                    error = true; // Marquer qu'il y a au moins une erreur
                    nombreErreurs++;
                    System.out.println("❌ Structure do-while n°" + nombreStructures + " contient des erreurs");
                }
                
                // Continuer à chercher d'autres structures même après une erreur
                // (ne pas réinitialiser error ici)
            } else {
                avancer();
            }
        }
        
        if (nombreStructures == 0) {
            System.out.println("❌ Aucune structure do-while trouvée dans le fichier.");
            return false;
        }
        
        // Afficher le résumé
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("📊 RÉSULTAT SYNTAXIQUE :");
        System.out.println("══════════════════════════════════════════════════");
        System.out.println("Structures do-while trouvées : " + nombreStructures);
        System.out.println("Structures avec erreurs : " + nombreErreurs);
        System.out.println("Structures correctes : " + (nombreStructures - nombreErreurs));
        
        return !error; // Retourne false si au moins une erreur trouvée
    }
    
    private void S() {
        if (verifierToken("do", "MOT_CLE")) {
            System.out.println(" 'do' trouvé");
            avancer();
            
            if (verifierToken("{", "SEPARATEUR")) {
                System.out.println(" '{' trouvé");
                avancer();
                
                inst();
                
                if (verifierToken("}", "SEPARATEUR")) {
                    System.out.println(" '}' trouvé");
                    avancer();
                    
                    if (verifierToken("while", "MOT_CLE")) {
                        System.out.println(" 'while' trouvé");
                        avancer();
                        
                        if (verifierToken("(", "SEPARATEUR")) {
                            System.out.println(" '(' trouvé");
                            avancer();
                            
                            cond();
                            
                            if (verifierToken(")", "SEPARATEUR")) {
                                System.out.println(" ')' trouvé");
                                avancer();
                                
                                if (verifierToken(";", "SEPARATEUR")) {
                                    System.out.println(" ';' trouvé");
                                    avancer();
                                    System.out.println(" Structure do-while correcte");
                                } else {
                                    erreurDansStructure = true;
                                    System.out.println(" ERREUR: ';' manquant à la fin");
                                }
                            } else {
                                erreurDansStructure = true;
                                System.out.println(" ERREUR: ')' manquant après condition");
                            }
                        } else {
                            erreurDansStructure = true;
                            System.out.println(" ERREUR: '(' manquant après while");
                        }
                    } else {
                        erreurDansStructure = true;
                        System.out.println(" ERREUR: 'while' manquant après bloc");
                    }
                } else {
                    erreurDansStructure = true;
                    System.out.println(" ERREUR: '}' manquant pour fermer bloc");
                }
            } else {
                erreurDansStructure = true;
                System.out.println(" ERREUR: '{' manquant après do");
            }
        } else {
            erreurDansStructure = true;
            System.out.println(" ERREUR: Structure ne commence pas par 'do'");
        }
    }
    
    private void inst() {
        // Ignorer les instructions jusqu'à trouver }
        int niveauAccolades = 1;
        
        while (i < tokens.size() && niveauAccolades > 0) {
            if (tokens.get(i).lexeme.equals("{")) {
                niveauAccolades++;
            } else if (tokens.get(i).lexeme.equals("}")) {
                niveauAccolades--;
                if (niveauAccolades == 0) {
                    // Ne pas avancer, on laisse S() vérifier le }
                    return;
                }
            }
            avancer();
        }
        
        if (niveauAccolades > 0) {
            erreurDansStructure = true;
            System.out.println(" ERREUR: '}' manquant (fin de fichier atteinte)");
        }
    }
    
    private void cond() {
        // Ignorer la condition jusqu'à trouver )
        int niveauParentheses = 1;
        
        while (i < tokens.size() && niveauParentheses > 0) {
            if (tokens.get(i).lexeme.equals("(")) {
                niveauParentheses++;
            } else if (tokens.get(i).lexeme.equals(")")) {
                niveauParentheses--;
                if (niveauParentheses == 0) {
                    // Ne pas avancer, on laisse S() vérifier le )
                    return;
                }
            }
            avancer();
        }
        
        if (niveauParentheses > 0) {
            erreurDansStructure = true;
            System.out.println(" ERREUR: ')' manquant dans condition");
        }
    }
    
    private boolean verifierToken(String lexeme, String type) {
        return tokenCourant != null && 
               tokenCourant.lexeme.equals(lexeme) && 
               tokenCourant.type.equals(type);
    }
    
    private void avancer() {
        i++;
        if (i < tokens.size()) {
            tokenCourant = tokens.get(i);
        } else {
            tokenCourant = null;
        }
    }
}