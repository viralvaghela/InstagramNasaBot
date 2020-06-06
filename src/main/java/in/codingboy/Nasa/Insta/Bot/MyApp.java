package in.codingboy.Nasa.Insta.Bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class MyApp {

	public static void main(String[] args) {
		SpringApplication.run(MyApp.class, args);
		wakeUpDyno();

	}
	private static void wakeUpDyno() {
		try {
			while(true) {

				URL url = new URL("https://nasainstabot.herokuapp.com/");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				int responcode = connection.getResponseCode();
				System.out.println("RS CODE:"+responcode);
				if (responcode == HttpURLConnection.HTTP_OK)
				{
					System.out.println("Working");
				}
				else{
					System.out.println("GET not working");
				}

				Thread.sleep(60000); //hit heroku free server each 1 m,so dyno will never sleep xD

			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

}
