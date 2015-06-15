package javaday.lambdas.usage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * @author Oleg Tsal-Tsalko
 */
public class ProcessingCsvFiles {

    static final String MASTER_BOOK_DATA = "MASTER_BOOK_DATA.csv";
    static final String TRANSACTIONAL_DATA = "TRANSACTIONAL_DATA.csv";

    public static void main(String[] args) throws FileNotFoundException {

        Map<String, List<BookInfo>> masterBookData = new HashMap<>();

        Scanner scanner = new Scanner(ProcessingCsvFiles.class.getClassLoader().getResourceAsStream(MASTER_BOOK_DATA));
        while (scanner.hasNextLine()) {
            String[] bookDetails = scanner.nextLine().split(",");
            BookInfo book = new BookInfo(Integer.parseInt(bookDetails[0]), bookDetails[1], bookDetails[2], ("1".equals(bookDetails[3])));
            List<BookInfo> books = masterBookData.getOrDefault(book.name, new ArrayList<>());
            books.add(book);
            masterBookData.put(book.name, books);
        }

        Map<String, Book> existingBooks = readTransactionalData(TRANSACTIONAL_DATA);

        List<Book> missingBooks = new ArrayList<>();
        List<Book> activeBooks = new ArrayList<>();
        List<Book> expiredBooks = new ArrayList<>();
        for (Book book : existingBooks.values()) {
            List<BookInfo> books = masterBookData.get(book.name);
            if (books == null) {
                missingBooks.add(book);
            } else if (!isExpired(books)) {
                book.setSources(books.stream()
                        .filter(bookInfo -> !bookInfo.isExpired)
                        .map(b -> b.source)
                        .collect(joining("/")));
                activeBooks.add(book);
            } else {
                expiredBooks.add(book);
            }
        }
        System.out.println("===========================================================================");
        System.out.println("Missing bookNames:");
        System.out.println("===========================================================================");
        missingBooks.stream().map(book -> book.name + "," + book.receiveDate).forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Total: " + missingBooks.size());
        System.out.println("===========================================================================");
        System.out.println("Active bookNames:");
        System.out.println("===========================================================================");
        activeBooks.stream().map(book -> book.name + "," + book.sources + "," + book.receiveDate).forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Total: " + activeBooks.size());
        System.out.println("===========================================================================");
        System.out.println("Expired bookNames:");
        System.out.println("===========================================================================");
        expiredBooks.stream().map(book -> book.name).forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Total: " + expiredBooks.size());
    }

    private static Map<String, Book> readTransactionalData(String transactionalDataFile) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(ProcessingCsvFiles.class.getClassLoader().getResource(transactionalDataFile).getFile()));
        return reader.lines().map(ProcessingCsvFiles::toBook).collect(Collectors.toMap(book -> book.name, b->b));
    }

    private static boolean isExpired(List<BookInfo> books) {
        return books.stream().allMatch(BookInfo::isExpired);
    }

    public static Book toBook(String line) {
        String[] book = line.split(",");
        return new Book(book[0], LocalDate.parse(book[1], DateTimeFormatter.ofPattern("M/d/y")));
    }

    static class Book {

        public final String name;
        public final LocalDate receiveDate;
        public String sources;

        Book(String name, LocalDate receiveDate) {
            this.name = name;
            this.receiveDate = receiveDate;
        }

        void setSources(String sources) {
            this.sources = sources;
        }

    }

    static class BookInfo {
        public final int id;
        public final String name;
        public final String source;
        public final boolean isExpired;

        public boolean isExpired() {
            return isExpired;
        }

        BookInfo(int id, String name, String source, boolean expired) {
            this.id = id;
            this.name = name;
            this.source = source;
            isExpired = expired;
        }
    }
}
