package bilclickhost;

import question.history.* ;
import java.util.ArrayList;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HistoryGraph
{
    final static String choiceA = "A";
    final static String choiceB = "B";
    final static String choiceC = "C";
    final static String choiceD = "D";
    final static String choiceE = "E";
    final static String choiceF = "F";

    public static VBox historyGraph(Entry entry)
    {
        Stage stage = new Stage();
        VBox vbox = new VBox();
        stage.setTitle("History");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle(entry.getDateAsked());
        xAxis.setLabel("Choices");
        yAxis.setLabel("Answer Count");

        ArrayList<String> choices = new ArrayList<String>();
        choices.add(choiceA);
        choices.add(choiceB);
        choices.add(choiceC);
        choices.add(choiceD);
        choices.add(choiceE);
        choices.add(choiceF);
        ArrayList<Integer> choiceDistribution = entry.getChoiceDistribution();

        XYChart.Series series = new XYChart.Series();
        series.setName("Participants");

        for ( int i = 0; i < choiceDistribution.size(); i++)
            series.getData().add(new XYChart.Data(choices.get(i), choiceDistribution.get(i)));

        vbox.setMaxSize(300,400);
        bc.getData().addAll(series);
        vbox.getChildren().add(bc);
        return vbox;
    }

}

