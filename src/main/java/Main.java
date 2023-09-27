import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Document document1 = new Document(1, "/home/saten/Desktop/Docs/doc1.txt");
        Document document2 = new Document(2, "/home/saten/Desktop/Docs/doc2.txt");
        Document document3 = new Document(3, "/home/saten/Desktop/Docs/doc3.txt");
        Document document4 = new Document(4, "/home/saten/Desktop/Docs/doc4.txt");
        
        List<Document> documentList = new ArrayList<>();
        documentList.add(document1);
        documentList.add(document2);
        documentList.add(document3);
        documentList.add(document4);

        InvertedIndexing invertedIndex = new InvertedIndexing(documentList);
        invertedIndex.printInvertedIndexMatrix();
        invertedIndex.searchAndPrintResult();
        invertedIndex.createPDFInvertedIndex("/home/saten/Desktop/Docs/index.pdf");



    }
}