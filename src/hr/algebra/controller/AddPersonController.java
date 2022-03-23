/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Person;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.KlubViewModel;
import hr.algebra.viewmodel.PersonViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author zakesekresa
 */
public class AddPersonController implements Initializable {

    @FXML
    private ImageView ivPerson;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfEmail;
    @FXML
    private Label lbFirstNameError;
    @FXML
    private Label lbAgeError;
    @FXML
    private Label lbLastNameError;
    @FXML
    private Label lbEmailError;
    @FXML
    private Label lbIconError;
    private static Label lbIconErrorOut;

    public static TextField tfFirstNameOut, tfLastNameOut, tfAgeOut, tfEmailOut;
    public static ImageView ivPersonOut;

    private static Map<TextField, Label> validationMap;
    @FXML
    private ComboBox<KlubViewModel> cbKlub;

    private static ComboBox<KlubViewModel> cbKlubOut;
    @FXML
    private Label lblKlubError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfFirstNameOut = tfFirstName;
        tfLastNameOut = tfLastName;
        tfAgeOut = tfAge;
        tfEmailOut = tfEmail;
        ivPersonOut = ivPerson;
        addIntegerMask(tfAge);
        initValidation();
        cbKlubOut = cbKlub;
        cbKlub.setItems(ListKlubsController.klubs);
        lbIconErrorOut = lbIconError;
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty() || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });

        if (ListPeopleController.selectedPersonViewModel.getPictureProperty().get() == null) {
            lbIconError.setVisible(true);
            ok.set(false);
        } else {
            lbIconError.setVisible(false);
        }
        if (cbKlub.getSelectionModel().getSelectedItem() == null) {
            ok.set(false);
            lblKlubError.setVisible(true);
        } else {
            lblKlubError.setVisible(false);
        }

        return ok.get();
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbFirstNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbLastNameError),
                new AbstractMap.SimpleImmutableEntry<>(tfAge, lbAgeError),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbEmailError))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void addIntegerMask(TextField tf) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("\\d*")) {
                return change;
            }
            return null;
        };
        tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

    @FXML
    private void commit(ActionEvent event) {

        ListPeopleController.selectedPersonViewModel = ListPeopleController.selectedPersonViewModel != null ? ListPeopleController.selectedPersonViewModel : new PersonViewModel(null);

        if (formValid()) {
            ListPeopleController.selectedPersonViewModel.getPerson().setFirstName(tfFirstName.getText().trim());
            ListPeopleController.selectedPersonViewModel.getPerson().setLastName(tfLastName.getText().trim());
            ListPeopleController.selectedPersonViewModel.getPerson().setAge(Integer.valueOf(tfAge.getText().trim()));
            ListPeopleController.selectedPersonViewModel.getPerson().setEmail(tfEmail.getText().trim());
            ListPeopleController.selectedPersonViewModel.getPerson().setOmiljenKlub(cbKlub.getSelectionModel().getSelectedItem().getKlub());
            if (ListPeopleController.selectedPersonViewModel.getPerson().getIDPerson() == 0) {
                ListPeopleController.people.add(ListPeopleController.selectedPersonViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository(Person.class).update(ListPeopleController.selectedPersonViewModel.getPerson());

                } catch (Exception ex) {
                    Logger.getLogger(AddPersonController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            TabMenuController.switchTab(0);
            ListPeopleController.tvPeopleOut.refresh();
            resetForm();
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        ListPeopleController.selectedPersonViewModel = ListPeopleController.selectedPersonViewModel != null ? ListPeopleController.selectedPersonViewModel : new PersonViewModel(null);
        File file = FileUtils.uploadFileDialog(tfAge.getScene().getWindow(), "jpg", "jpeg", "png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            try {
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                ListPeopleController.selectedPersonViewModel.getPerson().setPicture(ImageUtils.imageToByteArray(image, ext));
                ivPerson.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(AddPersonController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void fillForm() {

         try {
            Integer idKlub = ListPeopleController.selectedPersonViewModel.getPerson().getOmiljenKlub().getIDKlub();
            KlubViewModel get = ListKlubsController.klubs.filtered(kvm -> kvm.getKlub().getIDKlub() == idKlub).get(0);
            cbKlubOut.getSelectionModel().select(get);
        } catch (Exception e) {
        }
    
        tfFirstNameOut.setText(ListPeopleController.selectedPersonViewModel.getFirstNameProperty().get());
        tfLastNameOut.setText(ListPeopleController.selectedPersonViewModel.getLastNameProperty().get());
        tfAgeOut.setText(String.valueOf(ListPeopleController.selectedPersonViewModel.getAgeProperty().get()));
        tfEmailOut.setText(ListPeopleController.selectedPersonViewModel.getEmailProperty().get());
        ivPersonOut.setImage(ListPeopleController.selectedPersonViewModel.getPictureProperty().get() != null ? new Image(new ByteArrayInputStream(ListPeopleController.selectedPersonViewModel.getPictureProperty().get())) : new Image(new File("src/assets/no_image.png").toURI().toString()));

    }

    private void resetForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
        lbIconErrorOut.setVisible(false);
        ListPeopleController.selectedPersonViewModel = new PersonViewModel(null);
    }

    @FXML
    private void reset(ActionEvent event) {
        resetForm();
        fillForm();
        ListPeopleController.selectedPersonViewModel = null;
    }

}
