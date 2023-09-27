import java.io.*;

public class Document {
    private final int id;
    private final String documentPath;

    public Document(int id, String documentPath) {
        this.id = id;
        this.documentPath = documentPath;
    }

    public int getId() {
        return id;
    }

    public String readDocument() {
        StringBuilder documentText = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(documentPath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                documentText.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentText.toString();
    }

}
