
public class PageEntry implements Comparable <PageEntry> {
    private final String pdfName;
    private final int page;
    private final Integer count;

    public PageEntry (String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }


    @Override
    public int compareTo(PageEntry pageEntry) {
        int result = this.count.compareTo(pageEntry.count);

        if (result == 0) {
            return this.pdfName.compareTo(pageEntry.pdfName);
        }

        if (result == 1) {
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return "Имя файла: " + pdfName + " номер страницы: " + page + " количество: " + count + "\n";
    }
}
