package Data;

import org.json.JSONArray;
import org.json.JSONException;

import gobind.Gobind;

public class GlobalData {
    private static String url, token;
    private static String[] Namespaces, Groups;

    public static void setToken(String token) {
        GlobalData.token = token;
    }

    public static void setUrl(String url) {
        GlobalData.url = url;
    }

    // Load namespaces
    public static boolean loadNamespaces() {
        String ns = Gobind.listNamespaces(url, token);
        if (ns.startsWith("[") && ns.endsWith("]")){
            try {
                JSONArray jarr = new JSONArray(ns);
                String[] items = new String[jarr.length()];
                for (int i = 0; i < jarr.length(); i++){
                    items[i] = jarr.getString(i);
                }
                Namespaces = items;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }

        return true;
    }

    public static boolean loadGroups() {
        return true;
    }

    public static boolean loadAll() {
        if (!loadNamespaces()) {
            return false;
        }
        return loadGroups();
    }

    public static String[] getNamespaces() {
        return Namespaces;
    }

    public static String[] getGroups() {
        return Groups;
    }
}
