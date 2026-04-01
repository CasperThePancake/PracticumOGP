import filesystem.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing suite for the filesystem package, testing all method cases
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class TestSuite {
    // =================================================================================
    // Before tests
    // =================================================================================

    @BeforeAll
    public static void BeforeAllTests() {
        // Stuff to run once before all the tests begin
    }

    @BeforeEach
    public void BeforeEachTest() {
        // Stuff to run before each test begins
    }

    // =================================================================================
    // Tests: File
    // =================================================================================
    @Test
    public void fileCreationTest() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"myEpicMovie",2048,false, FileExtension.PDF);

        assertEquals("myEpicMovie",testFile.getName());
        assertEquals(2048,testFile.getSize());
        assertEquals(FileExtension.PDF,testFile.getFileExtension());
        assertFalse(testFile.isWritable());
    }

    @Test
    public void illegalNameTest() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"Adam&Eve",FileExtension.TXT);

        assertEquals("AdamEve",testFile.getName());
    }

    @Test
    public void enlargeTest() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"toBeEnlarged",90,true,FileExtension.PDF);
        testFile.enlarge(10);
        assertEquals(100,testFile.getSize());
    }

    @Test
    public void shortenTest() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"toBeShortened",110,true,FileExtension.PDF);
        testFile.shorten(10);
        assertEquals(100,testFile.getSize());
    }

    @Test
    public void overlapTest1() throws InterruptedException {
        Directory rootDir = new Directory("root");
        File testFile1 = new File(rootDir,"FirstFile",FileExtension.JAVA);
        sleep(50);
        File testFile2 = new File(rootDir,"SecondFile",FileExtension.JAVA);
        sleep(50);
        testFile2.changeSize(10);

        assertFalse(testFile1.hasOverlappingUsePeriod(testFile2));
    }

    @Test
    public void overlapTest2() throws InterruptedException {
        Directory rootDir = new Directory("root");
        File testFile1 = new File(rootDir,"FirstFile",FileExtension.JAVA);
        sleep(50);
        File testFile2 = new File(rootDir,"SecondFile",FileExtension.JAVA);
        sleep(50);
        testFile2.changeSize(10);
        sleep(50);
        testFile1.changeSize(15);

        assertTrue(testFile1.hasOverlappingUsePeriod(testFile2));
    }

    @Test
    public void overlapTest3() throws InterruptedException {
        Directory rootDir = new Directory("root");
        File testFile1 = new File(rootDir,"FirstFile",FileExtension.JAVA);
        sleep(50);
        testFile1.changeSize(15);
        sleep(50);
        File testFile2 = new File(rootDir,"SecondFile",FileExtension.JAVA);
        sleep(50);
        testFile2.changeSize(10);

        assertFalse(testFile1.hasOverlappingUsePeriod(testFile2));
    }

    @Test
    public void setWritableTest() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"myWritableFile",FileExtension.PDF);
        testFile.setWritable(false);

        assertFalse(testFile.isWritable());
    }

    @Test
    public void illegalWritingTest() {
        assertThrows(WriteException.class, () -> {
            Directory rootDir = new Directory("root");
            File testFile = new File(rootDir,"mySafeFile",44,false,FileExtension.JAVA);
            testFile.enlarge(50);
        });
    }

    @Test
    public void testConstructorDefault() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"test",FileExtension.TXT);

        assertEquals("test",testFile.getName());
        assertEquals(0,testFile.getSize());
        assertTrue(testFile.isWritable());
        assertNull(testFile.getModifyTime());
        assertNotNull(testFile.getCreateTime());
    }

    @Test
    public void testDeleteFile1() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"test",FileExtension.TXT);
        testFile.delete();

        assertFalse(rootDir.hasAsItem(testFile));
    }

    @Test
    public void testDeleteFile2() {
        assertThrows(IllegalStateException.class, () -> {
            Directory rootDir = new Directory("root");
            File testFile = new File(rootDir,"testy",44,true,FileExtension.JAVA);
            testFile.delete();
            testFile.enlarge(50);
        });
    }

    @Test
    public void filePathTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");
        File testFile = new File(myDir,"test",FileExtension.TXT);

        assertEquals("/root/documents/test.txt",testFile.getAbsolutePath());
    }

    @Test
    public void defaultNameTest() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"",FileExtension.TXT);

        assertEquals("New-File",testFile.getName());
    }

    @Test
    public void fileDiskUsageCheck() {
        Directory rootDir = new Directory("root");
        File testFile = new File(rootDir,"",40,true,FileExtension.TXT);

        assertEquals(40,testFile.getTotalDiskUsage());
    }

    // =================================================================================
    // Tests: Directory
    // =================================================================================

    @Test
    public void directoryConstructorTest1() {
        Directory rootDir = new Directory("root");
        assertTrue(rootDir.isRoot());
        assertNull(rootDir.getParentDirectory());
    }

    @Test
    public void directoryConstructorTest2() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        assertEquals(rootDir,myDir.getParentDirectory());
        assertTrue(myDir.isWritable());
        assertEquals("documents",myDir.getName());
    }

    @Test
    public void directoryIllegalNameTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"docu.ments");

        assertEquals("documents",myDir.getName());
    }

    @Test
    public void makeRootTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        myDir.makeRoot();

        assertFalse(rootDir.hasAsItem(myDir));
        assertNull(myDir.getParentDirectory());
        assertTrue(myDir.isRoot());
    }

    @Test
    public void directoryAddItemTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(rootDir,"secret",FileExtension.TXT);

        myFile.move(myDir);

        assertFalse(rootDir.hasAsItem(myFile));
        assertTrue(myDir.hasAsItem(myFile));
    }

    @Test
    public void directoryNbItemsTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory("documents");

        File myFile1 = new File(rootDir,"secret1",FileExtension.TXT);
        File myFile2 = new File(rootDir,"secret2",FileExtension.TXT);
        File myFile3 = new File(rootDir,"secret3",FileExtension.TXT);
        File myFile4 = new File(rootDir,"secret4",FileExtension.TXT);

        myFile1.move(myDir);

        assertEquals(3,rootDir.getNbItems());
    }

    @Test
    public void illegalMoveTest1() {
        assertThrows(MoveException.class, () -> {
            Directory rootDir = new Directory("root");
            Directory myDir = new Directory(rootDir,"documents");
            Directory myLowerDir = new Directory(myDir,"OGP");

            myDir.move(myLowerDir);
        });
    }

    @Test
    public void illegalMoveTest2() {
        assertThrows(MoveException.class, () -> {
            Directory rootDir = new Directory("root");
            File myFile = new File(rootDir,"secret",FileExtension.TXT);
            Directory myDir = new Directory(rootDir,"documents");
            File mySameFile = new File(myDir,"secret",FileExtension.TXT);

            mySameFile.move(rootDir);
        });
    }

    @Test
    public void directoryContainsTest1() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(myDir,"secret",FileExtension.TXT);

        assertTrue(myDir.containsDiskItemWithName("secret"));
    }

    @Test
    public void directoryContainsTest2() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(myDir,"secret",FileExtension.TXT);

        assertEquals(myFile,myDir.getItem("secret"));
    }

    @Test
    public void directoryContainsTest3() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(myDir,"secret",FileExtension.TXT);

        assertNull(myDir.getItem("thisfiledoesnotexist"));
    }

    @Test
    public void directoryContainsTest4() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(myDir,"secret",FileExtension.TXT);

        assertEquals(myFile,myDir.getItemAt(1));
    }

    @Test
    public void directoryContainsTest5() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(myDir,"secret",FileExtension.TXT);
        File myOtherFile = new File(rootDir,"assignment",FileExtension.PDF);

        assertTrue(myDir.hasAsItem(myFile));
        assertFalse(myDir.hasAsItem(myOtherFile));
        assertTrue(rootDir.hasAsItem(myOtherFile));
        assertFalse(rootDir.hasAsItem(myFile));
    }

    @Test
    public void getRootTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");
        Directory myOtherDir = new Directory(myDir,"OGP");

        File myFile = new File(myOtherDir,"secret",FileExtension.TXT);

        assertEquals(rootDir,myFile.getRoot());
    }

    @Test
    public void directoryTotalDiskUsageTest() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        File myFile = new File(myDir,"secret",40,true,FileExtension.TXT);
        File myOtherFile = new File(rootDir,"assignment",60,true,FileExtension.PDF);

        assertEquals(100,rootDir.getTotalDiskUsage());
    }

    @Test
    public void directoryDeleteTest1() {
        assertThrows(DeleteException.class, () -> {
            Directory rootDir = new Directory("root");
            File myFile = new File(rootDir,"secret",FileExtension.TXT);

            rootDir.delete();
        });
    }

    @Test
    public void directoryDeleteTest2() {
        assertThrows(DeleteException.class, () -> {
            Directory rootDir = new Directory("root");
            rootDir.setWritable(false);

            rootDir.delete();
        });
    }

    @Test
    public void directoryDeleteTest3() {
        Directory rootDir = new Directory("root");
        Directory myDir = new Directory(rootDir,"documents");

        myDir.delete();

        assertTrue(myDir.isTerminated());
        assertFalse(rootDir.hasAsItem(myDir));
        assertEquals(0,rootDir.getNbItems());
    }

    @Test
    public void directoryDefaultNameTest() {
        Directory rootDir = new Directory("");

        assertEquals("New-Directory",rootDir.getName());
    }

    // =================================================================================
    // Tests: Link
    // =================================================================================

    @Test
    public void linkConstructorTest() {
        Directory rootDir = new Directory("root");
        File myFile = new File(rootDir,"secret",FileExtension.TXT);
        Directory myDir = new Directory("documents");
        Link myLink = new Link(myDir,"myLink",myFile);

        assertEquals(myFile,myLink.getLinkedItem());
    }

    @Test
    public void linkToLinkTest() {
        assertThrows(LinkException.class, () -> {
            Directory rootDir = new Directory("root");
            File myFile = new File(rootDir,"secret",FileExtension.TXT);
            Link fileLink = new Link(rootDir,"uselessLink",myFile);
            Directory myDir = new Directory("documents");

            Link myLink = new Link(myDir,"myLink",fileLink);
        });
    }

    @Test
    public void linkAbsolutePathTest() {
        Directory rootDir = new Directory("root");
        File myFile = new File(rootDir,"secret",FileExtension.TXT);
        Directory myDir = new Directory(rootDir,"documents");
        Link myLink = new Link(myDir,"cool_link",myFile);

        assertEquals("/root/documents/cool_link",myLink.getAbsolutePath());
    }

    @Test
    public void linkDiskUsageTest() {
        Directory rootDir = new Directory("root");
        File myFile = new File(rootDir,"secret",FileExtension.TXT);
        Directory myDir = new Directory("documents");
        Link myLink = new Link(myDir,"cool_link",myFile);

        assertEquals(0,myLink.getTotalDiskUsage());
    }

    @Test
    public void linkDeleteTest1() {
        assertThrows(DeleteException.class, () -> {
            Directory rootDir = new Directory("root");
            File myFile = new File(rootDir,"secret",FileExtension.TXT);
            Directory myDir = new Directory("documents");
            Link myLink = new Link(myDir,"cool_link",myFile);

            myLink.delete();

            myLink.delete();
        });
    }

    @Test
    public void linkDeleteTest2() {
        Directory rootDir = new Directory("root");
        File myFile = new File(rootDir,"secret",FileExtension.TXT);
        Directory myDir = new Directory("documents");
        Link myLink = new Link(myDir,"cool_link",myFile);

        myLink.delete();

        assertFalse(myDir.hasAsItem(myLink));
        assertEquals(0,myDir.getNbItems());
    }

    @Test
    public void linkInactiveTest() {
        Directory rootDir = new Directory("root");
        File myFile = new File(rootDir,"secret",FileExtension.TXT);
        Directory myDir = new Directory("documents");
        Link myLink = new Link(myDir,"cool_link",myFile);

        myFile.delete();

        assertNull(myLink.getLinkedItem());
    }

    @Test
    public void linkDefaultNameTest() {
        Directory rootDir = new Directory("root");
        File myFile = new File(rootDir,"secret",FileExtension.TXT);
        Directory myDir = new Directory("documents");
        Link myLink = new Link(myDir,"",myFile);

        assertEquals("New-Link",myLink.getName());
    }
}
