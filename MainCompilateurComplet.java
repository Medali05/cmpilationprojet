import java.io.*;
import java.util.List;

public class MainCompilateurComplet {
    
    public static void main(String[] args) {
  
        
        // Chemin du fichier
        String fichier = "C:\\Users\\victus\\Desktop\\projetcompi\\code.c";
        
        

        // ================= ANALYSE LEXICALE =================
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println(" ANALYSE LEXICALE");
        System.out.println("══════════════════════════════════════════════════\n");
        
        System.out.println("Début de l'analyse lexicale...");
        System.out.println("(Les commentaires et directives sont ignorés)");
        System.out.println("\n LEXÈMES RÉCUPÉRÉS :");
        System.out.println("══════════════════════════════════════════════════");
        
        List<Token> tokens = AnalyseurLexical.analyser(fichier);
        
        // Afficher tous les lexèmes avec leur type
        int compteur = 0;
        for (Token token : tokens) {
            compteur++;
            if (token.type.equals("ERREUR")) {
                System.out.println(" ! " + token);
            } else {
                System.out.println("   " + token);
            }
        }
        
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println(" RÉSULTAT LEXICAL :");
        System.out.println("══════════════════════════════════════════════════");
        
        boolean erreurLexicale = AnalyseurLexical.aErreurLexicale();
        
        if (erreurLexicale) {
            System.out.println(" ERREUR LEXICALE RENCONTRÉE .. !");
            System.out.println("   " + compteur + " lexèmes analysés");
        } else {
            System.out.println(" ANALYSE LEXICALE BIEN FAITE");
            System.out.println("   " + compteur + " lexèmes correctement identifiés");
        }
        
        // ================= ANALYSE SYNTAXIQUE =================
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println(" ANALYSE SYNTAXIQUE DO-WHILE");
        System.out.println("══════════════════════════════════════════════════\n");
        
        System.out.println("Recherche des structures do-while...");
        System.out.println(" Le bloc d'instructions et la condition sont ignorés");
        System.out.println("Format vérifié: do { ... } while(...);");
        
        AnalyseurSyntaxiqueDoWhile analyseur = new AnalyseurSyntaxiqueDoWhile(tokens);
        boolean resultatSyntaxique = analyseur.analyser();
        
        // ================= RÉSULTAT FINAL =================
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println(" RÉSULTAT FINAL DE LA COMPILATION");
        System.out.println("══════════════════════════════════════════════════\n");
        
        if (!erreurLexicale && resultatSyntaxique) {
            System.out.println(" COMPILATION RÉUSSIE !");
            System.out.println("    Analyse lexicale : Correcte......!");
            System.out.println("    Analyse syntaxique : Toutes les structures do-while sont valides.....!");
        } else {
            System.out.println(" COMPILATION ÉCHOUÉE");
            if (erreurLexicale) {
                System.out.println("    Analyse lexicale : Erreurs détectées");
            }
            if (!resultatSyntaxique) {
                System.out.println("    Analyse syntaxique : Erreurs dans les structures do-while");
            }
        }
        
    }
    
   
   
}