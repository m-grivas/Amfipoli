package Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.function.IntConsumer;

public class TurnTimer {
    private final int startSeconds;
    private int remainingSeconds;
    private final Timer timer;

    private final IntConsumer onTick;
    private final Runnable onTimeout;

    public TurnTimer(int startSeconds, IntConsumer onTick, Runnable onTimeout) {
        this.startSeconds = startSeconds;
        this.onTick = onTick;
        this.onTimeout = onTimeout;

        this.timer = new Timer(1000, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                remainingSeconds--;
                onTick.accept(remainingSeconds);

                if (remainingSeconds <= 0) {
                    stop();
                    onTimeout.run();
                }
            }
        });
        this.timer.setRepeats(true);
    }

    /**
     * starts the 30-second timer
     */
    public void start() {
        stop();
        remainingSeconds = startSeconds;
        onTick.accept(remainingSeconds);
        timer.start();
    }

    public void stop() {
        if (timer.isRunning()) timer.stop();
    }
}
