package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Student;
import util.NotificationController;
import view.tm.StudentTM;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class ManageStudentFormController implements Initializable {
    public AnchorPane StudentFormContext;
    public AnchorPane StudentAnchorPane;
    public JFXTextField txtSId;
    public JFXTextField txtSName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtNic;
    public JFXTextField txtAddress;
    public JFXButton btnSave;
    public TableView<StudentTM> tblStudent;
    public JFXButton btnDelete;
    public JFXButton btnAddNew;
    public JFXTextField txtSearch;

    /**
     * Close OnAction
     */
    public void BtnCloseOnAction(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Restore OnAction
     */
    public void BtnRestoreDownOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setResizable(true);
    }

    /**
     * Minimize OnAction
     */
    public void BtnMinimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Save New Student and Update OnAction
     */
    public void btnSave_OnAction(ActionEvent actionEvent) {

        String id = txtSId.getText();
        String name = txtSName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();

        if (!name.matches("^[A-z ]{3,45}$")) {
            NotificationController.Warring("Student Name", "Invalid Name");
            txtSName.requestFocus();
            return;
        } else if (!contact.matches("^(07(0|1|2|4|5|6|7|8)[0-9]{7})$")) {
            NotificationController.Warring("Student Contact", "Invalid Contact");
            txtContact.requestFocus();
            return;
        } else if (!nic.matches("^([0-9]{12}|[0-9V]{10})$")) {
            NotificationController.Warring("Student NIC", "Invalid NIC");
            txtNic.requestFocus();
            return;
        } else if (!address.matches("^[A-z ]{3,45}$")) {
            NotificationController.Warring("Student Address", "Invalid Address");
            txtAddress.requestFocus();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("save")) {
            /*Save Customer*/
            try {
                if (existStudent(id)) {
                    NotificationController.WarringError("Save Customer Warning", id, "Already exists ");
                }
                Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Student (studentId,studentName, email,contact,address, nic) VALUES (?,?,?,?,?,?)");
                pstm.setString(1, id);
                pstm.setString(2, name);
                pstm.setString(3, email);
                pstm.setString(4, contact);
                pstm.setString(5, address);
                pstm.setString(6, nic);
                pstm.executeUpdate();
                tblStudent.getItems().add(new StudentTM(id, name, email, contact,address, nic));
                NotificationController.SuccessfulTableNotification("Save", "Student");
            } catch (SQLException e) {
                NotificationController.WarringError("Save Student Warning", id + e.getMessage(), "Failed to save the Student ");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            /*Update customer*/
            try {
                if (!existStudent(id)) {
                    NotificationController.WarringError("Update Student Warning", id, "There is no such Student associated with the ");
                }
                Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement pstm = connection.prepareStatement("UPDATE Student SET studentName=?, email=?,contact=?, address=?,nic=? WHERE studentId=?");
                pstm.setString(1, name);
                pstm.setString(2, email);
                pstm.setString(3, contact);
                pstm.setString(4, address);
                pstm.setString(5, nic);
                pstm.setString(6, id);
                pstm.executeUpdate();
                NotificationController.SuccessfulTableNotification("Update", "Student");
            } catch (SQLException e) {
                NotificationController.WarringError("Update Student Warning", id + e.getMessage(), "Failed to update the Student ");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            StudentTM selectedStudent = tblStudent.getSelectionModel().getSelectedItem();
            selectedStudent.setStudentName(name);
            selectedStudent.setEmail(email);
            selectedStudent.setContact(contact);
            selectedStudent.setAddress(address);
            selectedStudent.setNic(nic);
            tblStudent.refresh();
        }

        btnAddNew.fire();
    }


    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }

    /**
     * Add New OnAction
     */
    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        txtSId.setDisable(false);
        txtSName.setDisable(false);
        txtEmail.setDisable(false);
        txtContact.setDisable(false);
        txtNic.setDisable(false);
        txtAddress.setDisable(false);

        txtSId.clear();
        txtSName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtNic.clear();
        txtAddress.clear();

        txtSId.setText(generateNewId());
        txtSName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblStudent.getSelectionModel().clearSelection();
    }

    /**
     * Generate New Id OnAction
     */
    private String generateNewId() {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            ResultSet rst = connection.createStatement().executeQuery("SELECT studentId FROM Student ORDER BY studentId DESC LIMIT 1");
            if (rst.next()) {
                String id = rst.getString("studentId");
                int newItemId = Integer.parseInt(id.replace("STU-", "")) + 1;
                return String.format("STU-%03d", newItemId);
            } else {
                return "STU-001";
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "STU-001";
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("studentName"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nic"));

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                //------------------------Text Filed Load----------------------//
                txtSId.setText(newValue.getStudentId());
                txtSName.setText(newValue.getStudentName());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());
                txtNic.setText(newValue.getNic());


                txtSId.setDisable(false);
                txtSName.setDisable(false);
                txtEmail.setDisable(false);
                txtContact.setDisable(false);
                txtNic.setDisable(false);
                txtAddress.setDisable(false);
            }
        });

        txtAddress.setOnAction(event -> btnSave.fire());

        loadAllStudent();
        initUI();

    }

    private void initUI() {
        txtSId.clear();
        txtSName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtNic.clear();
        txtAddress.clear();

        txtSId.setDisable(true);
        txtSName.setDisable(true);
        txtEmail.setDisable(true);
        txtContact.setDisable(true);
        txtNic.setDisable(true);
        txtAddress.setDisable(true);

        txtSId.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void loadAllStudent() {
        tblStudent.getItems().clear();
        /*Get all Student*/
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Student");

            while (rst.next()) {
                tblStudent.getItems().add(new StudentTM(rst.getString("studentId"), rst.getString("studentName"), rst.getString("email"), rst.getString("contact"), rst.getString("address"), rst.getString("nic")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private boolean existStudent(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT studentId FROM Student WHERE studentId=?");
        pstm.setString(1, id);
        return pstm.executeQuery().next();
    }
}
