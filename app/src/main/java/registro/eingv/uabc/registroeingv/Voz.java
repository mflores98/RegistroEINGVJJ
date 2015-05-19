
package registro.eingv.uabc.registroeingv;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;

import java.util.Locale;

/**
 * Created by Mifc on 5/10/2015.
 */
public class Voz implements TextToSpeech.OnInitListener{
    private TextToSpeech engine;
    private double pitch=1.0;
    private double speed=1.0;
    private Geofence s;
    private String frase;

    public Voz() {

        speak("Registro");
    }

    private void speak( String str )
    {
        engine.speak( str, TextToSpeech.QUEUE_FLUSH, null );
        engine.setSpeechRate(0.0f);
        engine.setPitch(0.0f);
    }


    @Override
    public void onInit(int status) {

        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED ) {

        System.out.printf("ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED");

        }
    }
}

