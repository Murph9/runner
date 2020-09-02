package runner.saving;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RecordManager {
    private static final String FOLDER = System.getProperty("user.home") + "/.murph9/runner/";

    private static Map<Integer, Float> _records;

    private static File getFile() {
        String fileName = Paths.get(RecordManager.FOLDER, "high.score").toString();
        return new File(fileName);
    }

    private static void loadRecords() {
        _records = new HashMap<>(); // start to read records
        Scanner scoresScanner = null;
        try {
            File saveFile = getFile();

            // create file and its directory if they don't exist
            saveFile.getParentFile().mkdirs();
            saveFile.createNewFile();

            // read the file int per int
            FileReader reader = new FileReader(saveFile);
            scoresScanner = new Scanner(reader);
            while (scoresScanner.hasNext()) {
                _records.put(scoresScanner.nextInt(), scoresScanner.nextFloat());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (scoresScanner != null)
                scoresScanner.close();
        }
    }

    /** Returns the current record */
    public static float getRecord(int gameCount) {
        if (_records == null)
            loadRecords();

        if (_records.containsKey(gameCount)) {
            return _records.get(gameCount);
        }

        return 0;
    }

    /** Saves the record to file based on the type */
    public static void saveRecord(int gameCount, float score) {
        if (getRecord(gameCount) >= score)
            return; //no need to save, not a better score
        
        _records.put(gameCount, score);

        File saveFile = getFile();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(saveFile, false)));
            for (var r: _records.entrySet()) {
                out.println(r.getKey());
                out.println(r.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
        }
    }
}
