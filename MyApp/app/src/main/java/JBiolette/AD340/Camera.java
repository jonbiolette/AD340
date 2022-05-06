package JBiolette.AD340;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Camera {
    static ArrayList<String> dataList = new ArrayList<>();
    static String[][] dataset;
    public static void createArray(JSONArray arr) throws JSONException {
        dataset = new String[arr.length()][4];
        for (int i = 0; i < arr.length(); i++) {
            JSONObject camera = arr.getJSONObject(i);
            String[] caminfo = camera.getJSONArray("Cameras").toString().split(":");
            dataset[i][0] = caminfo[1].substring(0, (caminfo[1].length() - 14));;
            dataset[i][1] = caminfo[2].substring(0, (caminfo[2].length() - 11));;
            dataset[i][2] = caminfo[3].substring(0, (caminfo[3].length() - 7));;
            dataset[i][3] = caminfo[4].substring(0, (caminfo[4].length() - 2));;

        }
    }
    public static String getOneInfo(int index, int type){
        return dataset[index][type];
    }
    public static String[][] getAllInfo(){
        return dataset;
    }
    public static ArrayList<String> getAllSelectedInfo(int type){
        //String[][] selectedDataSet = new String[dataset.length][1];
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i < dataset.length; i++){
            arrayList.add(dataset[i][type]);
        }
        return arrayList;
    }
}

