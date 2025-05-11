package com.app.foodguard.utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableViewUtils {

    public static <T, V> void alinharColunaADireita(TableColumn<T, V> coluna) {
        coluna.getStyleClass().add("numeric-column");
        coluna.setCellFactory(getCellFactoryComAlinhamento("CENTER-RIGHT"));
    }

    public static <T, V> void alinharColunaAoCentro(TableColumn<T, V> coluna) {
        coluna.setCellFactory(getCellFactoryComAlinhamento("CENTER"));
    }

    public static <T, V> void alinharColunaAEsquerda(TableColumn<T, V> coluna) {
        coluna.setCellFactory(getCellFactoryComAlinhamento("CENTER-LEFT"));
    }

    private static <T, V> Callback<TableColumn<T, V>, TableCell<T, V>> getCellFactoryComAlinhamento(String alinhamento) {
        return coluna -> new TableCell<>() {
            @Override
            protected void updateItem(V item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setStyle("-fx-alignment: " + alinhamento + ";");
            }
        };
    }
}
