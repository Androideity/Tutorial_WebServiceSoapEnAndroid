package net.ivanvega.ConsumirWS;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ConsumirWSActivity extends Activity {
	//Constantes para la invocacion del web service
	private static final String NAMESPACE = "http://tempuri.org/";
	private static String URL="http://192.168.0.231/EjemploWS/Service.asmx"; 
	private static final String METHOD_NAME = "getAllAndroidOS";
	private static final String SOAP_ACTION ="http://tempuri.org/getAllAndroidOS";
	 
	//Declaracion de variables para consuymir el web service
	private SoapObject request=null;
	private SoapSerializationEnvelope envelope=null;
	private SoapPrimitive  resultsRequestSOAP=null;
	
	//Declaracion de variables para serealziar y deserealizar 
	//objetos y cadenas JSON
	Gson gson ;
	
	//Variables para manipular los controles de la UI
	Button btn;
	ListView lsvAndroidOS;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lsvAndroidOS = (ListView)findViewById(R.id.lst);
        btn = (Button)findViewById(R.id .btn);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Se crea un objeto SoapObject para poder realizar la peticion
//				para consumir el ws SOAP. El constructor recibe
//				el namespace. Por lo regular el namespace es el dominio 
//				donde se encuentra el web service
				request = new SoapObject(NAMESPACE, METHOD_NAME);
				
//				Se crea un objeto SoapSerializationEnvelope para serealizar la
//				peticion SOAP y permitir viajar el mensaje por la nube
//				el constructor recibe la version de SOAP
				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true; //se asigna true para el caso de que el WS sea de dotNet
				
				//Se envuelve la peticion soap
				envelope.setOutputSoapObject(request);
				
				//Objeto que representa el modelo de transporte
				//Recibe la URL del ws
				HttpTransportSE transporte = new HttpTransportSE(URL);
				
				
				try {	
					//Hace la llamada al ws
					transporte.call(SOAP_ACTION, envelope);
					
					//Se crea un objeto SoapPrimitive y se obtiene la respuesta 
					//de la peticion
					resultsRequestSOAP = (SoapPrimitive)envelope.getResponse();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//Almacenamos el resultado en un String ya que lo que represa
				//el ws es una cadena json, representando una lista AndroidOS
				//de objetos del tipo 
				String  strJSON = resultsRequestSOAP.toString();
				
				crearLista(strJSON);
			}
		});
   
    }
    
    /**
     * Metodo que recibe una cadena JSON y la convierte en una lista
     * de objetos AndroidOS para despues cargarlos en la lista
     * @param strJson (String) Cadena JSON
     */
    private void crearLista(String strJson){
		//se crea el objeto que ayuda deserealizar la cadena JSON
		gson = new Gson();
		
		//Obtenemos el tipo de un ArrayList<AndroidSO>
		Type lstT = new TypeToken< ArrayList<AndroidOS>>(){}.getType();

		//Creamos una objeto ArrayList<AndroidOS> 
		ArrayList<AndroidOS> arrListAOS = new ArrayList<AndroidOS>();
	
		//Deserealizamos la cadena JSON para que se convertida a un ArrayList<AndroidOS>
		arrListAOS = gson.fromJson(strJson, lstT);
		
		//Asignaos la ArrayList al controls ListView para mostrar
		//la lista de SO Android que se consumieron del web service
		lsvAndroidOS.setAdapter(new ArrayAdapter<AndroidOS>
		(getApplication(), android.R.layout.simple_list_item_1, arrListAOS));
		
	}
}