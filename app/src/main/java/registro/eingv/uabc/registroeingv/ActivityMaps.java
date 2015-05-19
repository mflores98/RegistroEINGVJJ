package registro.eingv.uabc.registroeingv;

import android.graphics.Color;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;
import java.util.Locale;

import registro.eingv.uabc.registroeingv.db.Registro;
import registro.eingv.uabc.registroeingv.db.RegistroDao;


public class ActivityMaps extends android.support.v4.app.FragmentActivity implements TextToSpeech.OnInitListener {
    private GoogleMap mapa = null;
    private int vista = 0;
    private ImageView speechButton;
    private TextToSpeech engine;
    private EditText editText;
    private SeekBar seekPitch;
    private SeekBar seekSpeed;
    private double pitch=1.0;
    private double speed=1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_maps);

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                Projection proj = mapa.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(
                        ActivityMaps.this,
                        "Click\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            public void onMapLongClick(LatLng point) {
                Projection proj = mapa.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(
                        ActivityMaps.this,
                        "Click Largo\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mapa.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                Toast.makeText(
                        ActivityMaps.this,
                        "Cambio C�mara\n" +
                                "Lat: " + position.target.latitude + "\n" +
                                "Lng: " + position.target.longitude + "\n" +
                                "Zoom: " + position.zoom + "\n" +
                                "Orientaci�n: " + position.bearing + "\n" +
                                "�ngulo: " + position.tilt,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(
                        ActivityMaps.this,
                        "Marcador pulsado:\n" +
                                marker.getTitle(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.menu_vista:
                alternarVista();
                break;
            case R.id.menu_mover:
                //Centramos el mapa en Espa�a
                CameraUpdate camUpd1 =
                        CameraUpdateFactory.newLatLng(new LatLng(40.41, -3.69));
                mapa.moveCamera(camUpd1);


                break;
            case R.id.menu_animar:
                //Centramos el mapa en Espa�a y con nivel de zoom 5
                CameraUpdate camUpd2 =
                        CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5F);
                mapa.animateCamera(camUpd2);
                break;
            case R.id.menu_3d:
                LatLng mexicali = new LatLng(32.4, -115.5);
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(mexicali)   //Centramos el mapa en Madrid
                        .zoom(19)         //Establecemos el zoom en 19
                        .bearing(45)      //Establecemos la orientaci�n con el noreste arriba
                        .tilt(70)         //Bajamos el punto de vista de la c�mara 70 grados
                        .build();

                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

                mapa.animateCamera(camUpd3);
                break;
            case R.id.menu_posicion:

                CameraPosition camPos2 = mapa.getCameraPosition();
                LatLng pos = camPos2.target;
               /* Toast.makeText(ActivityMaps.this,
                        "Lat: " + pos.latitude + " - Lng: " + pos.longitude,
                        Toast.LENGTH_LONG).show();

                */
                break;

            case R.id.menu_marcadores:
               // mostrarMarcador(registro.getLatitud(), registro.getLongitud());
              //  mostrarMarcador(40.5, -3.5);
                amarcador();
                break;

            case R.id.menu_lineas:
                mostrarLineas();break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void mostrarMarcador(double latitud, double longitud){

        //mapa.addMarker(new MarkerOptions()
          //              .position(new LatLng(registro.getLatitud(),registro.getLongitud()))
            //            .title("Lugar: "+registro.getLugar())
             //);
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(latitud, longitud))
                .title("Pais: Espa�a"));
////////////////////////////////////////////
       LatLng mexico=new LatLng(32.4,-115.5);
        mapa.addMarker(new MarkerOptions()
                        .title("Mexico")
                        .snippet("mex")
                        .position(mexico));
    }
    public void amarcador(){
        //Crea una lista vacia de Registro
        List<Registro> lista;
        //Obtener la lista de Registros en la BD
        lista = SingletonDB.getInstance().getDaoSession().getRegistroDao().loadAll();
        for (Registro dato:lista){
            LatLng re=new LatLng(dato.getLatitud(),dato.getLongitud());
            mapa.addMarker(new MarkerOptions()
                    .title(dato.getLugar())
                    .snippet(dato.getLugar())
                    .position(re)
                                    );
            }
        }

    private void mostrarLineas(){
        PolygonOptions lineas=new PolygonOptions()
          //      .add(new LatLng(registro.getLatitud(),registro.getLongitud()));
        ///lineas.strokeWidth(8);
        ///lineas.strokeColor(Color.RED);
        ///mapa.addPolygon(lineas)
        //
        //
        //
                   ;
 }
   private void alternarVista() {
        vista = (vista + 1) % 4;
        switch(vista)
        {
            case 0:
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 2:
                mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }
    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        Log.d("Speech", "OnInit - Status [" + status + "]");

        if (status == TextToSpeech.SUCCESS) {
            Log.d("Speech", "Success!");
            Locale spanish = new Locale("es", "ES");
            engine.setLanguage(spanish);
        }
    }
    private void speech(String cadena) {
        String s="horacion";
        engine.setPitch((float) pitch);
        engine.setSpeechRate((float) speed);
        //engine.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        engine.speak(cadena.toString(), TextToSpeech.QUEUE_FLUSH, null);
        // engine.speak(s.toString(), TextToSpeech.QUEUE_FLUSH, null);
    }
}
