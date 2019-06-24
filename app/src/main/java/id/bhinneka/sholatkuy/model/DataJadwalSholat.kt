package id.bhinneka.sholatkuy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DataJadwalSholat(
        @SerializedName("status") val status: String,
        @SerializedName("msg") val msg: String,
        @SerializedName("count") val count: Int,
        @SerializedName("data") val data: List<DataSholat>
) : Parcelable {
    constructor(parcel : Parcel) : this (
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.createTypedArrayList(DataSholat)
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(status)
        dest.writeString(msg)
        dest.writeInt(count)
        dest.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataJadwalSholat>{
        override fun createFromParcel(source: Parcel): DataJadwalSholat {
            return DataJadwalSholat(source)
        }

        override fun newArray(size: Int): Array<DataJadwalSholat?> {
            return arrayOfNulls(size)
        }
    }
}

data class DataSholat(
        @SerializedName("tanggal") val tanggal: String,
        @SerializedName("imsyak") val imsyak: String,
        @SerializedName("shubuh") val shubuh: String,
        @SerializedName("terbit") val terbit: String,
        @SerializedName("dzuhur") val dzuhur: String,
        @SerializedName("ashr") val ashr: String,
        @SerializedName("maghrib") val maghrib: String,
        @SerializedName("isya") val isya: String
) : Parcelable {
    constructor(parcel: Parcel) : this (
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(tanggal)
        dest.writeString(imsyak)
        dest.writeString(shubuh)
        dest.writeString(terbit)
        dest.writeString(dzuhur)
        dest.writeString(ashr)
        dest.writeString(maghrib)
        dest.writeString(isya)
    }

    override fun describeContents(): Int {
        return 0;
    }

    companion object CREATOR : Parcelable.Creator<DataSholat>{
        override fun createFromParcel(source: Parcel): DataSholat {
            return DataSholat(source)
        }

        override fun newArray(size: Int): Array<DataSholat?> {
            return arrayOfNulls(size)
        }
    }
}

