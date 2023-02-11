package com.example.app_ds

import android.os.Parcel
import android.os.Parcelable

//la classe station a comme parametres :
//id de la station
//nom de la station
//latitude
//longitude
//nombre d'emplacement
//nombre de place disponible

class Station(var id:Int , var nom:String, var long:Double, var lat:Double, var nb_emplacement:Int, var nb_place:Int):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() as String,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    //affiche du toString qui respecte le format demander pour l'affichage de la listeview
    override fun toString(): String {
        return nom + "\n" + nb_place + " place(s) libre(s) sur " + nb_emplacement
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nom)
        parcel.writeDouble(long)
        parcel.writeDouble(lat)
        parcel.writeInt(nb_emplacement)
        parcel.writeInt(nb_place)
    }

    companion object CREATOR : Parcelable.Creator<Station> {
        override fun createFromParcel(parcel: Parcel): Station {
            return Station(parcel)
        }

        override fun newArray(size: Int): Array<Station?> {
            return arrayOfNulls(size)
        }
    }
}