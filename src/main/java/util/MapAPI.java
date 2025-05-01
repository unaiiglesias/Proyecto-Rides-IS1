package util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapAPI {
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private Future<?> runningRequest;
	
	
	private static String getCityCoords (String city) {
		/**
		 * Given a city name, it attempts to fetch its longitude and latitude coordinates
		 * from nominatim.openstreetmap.org API
		 * 
		 * If city name is non-specific, the system might grab the wrong city (search is worldwide
		 * so duplicate city names might exist)
		 * 
		 * If no city with given name is found, it returns null
		 * 
		 * Coords are given in format "long,lat" Ready to be inserted in estimate time travel
		 * API call
		 */

		String coordsURL = "https://nominatim.openstreetmap.org/search?q=" + city + "&format=json";

		try {
			URL url = new URL(coordsURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "RidesApplication/1.0 (correo@ejemplo.com)");
			// This API requires us to provide a User-Agent header in order to work
			con.setRequestMethod("GET");

			int code = con.getResponseCode();
			if (code != 200)
			{
				System.out.println("Request terminated with error code: " + code);
				return null;
			}

			// We first read the response as a stirng and then parse it as JSON
			Scanner sc = new Scanner(con.getInputStream());
			String resString = "";
			while (sc.hasNext())
				resString += sc.nextLine();
			sc.close();

			// Response interpreted as JSON Array (array with each found city with given name)
			JSONArray respArray = new JSONArray(resString);
			JSONObject resp = respArray.getJSONObject(0); // We'll hope the first found city is the one we wanted

			double lat = resp.getDouble("lat"), lon = resp.getDouble("lon");

			return String.valueOf(lon) + "," + String.valueOf(lat);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}
   
	public static int getEstimatedTravelTime(String from, String to) {
		/**
		 * Returns estimated travel time in minutes according to router.project-osrm.org
		 * 
		 * It first needs to fetch each cities' coordinates with getCityCoords and then call the API to 
		 * get the estimate.
		 * 
		 * If city names are non-specific the wrong city might be fetched and estimates could be inaccurate.
		 * 
		 * Will return -1 if any error happened
		 */
		
		String fromCoords = getCityCoords(from), toCoords = getCityCoords(to);
		
		if (fromCoords == null || toCoords == null)
			return -1;
		
		String routeURL = "http://router.project-osrm.org/route/v1/driving/" + fromCoords + ";" + toCoords + "?overview=false";
		
		try {
			URL url = new URL(routeURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
		   int code = con.getResponseCode();
		   if (code != 200)
		   {
			   System.out.println("Request terminated with error code: " + code);
			   return -1;
		   }
			
			// We first read the response as a stirng and then parse it as JSON
			Scanner sc = new Scanner(con.getInputStream());
			String resString = "";
			while (sc.hasNext())
				resString += sc.nextLine();
			sc.close();
			
			// Response interpreted as JSON object
			JSONObject resp = new JSONObject(resString);
			
			//int distance = ((JSONObject) (resp.getJSONArray("routes").getJSONObject(0))).getInt("distance"); // In meters
			double duration = ((JSONObject) (resp.getJSONArray("routes").getJSONObject(0))).getInt("duration"); // In seconds
			return (int) Math.round(duration / 60);
		}	
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return -1;
		}
		
		
	}
	
	public synchronized void asyncRequest (String from, String to, Consumer<Integer> onSuccess, Consumer<Exception> onError) {
		/**
		 * Runs getEstimatedTravelTime in the background.
		 * 
		 * Ensures only one call to said method is running at the same time.
		 * If a new call is received, aborts the previous one and makes a new one
		 */
		
		// If there's a request running, cancel it
		if (runningRequest != null && !runningRequest.isDone())
			runningRequest.cancel(true);
		
        // Make a new reqeust
		runningRequest = executor.submit(() -> {
            try {
                Integer res = getEstimatedTravelTime(from, to);
                if (res != null && !Thread.currentThread().isInterrupted()) {
                	onSuccess.accept(res);
                }
            } catch (Exception e) {
                onError.accept(e);
            }
        });
		
	}
	
	public static void main (String[] args) {
		/**
		 * DEBUG and proof of concept only
		 */
		
		int est = getEstimatedTravelTime("Eibar", "Gasteiz");
		int h = est / 60, m = est % 60;
		System.out.println("Tiempo estimado de llegada: " + String.valueOf(h) + "h " + m + "min");
		
		return;
	}
}
