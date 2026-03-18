import filesystem.Directory;
import filesystem.File;
import filesystem.FileExtension;

/**
 * Main testing area
 */
void main() {
    Directory myRoot = new Directory("myRoot");
    Directory myDocuments = new Directory(myRoot, "Documents");
    Directory mySchool = new Directory(myDocuments, "School");
    Directory myOGP = new Directory(mySchool, "OGP-Practicum");
    File myFile = new File(myOGP, "CoolClass", FileExtension.JAVA);

    System.out.println(myFile.getAbsolutePath());
}
