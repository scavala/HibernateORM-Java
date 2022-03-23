/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Klub;
import hr.algebra.viewmodel.KlubViewModel;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author zakesekresa
 */
public class ListKlubsController implements Initializable {

    public static final ObservableList<KlubViewModel> klubs = FXCollections.observableArrayList();

    public static KlubViewModel selectedKlubViewModel;

    @FXML
    private TableView<KlubViewModel> tvKlubs;
    @FXML
    private TableColumn<KlubViewModel, String> tcName;

    public static TableView<KlubViewModel> tvKlubsOut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initKlubs();
        initTable();
        setupListeners();
        tvKlubsOut = tvKlubs;
    }

    private void initTable() {
        tcName.setCellValueFactory(klub -> klub.getValue().getNameProperty());
        tvKlubs.setItems(klubs);
    }

    private void initKlubs() {
        try {
            RepositoryFactory.getRepository(Klub.class).getAll().forEach(klub -> klubs.add(new KlubViewModel((Klub) klub)));
        } catch (Exception ex) {
            Logger.getLogger(ListKlubsController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private void setupListeners() {
        klubs.addListener((ListChangeListener.Change<? extends KlubViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository(Klub.class).delete(pvm.getKlub());
                        } catch (Exception ex) {
                            Logger.getLogger(ListKlubsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idKlub = RepositoryFactory.getRepository(Klub.class).add(pvm.getKlub());
                            pvm.getKlub().setIDKlub(idKlub);
                        } catch (Exception ex) {
                            Logger.getLogger(ListKlubsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvKlubs.getSelectionModel().getSelectedItem() != null) {
            ListPeopleController.people.removeIf(p -> p.getKlubProperty().getValue().getIDKlub() == tvKlubs.getSelectionModel().getSelectedItem().getKlub().getIDKlub());
            klubs.remove(tvKlubs.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvKlubs.getSelectionModel().getSelectedItem() != null) {
            selectedKlubViewModel = tvKlubs.getSelectionModel().getSelectedItem();
            AddKlubController.fillForm();
            TabMenuController.switchTab(3);
        }

    }

}
