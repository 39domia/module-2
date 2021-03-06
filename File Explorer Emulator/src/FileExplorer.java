import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class FileExplorer {
    Scanner sc = new Scanner(System.in);
    final String folderPath = "C:\\Baitap";
    final File folder = new File(folderPath);
    final String fileExtension = ".txt";
    final Pattern pt = Pattern.compile("[\\[\\]\\w\\-(){}.]{1,20}");
    boolean alreadyExecuted = false;

    private void mainMenu() {
        if (!alreadyExecuted) {
            makeSureExistFolder();
            alreadyExecuted = true;
        }
        while (true) {
            System.out.println("Welcome to File Explorer Emulator app");
            System.out.println("Menu");
            System.out.println("[F]ile\t[C]lose");
            char choice = validateChar("Press your choice:");
            switch (choice) {
                case 'f':
                    fileMenu();
                    break;
                case 'c':
                    if (yesNoQuestion("Exit program?")) {
                        System.out.println("Thanks for use, see you again!");
                        return;
                    }
                    break;
                default:
                    noFunction();
            }
        }
    }

    private void makeSureExistFolder() {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private boolean yesNoQuestion(String mess) {
        while (true) {
            System.out.println(mess);
            char choice = validateChar("[Y]es\t[N]o");
            switch (choice) {
                case 'y':
                    return true;
                case 'n':
                    return false;
                default:
                    noFunction();
            }
        }
    }

    private void noFunction() {
        System.err.println("We don't have that function, please try again");
        System.out.println("===========================================");
    }

    private char validateChar(String mess) {
        System.out.println(mess);
        try {
            return sc.nextLine().toLowerCase().charAt(0);
        } catch (Exception e) {
            System.err.println("Wrong input format, try again!");
            return validateChar(mess);
        }
    }

    private void fileMenu() {
        while (true) {
            System.out.println("File menu");
            System.out.println("[D]ir\t[S]earch\t[N]ew\t[C]opy\tD[e]lete\t[M]ove\t[B]ack");
            char choice = validateChar("Press your choice:");
            switch (choice) {
                case 'd':
                    dir();
                    break;
                case 's':
                    search();
                    break;
                case 'n':
                    newFile();
                    break;
                case 'c':
                    copy();
                    break;
                case 'e':
                    delete();
                    break;
                case 'm':
                    move();
                    break;
                case 'b':
                    return;
                default:
                    noFunction();
            }
        }
    }

    private void search() {
        String keyword = validateFileNameWithMessage("Enter file name to search:");
        boolean checkFound = false;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().contains(keyword)) {
                System.out.println(fileEntry.getName());
                checkFound = true;
            }
        }
        if (!checkFound) {
            System.out.println("File not found!");
        } else {
            System.out.println("===========================================");
            done();
        }
    }

    private void move() {
        String source = validateFileNameWithMessage("Enter file name to move:");
        if (checkExistFile(mapFileNameFull(source))) {
            String destination = validatePath("Enter the destination path to move the file:");
            try {
                if (!checkBackSlashChar(destination)) {
                    destination = destination.concat("\\");
                }
                destination = destination.concat(mapFileNameExtension(source));
                Files.copy(Paths.get(mapFileNameFull(source)), Paths.get(destination));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            File sourceFile = new File(mapFileNameFull(source));
            sourceFile.delete();
            done();
        } else {
            System.err.println("File do not exist!");
        }
    }

    private boolean checkBackSlashChar(String destination) {
        if (destination.charAt(destination.length() - 1) == '\\') {
            return true;
        } else {
            return false;
        }
    }

    private String validatePath(String mess) {
        System.out.println(mess);
        try {
            String dir = sc.nextLine();
            File file = new File(dir);
            if (!file.exists()) {
                throw new Exception();
            }
            return dir;
        } catch (Exception e) {
            System.err.println("Wrong format or folder not found!");
            return validatePath(mess);
        }
    }

    private void delete() {
        String fileName = validateFileNameWithMessage("Enter file name to delete:");
        if (checkExistFile(mapFileNameFull(fileName))) {
            File file = new File(mapFileNameFull(fileName));
            file.delete();
            done();
        } else {
            System.err.println("File do not exist!");
        }
    }

    private void copy() {
        String source = validateFileNameWithMessage("Enter file name to copy:");
        source = mapFileNameExtension(source);
        String destination = copyExistFileName(source);
        try {
            Files.copy(Paths.get(mapFileNameNoExtension(source)), Paths.get(mapFileNameNoExtension(destination)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        done();
    }

    private String copyExistFileName(String source) {
        File[] list = folder.listFiles();
        Set<String> names = new HashSet<>();
        for (File file : list) {
            names.add(file.getName());
        }
        int index = source.lastIndexOf(".");
        String name = source.substring(0, index);
        String ex = source.substring(index);
        int duplicate = 0;
        while (true) {
            String dup = "";
            if (duplicate == 1) {
                dup = " - Copy";
            } else if (duplicate > 1) {
                dup = " - Copy(" + duplicate + ")";
            }
            String newName = name + dup + ex;
            if (!names.contains(newName))
                return newName;
            duplicate++;
        }
    }

    private boolean checkExistFile(String source) {
        File file = new File(source);
        return file.exists();
    }

    private String mapFileNameFull(String fileName) {
        fileName = validateFileName(fileName);
        return folderPath.concat("\\").concat(fileName.concat(fileExtension));
    }

    private String mapFileNameFullMess(String mess) {
        String fileName = validateFileNameWithMessage(mess);
        return folderPath.concat("\\").concat(fileName.concat(fileExtension));
    }

    private String mapFileNameNoExtension(String fileName) {
        return folderPath.concat("\\").concat(fileName);
    }

    private String mapFileNameExtension(String fileName) {
        return fileName.concat(fileExtension);
    }

    private void newFile() {
        String fileName = mapFileNameFullMess("Enter the file name:");
        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()) {
            existFileMenu(fileName);
        } else {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        done();
    }

    private void done() {
        System.out.println("Executed successfully!");
        System.out.println("===========================================");
    }

    private void existFileMenu(String fileDir) {
        try {
            if (yesNoQuestion("This file already exists\nWant to overwrite it?")) {
                File file = new File(fileDir);
                file.delete();
                file.createNewFile();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void search1() {
        String keyword = validateFileNameWithMessage("Enter file name to search:");
        File[] matchingFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(keyword) && name.endsWith(fileExtension);
            }
        });
        if (matchingFiles.length == 0) {
            System.out.println("File not found!");
        } else {
            for (File file : matchingFiles) {
                System.out.println(file.getName());
            }
            System.out.println("===========================================");
            done();
        }
    }

    private void dir() {
        if (folder.listFiles().length == 0) {
            System.err.println("Empty folder!");
        } else {
            for (final File fileEntry : folder.listFiles()) {
                System.out.println(fileEntry.getName());
            }
        }
    }

    private String validateFileNameWithMessage(String mess) {
        System.out.println(mess);
        try {
            String folderName = sc.nextLine().trim();
            return validateFilNameString(folderName);
        } catch (Exception e) {
            System.err.println("Wrong file name format or too long!");
            return validateFileNameWithMessage(mess);
        }
    }

    private String validateFileName(String folderName) {
        try {
            folderName = folderName.trim();
            return validateFilNameString(folderName);
        } catch (Exception e) {
            System.err.println("Wrong file name format or too long!");
            return validateFileNameWithMessage(folderName);
        }
    }

    private String validateFilNameString(String folderName) throws Exception {
        if (pt.matcher(folderName).matches())
            return folderName;
        else
            throw new Exception();
    }

    public static void main(String[] args) {
        FileExplorer app = new FileExplorer();
        app.mainMenu();
    }
}
