package test.view;

import test.db.Datasource;
import test.model.SongArtist;

import java.util.List;

public class SqlInjectionExample {

    public static void main(String[] args) {

        Datasource datasource = Datasource.getInstance();

        datasource.openDBConnection();

        boolean result = datasource.createViewForSongArtists();
        System.out.println(result);

        // Example with SQL Injection
        List<SongArtist> injectionList = datasource.querySongInfoView("Go Your Own Way' OR 1 = 1 OR '");
        for (SongArtist songArtist : injectionList) {
            System.out.println("FROM VIEW - Artist name = " + songArtist.getArtist()
                    + " Album name = " + songArtist.getAlbum()
                    + " Track number = " + songArtist.getTrack());
        }

        datasource.closeDBConnection();
    }
}
