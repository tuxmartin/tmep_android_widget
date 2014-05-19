package eu.vancl.martin.tmep;

import android.app.Activity;
import android.app.AlarmManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView autorWeb;
	private AlarmManager alarmManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

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
