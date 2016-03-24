package de.akquinet.trainings.vaadin.framework.timer;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Axel Meier, akquinet engineering GmbH
 */
@JavaScript("timer_connector.js")
public class TimerExtension extends AbstractJavaScriptExtension
{
    interface TimerListener
    {
        void timeout(final TimerEvent timerEvent);
    }

    public static class TimerEvent
    {
        private final TimerExtension source;

        public TimerEvent(final TimerExtension source)
        {
            this.source = source;
        }

        public TimerExtension getSource()
        {
            return source;
        }
    }

    private final List<TimerListener> timerListeners = new ArrayList<>();

    public TimerExtension(final AbstractClientConnector target)
    {
        super(target);
        addDetachListener((e) -> stop());
        extend(target);

        addFunction("timeout", (JavaScriptFunction) arguments ->
        {
            if (isStarted())
            {
                for (TimerListener listener : timerListeners)
                {
                    listener.timeout(new TimerEvent(TimerExtension.this));
                }
                triggerNextInterval();
            }
        });
    }

    private void triggerNextInterval()
    {
        callFunction("triggerNextInterval");
    }

    public void addTimerListener(final TimerListener timerListener)
    {
        timerListeners.add(timerListener);
    }

    public void removeTimerListener(final TimerListener timerListener)
    {
        timerListeners.remove(timerListener);
    }

    public void setIntervalInMs(final int intervalInMs)
    {
        getState().intervalInMs = intervalInMs;
    }

    public int getIntervalInMs()
    {
        return getState().intervalInMs;
    }

    public void start()
    {
        getState().started = true;

        triggerNextInterval();
    }

    public void stop()
    {
        getState().started = false;
    }

    public boolean isStarted()
    {
        return getState().started;
    }

    @Override
    protected TimerState getState()
    {
        return (TimerState) super.getState();
    }
}
