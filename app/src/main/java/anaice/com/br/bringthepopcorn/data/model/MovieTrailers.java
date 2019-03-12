package anaice.com.br.bringthepopcorn.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailers {

    private int id;

    @SerializedName("results")
    private List<MovieTrailer> trailers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

}
