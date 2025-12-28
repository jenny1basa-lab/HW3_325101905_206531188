package HW3;

public class KnockKnockProtocol implements Protocol {

    private static final int WAITING = 0;
    private static final int SENT_KNOCKKNOCK = 1;
    private static final int SENT_CLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUM_JOKES = 5;

    private int state = WAITING;
    private int currentJoke = 0;

    private boolean finished = false;

    private final String[] clues = {"Turnip", "Little Old Lady", "Atch", "Who", "Who"};
    private final String[] answers = {
            "Turnip the heat, it's cold in here!",
            "I didn't know you could yodel!",
            "Bless you!",
            "Is there an owl in here?",
            "Is there an echo in here?"
    };

    @Override
    public String processInput(String theInput) {
        if (finished) {
            return "Bye.";
        }

        String theOutput;

        if (state == WAITING) {
            theOutput = "Knock! Knock!";
            state = SENT_KNOCKKNOCK;
        } else if (state == SENT_KNOCKKNOCK) {
            if (theInput != null && theInput.equalsIgnoreCase("Who's there?")) {
                theOutput = clues[currentJoke];
                state = SENT_CLUE;
            } else {
                theOutput = "You're supposed to say \"Who's there?\"! Try again. Knock! Knock!";
            }
        } else if (state == SENT_CLUE) {
            if (theInput != null && theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
                theOutput = answers[currentJoke] + " Want another? (y/n)";
                state = ANOTHER;
            } else {
                theOutput = "You're supposed to say \"" + clues[currentJoke] + " who?\"! Try again. Knock! Knock!";
                state = SENT_KNOCKKNOCK;
            }
        } else { 
            if (theInput != null && theInput.equalsIgnoreCase("y")) {
                theOutput = "Knock! Knock!";
                currentJoke = (currentJoke + 1) % NUM_JOKES;
                state = SENT_KNOCKKNOCK;
            } else {
                theOutput = "Bye.";
                finished = true;
                state = WAITING;
            }
        }

        return theOutput;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
