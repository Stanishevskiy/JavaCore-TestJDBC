package test.statement;

import test.db.Datasource;
import test.model.Album;
import test.model.Artist;
import test.model.Song;

import java.util.List;

public class SimpleExample {

    public static void main(String[] args) {

        Datasource datasource = Datasource.getInstance();

        datasource.openDBConnection();

        List<Artist> listArtists = datasource.getArtists();
        for (Artist artist : listArtists) {
            System.out.println("Id: " + artist.getId() + ", Name: " + artist.getName());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        List<Album> listAlbums = datasource.getAlbums();
        for (Album album : listAlbums) {
            System.out.println("Id: " + album.getId() + ", Name: " + album.getName() + ", Artist Id: " + album.getArtistId());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        List<Song> listSongs = datasource.getSongs();
        for (Song song : listSongs) {
            System.out.println("Id: " + song.getId() + ", Track Id: " + song.getTrackId()
                    + ", Track Title: " + song.getTrackTitle() + ", Album Id: " + song.getAlbumId());
        }
        System.out.println("-----------------------------------------------------------------------------------------");

        datasource.closeDBConnection();
    }



}
