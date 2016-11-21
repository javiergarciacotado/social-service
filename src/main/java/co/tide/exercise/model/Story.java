package co.tide.exercise.model;

public class Story {

    private int id;

    private int popularity;

    public Story(Builder builder) {
        id = builder.id;
        popularity = builder.popularity;
    }

    public Story() {}

    /**
     * In case of the app grows, to facilitate the creation of stories
     */
    public static class Builder {

        private int id;
        private int popularity;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withPopularity(int popularity) {
            this.popularity = popularity;
            return this;
        }

        public Story build() {
            return new Story(this);
        }
    }

    public int getPopularity() {
        return popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        return id == story.id && popularity == story.popularity;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + popularity;
        return result;
    }
}
