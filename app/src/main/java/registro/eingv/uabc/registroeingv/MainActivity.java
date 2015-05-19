package registro.eingv.uabc.registroeingv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import registro.eingv.uabc.registroeingv.lista.ListActivity;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtener el texto del XML
        //final TextView textoEnPantalla = (TextView) findViewById(R.id.textView);
        //Obtener el boton del XML
        Button registrarBoton = (Button)findViewById(R.id.buttonRegistrar);
        Button salirBoton = (Button) findViewById(R.id.buttonSalir);
        Button consultarBoton = (Button) findViewById(R.id.buttonConsultar);

        //Agregar un listener al ser presionado
        registrarBoton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cambiarVistaRegistro();
            }
        });
        salirBoton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        consultarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarVistaConsulta();
            }
        });
    }
    public void cambiarVistaRegistro(){

        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);


    }
    public void cambiarVistaConsulta(){

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);


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
}
