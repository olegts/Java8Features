package javaday.other;

import org.junit.Test;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Oleg Tsal-Tsalko
 */
public class FilesOperations {

    @Test
    public void showsHowEasilyGetFileBufferedReader() throws Exception {
        BufferedReader reader = Files.newBufferedReader(Paths.get("resources/MASTER_BOOK_DATA.csv"));
        reader.lines().forEach(System.out::println);
    }

    /**
     * If your file not huge and fits in memory probably the most convenient way to read a file
     */
    @Test
    public void showsHowEasilyGetAllLinesFromFile() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/MASTER_BOOK_DATA.csv"));
        lines.stream().forEach(System.out::println);
    }

    /**
     * More convenient version of previous method which can be used both on small and huge files
     */
    @Test
    public void showsHowToGetStreamOfLinesFromFile() throws Exception {
        Stream<String> lines = Files.lines(Paths.get("resources/MASTER_BOOK_DATA.csv"));
        lines.forEach(System.out::println);
    }

    @Test
    public void showsHowToWriteCharactersIntoFile() throws Exception {
        String fileContent = "This is file content we want to write in file";
        Path path = Paths.get("resources/MASTER_BOOK_DATA_COPY.csv");
        Files.write(path, singletonList(fileContent));

        assertTrue(Files.exists(path));
        assertThat(Files.readAllLines(path).get(0), equalTo(fileContent));
    }

    @Test
    public void shouldListOnlyOneLevelDepthSubDirectories() throws Exception {
        Stream<Path> dirContent = Files.list(Paths.get("/Users/oleg/dev/Projects"));
        dirContent.forEach(System.out::println);
    }

    /**
     * General purpose recursive traversal of your file system
     */
    @Test
    public void shouldTraverseWholeFileSystemStructureRecursively() throws Exception {

        Stream<Path> dirWholeRecursiveContent = Files.walk(Paths.get("/Users/oleg/dev/Projects"));

        System.out.println("There are in total "
                + dirWholeRecursiveContent.filter(p -> p.toString().endsWith(".java")).count()
                + " Java classes!");
    }

    /**
     * More convenient and efficient method to traverse your file system if you want to filter particular directories/files
     */
    @Test
    public void shouldLookForAllJavaFilesRecursively() throws Exception {

        Stream<Path> javaFilesWalker = Files.find(Paths.get("/Users/oleg/dev/Projects"), Integer.MAX_VALUE,
                (p, a) -> p.toString().endsWith(".java"));

        System.out.println("There are in total "
                + javaFilesWalker.count()
                + " Java classes!");
    }
}
