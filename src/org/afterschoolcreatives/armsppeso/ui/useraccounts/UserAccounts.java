/**
 *
 * Automated Record Management System
 * Public Placement and Employment Service Office
 * Bulacan State University, City of Malolos
 *
 * Copyright 2018 Jhon Melvin Perello, Joemar De La Cruz, Ericka Joy Dela Cruz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package org.afterschoolcreatives.armsppeso.ui.useraccounts;

import org.afterschoolcreatives.armsppeso.Context;
import org.afterschoolcreatives.armsppeso.models.UserAccountModel;
import org.afterschoolcreatives.polaris.javafx.fxml.PolarisFxController;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableCell;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableRow;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableView;
import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.armsppeso.ui.MainMenu;

/**
 *
 * @author Joemar
 */
public class UserAccounts extends PolarisFxController {

    @FXML
    private TextField txt_search_key;

    @FXML
    private JFXButton btn_search;

    @FXML
    private VBox vbox_message;

    @FXML
    private Label lbl_message;

    @FXML
    private VBox tbl_accounts;

    @FXML
    private TextField txt_full_name;

    @FXML
    private Label lbl_error_full_name;

    @FXML
    private TextField txt_username;

    @FXML
    private Label lbl_error_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private Label lbl_error_password;

    @FXML
    private PasswordField txt_password_confirm;

    @FXML
    private Label lbl_error_password_confirm;

    @FXML
    private ComboBox<String> cmb_account_type;

    @FXML
    private Label lbl_error_account_type;

    @FXML
    private JFXButton btn_add_account;

    @FXML
    private JFXButton btn_back;

    @FXML
    private JFXButton btn_clear;

    @FXML
    private Label lbl_created;

    @FXML
    private JFXButton btn_back_to_add;
    
    private SimpleTable tableAccounts = new SimpleTable();

    @Override
    protected void setup() {
        this.resetLabelMessages();
        this.vbox_message.setVisible(false);
        // enable upon start
        this.btn_back_to_add.setDisable(true);

        // fetch and create table of accounts
        ArrayList<UserAccountModel> allRecords = UserAccountModel.getAllRecords();
        this.constructTable(allRecords);
        this.cmb_account_type.getItems().add("User");
        this.cmb_account_type.getItems().add("Administrator");
        this.cmb_account_type.getSelectionModel().selectFirst();
        
        //events
        this.btn_add_account.setOnMouseClicked(value -> {
            if (this.btn_add_account.getText().equalsIgnoreCase("Update Account")) {
                if (selected != null) {
                    this.validate(false);
                } else {
                    this.backToAddAccount();
                }
            } else {
                this.validate(true);
            }
            ArrayList<UserAccountModel> records = UserAccountModel.getAllRecords();
            this.constructTable(records);
        });
        this.btn_back.setOnMouseClicked(value -> {
            MainMenu mm = new MainMenu();
            this.changeRoot(mm.load());
            value.consume();
        });
        this.btn_clear.setOnMouseClicked(value -> {
            this.clearAllFields();
        });
        this.btn_back_to_add.setOnMouseClicked(value->{
            this.backToAddAccount();
        });
    }

    private void clearAllFields() {
        this.resetLabelMessages();
        this.txt_full_name.setText("");
        this.txt_password.setText("");
        this.txt_password_confirm.setText("");
        this.txt_username.setText("");
    }

    private void resetLabelMessages() {
        this.lbl_error_account_type.setText("");
        this.lbl_error_full_name.setText("");
        this.lbl_error_password.setText("");
        this.lbl_error_password.setText("");
        this.lbl_error_password_confirm.setText("");
        this.lbl_error_username.setText("");
        this.lbl_created.setText("NONE");
    }

    private void validate(boolean insert) {
        this.resetLabelMessages();
        this.btn_back_to_add.setDisable(true);

        String full_name = this.txt_full_name.getText();
        if (full_name.isEmpty()) {
            this.lbl_error_full_name.setText("Name must not be empty.");
            return;
        }
        String username = this.txt_username.getText();
        if (username.isEmpty()) {
            this.lbl_error_username.setText("Username must not be empty.");
            return;
        }
        // check if existing
        int count = UserAccountModel.isExisting(this.txt_username.getText().toUpperCase(), (insert ? null : selected.getId()));
        if (count > (insert ? 0 : 1)) {
            this.lbl_error_username.setText("Username already exists.");
            return;
        }
        String password = this.txt_password.getText();
        if (password.isEmpty()) {
            this.lbl_error_password.setText("Password must not be empty.");
            return;
        }
        if (password.length() < 6) {
            this.lbl_error_password.setText("Password must be at least six(6) in length.");
            return;
        }
        String password_confirm = this.txt_password_confirm.getText();
        if (password_confirm.isEmpty()) {
            this.lbl_error_password_confirm.setText("Please re-enter password.");
            return;
        }
        if (!password.equals(password_confirm)) {
            this.lbl_error_password.setText("Password not matched.");
            this.lbl_error_password_confirm.setText("Password not matched.");
            return;
        }

        if (insert) {
            this.insertAccount();
        } else {
            selected.setAccount_type(cmb_account_type.getSelectionModel().getSelectedItem().toUpperCase());
            selected.setFull_name(txt_full_name.getText().toUpperCase());
            selected.setUsername(txt_username.getText().toUpperCase());
            boolean res = selected.update();
            this.showResult(res, false);
            if (res) {
                this.backToAddAccount();
            }
        }
    }

    private void backToAddAccount() {
        selected = null;
        this.clearAllFields();
        this.btn_add_account.setText("Add Account");
        this.txt_password.setDisable(false);
        this.txt_password_confirm.setDisable(false);
        this.lbl_created.setText("NONE");
        this.btn_back_to_add.setDisable(true);
        this.btn_add_account.setDisable(false);
        this.cmb_account_type.setDisable(false);
    }

    private void insertAccount() {
        UserAccountModel ua = new UserAccountModel();
        ua.setAccount_type(cmb_account_type.getSelectionModel().getSelectedItem().toUpperCase());
        ua.setFull_name(txt_full_name.getText().toUpperCase());
        ua.setPassword(txt_password.getText());
        ua.setUsername(txt_username.getText().toUpperCase());
        ua.setCreatedBy(Context.app().getAccountName().toUpperCase());
        ua.setCreatedDate(new Date());

        boolean res = ua.insert();
        this.showResult(res, true);
    }

    private void showResult(boolean res, boolean insert) {
        if (res) {
            PolarisDialog.create(PolarisDialog.Type.INFORMATION)
                    .setOwner(this.getStage())
                    .setHeaderText("Successfully " + (insert ? "Created!" : "Updated!"))
                    .setContentText(insert ? "New account is successfully created." : "Account successfully updated.")
                    .setTitle(insert ? "Add Account" : "Update Account")
                    .show();
        } else {
            PolarisDialog.create(PolarisDialog.Type.ERROR)
                    .setHeaderText("Failed!")
                    .setContentText("No account is " + (insert ? "created." : "updated."))
                    .setTitle(insert ? "Add Account" : "Update Account")
                    .show();
        }
    }

    private void constructTable(ArrayList<UserAccountModel> allRecords) {
        this.tableAccounts.getChildren().clear();
        this.vbox_message.setVisible(false);
        if (allRecords == null || allRecords.isEmpty()) {
            this.lbl_message.setText("No result found.");
            this.vbox_message.setVisible(true);
        } else {
            for (UserAccountModel account : allRecords) {
                this.createRow(account);
            }
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tableAccounts);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(tbl_accounts);
    }

    private UserAccountModel selected;

    private void createRow(UserAccountModel account) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);
        RowAccount userRow = new RowAccount();
        userRow.load();
        //informations
        userRow.getLbl_created_by().setText(account.getCreatedBy());
        System.out.println("CREATEDBY: "+account.getCreatedBy());
        if(account.getCreatedDate() != null) {
            System.out.println("DATE: "+account.getCreatedDate());
            userRow.getLbl_date().setText(account.getCreatedDate());
        } else {
            userRow.getLbl_date().setText("");
        }
        userRow.getLbl_full_name().setText(account.getFull_name());
        userRow.getLbl_position().setText(account.getAccount_type());

        // events
        userRow.getBtn_remove().setOnMouseClicked(value -> {
            Optional<ButtonType> res = PolarisDialog.create(PolarisDialog.Type.CONFIRMATION)
                    .setTitle("Exit")
                    .setOwner(this.getStage())
                    .setHeaderText("Remove Account?")
                    .setContentText("Are you sure you want to remove this account with a username of " + account.getUsername().toUpperCase() + "?")
                    .showAndWait();
            if (res.get().getText().equals("OK")) {
                UserAccountModel.delete(account.getId());
                this.tableAccounts.getChildren().remove(row);
                this.backToAddAccount();
            }
        });
        boolean disableRemove = account.getAccount_type().equalsIgnoreCase("ADMINISTRATOR") && Context.app().getAccountType().equalsIgnoreCase("ADMINISTRATOR");
        userRow.getBtn_remove().setDisable(disableRemove);
        userRow.getBtn_edit().setOnMouseClicked(value -> {
            this.btn_back_to_add.setDisable(false);
            this.resetLabelMessages();
            this.txt_full_name.setText(account.getFull_name());
            this.txt_password.setText(account.getPassword());
            this.txt_password.setDisable(true);
            this.txt_password_confirm.setText(account.getPassword());
            this.txt_password_confirm.setDisable(true);
            this.txt_username.setText(account.getUsername());
            this.cmb_account_type.getSelectionModel().select(account.getAccount_type());
            if(account.getCreatedDate()!=null) {
                this.lbl_created.setText(account.getCreatedDate() + " by " + account.getCreatedBy());
            } else {
                this.lbl_created.setText("UNKNOWN");
            }
            this.btn_add_account.setText("Update Account");
            selected = account;
            boolean disableButtons = account.getAccount_type().equalsIgnoreCase("ADMINISTRATOR") && Context.app().getAccountType().equalsIgnoreCase("ADMINISTRATOR");
            this.cmb_account_type.setDisable(disableButtons);
            this.btn_add_account.setDisable(disableButtons);
        });
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(userRow.getRootPane());

        row.addCell(cellParent);
        tableAccounts.addRow(row);
    }

}
