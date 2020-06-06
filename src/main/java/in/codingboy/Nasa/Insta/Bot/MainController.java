package in.codingboy.Nasa.Insta.Bot;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.io.FileUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Controller
public class MainController {

    Instagram4j instagram;
    String date="";
    String explanation="";
    String imageurl="";
    int countpost=0;

    String hashtags ="#nasa #space #spacex sun #astronomy #mars #moon #earth #programming #API #planets #universe #astronaut #astronaut #physics";

    public final String MYURL = "https://api.nasa.gov/planetary/apod?api_key=VCPmjAZa16rXbj2TodBoTnBLJ2Z6FfPZQE4oYPa4";
    @RequestMapping("/")
    public String index() {
        loginToInstagram(); //first login to Instagram and then fetch data from API
        getData();
        return "index";
    }
    private void getData() {

        while (true) {
            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(MYURL)
                        .build();
                try {
                    //parse JSON Object
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(data);

                    //set all data
                    date = (String) jsonObject.get("date");
                    explanation = (String) jsonObject.get("explanation");
                    imageurl = (String) jsonObject.get("url");
                    System.out.println(date+explanation+imageurl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //save image from url to image file
                FileUtils.copyURLToFile(
                        new URL(imageurl), //save on the desktop... and
                        new File("images\\img1.jpg"),
                        30000,
                        1000000);

                //check countpost and total number of post on the feed..if countpost<total post then upload
                InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest("codingboybot"));
                if(!(countpost>userResult.getUser().geo_media_count))
                {
                    //upload a post to instagram
                    instagram.sendRequest(new InstagramUploadPhotoRequest(new File("images\\img1.jpg"),explanation+"\n\n"+date+"\n"+hashtags));
                    countpost++;
                    Thread.sleep(30000); //sleep for 1 day == 24 hours == 86,400,000 ms
                }
                else {
                    System.out.println("Post is already uploaded");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
                //if it fails to upload try again
                getData();
            }
        }

    }

    private void loginToInstagram() {
        try {
            instagram = Instagram4j.builder().username("codingboybot").password("7109@Viral.").build();
            instagram.setup();
            instagram.login();
            System.out.println("login hua");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
