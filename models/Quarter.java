
package models;

public class Quarter {

    // Quarter identification details
    private String quarterId;
    private String quarterType;
    private String colony;
    private int houseNumber;

    // Occupancy information
    private boolean occupied;
    private int occupantEmployeeId;

    // Constructor
    public Quarter(String quarterId, String quarterType,
            String colony, int houseNumber) {

        this.quarterId = quarterId;
        this.quarterType = quarterType;
        this.colony = colony;
        this.houseNumber = houseNumber;

        // A newly created quarter is vacant by default
        this.occupied = false;
        this.occupantEmployeeId = -1;
    }

    // Getters

    public String getQuarterId() {
        return quarterId;
    }

    public String getQuarterType() {
        return quarterType;
    }

    public String getColony() {
        return colony;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getOccupantEmployeeId() {
        return occupantEmployeeId;
    }

    // Assign a vacant quarter to an employee
    public boolean allotQuarter(int employeeId) {

        if (occupied || employeeId <= 0) {
            return false;
        }

        occupied = true;
        occupantEmployeeId = employeeId;

        return true;
    }

    // Release an occupied quarter
    public boolean vacateQuarter() {

        if (!occupied) {
            return false;
        }

        occupied = false;
        occupantEmployeeId = -1;

        return true;
    }

    // Display quarter details in HR's quarter table
    public void display() {

        String status = occupied ? "OCCUPIED" : "VACANT";

        String occupant = occupied
                ? String.valueOf(occupantEmployeeId)
                : "-";

        System.out.printf(
                "%-10s %-8s %-12s %-10d %-12s %-10s%n",
                quarterId,
                quarterType,
                colony,
                houseNumber,
                status,
                occupant);
    }
}
