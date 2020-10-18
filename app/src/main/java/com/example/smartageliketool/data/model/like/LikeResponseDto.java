
package com.example.smartageliketool.data.model.like;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LikeResponseDto implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<LikeResponseDto> CREATOR = new Creator<LikeResponseDto>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LikeResponseDto createFromParcel(Parcel in) {
            return new LikeResponseDto(in);
        }

        public LikeResponseDto[] newArray(int size) {
            return (new LikeResponseDto[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3200377026328880252L;

    protected LikeResponseDto(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LikeResponseDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
