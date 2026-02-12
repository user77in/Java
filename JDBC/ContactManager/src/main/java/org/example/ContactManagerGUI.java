package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContactManagerGUI extends JFrame {

    private ContactDAO contactDAO;

    private JTextField nameField, phoneField, emailField, addressField, searchField;
    private JComboBox<String> categoryComboBox, filterCategoryComboBox;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, clearButton, searchButton, showAllButton;
    private JLabel totalContactsLabel;

    private int selectedContactId = -1;

    public ContactManagerGUI() {
        contactDAO = new ContactDAO();

        setTitle("Contact Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(240, 240, 240));
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        loadAllContacts();
        updateContactCount();
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("Contact Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        // Contact Count
        totalContactsLabel = new JLabel("Total Contacts : 0" + SwingConstants.RIGHT);
        totalContactsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalContactsLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        panel.add(totalContactsLabel);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.add(createFormPanel());
        panel.add(createTablePanel());
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2), "Contact Information", 0, 0,
                new Font("Arial", Font.BOLD, 14), new Color(52, 152, 219)));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        // name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name: *"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(nameField, gbc);

        // phone
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Phone: *"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email: "), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(emailField, gbc);

        // address
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Address: "), gbc);

        gbc.gridx = 1;
        addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(addressField, gbc);

        // category
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Category"), gbc);

        gbc.gridx = 1;
        String[] categories = {"Friends", "Family", "Work", "Other"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(createFormButtons(), gbc);
        return panel;
    }

    public JPanel createFormButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        addButton = new JButton("Add Contact");
        updateButton = new JButton("Update Contact");
        deleteButton = new JButton("Delete Contact");
        clearButton = new JButton("Clear Form");

        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> updateContact());
        deleteButton.addActionListener(e -> deleteContact());
        clearButton.addActionListener(e -> clearForm());
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Contact List",
                0,
                0,
                new Font("Arial", Font.BOLD, 14),
                new Color(52, 152, 219)));
        panel.setBackground(Color.WHITE);
        panel.add(createSearchPanel(), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Phone", "Email", "Category"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read only
            }
        };
        contactTable = new JTable(tableModel);
        contactTable.setFont(new Font("Arial", Font.PLAIN, 13));
        contactTable.setRowHeight(25);
        contactTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        contactTable.getTableHeader().setBackground(new Color(52, 152, 219));
        contactTable.getTableHeader().setForeground(Color.WHITE);

        // When row is clicked, fill form
        contactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
        JScrollPane scrollPane = new JScrollPane(contactTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Search: "));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchContacts());
        panel.add(searchButton);

        showAllButton = new JButton("Show All");
        showAllButton.addActionListener(e -> loadAllContacts());
        panel.add(showAllButton);

        panel.add(new JLabel("Filter by Category: "));
        String[] filterCategories = {"All", "Friends", "Family", "Work", "Together"};
        filterCategoryComboBox = new JComboBox<>(filterCategories);
        filterCategoryComboBox.addActionListener(e -> filterByCategory());
        panel.add(filterCategoryComboBox);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel infoLabel = new JLabel("Tip: Click a row in the table to edit or delete");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.WHITE);
        panel.add(infoLabel);
        return panel;
    }

    private void loadAllContacts(){
        tableModel.setRowCount(0); // clear table
        List<Contact> contacts = contactDAO.getAllContacts();
        for(Contact contact: contacts){
            Object[] row = {
                    contact.getId(),
                    contact.getName(),
                    contact.getPhone(),
                    contact.getEmail(),
                    contact.getAddress(),
            };
            tableModel.addRow(row);
        }
        updateContactCount();
    }
    private void searchContacts(){
        String searchTerm = searchField.getText().trim();
        if(searchTerm.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter a search term","Search Error",JOptionPane.WARNING_MESSAGE);
            return;
        }
        tableModel.setRowCount(0);
        List<Contact> contacts = contactDAO.searchContactsByName(searchTerm);
        if(contacts.isEmpty()){
            JOptionPane.showMessageDialog(this, "No contacts found matching " + searchTerm);
        }else{
            for(Contact contact: contacts){
                Object[] row = {
                        contact.getId(),
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        contact.getCategory()
                };
                tableModel.addRow(row);
            }
        }
    }


}

