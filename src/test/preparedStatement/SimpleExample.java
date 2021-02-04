package test.preparedStatement;

import test.db.Datasource;
import test.model.SongArtist;

import java.util.List;

public class SimpleExample {

    public static void main(String[] args) {

        Datasource datasource = Datasource.getInstance();

        datasource.openDBConnection();

        List<SongArtist> songArtistList = datasource.querySongInfoView2("Go Your Own Way");
        for (SongArtist songArtist : songArtistList) {
            System.out.println("FROM VIEW - Artist name = " + songArtist.getArtist()
                    + " Album name = " + songArtist.getAlbum()
                    + " Track number = " + songArtist.getTrack());
        }

        datasource.closeDBConnection();
    }
}
