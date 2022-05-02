import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import static java.util.Collections.sort;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<Map<String, Integer>>> pdfIndex;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        this.pdfIndex = indexing(pdfsDir);
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> resultat = new ArrayList<>();

        for (String namePdf : pdfIndex.keySet()) {
            List<Map<String, Integer>> doc = pdfIndex.get(namePdf);

            for (int i = 0; i < doc.size(); i++) {

                if (doc.get(i).containsKey(word)) {
                    resultat.add(new PageEntry(namePdf, i + 1, doc.get(i).get(word)));
                }

            }
        }
        sort(resultat);
        return resultat;
    }

    public Map<String, List<Map<String, Integer>>> indexing(File dir) {
        Map<String, List<Map<String, Integer>>> pdfFiles = new HashMap<>();
        List<Map<String, Integer>> pageList = null;

        if (dir.isDirectory()) {
            for (File pdf : dir.listFiles()) {
                try (var doc = new PdfDocument(new PdfReader(pdf))) {
                    pageList = new ArrayList<>();

                    for (int i = 0; i < doc.getNumberOfPages(); i++) {
                        var page = doc.getPage(i + 1);
                        var text = PdfTextExtractor.getTextFromPage(page);
                        var words = text.split("\\P{IsAlphabetic}+");

                        Map<String, Integer> wordCount = new HashMap<>();
                        for (String word : words) {
                            word = word.toLowerCase();
                            if (wordCount.get(word) == null) {
                                wordCount.put(word, 1);
                            } else {
                                wordCount.put(word, (Integer) wordCount.get(word) + 1);
                            }
                        }

                        //System.out.println(wordCount.entrySet());

                        pageList.add(i, wordCount);
                    }
                } catch (IOException error) {
                    System.out.println(error.getMessage());
                }
                pdfFiles.put(pdf.getName(), pageList);
            }
        }

        return pdfFiles;
    }
}
