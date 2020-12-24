package jp.db.mybatis.model;

public class RecordThirdPartWithBLOBs extends RecordThirdPart {
    private String testParam;

    private String formalParam;

    private String comments;

    public String getTestParam() {
        return testParam;
    }

    public void setTestParam(String testParam) {
        this.testParam = testParam == null ? null : testParam.trim();
    }

    public String getFormalParam() {
        return formalParam;
    }

    public void setFormalParam(String formalParam) {
        this.formalParam = formalParam == null ? null : formalParam.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }
}