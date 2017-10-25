package wordImpl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import jdk.nashorn.internal.ir.annotations.Immutable;
import play.Logger;

public interface WordCommand extends Jsonable {

    @Immutable
    @JsonDeserialize
    public final class SetWord implements WordCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {

        public final String word;

        @JsonCreator
        public SetWord(String word) {
            Logger.info("\n\n\n\n\nbefore create");
            this.word = Preconditions.checkNotNull(word, "word");
            Logger.info("after create\n\n\n\n");
        }

        @Override
        public boolean equals(Object another) {
            if (this == another)
                return true;
            return another instanceof SetWord && equalTo(this, (SetWord) another);
        }

        private boolean equalTo(SetWord setWord, SetWord another) {
            return setWord.word.equals(another.word);
        }

        @Override
        public int hashCode() {
            int h = 31;
            h = h * 17 + word.hashCode();
            return h;
        }
    }

    @JsonDeserialize
    @Immutable
    public final class GetCount implements WordCommand, PersistentEntity.ReplyType<String> {

        public final String word;

        @JsonCreator
        public GetCount(String word) {
            this.word = Preconditions.checkNotNull(word, "word");
        }

        @Override
        public boolean equals(Object another) {
            if (this == another)
                return true;
            return another instanceof GetCount && equalTo(this, (GetCount) another);
        }

        private boolean equalTo(GetCount word, GetCount another) {
            return word.word.equals(another.word);
        }

        @Override
        public int hashCode() {
            int h = 31;
            h = h * 17 + word.hashCode();
            return h;
        }
    }


}
