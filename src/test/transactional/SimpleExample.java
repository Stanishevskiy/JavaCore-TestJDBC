package test.transactional;

import test.db.Datasource;

public class SimpleExample {

    public static void main(String[] args) {

        Datasource datasource = Datasource.getInstance();

        datasource.openDBConnection();

        boolean result = datasource.queryAddArtistAlbumSong("Pixies", "Surfer Rosa", "Cactus Remastered");
//        boolean result = datasource.queryAddArtistAlbumSong("Pixies", "Surfer Rosa", "Test Song");
//        boolean result = datasource.queryAddArtistAlbumSong("Pixies", "Test Album", "Test Song");
//        boolean result = datasource.queryAddArtistAlbumSong("Test Artist", "Test Album", "Test Song");

        System.out.println("--------------------------------");
        System.out.println("Result of insert Rows: " + result);

        datasource.closeDBConnection();
    }
}
