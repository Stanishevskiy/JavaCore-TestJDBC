package test.db;

import test.model.Album;
import test.model.Artist;
import test.model.Song;
import test.model.SongArtist;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    public static final String VIEW_TABLE_ARTIST_SONG = "artist_list";


    private static Datasource instance = null;
    private static Connection connection;

    private Datasource() {}

    public static Datasource getInstance() {
        if (instance == null) {
            instance = new Datasource();
        }
        return instance;
    }

    public Connection openDBConnection() {

        try {
            if ((connection == null) ||
                    (connection.isClosed())) {
                connection = DriverManager.getConnection(CONNECTION_STRING);
            }
        } catch (SQLException e) {
            System.out.println("Error open Connection:\n" + e.getMessage());
        }
        return connection;
    }

    public void closeDBConnection() {

        try {
            if ((connection != null) &&
                    (!connection.isClosed())) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error close Connection:\n" + e.getMessage());
        }
    }

    public List<Artist> getArtists() {

        List<Artist> listArtists = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS)) {

            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(COLUMN_ARTIST_ID));
                artist.setName(resultSet.getString(COLUMN_ARTIST_NAME));
                listArtists.add(artist);
            }
        } catch (SQLException e) {
            System.out.println("Error SELECT Artists operation:\n" + e.getMessage());
        }
        return listArtists;
    }

    public List<Album> getAlbums() {

        List<Album> listAlbums = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_ALBUMS)) {

            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getInt(COLUMN_ALBUM_ID));
                album.setName(resultSet.getString(COLUMN_ALBUM_NAME));
                album.setArtistId(resultSet.getInt(COLUMN_ALBUM_ARTIST));
                listAlbums.add(album);
            }
        } catch (SQLException e) {
            System.out.println("Error SELECT Albums operation:\n" + e.getMessage());
        }
        return listAlbums;
    }

    public List<Song> getSongs() {

        List<Song> listSongs = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_SONGS)) {

            while (resultSet.next()) {
                Song song = new Song();
                song.setId(resultSet.getInt(COLUMN_SONG_ID));
                song.setTrackId(resultSet.getInt(COLUMN_SONG_TRACK));
                song.setTrackTitle(resultSet.getString(COLUMN_SONG_TITLE));
                song.setAlbumId(resultSet.getInt(COLUMN_SONG_ALBUM));
                listSongs.add(song);
            }
        } catch (SQLException e) {
            System.out.println("Error SELECT Songs operation:\n" + e.getMessage());
        }
        return listSongs;
    }

    public boolean createViewForSongArtists() {

        try (Statement statement = connection.createStatement()) {

            return statement.execute("CREATE VIEW IF NOT EXISTS " + VIEW_TABLE_ARTIST_SONG
                    + " AS SELECT "
                    + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", "
                    + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS album, "
                    + TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", "
                    + TABLE_SONGS + "." + COLUMN_SONG_TITLE
                    + " FROM " + TABLE_SONGS
                    + " INNER JOIN " + TABLE_ALBUMS
                    + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID
                    + " INNER JOIN " + TABLE_ARTISTS
                    + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID
                    + " ORDER BY "
                    + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", "
                    + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", "
                    + TABLE_SONGS + "." + COLUMN_SONG_TRACK);

        } catch (SQLException e) {
            System.out.println("Error CREATE VIEW failed: " + e.getMessage());
            return false;
        }
    }

    public List<SongArtist> querySongInfoView(String title) {

        List<SongArtist> songArtistList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery("SELECT "
                     + COLUMN_ARTIST_NAME + ", "
                     + COLUMN_SONG_ALBUM + ", "
                     + COLUMN_SONG_TRACK
                     + " FROM " + VIEW_TABLE_ARTIST_SONG
                     + " WHERE " + COLUMN_SONG_TITLE + " = " + "'" + title + "'")) {

            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtist(results.getString(1));
                songArtist.setAlbum(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtistList.add(songArtist);
            }
            return songArtistList;
        } catch (SQLException e) {
            System.out.println("Error get List Song: " + e.getMessage());
            return songArtistList;
        }
    }

    public List<SongArtist> querySongInfoView2(String title) {

        List<SongArtist> songArtistList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT "
                + COLUMN_ARTIST_NAME + ", "
                + COLUMN_SONG_ALBUM + ", "
                + COLUMN_SONG_TRACK
                + " FROM " + VIEW_TABLE_ARTIST_SONG
                + " WHERE " + COLUMN_SONG_TITLE + " = ?")) {

            preparedStatement.setString(1, title);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtist(results.getString(1));
                songArtist.setAlbum(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtistList.add(songArtist);
            }
            results.close();
            return songArtistList;
        } catch (SQLException e) {
            System.out.println("Error get List Song: " + e.getMessage());
            return songArtistList;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Test Transaction
    //------------------------------------------------------------------------------------------------------------------
    public boolean queryAddArtistAlbumSong(String artist, String album, String song) {

        try (PreparedStatement artistCreationStmt = connection.prepareStatement(
                "INSERT INTO artists ("
                        + COLUMN_ARTIST_NAME +
                        ") VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement albumCreationStmt = connection.prepareStatement(
                    "INSERT INTO albums ("
                            + COLUMN_ALBUM_NAME + ", "
                            + COLUMN_ALBUM_ARTIST +
                            ") VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement songCreationStmt = connection.prepareStatement(
                    "INSERT INTO songs ("
                            + COLUMN_SONG_TRACK + ", "
                            + COLUMN_SONG_TITLE + ", "
                            + COLUMN_SONG_ALBUM +
                            ") VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)

            ) {

            connection.setAutoCommit(false);

            int artistQuantity = queryGetArtistQuantity();
            int artistId = querySelectArtistId(artist);
            int albumQuantity = queryGetAlbumsQuantity();
            int albumId = querySelectAlbumId(album, artistId);
            int songQuantity = queryGetTrackQuantity(albumId);
            int songId = querySelectSongId(song, albumId);
            int trackId = querySelectTrackId(song, albumId);;

            System.out.println("Artist Quantity: " + artistQuantity);
            System.out.println("Artist ID: " + artistId);
            System.out.println("Album Quantity: " + albumQuantity);
            System.out.println("Album ID: " + albumId);
            System.out.println("Song Quantity: " + songQuantity);
            System.out.println("Song ID: " + songId);
            System.out.println("Track ID: " + trackId);
            System.out.println("-------------------------------------");

            if (artistId == -1) {
                artistCreationStmt.setString(1, artist);
                int result = artistCreationStmt.executeUpdate();
                if (result == 1) {
                    artistId = artistQuantity + 1;
                }
            }
            if (albumId == -1) {
                trackId = 1;
                albumCreationStmt.setString(1, album);
                albumCreationStmt.setInt(2, artistId);
                int result = albumCreationStmt.executeUpdate();
                if (result == 1) {
                    albumId = albumQuantity + 1;
                }
            }
            if (songId == -1) {
                if (songQuantity == -1) {
                    songId = 1;
                    trackId = 1;
                } else if (songQuantity > 0) {
                    songId = 1;
                    trackId = songQuantity + 1;
                }
                songCreationStmt.setInt(1, trackId);
                songCreationStmt.setString(2, song);
                songCreationStmt.setInt(3, albumId);
                songCreationStmt.execute();
            }

            System.out.println("Artist Quantity: " + artistQuantity);
            System.out.println("Artist ID: " + artistId);
            System.out.println("Album Quantity: " + albumQuantity);
            System.out.println("Album ID: " + albumId);
            System.out.println("Song Quantity: " + songQuantity);
            System.out.println("Song ID: " + songId);
            System.out.println("Track ID: " + trackId);

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                System.out.println("Error rollback changes: " + e.getMessage());
            }
            System.out.println("Error adding artist/album/song field: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e2) {
                System.out.println("Error while reset auto-commit");
            }
        }
    }

    private int queryGetArtistQuantity() {

        try (PreparedStatement selectArtistStmt = connection.prepareStatement(
                "SELECT COUNT(" + COLUMN_ARTIST_ID + ") AS artists" +
                        " FROM " + TABLE_ARTISTS)) {

            ResultSet results = selectArtistStmt.executeQuery();
            int artistsCount = -1;
            if (results.next()) {
                artistsCount = results.getInt("artists");
            }

            results.close();
            return artistsCount;

        } catch (SQLException e) {
            System.out.println("Error retrieving Artists count: " + e.getMessage());
            return -1;
        }
    }


    private int querySelectArtistId(String artist) {

        try (PreparedStatement selectArtistStmt = connection.prepareStatement(
                "SELECT " + COLUMN_ARTIST_ID +
                        " FROM " + TABLE_ARTISTS +
                        " WHERE " + COLUMN_ARTIST_NAME + " = ?")) {

            selectArtistStmt.setString(1, artist);
            ResultSet results = selectArtistStmt.executeQuery();
            int artistId = -1;
            if (results.next()) {
                artistId = results.getInt(COLUMN_ARTIST_ID);
            }

            results.close();
            return artistId;

        } catch (SQLException e) {
            System.out.println("Error retrieving Artist Id: " + e.getMessage());
            return -1;
        }
    }

    private int queryGetAlbumsQuantity() {

        try (PreparedStatement selectAlbumStmt = connection.prepareStatement(
                "SELECT COUNT(" + COLUMN_ALBUM_ID + ") AS albums" +
                        " FROM " + TABLE_ALBUMS)) {

            ResultSet results = selectAlbumStmt.executeQuery();
            int albumsCount = -1;
            if (results.next()) {
                albumsCount = results.getInt("albums");
            }

            results.close();
            return albumsCount;

        } catch (SQLException e) {
            System.out.println("Error retrieving Albums count: " + e.getMessage());
            return -1;
        }
    }


    private int querySelectAlbumId(String album, int artistId) {

        try (PreparedStatement selectAlbumStmt = connection.prepareStatement(
                "SELECT " + COLUMN_ALBUM_ID +
                        " FROM " + TABLE_ALBUMS +
                        " WHERE " + COLUMN_ALBUM_NAME + " = ?"
                        + " AND " + COLUMN_ALBUM_ARTIST + " = ?")) {

            selectAlbumStmt.setString(1, album);
            selectAlbumStmt.setInt(2, artistId);
            ResultSet results = selectAlbumStmt.executeQuery();
            int albumsId = -1;
            if (results.next()) {
                albumsId = results.getInt(COLUMN_ALBUM_ID);
            }

            results.close();
            return albumsId;

        } catch (SQLException e) {
            System.out.println("Error retrieving Album Id: " + e.getMessage());
            return -1;
        }
    }

    private int queryGetTrackQuantity(int albumId) {

        try (PreparedStatement selectAlbumStmt = connection.prepareStatement(
                "SELECT COUNT(" + COLUMN_SONG_ID + ") AS songs" +
                        " FROM " + TABLE_SONGS +
                        " WHERE " + COLUMN_SONG_ALBUM + " = ?" +
                        " GROUP BY " + COLUMN_SONG_ALBUM)) {

            selectAlbumStmt.setInt(1, albumId);
            ResultSet results = selectAlbumStmt.executeQuery();
            int trackCount = -1;
            if (results.next()) {
                trackCount = results.getInt("songs");
            }

            results.close();
            return trackCount;

        } catch (SQLException e) {
            System.out.println("Error retrieving Songs count: " + e.getMessage());
            return -1;
        }
    }

    private int querySelectTrackId(String song, int albumId) {

        try (PreparedStatement selectAlbumStmt = connection.prepareStatement(
                "SELECT " + COLUMN_SONG_TRACK +
                        " FROM " + TABLE_SONGS +
                        " WHERE " + COLUMN_SONG_TITLE + " = ?"
                        + " AND " + COLUMN_SONG_ALBUM + " = ?")) {

            selectAlbumStmt.setString(1, song);
            selectAlbumStmt.setInt(2, albumId);
            ResultSet results = selectAlbumStmt.executeQuery();
            int trackId = -1;
            if (results.next()) {
                trackId = results.getInt(COLUMN_SONG_TRACK);
            }

            results.close();
            return trackId;

        } catch (SQLException e) {
            System.out.println("Error retrieving Track Id: " + e.getMessage());
            return -1;
        }
    }

    private int querySelectSongId(String song, int albumId) {

        try (PreparedStatement selectAlbumStmt = connection.prepareStatement(
                "SELECT " + COLUMN_SONG_ID +
                        " FROM " + TABLE_SONGS +
                        " WHERE " + COLUMN_SONG_TITLE + " = ?"
                        + " AND " + COLUMN_SONG_ALBUM + " = ?")) {

            selectAlbumStmt.setString(1, song);
            selectAlbumStmt.setInt(2, albumId);
            ResultSet results = selectAlbumStmt.executeQuery();
            int songId = -1;
            if (results.next()) {
                songId = results.getInt(COLUMN_SONG_ID);
            }

            results.close();
            return songId;

        } catch (SQLException e) {
            System.out.println("Error retrieving Song Id: " + e.getMessage());
            return -1;
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}
