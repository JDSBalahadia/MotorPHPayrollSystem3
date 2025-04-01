package employeeManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class EmployeeManagementSystem {
    static Map<String, String> users = new HashMap<>(); // User database
    static Map<String, String[]> employeeData = new HashMap<>(); // Stores employee input
    static Map<String, String> overtimeRequests = new HashMap<>(); // Stores overtime requests


    static {
        users.put("admin", "password123");
        users.put("employee", "password456");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}

class LoginScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Employee Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(new LoginActionListener());
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (EmployeeManagementSystem.users.containsKey(username) &&
                EmployeeManagementSystem.users.get(username).equals(password)) {
                dispose();
                if (username.equals("admin")) {
                    new EmployeeProfile(username);
                } else {
                    new ViewSavedEmployeeDetails();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid login!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class EmployeeProfile extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField nameField, emailField, addressField, deductionsField, taxField, overtimeField,
                       attendanceField, payDateField, paySlipsField, salaryField;
    private boolean isAdmin;
    private boolean isEmployee;

    public EmployeeProfile(String username) {
        this.isAdmin = username.equals("admin");
        this.isEmployee = username.equals("employee");

        setTitle("Employee Profile");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(13, 2, 10, 10));


        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Deductions:"));
        deductionsField = new JTextField();
        panel.add(deductionsField);

        panel.add(new JLabel("Tax:"));
        taxField = new JTextField();
        panel.add(taxField);

        panel.add(new JLabel("Overtime:"));
        overtimeField = new JTextField();
        panel.add(overtimeField);

        panel.add(new JLabel("Attendance:"));
        attendanceField = new JTextField();
        panel.add(attendanceField);

        panel.add(new JLabel("Pay Date:"));
        payDateField = new JTextField();
        panel.add(payDateField);

        panel.add(new JLabel("Pay Slips:"));
        paySlipsField = new JTextField();
        panel.add(paySlipsField);

        panel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        panel.add(salaryField);
        
     
        
        JButton clearButton = new JButton("Clear");
        panel.add(clearButton);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveEmployeeData());

        JButton viewButton = new JButton("View Saved Data");
        viewButton.addActionListener(e -> new viewSavedEmployeeDetails(username));
        
        JButton payslipButton = new JButton("View Payslip");
        payslipButton.addActionListener(e -> viewPayslip()); 
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        if (isAdmin) {
            panel.add(saveButton);
        } else {
        	new ViewSavedEmployeeDetails(); // Employee cannot edit
        }
        
        if (isEmployee) { 
            JButton overtimeButton = new JButton("Request Overtime");
            overtimeButton.addActionListener(e -> requestOvertime()); 
            panel.add(overtimeButton);
        } else { 
            JButton viewOvertimeButton = new JButton("View Overtime Requests");
            viewOvertimeButton.addActionListener(e -> viewOvertimeRequests()); 
            panel.add(viewOvertimeButton);
        }


        panel.add(viewButton);
        panel.add(payslipButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
	
    private void requestOvertime() { //for employees to create 
        JFrame overtimeFrame = new JFrame("Overtime Request");
        overtimeFrame.setSize(300, 200);
        overtimeFrame.setLayout(new GridLayout(3, 2));

        overtimeFrame.add(new JLabel("Overtime Hours:"));
        JTextField txtOvertimeHours = new JTextField();
        overtimeFrame.add(txtOvertimeHours);

        JButton btnSubmit = new JButton("Submit Request");
        overtimeFrame.add(btnSubmit);

        btnSubmit.addActionListener(e -> {
            String overtimeHours = txtOvertimeHours.getText();
            if (!overtimeHours.isEmpty()) {
                EmployeeManagementSystem.overtimeRequests.put(getName(), overtimeHours);
                JOptionPane.showMessageDialog(overtimeFrame, "Overtime request submitted for admin approval.");
                overtimeFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(overtimeFrame, "Please enter overtime hours.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        overtimeFrame.setVisible(true);
    }


    private void viewOvertimeRequests() { //method for administrator to view them
        JFrame viewFrame = new JFrame("Overtime Requests");
        viewFrame.setSize(300, 200);
        viewFrame.setLayout(new GridLayout(0, 1));

        if (EmployeeManagementSystem.overtimeRequests.isEmpty()) {
            JOptionPane.showMessageDialog(viewFrame, "No overtime requests found.", "List of Overtime Requests", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Map.Entry<String, String> entry : EmployeeManagementSystem.overtimeRequests.entrySet()) {
            viewFrame.add(new JLabel(entry.getKey() + " requested " + entry.getValue() + " hours"));
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> viewFrame.dispose());
        viewFrame.add(closeButton);

        viewFrame.setVisible(true);
    }

	private void viewPayslip() {
		showPayslip();}
	
	private void saveEmployeeData() {
        if (!isAdmin) return;

        String[] employeeDetails = {
                nameField.getText(), emailField.getText(), addressField.getText(),
                deductionsField.getText(), taxField.getText(), overtimeField.getText(),
                attendanceField.getText(), payDateField.getText(), paySlipsField.getText(),
                salaryField.getText()
        };
        JTextField[] numericFields = {deductionsField, taxField, salaryField, overtimeField, payDateField};

        for (JTextField field : numericFields) {
            if (!field.getText().matches("[0-9+\\-*/.]+")) { // Allow numbers and basic symbols
                JOptionPane.showMessageDialog(this, "Only numbers and symbols (+, -, *, /, .) are allowed in Deductions, Tax, Salary, Paydate and Overtime!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        for (String detail : employeeDetails) {
            if (detail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled before saving!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        EmployeeManagementSystem.employeeData.put("admin", employeeDetails);
        JOptionPane.showMessageDialog(this, "Employee details saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

class viewSavedEmployeeDetails extends JFrame {
    	private static final long serialVersionUID = 1L;
        private boolean isAdmin;
		private boolean isEmployee;
        private JTable employeeTable;
        private DefaultTableModel tableModel;

        
        public viewSavedEmployeeDetails(String username) {
        	this.isAdmin = username.equals("admin");
        	this.isEmployee = username.equals("employee");
        	
        	setTitle("Saved Employee Details");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            tableModel = new DefaultTableModel();
            employeeTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(employeeTable);
            add(scrollPane, BorderLayout.CENTER);
            
            tableModel.setColumnIdentifiers(new String[]{
                    "Name", "Email", "Address", "Deductions", "Tax", "Overtime", "Attendance", "Pay Date", "Pay Slips", "Salary"
                });
            loadEmployeeData();
            
            String[] details = EmployeeManagementSystem.employeeData.get("admin"); // Show admin's saved data
            
            if (details == null) {
                JOptionPane.showMessageDialog(this, "No data found. Please save first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            //buttons panel for bottom. 
            //dear god... the buttons were dancing when I ran this without giving them directions
            JButton payslipButton = new JButton("View Payslip");
            payslipButton.addActionListener(e -> showPayslip());
            buttonsPanel.add(payslipButton);

            JButton logoutButton = new JButton("Logout");
            logoutButton.addActionListener(e -> logout());
            buttonsPanel.add(logoutButton);

            if (isAdmin) {
            	JButton viewOvertimeButton = new JButton("View Overtime Requests");
                viewOvertimeButton.addActionListener(e -> viewOvertimeRequests()); 
                buttonsPanel.add(viewOvertimeButton);
            }
            else { 
            JButton overtimeButton = new JButton("Request Overtime");
            overtimeButton.addActionListener(e -> requestOvertime());
            buttonsPanel.add(overtimeButton);
            }
            if (isAdmin) {
                JButton clearButton = new JButton("Clear");
                clearButton.addActionListener(e -> clearSelectedRow());
                buttonsPanel.add(clearButton);
            }
            
         
            
            add(buttonsPanel, BorderLayout.SOUTH);
            setVisible(true);
        }
        
        

        private void loadEmployeeData() {
            tableModel.setRowCount(0);
            String[] details = EmployeeManagementSystem.employeeData.get("admin");
            if (details != null) {
                tableModel.addRow(details);
            }
        }
        
        private void clearSelectedRow() {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this row?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                    EmployeeManagementSystem.employeeData.clear();
                    JOptionPane.showMessageDialog(this, "Row deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

      
   	    }

       
   
    static void showPayslip() {
        	
            JFrame payslipFrame = new JFrame("Payslip");
            payslipFrame.setSize(300, 200);
            payslipFrame.setLayout(new GridLayout(5, 2, 10, 10));
            String[] details = EmployeeManagementSystem.employeeData.get("admin"); // Show admin's saved data
            
            if (details == null) {
                JOptionPane.showMessageDialog(payslipFrame, "Employee data not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double basicSalary = Double.parseDouble(details[9]);
            double deductions = Double.parseDouble(details[3]);
            double netSalary = basicSalary - deductions; // subtract to get Net Salary

            
            payslipFrame.add(new JLabel("Employee Name:"));
            payslipFrame.add(new JLabel(details[0]));
            payslipFrame.add(new JLabel("Basic Salary: PHP"));
            payslipFrame.add(new JLabel("PHP" + details[9]));
            payslipFrame.add(new JLabel("Deductions: PHP"));
            payslipFrame.add(new JLabel("PHP" + details[3]));
            payslipFrame.add(new JLabel("Net Salary:"));
            
            payslipFrame.add(new JLabel("PHP" + String.valueOf(netSalary))); //answer from the subtraction
            
            JButton btnClose = new JButton("Close");
            payslipFrame.add(btnClose, BorderLayout.SOUTH);
            btnClose.addActionListener(e -> payslipFrame.dispose());
            
            payslipFrame.setVisible(true);
        }


    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginScreen();
        }
    }
}
