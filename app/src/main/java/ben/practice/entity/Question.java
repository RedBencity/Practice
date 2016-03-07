package ben.practice.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private String title;
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;
    private String analyze;
    private String subject;
    private String point;

    public Question(String title, String a, String b, String c, String d, String answer, String analyze,String subject,String point) {
        this.title = title;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
        this.analyze = analyze;
        this.subject = subject;
        this.point = point;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalyze() {
        return analyze;
    }

    public void setAnalyze(String analyze) {
        this.analyze = analyze;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel in, int flags) {
        in.writeString(title);
        in.writeString(a);
        in.writeString(b);
        in.writeString(c);
        in.writeString(d);
        in.writeString(answer);
        in.writeString(analyze);
        in.writeString(subject);
        in.writeString(point);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private Question(Parcel in) {
        title = in.readString();
        a = in.readString();
        b = in.readString();
        c = in.readString();
        d = in.readString();
        answer = in.readString();
        analyze = in.readString();
        subject = in.readString();
        point = in.readString();
    }

}
