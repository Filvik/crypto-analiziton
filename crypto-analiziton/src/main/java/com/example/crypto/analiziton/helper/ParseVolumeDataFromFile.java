package com.example.crypto.analiziton.helper;

import com.example.crypto.analiziton.model.DataFromBinanceAboutVolume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class ParseVolumeDataFromFile {

private final CorrectingTimeZone correctingTimeZone;

    public DataFromBinanceAboutVolume parseVolumeData(String[] data, String currencyName) {
        DataFromBinanceAboutVolume volume = new DataFromBinanceAboutVolume();
        volume.setCurrencyName(currencyName);
        volume.setPrice(Double.parseDouble(data[1]));
        volume.setAmountOfBaseCurrency(Double.parseDouble(data[2]));
        volume.setTransactionAmount(Double.parseDouble(data[3]));
        volume.setIsBuyer(whoBuyerIs(String.valueOf(data[5])));

        long originalTimestamp = Long.parseLong(data[4]);
        Timestamp adjustedTimestamp = correctingTimeZone.correctingTimeByHours(new Timestamp(originalTimestamp));

        volume.setTime(adjustedTimestamp);
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
