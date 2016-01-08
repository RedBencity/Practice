package ben.practice.entity;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class PracticePointInfo {
    public PracticePointInfo(String pointName) {
        this.pointName = pointName;
    }

    private String pointName;

    public String getName() {
        return pointName;
    }

    public void setName(String name) {
        this.pointName = name;
    }
}
