package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class ManageStudentFormController {
    public AnchorPane StudentFormContext;
    public AnchorPane StudentAnchorPane;
    public JFXTextField txtSId;
    public JFXTextField txtSName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtNic;
    public JFXTextField txtAddress;
    public JFXButton btnSave;
    public TableView tblStudent;
    public JFXButton btnDelete;
    public JFXButton btnAddNew;
    public JFXTextField txtSearch;

    public void BtnCloseOnAction(MouseEvent mouseEvent) {
    }

    public void BtnRestoreDownOnAction(MouseEvent mouseEvent) {
    }

    public void BtnMinimizeOnAction(MouseEvent mouseEvent) {
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
    }
}
