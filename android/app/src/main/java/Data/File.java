package Data;

public class File {
    public String namespace;
    public String[] tags, groups;
    public String name,fileType;
    public boolean isPublic, isEncrypted;
    public int id;

    public static File[] parseFiles(String json) {
        File[] files = new File[0];

        return files;
    }
}
