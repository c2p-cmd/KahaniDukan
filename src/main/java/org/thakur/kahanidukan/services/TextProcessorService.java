package org.thakur.kahanidukan.services;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Service;
import org.thakur.kahanidukan.models.WordFrequency;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.function.Function;

@Service
public class TextProcessorService {
    private final SentenceDetectorME sentenceDetector;
    private final TokenizerME tokenizer;

    // Constructor initializes OpenNLP models
    public TextProcessorService() {
        try {
            // Load OpenNLP sentence detector model
            try (InputStream sentenceModelIn = getClass().getResourceAsStream("/models/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin")) {
                assert sentenceModelIn != null;
                SentenceModel sentenceModel = new SentenceModel(sentenceModelIn);
                this.sentenceDetector = new SentenceDetectorME(sentenceModel);
            }

            // Load tokenizer model
            try (InputStream tokenizerModelIn = getClass().getResourceAsStream("/models/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin")) {
                assert tokenizerModelIn != null;
                TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelIn);
                this.tokenizer = new TokenizerME(tokenizerModel);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load OpenNLP models", e);
        }
    }

    public List<WordFrequency> getWordFrequency(String text, int limit) {
        return calculateWordFrequency(text, limit);
    }

    private List<WordFrequency> calculateWordFrequency(String text, int limit) {
        final List<String> stopWords = List.of("the", "and", "is", "to", "in", "that", "it",
                "with", "as", "for", "on", "was", "of", "a", "an", "are", "be", "by", "this");
        final Pattern pattern = Pattern.compile("[^a-zA-Z]");

        final Map<String, Long> wordCount = Arrays
                .stream(text.toLowerCase().split(" "))
                .map(word -> pattern.matcher(word).replaceAll(""))
                .filter(word -> !word.isEmpty() && !stopWords.contains(word))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return wordCount.entrySet().stream()
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue().intValue()))
                .sorted(Comparator.comparingInt(WordFrequency::frequency).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Implements TextRank algorithm to summarize text
     * @param text The input text to summarize
     * @param sentenceCount Number of sentences to include in summary
     * @return The summarized text
     */
    public String summarizeText(String text, int sentenceCount) {
        // Detect sentences using OpenNLP
        String[] sentences = sentenceDetector.sentDetect(text);

        if (sentences.length <= sentenceCount) {
            return text; // Text is already short enough
        }

        // Create adjacency matrix for TextRank
        double[][] adjacencyMatrix = buildAdjacencyMatrix(sentences);

        // Apply TextRank algorithm
        double[] scores = textRank(adjacencyMatrix);

        // Select top sentences based on scores
        final Map<Integer, Double> sentenceScores = new HashMap<>();
        for (int i = 0; i < sentences.length; i++) {
            sentenceScores.put(i, scores[i]);
        }

        // Get indices of top-scoring sentences
        List<Integer> topSentenceIndices = sentenceScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(sentenceCount)
                .map(Map.Entry::getKey)
                .sorted()  // Sort by original position
                .toList();

        // Build the summary
        StringBuilder summary = new StringBuilder();
        for (Integer index : topSentenceIndices) {
            summary.append(sentences[index]).append(" ");
        }

        return summary.toString().trim();
    }

    private double[][] buildAdjacencyMatrix(String[] sentences) {
        int n = sentences.length;
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    matrix[i][j] = calculateSimilarity(sentences[i], sentences[j]);
                }
            }
        }

        return matrix;
    }

    private double calculateSimilarity(String sentence1, String sentence2) {
        // Tokenize sentences
        String[] tokens1 = tokenizer.tokenize(sentence1.toLowerCase());
        String[] tokens2 = tokenizer.tokenize(sentence2.toLowerCase());

        // Create word sets
        Set<String> set1 = new HashSet<>(Arrays.asList(tokens1));
        Set<String> set2 = new HashSet<>(Arrays.asList(tokens2));

        // Calculate intersection and union
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        // Calculate Jaccard similarity
        final double jaccardSimilarity = (double) intersection.size() / intersection.size();
        return union.isEmpty() ? 0 : jaccardSimilarity;
    }

    private double[] textRank(double[][] adjacencyMatrix) {
        final int n = adjacencyMatrix.length;
        final double d = 0.85; // Damping factor
        double[] scores = new double[n];
        Arrays.fill(scores, 1.0 / n); // Initialize scores

        // Run TextRank algorithm for 30 iterations or until convergence
        for (int iter = 0; iter < 30; iter++) {
            final double[] newScores = new double[n];

            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j && adjacencyMatrix[j][i] > 0) {
                        // Calculate outgoing edge weight sum for node j
                        double outSum = 0;
                        for (int k = 0; k < n; k++) {
                            if (j != k) {
                                outSum += adjacencyMatrix[j][k];
                            }
                        }

                        // Add weighted score
                        if (outSum > 0) {
                            sum += adjacencyMatrix[j][i] / outSum * scores[j];
                        }
                    }
                }

                newScores[i] = (1 - d) + d * sum;
            }

            // Update scores
            scores = newScores;
        }

        return scores;
    }
}