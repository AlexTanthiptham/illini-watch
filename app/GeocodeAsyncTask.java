import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeocodeAsyncTask extends AsyncTask<String, Void, LatLng> {
    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    @Override
    protected LatLng doInBackground(String... addresses) {
        if (addresses.length == 0) {
            return null;
        }

        String address = addresses[0];
        HttpURLConnection urlConnection = null;
        String jsonResponse = "";

        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            URL url = new URL(GEOCODING_API_URL + "?address=" + encodedAddress + "&key=YOUR_API_KEY");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonResponse = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsonResponse);

            double lat = jsonObject.getJSONArray("results").getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").getDouble("lat");

            double lng = jsonObject.getJSONArray("results").getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            return new LatLng(lat, lng);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        if (latLng != null) {
            // Use the latLng for your map operations
        }
    }
}