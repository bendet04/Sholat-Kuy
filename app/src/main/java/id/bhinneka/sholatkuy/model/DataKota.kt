package id.bhinneka.sholatkuy.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DataKota(
        @SerializedName("status") val status: String,
        @SerializedName("msg") val msg: String,
        @SerializedName("count") val count: Int,
        @SerializedName("data") val data: List<Data>
)

data class Data (
        @SerializedName("id") val id:String,
        @SerializedName("nama_kota") val namaKota: String
) : Parcelable {
    constructor(parcel: Parcel): this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(namaKota)
    }

    override fun describeContents(): Int {
        return 0;
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(source: Parcel): Data {
            return Data(source)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }

    }
}