public class ExamFactory {
    private static ExamFactory instance = new ExamFactory();
    private ExamFactory() {}
    public static ExamFactory getInstance() {
        if(instance == null) {
            instance = new ExamFactory();
        }
        return instance;
    }

    public iExam getExam() {
        return new NonTimedExam();
    }

    public iExam getExam(int timeLimit) {
        try{
            return new TimedExam(timeLimit);
        }
        catch(Exception e){
            throw new IllegalArgumentException("Time limit must be a positive integer in minutes");
        }
    }
}