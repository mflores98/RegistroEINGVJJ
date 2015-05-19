package registro.eingv.uabc.registroeingv;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import registro.eingv.uabc.registroeingv.db.DaoMaster;
import registro.eingv.uabc.registroeingv.db.Registro;
import registro.eingv.uabc.registroeingv.lista.ListActivity;


public class RegistroActivity extends ActionBarActivity implements LocationListener,View.OnClickListener {
    private LocationManager locationManager;
    private ArrayList<Float> puntos;
    private EditText lugarText;
    private TextView coordenadasText;
    private Button guardarBoton,botonMap;


    private GoogleMap mapa=null;
    private int vista=0;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Mostrar en el texto de posicion  "Posicion: [latitud, longitud]"

        //Un boton para guardar la posicion en la base de datos
        initDataBase();
//  List<Registro> reg=SingletonDB.getInstance().getDaoSession().getRegistroDao().loadAll();
  // for(Registro registro:reg){
 //System.out.println(registro.getLugar());
  //}
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
        lugarText=(EditText)findViewById(R.id.editTextLugar);
        coordenadasText=(TextView)findViewById(R.id.textViewCordenadas);
        guardarBoton= (Button) findViewById(R.id.buttonRegistro);
        guardarBoton.setOnClickListener(this);

        botonMap= (Button) findViewById(R.id.botonMap);
        botonMap.setOnClickListener(this);

    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegistro:regiss();break;
            case R.id.botonMap: todd();break;
        }
    }
    public  void  regiss(){
        //Insertar datos de Registro
        Registro registro=new Registro();
        //Validar y llenar

        if(puntos!= null){
            if(!puntos.isEmpty()) {
                if(lugarText.getText().length()>=5) {
                    registro.setLugar(lugarText.getText().toString());
                    registro.setLatitud(puntos.get(0));
                    registro.setLongitud(puntos.get(1));
                    registro.setAltitud(puntos.get(2));

                    SingletonDB.getInstance().getDaoSession().getRegistroDao().insert(registro);
                    miNotificacion("Guardado",Toast.LENGTH_SHORT);
                    //        lugarText.setText("");
                }else{
                    miNotificacion("Ingresa un nombre mayor a 5 caracteres",Toast.LENGTH_SHORT);
                }
            }else{
                //Se crea una notificacion
                miNotificacion("Espera que carguen las coordenadas",Toast.LENGTH_SHORT);
            }
        }else{
            miNotificacion("Espera que carguen las coordenadas",Toast.LENGTH_SHORT);
        }
    }
    private void todd() {
        Intent intent=new Intent(getApplicationContext(),ActivityMaps.class);
        startActivity(intent);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    public void miNotificacion(String texto, int duracion){
        Toast toast = Toast.makeText(this.getApplicationContext(),
                texto, duracion);
        //Aqui se muestra la notificacion
        toast.show();
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas habilitar tu GPS?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick( final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onLocationChanged(Location location) {

        coordenadasText.setText("Latitud: [" + location.getLatitude() + "]\n Longitud: [" + location.getLongitude() + "]\n Altitud: [" + location.getAltitude() + "]");
        puntos=new ArrayList<>();
        puntos.add((float) location.getLatitude());
        puntos.add((float) location.getLongitude());
        puntos.add((float) location.getAltitude());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status){
            case LocationProvider.AVAILABLE:
                System.err.println("AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                System.err.println("OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                System.err.println("TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    //Instancia de base de datos
    private static SQLiteDatabase db;
    //Clase ayudante para abrir y crear la base de datos
    SQLiteOpenHelper helper;
    private void initDataBase() {
        helper = new DaoMaster.DevOpenHelper(this, "registro", null);
        db = helper.getWritableDatabase();
        setDb(helper.getWritableDatabase());
    }
    public static SQLiteDatabase getDb() {
        return db;
    }
    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}