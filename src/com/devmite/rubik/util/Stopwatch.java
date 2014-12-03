package com.devmite.rubik.util;

/*
 * Copyright (c) 2005, Corey Goldberg
 * 
 * StopWatch.java is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 */

public class Stopwatch {
	private long startTime = 0;
	private boolean running = false;
	private long currentTime = 0;

	public void start() {
		this.startTime = System.currentTimeMillis();
		this.running = true;
	}

	public void stop() {
		this.running = false;
	}

	public void pause() {
		this.running = false;
		currentTime = System.currentTimeMillis() - startTime;
	}

	public void resume() {
		this.running = true;
		this.startTime = System.currentTimeMillis() - currentTime;
	}

	// elaspsed time in milliseconds
	public long getElapsedTimeMili() {
		long elapsed = 0;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 10) % 100;
		}
		return elapsed;
	}

	// elaspsed time in seconds
	public long getElapsedTimeSecs() {
		long elapsed = 0;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
		}
		return elapsed;
	}

	// elaspsed time in minutes
	public long getElapsedTimeMin() {
		long elapsed = 0;
		if (running) {
			elapsed = (((System.currentTimeMillis() - startTime) / 1000) / 60) % 60;
		}
		return elapsed;
	}

	// elaspsed time in hours
	public long getElapsedTimeHour() {
		long elapsed = 0;
		if (running) {
			elapsed = ((((System.currentTimeMillis() - startTime) / 1000) / 60) / 60);
		}
		return elapsed;
	}
}