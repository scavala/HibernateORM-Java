/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Klub;
import hr.algebra.model.Person;
import hr.algebra.viewmodel.PersonViewModel;
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
public class ListPeopleController implements Initializable {

    public static final ObservableList<PersonViewModel> people = FXCollections.observableArrayList();

    public static PersonViewModel selectedPersonViewModel;

    @FXML
    private TableView<PersonViewModel> tvPeople;
    @FXML
    private TableColumn<PersonViewModel, String> tcFirstName, tcLastName, tcEmail;

    @FXML
    private TableColumn<PersonViewModel, Integer> tcAge;
    @FXML
    private TableColumn<PersonViewModel, Klub> tcKlub;

    public static TableView<PersonViewModel> tvPeopleOut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initPeople();
        initTable();
        setupListeners();
        tvPeopleOut = tvPeople;
    }

    private void initTable() {
        tcFirstName.setCellValueFactory(person -> person.getValue().getFirstNameProperty());
        tcLastName.setCellValueFactory(person -> person.getValue().getLastNameProperty());
        tcAge.setCellValueFactory(person -> person.getValue().getAgeProperty().asObject());
        tcEmail.setCellValueFactory(person -> person.getValue().getEmailProperty());
        tcKlub.setCellValueFactory(person -> person.getValue().getKlubProperty());
        tvPeople.setItems(people);
    }

    private void initPeople() {
        try {
            RepositoryFactory.getRepository(Person.class).getAll().forEach(person -> people.add(new PersonViewModel((Person) person)));
        } catch (Exception ex) {
            Logger.getLogger(ListPeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private void setupListeners() {

        people.addListener((ListChangeListener.Change<? extends PersonViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository(Person.class).delete(pvm.getPerson());
                        } catch (Exception ex) {
                            Logger.getLogger(ListPeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idPerson = RepositoryFactory.getRepository(Person.class).add(pvm.getPerson());
                            pvm.getPerson().setIDPerson(idPerson);
                        } catch (Exception ex) {
                            Logger.getLogger(ListPeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            people.remove(tvPeople.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            selectedPersonViewModel = tvPeople.getSelectionModel().getSelectedItem();
            AddPersonController.fillForm();
            TabMenuController.switchTab(1);
        }
    }
}
