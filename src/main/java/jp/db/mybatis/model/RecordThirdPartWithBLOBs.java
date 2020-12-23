package jp.db.mybatis.model;

public class RecordThirdPartWithBLOBs extends RecordThirdPart {
    private String testparam;

    private String formalparam;

    private String comments;

    public String getTestparam() {
        return testparam;
    }

    public void setTestparam(String testparam) {
        this.testparam = testparam == null ? null : testparam.trim();
    }

    public String getFormalparam() {
        return formalparam;
    }

    public void setFormalparam(String formalparam) {
        this.formalparam = formalparam == null ? null : formalparam.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }
}