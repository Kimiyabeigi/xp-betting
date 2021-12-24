package com.xpbetting.util;

import com.xpbetting.domain.enums.UploadHeaderType;
import com.xpbetting.domain.model.GameEvent;
import com.xpbetting.exception.CSVUtilException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
  public static final String TYPE = "text/csv";
  public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

  private CSVUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<GameEvent> csvToGameEventDTOList(InputStream inputStream)
      throws CSVUtilException {
    try (BufferedReader fileReader =
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVParser csvParser =
            new CSVParser(
                fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()); ) {

      List<GameEvent> gameEvents = new ArrayList<>();
      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        GameEvent gameEvent =
            new GameEvent(
                null,
                Integer.parseInt(csvRecord.get(UploadHeaderType.CODE)),
                Integer.parseInt(csvRecord.get(UploadHeaderType.PLAYER_ID)),
                Float.parseFloat(csvRecord.get(UploadHeaderType.AMOUNT)),
                LocalDateTime.parse(
                    csvRecord.get(UploadHeaderType.EVENT_DATE),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                csvRecord.get(UploadHeaderType.CHAPTER),
                Integer.parseInt(csvRecord.get(UploadHeaderType.PARTNER_ID)),
                csvRecord.get(UploadHeaderType.GAME_NAME),
                Integer.parseInt(csvRecord.get(UploadHeaderType.SESSION_ID)));

        gameEvents.add(gameEvent);
      }

      return gameEvents;
    } catch (Exception e) {
      throw new CSVUtilException("fail to parse CSV file: ", e);
    }
  }
}
