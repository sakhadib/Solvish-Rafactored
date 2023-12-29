import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class TimedExam implements iExam{
    int examID;
    List<iQuestion> questions;
    int timeLimit;

    public TimedExam(int examID, List<iQuestion> questions, int timeLimit) {
        this.examID = examID;
        this.questions = questions;
        this.timeLimit = timeLimit;
    }

    public TimedExam(int examID, int timeLimit) {
        this.examID = examID;
        this.timeLimit = timeLimit;
    }

    public void addQuestion(iQuestion question) {
        questions.add(question);
    }

    public int getCorrect() {
        int correct = 0;
        for (iQuestion question : questions) {
            if (question.isCorrect()) {
                correct++;
            }
        }
        return correct;
    }

    public int getIncorrect() {
        int incorrect = 0;
        for (iQuestion question : questions) {
            if (!question.isCorrect()) {
                incorrect++;
            }
        }
        return incorrect;
    }

    public int getUnanswered() {
        int unanswered = 0;
        for (iQuestion question : questions) {
            if (!question.isAnswered()) {
                unanswered++;
            }
        }
        return unanswered;
    }

    public double getScore() {
        int corr = getCorrect();
        int incorr = getIncorrect();
        double score = corr - (incorr * 0.25);

        return score;
    }

    public void runExam() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Scanner input = new Scanner(System.in);

        for (iQuestion question : questions) {
            System.out.println(question.getQuestion());
            System.out.println("A. " + question.getOptionA());
            System.out.println("B. " + question.getOptionB());
            System.out.println("C. " + question.getOptionC());
            System.out.println("D. " + question.getOptionD());
            System.out.println("Enter your answer: ");

            Future<String> future = executor.submit(() -> input.nextLine());

            try {
                String answer = future.get(timeLimit, TimeUnit.MINUTES);
                question.checkAnswer(answer);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Time's up!");
                break;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdownNow();
    }
}