package com.overops.examples.web;

import java.util.Random;

public class RestUtil {
	public void doWork(String s)
	{
		try {
			Thread.sleep(Math.abs(new Random().nextLong() % 2000) + 20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		moreWork(s);
	}
	
	public void moreWork(String s)
	{
		try {
			Thread.sleep(Math.abs(new Random().nextLong() % 2000) + 20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(s);
	}
}
