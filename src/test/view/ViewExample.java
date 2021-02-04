package test.view;

import test.db.Datasource;
import test.model.SongArtist;

import java.util.List;

public class ViewExample {

    public static void main(String[] args) {

        Datasource datasource = Datasource.getInstance();

        datasource.openDBConnection();

        boolean result = datasource.createViewForSongArtists();
        System.out.println(result);

        List<SongArtist> songArtistList = datasource.querySongInfoView("Heartless");
        for (SongArtist songArtist : songArtistList) {
            System.out.println("FROM VIEW - Artist name = " + songArtist.getArtist()
                    + " Album name = " + songArtist.getAlbum()
                    + " Track number = " + songArtist.getTrack());
        }

        datasource.closeDBConnection();
    }
}
