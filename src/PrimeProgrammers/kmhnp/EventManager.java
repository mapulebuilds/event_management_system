
package PrimeProgrammers.kmhnp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EventManager extends JFrame implements ActionListener, ListSelectionListener {

    private JTextField eventNameField;
    private JTextField dateField;
    private JTextArea eventDescriptionArea;
    private JButton addButton;
    private JButton loadButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField searchDateField;
    private JButton searchButton;
    private JList<Event> eventList;
    private DefaultListModel<Event> listModel;

    private List<Event> events = new ArrayList<>();
    private static final String DATA_FILE = "events.txt";
    private Event selectedEvent = null; 

    
    private static final Font APP_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); 
    private static final Color MEDIUM_BLUE = new Color(100, 149, 237); 
    private static final Color DARK_BLUE = new Color(0, 0, 139);  

    public EventManager() {
        setTitle("University Events");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BLUE); 
        setFont(APP_FONT);

        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout()); 
        inputPanel.setBackground(MEDIUM_BLUE);
        inputPanel.setBorder(createPanelBorder("Event Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 5, 0, 5); 

        JLabel nameLabel = new JLabel("Event Name:");
        nameLabel.setFont(APP_FONT);
        inputPanel.add(nameLabel, gbc);

        gbc.gridy++;
        eventNameField = new JTextField(20);
        eventNameField.setFont(APP_FONT);
        inputPanel.add(eventNameField, gbc);

        gbc.gridy++;
        JLabel dateLabel = new JLabel("Date(YYYY-MM-DD):");
        dateLabel.setFont(APP_FONT);
        inputPanel.add(dateLabel, gbc);

        gbc.gridy++;
        dateField = new JTextField(10);
        dateField.setFont(APP_FONT);
        inputPanel.add(dateField, gbc);

        gbc.gridy++;
        JLabel descriptionLabel = new JLabel("Event Description:");
        descriptionLabel.setFont(APP_FONT);
        inputPanel.add(descriptionLabel, gbc);

        gbc.gridy++;
        eventDescriptionArea = new JTextArea(3, 20);
        eventDescriptionArea.setFont(APP_FONT);
        JScrollPane descriptionScrollPane = new JScrollPane(eventDescriptionArea);
        inputPanel.add(descriptionScrollPane, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(MEDIUM_BLUE);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        addButton = createStyledButton("Add");
        loadButton = createStyledButton("Load");
        editButton = createStyledButton("Edit");
        deleteButton = createStyledButton("Delete");
        addButton.addActionListener(this);
        loadButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(MEDIUM_BLUE);
        leftPanel.add(inputPanel);
        leftPanel.add(buttonPanel);

        
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(LIGHT_BLUE);
        searchPanel.setBorder(createPanelBorder("Search"));

        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.gridx = 0;
        searchGbc.gridy = 0;
        searchGbc.anchor = GridBagConstraints.LINE_START;
        searchGbc.insets = new Insets(5, 5, 0, 5);

        JLabel searchLabel = new JLabel("Search by date:");
        searchLabel.setFont(APP_FONT);
        searchPanel.add(searchLabel, searchGbc);

        searchGbc.gridy++;
        searchDateField = new JTextField(10);
        searchDateField.setFont(APP_FONT);
        searchPanel.add(searchDateField, searchGbc);

        searchGbc.gridy++;
        searchButton = createStyledButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton, searchGbc);

        
        listModel = new DefaultListModel<>();
        eventList = new JList<>(listModel);
        eventList.setFont(APP_FONT);
        eventList.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(eventList);
        listScrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DARK_BLUE, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(LIGHT_BLUE);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(listScrollPane, BorderLayout.CENTER);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        loadEventsFromFile(); 

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(APP_FONT);
        button.setBackground(MEDIUM_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        Border lineBorder = new LineBorder(DARK_BLUE, 1);
        Border emptyBorder = new EmptyBorder(5, 15, 5, 15);
        button.setBorder(new CompoundBorder(lineBorder, emptyBorder));
        return button;
    }

    
    private Border createPanelBorder(String title) {
        Border lineBorder = new LineBorder(DARK_BLUE, 2);
        Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
        Border compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        return BorderFactory.createTitledBorder(compoundBorder, title,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                APP_FONT, DARK_BLUE);
    }

    private void saveEventsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Event event : events) {
                writer.println(event.getEventId() + "," + event.getEventName() + "," + event.dateToString() + "," + event.getEventDescription());
            }
            JOptionPane.showMessageDialog(this, "Events saved to " + DATA_FILE, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving events to file: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEventsFromFile() {
        listModel.clear();
        events.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        int eventId = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        LocalDate date = Event.stringToDate(parts[2].trim());
                        String description = parts[3].trim();
                        if (date != null) {
                            Event event = new Event(eventId, name, date, description);
                            events.add(event);
                            listModel.addElement(event);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error event ID: " + line);
                    }
                } else {
                    System.err.println("Invalid event data format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading events from file: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEvent() {
        String name = eventNameField.getText().trim();
        String dateStr = dateField.getText().trim();
        String description = eventDescriptionArea.getText().trim();

        if (name.isEmpty() || dateStr.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all event details.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate date = Event.stringToDate(dateStr);
        if (date != null) {
            Event newEvent = new Event(name, date, description);
            events.add(newEvent);
            listModel.addElement(newEvent);
            clearInputFields();
            saveEventsToFile();
        }
    }

    private void editEvent() {
        if (selectedEvent == null) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = eventNameField.getText().trim();
        String dateStr = dateField.getText().trim();
        String description = eventDescriptionArea.getText().trim();

        if (name.isEmpty() || dateStr.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all event details.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate date = Event.stringToDate(dateStr);
       int selectedIndex = eventList.getSelectedIndex(); 
if (selectedIndex >= 0) {
    selectedEvent.setEventName(name);
    selectedEvent.setDate(date);
    selectedEvent.setEventDescription(description);
    listModel.setElementAt(selectedEvent, selectedIndex);
    clearInputFields();
    saveEventsToFile();
    selectedEvent = null;
} 
        
    }

    private void deleteEvent() {
        if (selectedEvent == null) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            events.remove(selectedEvent);
            listModel.removeElement(selectedEvent);
            clearInputFields();
            saveEventsToFile();
            selectedEvent = null; 
        }
    }

    private void searchEventsByDate() {
        String searchDateStr = searchDateField.getText().trim();
        LocalDate searchDate = Event.stringToDate(searchDateStr);
        if (searchDate != null) {
            listModel.clear();
            for (Event event : events) {
                if (event.getDate().equals(searchDate)) {
                    listModel.addElement(event);
                }
            }
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No events found on " + searchDateStr, "Search Results", JOptionPane.INFORMATION_MESSAGE);
                loadEventsFromFile(); 
            }
        } else if (!searchDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid date format for search. Use YYYY-MM-DD.", "Search Error", JOptionPane.ERROR_MESSAGE);
            loadEventsFromFile(); 
        } else {
            loadEventsFromFile(); 
        }
    }

    private void clearInputFields() {
        eventNameField.setText("");
        dateField.setText("");
        eventDescriptionArea.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addEvent();
        } else if (e.getSource() == loadButton) {
            loadEventsFromFile();
        } else if (e.getSource() == editButton) {
            editEvent();
        } else if (e.getSource() == deleteButton) {
            deleteEvent();
        } else if (e.getSource() == searchButton) {
            searchEventsByDate();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            selectedEvent = eventList.getSelectedValue();
            if (selectedEvent != null) {
                eventNameField.setText(selectedEvent.getEventName());
                dateField.setText(selectedEvent.dateToString());
                eventDescriptionArea.setText(selectedEvent.getEventDescription());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EventManager::new);
    }
}

