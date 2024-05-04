package com.example.crypto.analiziton.helper;

import com.example.crypto.analiziton.model.DataFromBinanceAboutVolume;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ParseVolumeDataFromFile {

    public DataFromBinanceAboutVolume parseVolumeData(String[] data, String currencyName) {
        DataFromBinanceAboutVolume volume = new DataFromBinanceAboutVolume();
        volume.setCurrencyName(currencyName);
        volume.setPrice(Double.parseDouble(data[1]));
        volume.setAmountOfBaseCurrency(Double.parseDouble(data[2]));
        volume.setTransactionAmount(Double.parseDouble(data[3]));
        volume.setTime(new Timestamp(Long.parseLong(data[4])));
        volume.setIsBuyer(whoBuyerIs(String.valueOf(data[5])));
        return volume;
    }

    private String whoBuyerIs(String string){
        if(string.equalsIgnoreCase("true")){
            return "Maker";
        }
       else if (string.equalsIgnoreCase("false")){
           return "Taker";
        }
       else {
           return null;
        }
    }
}
