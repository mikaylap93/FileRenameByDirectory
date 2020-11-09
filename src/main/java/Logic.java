import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ListIterator;

public class Logic {
    String home = System.getProperty("user.home");
    String mainDirectoryLocation = "/Users/mikaylapace/IdeaProjects/FileReNameByFolder/src/test/MainTestDirectory";
    ArrayList<File> masterSubDirectoriesList = new ArrayList<>();

    Logic() throws IOException {
        System.out.println(home);
        searchAllDirectoriesForFiles(mainDirectoryLocation);

    }

    public void searchAllDirectoriesForFiles(String directoryLocation) throws IOException {
    //Searches directory and renames and files in the directory,
        // throws subdirectories in a list and recalls the method on them.

        File[] directoryAndFileArray = new File(directoryLocation).listFiles();

        if (!(directoryAndFileArray == null)) {
            System.out.println("directoryAndFileArray is not null");
            for (int i = 0; i < directoryAndFileArray.length; i++) {

                if (directoryAndFileArray[i].isFile()) {
                    System.out.println("it's a file");
                    reNameFile(directoryAndFileArray[i],directoryLocation,i);

                } else if (directoryAndFileArray[i].isDirectory()) {
                    System.out.println("it's a directory");
                    masterSubDirectoriesList.add(directoryAndFileArray[i]);
                }

            }
            /*Fixes the concurrent editing problem.
            ITERATION MUST BE REMOVED BEFORE MAKING RECURSIVE CALL, or mem will overload
            */
            ListIterator<File> subDirectoriesIterator = masterSubDirectoriesList.listIterator();
            while(subDirectoriesIterator.hasNext()){
                String iterationFile = subDirectoriesIterator.next().getAbsolutePath();
                subDirectoriesIterator.remove();
                searchAllDirectoriesForFiles(iterationFile);

            }
        }
    }

    public void reNameFile(File file, String directoryLocation,int iteration) throws IOException {
        //Spells out file as string, if an extensions is available, renames file with matching extension

        String fileName = file.toString();
        int index = file.toString().lastIndexOf(".");

        if(index>0){
            String extension = fileName.substring(index+1);
            Path source = Paths.get(directoryLocation+File.separator+file.getName());
            Files.move(source, source.resolveSibling(""+file.getParentFile()+
                    File.separator+file.getParentFile().getName()+iteration+"."+extension));
        }

        else if(index == -1){
            Path source = Paths.get(directoryLocation+File.separator+file.getName());
            Files.move(source, source.resolveSibling(""+file.getParentFile()+
                    File.separator+file.getParentFile().getName()+iteration));
        }

    }

}

