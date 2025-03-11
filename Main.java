import java.util.HashMap;

class ShipmentNode {
    String status;
    String time;
    String location;
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

    public ShipmentHistory() {
        this.head = null;
    }

    public void addStatus(String status, String time, String location) {
        ShipmentNode newNode = new ShipmentNode(status, time, location);
        if (head == null) {
            head = newNode;
        } else {
            ShipmentNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public void getStatus() {
        ShipmentNode current = head;
        while (current != null) {
            System.out.println("Time: " + current.time + ", Status: " + current.status + ", Location: " + current.location);
            current = current.next;
        }
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

    public DeliveryPriorityQueue() {
        this.head = null;
    }

    public PackageNode addPackage(String packageId, String status, String time, String location, int priority) {
        PackageNode newPackage = new PackageNode(packageId, status, time, location, priority);

        if (head == null || head.priority > priority) {
            newPackage.next = head;
            head = newPackage;
        } else {
            PackageNode current = head;

            while (current.next != null && current.next.priority < priority) {
                current = current.next;
            }

            while (current.next != null && current.next.priority == priority) {
                current = current.next;
            }

            newPackage.next = current.next;
            current.next = newPackage;
        }

        return newPackage;
    }

    public boolean removePackage(String packageId) {
        if (head == null) {
            System.out.println("Cannot remove. Queue is empty.");
            return false;
        }

        if (head.packageId.equals(packageId)) {
            head = head.next;
            System.out.println("Package " + packageId + " removed from queue.");
            return true;
        }

        PackageNode current = head;
        while (current.next != null && !current.next.packageId.equals(packageId)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            System.out.println("Package " + packageId + " removed from queue.");
            return true;
        }

        System.out.println("Package " + packageId + " not found in queue.");
        return false;
    }
}

class PackageTrackingSystem {
    private DeliveryPriorityQueue deliveryQueue;
    private HashMap<String, PackageNode> packages;

    public PackageTrackingSystem() {
        this.deliveryQueue = new DeliveryPriorityQueue();
        this.packages = new HashMap<>();
    }

    public PackageNode addPackage(String packageId, String status, String time, String location, int priority) {
        if (packages.containsKey(packageId)) {
            System.out.println("Error: Package " + packageId + " already exists!");
            return null;
        }
        PackageNode packageNode = deliveryQueue.addPackage(packageId, status, time, location, priority);
        packages.put(packageId, packageNode);
        return packageNode;
    }

    public void updateStatus(String packageId, String newStatus, String time, String location) {
        PackageNode packageNode = packages.get(packageId);
        if (packageNode != null) {
            packageNode.history.addStatus(newStatus, time, location);
        } else {
            System.out.println("Error: Package " + packageId + " not found in the system!");
        }
    }

    public void getHistory(String packageId) {
        PackageNode packageNode = packages.get(packageId);
        if (packageNode != null) {
            packageNode.history.getStatus();
        } else {
            System.out.println("Error: Package " + packageId + " not found in the system!");
        }
    }

    public void removePackage(String packageId) {
        if (packages.containsKey(packageId)) {
            boolean success = deliveryQueue.removePackage(packageId);
            if (success) {
                packages.remove(packageId);
                System.out.println("Package " + packageId + " successfully removed from the system.");
            }
        } else {
            System.out.println("Package " + packageId + " not found in the system!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        PackageTrackingSystem trackingSystem = new PackageTrackingSystem();
    }
}

        
