package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Data.GlobalData;
import gobind.Gobind;

public class File {
    public String namespace, encryption, creationDate;
    public String[] tags, groups;
    public String name, publicName, fileType;
    public boolean isPublic;
    public long id;
    public long size;

    public static File[] parseFiles(String json) {
        try {
            JSONArray arr = new JSONArray(json);

            File[] files = new File[arr.length()];
            for(int i = 0; i < arr.length(); i++){
                  File f = new File();
                  JSONObject fileObj = arr.getJSONObject(i);
                  f.id = fileObj.getLong("id");
                  f.size = fileObj.getLong("size");
                  f.name = fileObj.getString("name");
                  f.isPublic = fileObj.getBoolean("isPub");
                  f.publicName = fileObj.getString("pubname");
                  f.encryption = fileObj.getString("e");
                  f.creationDate = fileObj.getString("creation");
                  files[i] = f;
            }

            return files;
        } catch (JSONException e) {
            e.printStackTrace();
            return new File[]{};
        }
    }

    public static File[] getFiles(String name, String namespace, String[] groups, String[] tags, int id){
        return parseFiles(Gobind.listFiles(GlobalData.token, GlobalData.url, name, namespace, join(groups,","), join(tags, ","), id));
    }

    public static String join(String[] arr, String sep){
        if (arr == null){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s).append(sep);
        }

        if (sb.length()> 0){
            return sb.toString().substring(0,sb.length()-1);
        }
        return "";
    }
}
