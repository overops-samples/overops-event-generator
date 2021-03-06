package com.overops.examples.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EventType {

    SWALLOWED_EXCEPTION,
    CAUGHT_EXCEPTION,
    UNCAUGHT_EXCEPTION,
    LOGGED_WARNING,
    LOGGED_ERROR,
    TIMER,
    CUSTOM_EVENT,
    HTTP_ERROR;

    private static final Random RANDOM = new Random();

    private static final List<EventType> EVENT_TYPES = Collections.unmodifiableList(Arrays.asList(values()));

    private static final int SIZE = EVENT_TYPES.size();

    public static EventType randomEvent() {
        return EVENT_TYPES.get(RANDOM.nextInt(SIZE));
    }

}
