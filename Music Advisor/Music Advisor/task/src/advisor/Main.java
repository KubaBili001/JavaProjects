package advisor;

import java.util.Objects;
import java.util.Scanner;

        //https://accounts.spotify.com/authorize?client_id=5aaada315f4641a8adc272bfdee529fd&redirect_uri=http://localhost:8080&response_type=code
        // nie uzywać ? https://accounts.spotify.com/api/token?client_id=5aaada315f4641a8adc272bfdee529fd&redirect_uri=http://localhost:8080&response_type=code

public class Main {

    private static final Scanner input = new Scanner(System.in);
    protected static String authorizationServerUrl = "https://accounts.spotify.com";
    protected static String apiServerUrl = "https://api.spotify.com";
    protected static int instancesPerPage = 5;
    protected static boolean isAuthorized = false;
    public static void main(String[] args) throws InterruptedException {

        String userInput = "";

        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-access")) {
                    authorizationServerUrl = args[i+1];
                } else if (args[i].equals("-resource")) {
                    apiServerUrl = args[i+1];
                } else if (args[i].equals("-page")){
                    instancesPerPage = Integer.parseInt(args[i+1]);
                }
            }
        }

        while(!Objects.equals(userInput.split(" ")[0], "exit")){

            userInput = input.nextLine();
            String userCommand = userInput.split(" ")[0];

            if (Objects.equals(userCommand, "exit")) {
                log("---GOODBYE!---");
                break;
            }

            if (!isAuthorized) {

                if (Objects.equals(userCommand, "auth")) {

                    log("use this link to request the access code:");
                    log(authorizationServerUrl
                            + "/authorize?"
                            + "client_id=" + Controller.CLIENT_ID
                            + "&redirect_uri=" + Controller.REDIRECT_URI
                            + "&response_type=code");

                    Controller.getAuthCode();
                    Controller.accessSpotify();

                    Controller.CATEGORY = "categories";
                    Controller.accessCategory();
                    Controller.CATEGORY = "";

                } else {
                    log("Please, provide access for application.");
                }

            } else {
                switch (userCommand) {
                    case "featured"   -> {
                        Controller.CATEGORY = "featured-playlists";
                        Controller.accessCategory();
                    }
                    case "new"        -> {
                        Controller.CATEGORY = "new-releases";
                        Controller.accessCategory();
                    }
                    case "playlists"  -> {
                        String key = "";
                        for (int i = 1; i < userInput.split(" ").length; i++){
                            if (i != userInput.split(" ").length - 1)
                                key += userInput.split(" ")[i] + " ";
                            else
                                key += userInput.split(" ")[i];
                        }
                        Controller.CATEGORY = "categories/"+View.categoriesMap.get(key)+"/playlists";
                        Controller.accessCategory();
                    }
                    case "categories" -> {
                        Controller.CATEGORY = "categories";
                        View.printView("",View.chooseList());
                    }
                    case "next" -> {
                        if (!Objects.equals(Controller.CATEGORY, ""))
                            View.printView("next",View.chooseList());
                        else
                            log("No more pages.");
                    }
                    case "prev" -> {
                        if (!Objects.equals(Controller.CATEGORY, ""))
                            View.printView("prev",View.chooseList());
                        else
                            log("No more pages.");
                    }
                    default -> log("Wrong input, try again.");
                }
            }
        }
    }
    public static void log(String str){
        System.out.println(str);
    }
}
