import java.util.*;
import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class InvertedIndexing {
    private final List<Document> documents;
    private final TreeMap<String, TreeSet<Integer>> invertedIndexMatrix = new TreeMap<>();

    public InvertedIndexing(List<Document> documents) {
        this.documents = documents;
        buildInvertedIndex();
    }

    private void buildInvertedIndex() {
        for (int docID = 1; docID <= documents.size(); docID++) {
            Document document = documents.get(docID - 1);
            Set<String> words = new HashSet<>(TextTokenizer.tokenizeText(document.readDocument()));
            for (String word : words) {
                invertedIndexMatrix.computeIfAbsent(word, k -> new TreeSet<>()).add(docID);
            }
        }
    }

    public SortedSet<Integer> search(String searchTerm) {
        return invertedIndexMatrix.getOrDefault(searchTerm, new TreeSet<>());
    }

    public SortedSet<Integer> searchMultipleTerms(List<String> searchTerms) {
        List<SortedSet<Integer>> termResults = new ArrayList<>();
        for (String term : searchTerms) {
            termResults.add(new TreeSet<>(search(term)));
        }
        return mergingAlgorithm(termResults);
    }

    private SortedSet<Integer> mergingAlgorithm(List<SortedSet<Integer>> termResults) {
        if (termResults.isEmpty()) {
            return new TreeSet<>();
        }

        SortedSet<Integer> result = new TreeSet<>(termResults.get(0)); // Initialize with the first set

        for (int i = 1; i < termResults.size(); i++) {
            result.retainAll(termResults.get(i));
        }
        return result;
    }

    public void searchAndPrintResult() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of search terms: ");
        int numSearchTerms = scanner.nextInt();
        List<String> searchTerms = new ArrayList<>();
        scanner.nextLine();

        for (int i = 0; i < numSearchTerms; i++) {
            System.out.println("Enter search term" + (i + 1) + ": ");
            String term = scanner.nextLine();
            searchTerms.add(term);
        }

        SortedSet<Integer> combineResults = searchMultipleTerms(searchTerms);

        if (combineResults.isEmpty()) {
            System.out.println("No document found with the all specified terms");
        } else {
            System.out.println("Documents containing all the specified terms: " + combineResults);
        }
    }

    public void printInvertedIndexMatrix() {
        for (Map.Entry<String, TreeSet<Integer>> entry : invertedIndexMatrix.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            TreeSet<Integer> docIDs = entry.getValue();
            for (int docID : docIDs) {
                System.out.print(docID + " ");
            }
            System.out.println();
        }
    }

    public void createPDFInvertedIndex(String outputFilePath) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            int yPosition = (int) page.getMediaBox().getHeight() - 50;

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Inverted Index Matrix");
            contentStream.endText();

            yPosition -= 20;

            contentStream.setFont(PDType1Font.HELVETICA, 12);

            for (Map.Entry<String, TreeSet<Integer>> entry : invertedIndexMatrix.entrySet()) {
                String key = entry.getKey();
                TreeSet<Integer> docIDs = entry.getValue();

                StringBuilder row = new StringBuilder(key + ": ");
                for (int docID : docIDs) {
                    row.append(docID).append(" ");
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(row.toString());
                contentStream.endText();

                yPosition -= 20;
            }

            contentStream.close();

            document.save(outputFilePath);
            document.close();

            System.out.println("PDF created successfully at " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//    private SortedSet<Integer> intersection(List<SortedSet<Integer>> sets) {
//        int numSets = sets.size();
//        if (numSets == 0) {
//            return new TreeSet<>();
//        }
//
//        SortedSet<Integer> result = new TreeSet<>();
//        Iterator<Integer>[] iterators = new Iterator[numSets];
//
//        for (int i = 0; i < numSets; i++) {
//            iterators[i] = sets.get(i).iterator();
//        }
//
//        while (true) {
//            boolean foundInAll = true;
//            int min = Integer.MAX_VALUE;
//
//            if (!iterators[i].hasNext()) {
//                return result;
//            }
//            int current = iterators[i].next();
//            if (current < min) {
//                min = current;
//            }
//            if (current != min) {
//                foundInAll = false;
//            }
//        }
//
//        if (foundInAll) {
//            result.add(min);
//        }
//    }


