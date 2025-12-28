package HW3;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RuppinRegistrationProtocol implements Protocol {

    private static final List<Client> clientState = Collections.synchronizedList(new ArrayList<>());
private enum State {
        START, NEW_USERNAME, NEW_PASSWORD, NEW_STATUS, NEW_YEARS,
        EXIST_USERNAME, EXIST_PASSWORD, UPDATE_ASK,
        CHANGE_USERNAME_ASK, CHANGE_USERNAME_VAL,
        CHANGE_PASSWORD_ASK, CHANGE_PASSWORD_VAL,
        CHANGE_YEARS_ASK, CHANGE_YEARS_VAL,
        FINISHED
    }

    private State state = State.START;
    private boolean finished = false;

    private Client current;

    public String processInput(String input) {
        switch(state) {

        case START:
            state = State.NEW_USERNAME;
            return "Do you want to register? (yes/no)";

        case NEW_USERNAME:
            if (input == null) return "Do you want to register? (yes/no)";

            if (input.equalsIgnoreCase("yes")) {
                state = State.NEW_PASSWORD; 
                return "Enter a new username:";
            } else {
                state = State.EXIST_USERNAME;
                return "Username:";
            }
            case NEW_PASSWORD:
                if (usernameExists(input))
                    return "Name not OK. Username exists. Choose a different name:";
                current = new Client(input, "", "", 0);
                state = State.NEW_STATUS;
                return "Checking name...\nOK. Enter a strong password:";

            case NEW_STATUS:
                if (!strongPassword(input))
                    return "Password not OK. Must contain upper, lower, digit, length>=9\nEnter a strong password:";
                current.setPassword(input);
                state = State.NEW_YEARS;
                return "Password accepted.\nWhat is your academic status? (student/teacher/other)";

            case NEW_YEARS:
                current.setAcademicStatus(input);
                state = State.FINISHED;
                return "How many years have you been at Ruppin?";

            case FINISHED:
                current.setYears(Integer.parseInt(input));
                clientState.add(current);
                if (clientState.size() % 3 == 0) backupCSV();
                finished = true;
                return "Registration complete.";

            case EXIST_USERNAME:
                current = findClient(input);
                if (current == null) {
                    state = State.NEW_PASSWORD;
                    return "User not found. Enter a username to register:";
                }
                state = State.EXIST_PASSWORD;
                return "Password:";

            case EXIST_PASSWORD:
                if (!current.getPassword().equals(input))
                    return "Password incorrect. Try again:";
                state = State.UPDATE_ASK;
                return "Welcome back, " + current.getUsername() +
                       ". Last time you defined yourself as " +
                       current.getAcademicStatus() + " for " +
                       current.getYears() + " years.\nDo you want to update your information? (yes/no)";

            case UPDATE_ASK:
                if (input.equalsIgnoreCase("no")) {
                    finished = true;
                    return "Bye.";
                }
                state = State.CHANGE_USERNAME_ASK;
                return "Do you want to change your username? (yes/no)";

            case CHANGE_USERNAME_ASK:
                if (input.equalsIgnoreCase("yes")) {
                    state = State.CHANGE_USERNAME_VAL;
                    return "Enter new username:";
                }
                state = State.CHANGE_PASSWORD_ASK;
                return "Do you want to change your password? (yes/no)";

            case CHANGE_USERNAME_VAL:
                if (usernameExists(input))
                    return "Name not OK. Username exists. Choose a different name:";
                current.setUsername(input);
                state = State.CHANGE_PASSWORD_ASK;
                return "Username updated successfully.\nDo you want to change your password? (yes/no)";

            case CHANGE_PASSWORD_ASK:
                if (input.equalsIgnoreCase("yes")) {
                    state = State.CHANGE_PASSWORD_VAL;
                    return "Enter new password:";
                }
                state = State.CHANGE_YEARS_ASK;
                return "Do you want to update your years of study? (yes/no)";

            case CHANGE_PASSWORD_VAL:
                if (!strongPassword(input))
                    return "Password not OK. Enter again:";
                current.setPassword(input);
                state = State.CHANGE_YEARS_ASK;
                return "Password updated successfully.\nDo you want to update your years of study? (yes/no)";

            case CHANGE_YEARS_ASK:
                if (input.equalsIgnoreCase("yes")) {
                    state = State.CHANGE_YEARS_VAL;
                    return "Enter number of years:";
                }
                finished = true;
                return "Thanks. Your information has been updated.";

            case CHANGE_YEARS_VAL:
                current.setYears(Integer.parseInt(input));
                finished = true;
                return "Years of study updated successfully.\nThanks. Your information has been updated.";
        }
        return "";
    }

    public boolean isFinished() { return finished; }


    private boolean usernameExists(String name) {
        return clientState.contains(new Client(name, "", "", 0));
    }

    private Client findClient(String name) {
        synchronized (clientState) {
            for (Client c : clientState) {
                if (c.getUsername().equalsIgnoreCase(name)) {
                    return c;
                }
            }
        }
        return null;
    }

private boolean strongPassword(String p) {
        if (p.length() < 9) return false;
        boolean up=false, low=false, dig=false;
        for(char c:p.toCharArray()) {
            if (Character.isUpperCase(c)) up=true;
            else if (Character.isLowerCase(c)) low=true;
            else if (Character.isDigit(c)) dig=true;
        }
        return up && low && dig;
    }

    private void backupCSV() {
        List<Client> snapshot;
        synchronized (clientState) {
            snapshot = new ArrayList<>(clientState);
        }

        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = "backup_" + date + ".csv";

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("username,password,academicStatus,years\n");
                for (Client c : snapshot) {
                    writer.write(c.toCsvRow() + "\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



