package com.app.foodguard;

import com.app.foodguard.controller.DashboardController;
import com.app.foodguard.model.Lote;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/app/foodguard/dashboard/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());
            DashboardController controller = loader.getController();

            List<Lote> lotes = carregarLotes();
            controller.carregarNotificacoes(lotes);

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Lote> carregarLotes() {
        List<Lote> lotes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                MainApplication.class.getResourceAsStream("/csv/lotes.csv")))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                Lote lote = new Lote(
                        Integer.parseInt(dados[0]),
                        Integer.parseInt(dados[1]),
                        Float.parseFloat(dados[2]),
                        Float.parseFloat(dados[3]),
                        LocalDate.parse(dados[4]),
                        LocalDate.parse(dados[5]),
                        dados[6]
                );
                lotes.add(lote);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lotes;
    }

    public static void main(String[] args) {
        launch();
    }
}
