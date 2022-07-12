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
import view.tm.StudentTM;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    /** Close OnAction */
    public void BtnCloseOnAction(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    /** Restore OnAction */
    public void BtnRestoreDownOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setResizable(true);
    }

    /** Minimize OnAction */
    public void BtnMinimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }

    /** Add New OnAction */
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

    /** GenerateNewId OnAction */
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
                txtNic.setText(newValue.getNic());
                txtAddress.setText(newValue.getAddress());


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
}
