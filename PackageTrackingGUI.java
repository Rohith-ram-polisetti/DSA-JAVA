import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

class ShipmentNode {
    String status, time, location;
    ShipmentNode next;

    public ShipmentNode(String status, String time, String location) {
        this.status = status;
        this.time = time;
        this.location = location;
        this.next = null;
    }
}

class ShipmentHistory {
    private ShipmentNode head;

    public void addStatus(String status, String time, String location) {
        ShipmentNode newNode = new ShipmentNode(status, time, location);
        if (head == null) head = newNode;
        else {
            ShipmentNode temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        ShipmentNode current = head;
        while (current != null) {
            sb.append("Time: ").append(current.time)
              .append(", Status: ").append(current.status)
              .append(", Location: ").append(current.location).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}

class PackageNode {
    String packageId;
    int priority;
    ShipmentHistory history;
    PackageNode next;

    public PackageNode(String packageId, String status, String time, String location, int priority) {
        this.packageId = packageId;
        this.priority = priority;
        this.history = new ShipmentHistory();
        this.history.addStatus(status, time, location);
        this.next = null;
    }
}

class DeliveryPriorityQueue {
    private PackageNode head;

    public PackageNode addPackage(String packageId, String status, String time, String location, int priority) {
        PackageNode newPkg = new PackageNode(packageId, status, time, location, priority);
        if (head == null || head.priority > priority) {
            newPkg.next = head;
            head = newPkg;
        } else {
            PackageNode curr = head;
            while (curr.next != null && curr.next.priority <= priority) curr = curr.next;
            newPkg.next = curr.next;
            curr.next = newPkg;
        }
        return newPkg;
    }

    public boolean removePackage(String packageId) {
        if (head == null) return false;
        if (head.packageId.equals(packageId)) {
            head = head.next;
            return true;
        }
        PackageNode curr = head;
        while (curr.next != null && !curr.next.packageId.equals(packageId)) {
            curr = curr.next;
        }
        if (curr.next != null) {
            curr.next = curr.next.next;
            return true;
        }
        return false;
    }
}

class PackageTrackingSystem {
    private DeliveryPriorityQueue queue;
    private HashMap<String, PackageNode> packages;

    public PackageTrackingSystem() {
        queue = new DeliveryPriorityQueue();
        packages = new HashMap<>();
    }

    public String addPackage(String id, String status, String time, String location, int priority) {
        if (packages.containsKey(id)) return "❌ Package already exists!";
        PackageNode pkg = queue.addPackage(id, status, time, location, priority);
        packages.put(id, pkg);
        return "✅ Package added!";
    }

    public String updateStatus(String id, String status, String time, String location) {
        PackageNode pkg = packages.get(id);
        if (pkg == null) return "❌ Package not found!";
        pkg.history.addStatus(status, time, location);
        return "✅ Status updated!";
    }

    public String getHistory(String id) {
        PackageNode pkg = packages.get(id);
        if (pkg == null) return "❌ Package not found!";
        return pkg.history.getStatus();
    }

    public String removePackage(String id) {
        if (!packages.containsKey(id)) return "❌ Package not found!";
        if (queue.removePackage(id)) {
            packages.remove(id);
            return "✅ Package removed!";
        }
        return "❌ Error removing package!";
    }
}

public class PackageTrackingGUI extends JFrame {
    private PackageTrackingSystem system = new PackageTrackingSystem();

    private JTextField idField, statusField, timeField, locationField;
    private JComboBox<String> priorityBox;
    private JTextArea outputArea;

    public PackageTrackingGUI() {
        setTitle("📦 Package Tracking System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2));
        form.add(new JLabel("Package ID:"));
        idField = new JTextField(); form.add(idField);
        form.add(new JLabel("Status:"));
        statusField = new JTextField(); form.add(statusField);
        form.add(new JLabel("Time:"));
        timeField = new JTextField(); form.add(timeField);
        form.add(new JLabel("Location:"));
        locationField = new JTextField(); form.add(locationField);
        form.add(new JLabel("Priority (1-High, 2-Med, 3-Low):"));
        priorityBox = new JComboBox<>(new String[]{"1", "2", "3"});
        form.add(priorityBox);

        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add Package");
        JButton updateBtn = new JButton("Update Status");
        JButton viewBtn = new JButton("View History");
        JButton removeBtn = new JButton("Remove Package");
        buttons.add(addBtn); buttons.add(updateBtn); buttons.add(viewBtn); buttons.add(removeBtn);

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        add(form, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String res = system.addPackage(
                idField.getText(),
                statusField.getText(),
                timeField.getText(),
                locationField.getText(),
                Integer.parseInt((String) priorityBox.getSelectedItem())
            );
            outputArea.setText(res);
        });

        updateBtn.addActionListener(e -> {
            String res = system.updateStatus(
                idField.getText(),
                statusField.getText(),
                timeField.getText(),
                locationField.getText()
            );
            outputArea.setText(res);
        });

        viewBtn.addActionListener(e -> {
            String res = system.getHistory(idField.getText());
            outputArea.setText(res);
        });

        removeBtn.addActionListener(e -> {
            String res = system.removePackage(idField.getText());
            outputArea.setText(res);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PackageTrackingGUI().setVisible(true));
    }
}