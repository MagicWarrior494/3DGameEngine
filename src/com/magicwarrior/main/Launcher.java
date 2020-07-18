package com.magicwarrior.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Launcher {

	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /PID ";

	public static void main(String[] args) throws Exception {
		int pid = getPid("javaw.exe");
		if (countProcessRunning("javaw.exe") > 1) {
			killProcess(pid);
		}
		Main main = new Main("3D Game Engine", 1200, 1000);
		main.start();
	}

	public static int getPid(String serviceName) throws Exception {
		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {

//			System.out.println(line);
			if (line.contains(serviceName)) {
				String[] line1 = line.split(" ");
				for (int i = 0; i < line1.length; i++) {
					if (line1[i].equalsIgnoreCase("console")) {
						return Integer.parseInt(line1[i - 1]);
					}
				}
			}
		}
		return 0;
	}

	public static int countProcessRunning(String serviceName) throws Exception {
		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		int counter = 0;
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			if (line.contains(serviceName)) {
				counter++;
			}
		}
		return counter;
	}

	public static void killProcess(int servicePid) throws Exception {
		Runtime.getRuntime().exec(KILL + servicePid);
	}
}
