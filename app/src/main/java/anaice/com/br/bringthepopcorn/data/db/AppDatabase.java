package anaice.com.br.bringthepopcorn.data.db;

import android.content.Context;
import android.util.Log;

import anaice.com.br.bringthepopcorn.data.db.dao.FavoriteMovieDao;
import anaice.com.br.bringthepopcorn.data.db.entity.FavoriteMovie;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMovie.class},
          version = 1,
          exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "bringThePopcorn";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                 AppDatabase.class,
                                                 AppDatabase.DATABASE_NAME).build();
            }

        }

        Log.d(TAG, "Getting the database instance...");
        return sInstance;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();

}
