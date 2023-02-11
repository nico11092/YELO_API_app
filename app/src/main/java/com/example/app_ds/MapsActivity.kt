package com.example.app_ds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.app_ds.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fonctionnalité suplémentaire pour le bouton de retour
        val actionbar = supportActionBar
        //set back button
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    //function pour le retour en arrière
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //on recupere le textview pour afficher les données de la station
        val affichage: TextView = findViewById(R.id._affiche_station)

        //deux tests ci-dessous pour savoir quel avec quel type de parametre l'activité est appele
        //cas 1 : affichage d'une station
        if(intent.extras?.get("station") != null) {
            //on recupere la station placé en parametre
            val station = intent.extras?.get("station") as Station

            //on met à jour le textview avec le nom de la station
            affichage.text = station.nom

            //on place le marker en fonction des coordonnées de la station et on lui donne un titre(nom) et une description (place)
            val coo_station = LatLng(station.lat, station.long)
            val place = station.nb_place.toString()+" / "+station.nb_emplacement.toString()+" places"
            mMap.addMarker(MarkerOptions().position(coo_station).title(station.nom).snippet(place))

            //on place la caméra sur la station avec un zomm pour pouvoir la visualiser
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coo_station))
            mMap.setMinZoomPreference(14.0f)
        }
        //cas 2 : affichage de toutes les stations
        if(intent.extras?.get("stations") != null) {
            //on recupere la liste de station placé en parametre
            var ListStation:ArrayList<Station> = intent.extras?.get("stations") as ArrayList<Station>

            //on parcours la liste de station pour pouvoir les afficher sur la map
            for(i in 0 until ListStation.size){
                //on place le marker en fonction des coordonnées de la station et on lui donne un titre(nom) et une description (place)
                val coo_station = LatLng(ListStation.get(i).lat, ListStation.get(i).long)
                val place = ListStation.get(i).nb_place.toString()+" / "+ListStation.get(i).nb_emplacement.toString()+" places"
                mMap.addMarker(MarkerOptions().position(coo_station).title(ListStation.get(i).nom).snippet(place))
            }

            //on prend la rochelle comme point de référence pour faire un zoom global sur les stations
            val coo_LaRochelle = LatLng(46.160329, 	-1.151139)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coo_LaRochelle))
            mMap.setMinZoomPreference(11.0f)
        }
    }


}