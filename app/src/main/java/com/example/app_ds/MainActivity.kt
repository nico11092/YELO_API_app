package com.example.app_ds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ListView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var ListStation = ArrayList<Station>()
    private lateinit var arrayAdapterStation:ArrayAdapter<Station>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialisation de la liste des stations
        this.initListStations()

        //on creer l'adapter sur la listview
        val listView:ListView = findViewById(R.id._list)
        arrayAdapterStation = ArrayAdapter<Station>(this, android.R.layout.simple_list_item_1, ListStation)
        listView.adapter = arrayAdapterStation

        //on creer le listener sur le bouton
        val bouton:Button = findViewById(R.id._button)
        bouton.setOnClickListener {
            //creation de l'intent
            val intent = Intent(this, MapsActivity::class.java)

            //on lui place la liste de station en parametre
            intent.putExtra("stations", ListStation)

            //on démarre l'activité
            startActivity(intent)
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener {  parent, _, position, _ ->
            //on recupere la station selectionne par l'utilisateur
            val station_rechercher:Station = parent.getItemAtPosition(position) as Station

            //creation de l'intent
            val intent = Intent(this, MapsActivity::class.java)

            //on lui place la station en parametre
            intent.putExtra("station", station_rechercher)

            //on démarre l'activité
            startActivity(intent)
        }
    }

    //function pour récuperer le JSON de l'API
    private fun initListStations(): Unit{
        val url = "https://api.agglo-larochelle.fr/production/opendata/api/records/1.0/search/dataset=yelo___disponibilite_des_velos_en_libre_service&facet=station_nom&api-key=a237e560-ca4b-4059-86c6-4f9111b6ae7a"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("\n\n\n\n ERROR \n\n\n\n")
            }
            override fun onResponse(call: Call, response: Response) {
                //on appelle la fontion pour traiter le JSON
                showStations(JSONObject(response.body?.string()))
            }
        })
    }

    //function pour traiter le JSON
    private fun showStations(response:JSONObject){
        //on recupere la partie record qui contient les données sur les stations
        val listStationJSON = response.getJSONArray("records") as JSONArray

        for(i in 0 until listStationJSON.length()){
            //recuperation des données
            val id = listStationJSON.getJSONObject(i).getString("recordid").toInt()

            //le substring est obligatoire pour enlever le numéro d'identification dans le nom de la station
            val nom = listStationJSON.getJSONObject(i).getJSONObject("fields").getString("station_nom").substring(3)
            val lat = listStationJSON.getJSONObject(i).getJSONObject("fields").getString("station_latitude").toDouble()
            val long = listStationJSON.getJSONObject(i).getJSONObject("fields").getString("station_longitude").toDouble()
            val nb_emp = listStationJSON.getJSONObject(i).getJSONObject("fields").getString("nombre_emplacements").toInt()
            val place = listStationJSON.getJSONObject(i).getJSONObject("fields").getString("velos_disponibles").toInt()

            //création de la station
            val station = Station(id, nom, long, lat, nb_emp, place )

            //on l'ajoute à la liste de station
            ListStation.add(station)
        }

        //une fois qu'on a récuperer toutes les stations on met à jour l'adapter
        this@MainActivity.runOnUiThread(java.lang.Runnable {
            arrayAdapterStation.notifyDataSetChanged()
        })
    }


}