package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class View {
    protected static HashMap<String,String> categoriesMap = new HashMap<>();
    protected static ArrayList<ArrayList<String>> newList = new ArrayList<>();
    protected static ArrayList<ArrayList<String>> featuredList = new ArrayList<>();
    protected static ArrayList<ArrayList<String>> categoriesList = new ArrayList<>();
    protected static ArrayList<ArrayList<String>> playlistsList = new ArrayList<>();
    private static int listCounterMin = 0;
    private static int listCounterMax = 0;
    private static int pageCounter = 1;
    private static int maxPageCounter = 0;

    public static String parseToken(String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        return json.get("access_token").getAsString();
    }

    public static void parseNew(String body) {

        if (View.newList.isEmpty()) {

            JsonObject test = JsonParser.parseString(body).getAsJsonObject().get("albums").getAsJsonObject();
            JsonArray items = test.get("items").getAsJsonArray();

            for (JsonElement item : items) {

                ArrayList<String> artists = new ArrayList<>();
                JsonObject album = item.getAsJsonObject();

                String albumName = album.get("name").getAsString();
                String url = album.get("external_urls").getAsJsonObject().get("spotify").getAsString();

                album.get("artists").getAsJsonArray().forEach(artist -> {
                    artists.add(artist.getAsJsonObject().get("name").getAsString());
                });

                newList.add(new ArrayList<>(Arrays.asList(albumName, String.valueOf(artists), url + "\n")));

            }
        }
        printView("",chooseList());
    }

    public static void parseCategories(String body){

        if(View.categoriesList.isEmpty()) {
            JsonObject test = JsonParser.parseString(body).getAsJsonObject().get("categories").getAsJsonObject();
            JsonArray items = test.get("items").getAsJsonArray();

            for (JsonElement item : items) {

                JsonObject category = item.getAsJsonObject();

                String categoryId = category.get("id").getAsString();
                String categoryName = category.get("name").getAsString();

                categoriesMap.put(categoryName, categoryId);

                categoriesList.add(new ArrayList<>(Arrays.asList(categoryName)));
            }
        }
    }

    public static void parsePlaylists(String body) {

        if (Objects.equals(Controller.CATEGORY, "featured-playlists")) {
            featuredList.clear();
        } else {
            playlistsList.clear();
        }

        JsonObject test = JsonParser.parseString(body).getAsJsonObject().get("playlists").getAsJsonObject();
        JsonArray items = test.get("items").getAsJsonArray();

        for (JsonElement item : items) {

            JsonObject playlist = item.getAsJsonObject();

            String playlistUrl = playlist.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            String playlistName = playlist.get("name").getAsString();


            if (Objects.equals(Controller.CATEGORY, "featured-playlists")) {
                featuredList.add(new ArrayList<>(Arrays.asList(playlistName,playlistUrl+"\n")));
            } else {
                playlistsList.add(new ArrayList<>(Arrays.asList(playlistName,playlistUrl+"\n")));
            }
        }

        printView("",chooseList());
    }

    public static void printView(String argument, ArrayList<ArrayList<String>> list) {
        if ((pageCounter != maxPageCounter && Objects.equals(argument, "next")) ||
           (Objects.equals(argument, "prev") && pageCounter!=1) ||
           (Objects.equals(argument, ""))) {

            if (Objects.equals(argument, "")) {
                maxPageCounter=calcMaxPage(list);
                pageCounter = 1;
                listCounterMin = 0;
                listCounterMax = Main.instancesPerPage;
            } else if (Objects.equals(argument, "next")) {
                if (listCounterMax + Main.instancesPerPage > list.size()) {
                    listCounterMax = list.size();
                } else {
                    listCounterMax += Main.instancesPerPage;
                }
                listCounterMin += Main.instancesPerPage;
                pageCounter++;
            } else {
                if (listCounterMax == list.size()) {
                    listCounterMax = maxPageCounter * Main.instancesPerPage - Main.instancesPerPage;
                    listCounterMin = listCounterMax - 5;
                } else {
                    listCounterMin -= Main.instancesPerPage;
                    listCounterMax -= Main.instancesPerPage;
                }
                pageCounter--;
            }

            for (int i = listCounterMin; i < listCounterMax; i++) {
                for (int j = 0; j < list.get(i).size(); j++) {
                    log(list.get(i).get(j));
                }
            }

            log("---PAGE " + pageCounter + " OF " + maxPageCounter + "---");
        } else {
            log("No more pages.");
        }
    }

    public static ArrayList<ArrayList<String>> chooseList(){
        switch (Controller.CATEGORY){
            case "new-releases" -> {
                return newList;
            }
            case "categories" -> {
                return categoriesList;
            }
            case "featured-playlists" -> {
                return featuredList;
            }
            default -> {
                return playlistsList;
            }
        }
    }
    public static int calcMaxPage(ArrayList<ArrayList<String>> list){
        int listSize = list.size();
        int var = 0;
        while(listSize>0){
            listSize-=Main.instancesPerPage;
            var++;
        }
        return var;
    }

    public static void log(String str){
        System.out.println(str);
    }
}
