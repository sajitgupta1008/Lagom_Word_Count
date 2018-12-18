package wordImpl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import play.Logger;

import java.util.Optional;

public class WordEntity extends PersistentEntity<WordCommand, WordEvent, WordState> {
    
    public Behavior initialBehavior(Optional<WordState> snapshotState) {
        
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(snapshotState.orElse(new WordState(0)));
        
        Logger.info("in behavior");
        behaviorBuilder.setCommandHandler(WordCommand.SetWord.class, (cmd, ctx) ->
        {
            Logger.info("setWORD!!!!!!!\n\n\n\n");
            if (cmd.word == null || cmd.word.equals("")) {
                ctx.invalidCommand("Word must be defined to set count.");
                return ctx.done();
            } else
                return ctx.thenPersist(new WordEvent.WordCountChanged(cmd.word), evt -> ctx.reply(Done.getInstance()));
        });
        
        
        behaviorBuilder.setEventHandler(WordEvent.WordCountChanged.class, evt -> {
                    Logger.info("\n\nSetting state - old state : " + state().count);
                    return new WordState(state().count + 1);
                }
        );
        
        behaviorBuilder.setReadOnlyCommandHandler(WordCommand.GetCount.class, (cmd, ctx) ->
                ctx.reply(entityId() + " count : " + state().count));
        
        return behaviorBuilder.build();
    }
}
