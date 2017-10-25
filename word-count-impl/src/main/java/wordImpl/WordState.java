package wordImpl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
@JsonDeserialize
public final class WordState implements Jsonable {

    public int count;

    @JsonCreator
    public WordState(int count) {
        this.count = Preconditions.checkNotNull(count, "count");
    }

    @Override
    public boolean equals(Object another) {
        if (this == another)
            return true;
        return another instanceof WordState && equalTo(this, (WordState) another);
    }

    private boolean equalTo(WordState wordState, WordState another) {
        return this.count == another.count;
    }

    @Override
    public int hashCode() {
        int h = 31;
        h = h * 17 + String.valueOf(count).hashCode();
        return h;
    }
}
