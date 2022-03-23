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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author zakesekresa
 */
public class AddKlubController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private Label lbNameError;

    /**
     * Initializes the controller class.
     */
    private static TextField tfNameOut;

    private static Label lbNameErrorOut;
    @FXML
    private Label lbIconError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfNameOut = tfName;
        lbNameErrorOut = lbNameError;
    }

    @FXML
    private void commit(ActionEvent event) {
        ListKlubsController.selectedKlubViewModel = ListKlubsController.selectedKlubViewModel != null ? ListKlubsController.selectedKlubViewModel : new KlubViewModel(null);

        if (formValid()) {
            ListKlubsController.selectedKlubViewModel.getKlub().setName(tfName.getText().trim());
            if (ListKlubsController.selectedKlubViewModel.getKlub().getIDKlub() == 0) {
                ListKlubsController.klubs.add(ListKlubsController.selectedKlubViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository(Klub.class).update(ListKlubsController.selectedKlubViewModel.getKlub());
                    ListKlubsController.tvKlubsOut.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(AddKlubController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ListKlubsController.selectedKlubViewModel = null;
            TabMenuController.switchTab(2);
            ListKlubsController.tvKlubsOut.refresh();
            resetForm();
        } else {
            lbNameError.setVisible(true);
        }
    }

    private boolean formValid() {
        return !tfName.getText().trim().isEmpty();
    }

    public static void fillForm() {
        tfNameOut.setText(ListKlubsController.selectedKlubViewModel.getKlub().getName());
    }

    private void resetForm() {
        ListKlubsController.selectedKlubViewModel = null;
        tfNameOut.clear();
        lbNameErrorOut.setVisible(false);
    }

    @FXML
    private void reset(ActionEvent event) {
        resetForm();
    }

}
