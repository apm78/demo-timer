package de.akquinet.trainings.vaadin.framework.timer;

import com.vaadin.shared.JavaScriptExtensionState;

/**
 * @author Axel Meier, akquinet engineering GmbH
 */
public class TimerState extends JavaScriptExtensionState
{
    public int intervalInMs = 1000;
    public boolean started = false;
}
