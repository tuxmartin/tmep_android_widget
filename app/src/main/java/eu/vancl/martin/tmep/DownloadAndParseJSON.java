package eu.vancl.martin.tmep;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DownloadAndParseJSON {
	
	private String teplota;
	private String vlhkost;
	private String casMereni;
	
	private String chyba;
	
	private String url;
	
	public void downloadAndParse() {		
		try {
			URL u = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) urlConn;
			httpConn.setAllowUserInteraction(false);
			httpConn.connect();
			InputStream in = httpConn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in);
			ByteArrayBuffer baf = new ByteArrayBuffer(1000);
			int read = 0;
			int bufSize = 1024;
			byte[] buffer = new byte[bufSize];
			while (true) {
				read = bis.read(buffer);
				if (read == -1) {
					break;
				}
				baf.append(buffer, 0, read);
			}
			String queryResult = new String(baf.toByteArray());

			JSONObject jsonObject = new JSONObject(queryResult);

			teplota = jsonObject.getString("teplota");
			Log.i("TEMP", teplota);
			vlhkost = jsonObject.getString("vlhkost");
			casMereni = jsonObject.getString("cas");			

		} catch (MalformedURLException e) {
			chyba = "MalformedURLException";
			Log.wtf("TEMP", "MalformedURLException");
			e.printStackTrace();
		} catch (IOException e) {
			chyba = "IOException";
            Log.wtf("TEMP", "IOException");
			e.printStackTrace();
		} catch (JSONException e) {
			chyba = "JSONException";
            Log.wtf("TEMP", "JSONException");
			e.printStackTrace();
		}	
	}

	public DownloadAndParseJSON(String url) {
		this.url = url;
	}

	public String getTeplota() {
		return teplota;
	}

	public void setTeplota(String teplota) {
		this.teplota = teplota;
	}

	public String getVlhkost() {
		return vlhkost;
	}

	public void setVlhkost(String vlhkost) {
		this.vlhkost = vlhkost;
	}

	public String getCasMereni() {
		return casMereni;
	}

	public void setCasMereni(String casMereni) {
		this.casMereni = casMereni;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getChyba() {
		return chyba;
	}

	public void setChyba(String chyba) {
		this.chyba = chyba;
	}
	
	
	

}
