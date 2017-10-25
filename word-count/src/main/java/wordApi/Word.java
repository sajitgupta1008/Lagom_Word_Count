package wordApi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

@JsonDeserialize
public final class Word {

    @JsonProperty("message")
   public final String message;

    @JsonCreator
    public Word(@JsonProperty("message") String message) {
        this.message = Preconditions.checkNotNull(message, "message");
    }

    @Override
    public boolean equals(@Nullable Object another) {
        if (this == another)
            return true;
        return another instanceof Word && checkMessage(this, (Word) another);
    }

    private boolean checkMessage(Word word, Word another) {
        return word.message.equals(another.message);
    }

    @Override
    public int hashCode() {
        int h = 31;
        h = h * 17 + message.hashCode();
        return h;
    }

}
