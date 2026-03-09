package filesystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the filesystem.File class
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class FileTest {

    @BeforeAll
    public static void BeforeAllTests() {
        // Stuff to run once before all the tests begin
    }

    @BeforeEach
    public void BeforeEachTest() {
        // Stuff to run before each test begins
    }

    @Test
    public void fileCreationTest() {
        File testFile = new File("myEpicMovie.mp4",2048,false);

        assertEquals("myEpicMovie.mp4",testFile.getName());
        assertEquals(2048,testFile.getSize());
        assertFalse(testFile.isWritable());
    }

    @Test
    public void illegalNameTest() {
        File testFile = new File("Adam&Eve");

        assertEquals("AdamEve",testFile.getName());
    }

    @Test
    public void enlargeTest() {
        File testFile = new File("toBeEnlarged",90,true);
        testFile.enlarge(10);
        assertEquals(100,testFile.getSize());
    }

    @Test
    public void shortenTest() {
        File testFile = new File("toBeShortened",110,true);
        testFile.shorten(10);
        assertEquals(100,testFile.getSize());
    }

    @Test
    public void overlapTest1() throws InterruptedException {
        File testFile1 = new File("FirstFile");
        File testFile2 = new File("SecondFile");
        testFile1.setCreateTime(new Date(0));
        testFile1.setModifyTime(new Date(0));
        testFile2.setCreateTime(new Date(50));
        testFile2.setModifyTime(new Date(70));

        assertFalse(testFile1.hasOverlappingUsePeriod(testFile2));
    }

    @Test
    public void overlapTest2() throws InterruptedException {
        File testFile1 = new File("FirstFile");
        File testFile2 = new File("SecondFile");
        testFile1.setCreateTime(new Date(0));
        testFile1.setModifyTime(new Date(100));
        testFile2.setCreateTime(new Date(50));
        testFile2.setModifyTime(new Date(70));

        assertTrue(testFile1.hasOverlappingUsePeriod(testFile2));
    }

    @Test
    public void overlapTest3() throws InterruptedException {
        File testFile1 = new File("FirstFile");
        File testFile2 = new File("SecondFile");
        testFile1.setCreateTime(new Date(0));
        testFile1.setModifyTime(new Date(20));
        testFile2.setCreateTime(new Date(50));
        testFile2.setModifyTime(new Date(70));

        assertFalse(testFile1.hasOverlappingUsePeriod(testFile2));
    }

    @Test
    public void setWritableTest() {
        File testFile = new File("myWritableFile");
        testFile.setWritable(false);

        assertFalse(testFile.isWritable());
    }

    @Test
    public void illegalWritingTest() {
        assertThrows(WriteException.class, () -> {
            File testFile = new File("mySafeFile",44,false);
            testFile.enlarge(50);
        });
    }

    @Test
    public void testConstructorDefault() {
        File testFile = new File("test.txt");

        assertEquals("test.txt",testFile.getName());
        assertEquals(0,testFile.getSize());
        assertTrue(testFile.isWritable());
        assertNull(testFile.getModifyTime());
        assertNotNull(testFile.getCreateTime());
    }
}
