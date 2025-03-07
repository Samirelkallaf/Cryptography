package playfaircipher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PlayfairCipher {

    private static final int SIZE = 5; // 5x5 Matrix
    private char[][] matrix;
    
    public PlayfairCipher(String keyword) {
        matrix = generateMatrix(keyword);
    }

    // Step 1: Generate the 5x5 Playfair matrix
    private char[][] generateMatrix(String keyword) {
        Set<Character> seen = new LinkedHashSet<>();
        keyword = keyword.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");

        for (char ch : keyword.toCharArray()) {
            seen.add(ch);
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch != 'J' && !seen.contains(ch)) {
                seen.add(ch);
            }
        }

        char[][] matrix = new char[SIZE][SIZE];
        Iterator<Character> iterator = seen.iterator();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                matrix[row][col] = iterator.next();
            }
        }
        return matrix;
    }

    // Step 2: Print the matrix
    public void printMatrix() {
        System.out.println("Playfair Matrix:");
        for (char[] row : matrix) {
            for (char ch : row) {
                System.out.print(ch + " ");
            }
            System.out.println();
        }
    }

    // Step 3: Encrypt a plaintext message
    public String encrypt(String text) {
        return processText(text, true);
    }

    // Step 4: Decrypt a ciphertext message
    public String decrypt(String text) {
        return processText(text, false);
    }

    // Helper function to process text (encrypt/decrypt)
    private String processText(String text, boolean encrypt) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        List<String> pairs = getPairs(text);

        StringBuilder result = new StringBuilder();
        for (String pair : pairs) {
            char[] encryptedPair = processPair(pair.charAt(0), pair.charAt(1), encrypt);
            result.append(encryptedPair[0]).append(encryptedPair[1]);
        }
        return result.toString();
    }

    // Step 5: Process letter pairs based on Playfair cipher rules
    private char[] processPair(char a, char b, boolean encrypt) {
        int[] posA = findPosition(a);
        int[] posB = findPosition(b);
        int shift = encrypt ? 1 : -1;

        if (posA[0] == posB[0]) { // Same row
            return new char[]{matrix[posA[0]][(posA[1] + shift + SIZE) % SIZE], 
                              matrix[posB[0]][(posB[1] + shift + SIZE) % SIZE]};
        } else if (posA[1] == posB[1]) { // Same column
            return new char[]{matrix[(posA[0] + shift + SIZE) % SIZE][posA[1]], 
                              matrix[(posB[0] + shift + SIZE) % SIZE][posB[1]]};
        } else { // Rectangle swap
            return new char[]{matrix[posA[0]][posB[1]], matrix[posB[0]][posA[1]]};
        }
    }

    // Helper function to find position of a character in the matrix
    private int[] findPosition(char ch) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (matrix[row][col] == ch) {
                    return new int[]{row, col};
                }
            }
        }
        return null; // Should never happen
    }

    // Helper function to format text into pairs for Playfair processing
    private List<String> getPairs(String text) {
        List<String> pairs = new ArrayList<>();
        StringBuilder modifiedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            modifiedText.append(text.charAt(i));
            if (i < text.length() - 1 && text.charAt(i) == text.charAt(i + 1)) {
                modifiedText.append('X'); // Insert 'X' for duplicate letters
            }
        }

        if (modifiedText.length() % 2 != 0) {
            modifiedText.append('X'); // If odd length, append 'X'
        }

        for (int i = 0; i < modifiedText.length(); i += 2) {
            pairs.add(modifiedText.substring(i, i + 2));
        }
        return pairs;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Get the keyword
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();

        PlayfairCipher playfair = new PlayfairCipher(keyword);
        playfair.printMatrix();

        // Choose encryption or decryption
        System.out.print("Enter text to encrypt or decrypt: ");
        String inputText = scanner.nextLine();
        System.out.print("Choose operation (E for Encrypt, D for Decrypt): ");
        char choice = scanner.next().toUpperCase().charAt(0);

        if (choice == 'E') {
            System.out.println(" Encrypted Text: " + playfair.encrypt(inputText));
        } else if (choice == 'D') {
            System.out.println(" Decrypted Text: " + playfair.decrypt(inputText));
        } else {
            System.out.println("Invalid choice! Please enter E or D.");
        }

        scanner.close();

}

    
}
