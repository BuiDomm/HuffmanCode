
package huffmancode;
import java.io.*;
import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    char data;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }
}

public class HuffmanEncoder {
    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "encoded.txt";

        try {
            // Step 1: Create a frequency table for characters in the input file
            Map<Character, Integer> frequencyTable = buildFrequencyTable(inputFile);

            // Step 2: Build a Huffman tree using a priority queue
            HuffmanNode root = buildHuffmanTree(frequencyTable);

            // Step 3: Generate Huffman codes
            Map<Character, String> huffmanCodes = generateHuffmanCodes(root, "");

            // Step 4: Write the character encodings to an output file
            writeHuffmanCodes(outputFile, huffmanCodes);

            System.out.println("Huffman codes written to " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Character, Integer> buildFrequencyTable(String inputFile) throws IOException {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                char character = (char) ch;
                frequencyTable.put(character, frequencyTable.getOrDefault(character, 0) + 1);
            }
        }
        return frequencyTable;
    }

    private static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyTable) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode mergedNode = new HuffmanNode('\0', left.frequency + right.frequency);
            mergedNode.left = left;
            mergedNode.right = right;
            priorityQueue.add(mergedNode);
        }

        return priorityQueue.poll();
    }

    private static Map<Character, String> generateHuffmanCodes(HuffmanNode root, String code) {
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodesRecursive(root, code, huffmanCodes);
        return huffmanCodes;
    }

    private static void generateHuffmanCodesRecursive(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }

        if (node.data != '\0') {
            huffmanCodes.put(node.data, code);
        }

        generateHuffmanCodesRecursive(node.left, code + "0", huffmanCodes);
        generateHuffmanCodesRecursive(node.right, code + "1", huffmanCodes);
    }

    private static void writeHuffmanCodes(String outputFile, Map<Character, String> huffmanCodes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("Character | Huffman Code\n");
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                writer.write(entry.getKey() + " | " + entry.getValue() + "\n");
            }
        }
    }
}
