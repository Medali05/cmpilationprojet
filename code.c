lebsir;
#include <stdio.h>

int main() {
    int nombre = 7;           // Nombre déjà initialisé
    char nom[] = "Mohamed";   // Chaîne déjà initialisée
    char initiale = 'M';      // Caractère déjà initialisé

    // Boucle do...while pour vérifier que le nombre est positif
    do {
        if (nombre <= 0) {
          printf("Erreur ! Le nombre doit être positif.\n");
            nombre = 5; // On peut réinitialiser ici si besoin
        }
        
    } while ( nombre <= 0);

    // Vérification pair ou impair
    if (nombre % 2 == 0) {
        printf("Le nombre %d est pair.\n", nombre);
    } else {
        printf("Le nombre %d est impair.\n", nombre);
    }

    // Affichage de la chaîne et du caractère
    printf("Nom : %s\n", nom);
    printf("Initiale : %c\n", initiale);

    return 0;
}



