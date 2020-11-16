package sample;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
;import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Controller {
    @FXML
    private Button btnStart;
    @FXML
    private Label word1;

    @FXML
    private Label word2;

    @FXML
    private Label word3;
    @FXML
    private Label output;
    @FXML
    private Button skipBtn;

    Configuration configuration;
    LiveSpeechRecognizer recognizer;
    public static ArrayList<String> words;
    private String fileName = "9010";


    @FXML
    void skipBtnClicked(ActionEvent event) {
        randomLabelWord();
        changeColor(null, word1);
    }

    @FXML
    void btnStartClicked(ActionEvent event) throws IOException {
        String actual = word1.getText()+word2.getText()+word3.getText();
        recognizer.startRecognition(true);

        SpeechResult result=null;
        result = recognizer.getResult();
        String saidText = result.getHypothesis();
        recognizer.stopRecognition();
        System.out.println("WORD IS  = "+actual+" YOU SAID = "+saidText);
        String[] saidTexts = saidText.split(" ");

      //  boolean correct = changeColor(saidTexts[0], word1) && changeColor(saidTexts[1], word2)&& changeColor(saidTexts[2], word3);
      boolean correct = changeColor(saidText, word1);
        if (correct){
            randomLabelWord();
            changeColor(null, word1);

            output.setText("Correct !! Try next word");
            output.setTextFill(Color.rgb(0, 100, 0));

        }else{
            output.setText("Incorrect !! Try again");
            output.setTextFill(Color.color(0.9, 0, 0));
        }
    }

    public Controller() throws  Exception{
        configuration = new Configuration();

        configuration.setDictionaryPath(fileName+".dic");
        configuration.setLanguageModelPath(fileName+".lm");
        configuration.setAcousticModelPath("en-us");
        recognizer = new LiveSpeechRecognizer(configuration);
        getWords();
        //randomLabelWord();
    }

    public void getWords()  {

        try{
            this.words =  new ArrayList();
            File myFile = new File(fileName+".dic");
            Scanner scanner = new Scanner(myFile);
            while(scanner.hasNextLine()){
                String word = scanner.nextLine().split("\t")[0];
                words.add(word);
            }
            scanner.close();
        }catch(Exception e){
            System.out.println("cannot read from file");
        }

    }

    public void randomLabelWord(){
        if (words.isEmpty())
            getWords();


        Random r = new Random();
        int index1 = r.nextInt(words.size());
       // int index2 = r.nextInt(words.size());
       // int index3 = r.nextInt(words.size());
        String word = words.get(index1);
        if (word.indexOf('(')>-1)
        word = word.substring(0,word.indexOf('('));
        word1.setText(word);
       // word2.setText(words.get(index2) +",");
       // word3.setText(words.get(index3));
    }

    public void initialize() {

        randomLabelWord();

    }
    public boolean changeColor(String word, Label label){
        if (word == null){
            label.setTextFill(Color.color(0,0,0));
            return true;
        }

        if (! (word).equals(label.getText())) {
            label.setTextFill(Color.color(1, 0, 0));
            return false;
        }
        else{
            label.setTextFill(Color.color(0.4, 1, 0.4));
            return true;
        }
    }
}
