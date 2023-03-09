package br.com.mtinet.util;

public class SplashScreen {
	
	public static void show() {
		imageLage();
	}
	
	private static void print(String value) {
		System.out.println(value);
	}
	
	private static void imageLage(){
		print("      ___                    __________                        ");
		print("     |   |____ ___  _______  \\_  _____/______   ____   ____    ");
		print("     |   \\__  \\   \\/  /\\__ \\  |   __) \\_  __ \\_/ __ \\_/ __ \\   ");
		print(" /\\__|   |/ __ \\    /  / __ \\_|    \\   |  | \\/\\  ___/\\  ___/   ");
		print(" \\_______(____  /\\_/  (____  /\\__  /   |__|    \\___ > \\___ >  ");
		print("              \\/           \\/    \\/                \\/     \\/ ");
	}
	
	
	
	private static void listLowerFix() {
		print("      __               ____             ");
		print("  __ / /__ __  _____ _/ __/______ ___   ");
		print(" / // / _ `/ |/ / _ `/ _// __/ -_) -_)  ");
		print(" \\___/\\_,_/|___/\\_,_/_/ /_/  \\__/\\__/   ");
	}
	
	private static void imageLower() {
		print("      |                   ____|               ");
		print("      |  _` |\\ \\   / _` | |    __| _ \\  _ \\   ");
		print("  \\   | (   | \\ \\ / (   | __| |    __/  __/   ");
		print(" \\___/ \\__,_|  \\_/ \\__,_|_|  _|  \\___|\\___|   ");
	}
	
	private static void lista() {
		print("       _                  ______               ");
		print("      | |                |  ____|              ");
		print("      | | __ ___   ____ _| |__ _ __ ___  ___   ");
		print("  _   | |/ _` \\ \\ / / _` |  __| '__/ _ \\/ _ \\  ");
		print(" | |__| | (_| |\\ V / (_| | |  | | |  __/  __/  ");
		print("  \\____/ \\__,_| \\_/ \\__,_|_|  |_|  \\___|\\___|  ");
	}
}
