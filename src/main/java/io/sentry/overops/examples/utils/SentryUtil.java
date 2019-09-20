package io.sentry.overops.examples.utils;

import io.sentry.Sentry;

public class SentryUtil {
	private static boolean enabled = false;
	
	public static void enable()
	{
		enabled = true;
	}
	
	public static void init()
	{
		if (!enabled)
		{
			return;
		}
		
		Sentry.init();
	}
	
	public static void capture(Throwable e)
	{
		if (!enabled)
		{
			return;
		}
		
		Sentry.capture(e);
	}
	
	public static void capture(String s)
	{
		if (!enabled)
		{
			return;
		}
		
		Sentry.capture(s);
	}
}
