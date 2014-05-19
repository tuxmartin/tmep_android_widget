package eu.vancl.martin.tmep;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView autorWeb;
	private AlarmManager alarmManager;
	private Button btnSave;
	private TextView textURL;

	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		prefs = getSharedPreferences("eu.vancl.martin.tmep", Context.MODE_PRIVATE);
		
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		textURL = (TextView) findViewById(R.id.textURL);

		String tmepURL = prefs.getString("url", null);
		
		if (tmepURL != null) {
			textURL.setText(tmepURL);			
		} else {
			textURL.setText("http://teplomer.obechnanice.cz/json.php");
		}

		btnSave = (Button) findViewById(R.id.btnSaveURL);
		
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				prefs.edit().putString( "url", (String) textURL.getText().toString() ).commit();			
				Toast.makeText(getApplicationContext(), 
						"Nová adresa uložena.", Toast.LENGTH_SHORT).show();
			}			
		});

		autorWeb = (TextView) findViewById(R.id.autorWeb);
		if (autorWeb != null) {
			autorWeb.setMovementMethod(LinkMovementMethod.getInstance());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
