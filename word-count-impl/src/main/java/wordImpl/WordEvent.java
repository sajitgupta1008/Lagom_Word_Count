package wordImpl;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.gdata.util.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;


public interface WordEvent extends Jsonable, AggregateEvent<WordEvent> {

    AggregateEventTag<WordEvent> TAG = AggregateEventTag.of(WordEvent.class);

    @Immutable
    public final class WordCountChanged implements WordEvent {

        @JsonProperty("word")
        public final String word;

        @JsonCreator()
        public WordCountChanged(@JsonProperty("word") String word) {
            this.word = Preconditions.checkNotNull(word, "word ");
        }


        @Override
        public AggregateEventTagger<WordEvent> aggregateTag() {
            return TAG;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            else
                return obj instanceof WordCountChanged && equalTo((WordCountChanged) obj);
        }

        private boolean equalTo(WordCountChanged another) {
            return this.word.equals(another.word);
        }

        @Override
        public int hashCode() {
            int h = 31;
            h = 17 * h + word.hashCode();
            return h;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("wordImpl.WordEvent").add("word", word).toString();
        }

    }
}
