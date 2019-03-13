package anaice.com.br.bringthepopcorn.data.db.dao;

import java.util.List;

import anaice.com.br.bringthepopcorn.data.db.entity.FavoriteMovie;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie")
    LiveData<List<FavoriteMovie>> getAll();

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    LiveData<FavoriteMovie> getFavoriteMovieById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMovie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FavoriteMovie movie);

    @Delete
    void delete(FavoriteMovie movie);
}
