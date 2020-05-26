package com.example.util.enums;

public enum ReportStatus {

    COMPLETE("1", "运行完成"),

    BEING("2", "运行中"),

    FAIL("3", "运行失败");

    private String code;
    private String status;

    ReportStatus(String code, String status) {
        this.code = code;
        this.status = status;
    }

    /**
     * 根据编码来查询beanName
     *
     * @param code 编码
     * @return beanName
     */
    public static String getBeanNameByCode(String code) {
        /*
            这里应该从数据字典中找到对应的值
            使用了策略模式
         */
        for (ReportStatus reprotType : ReportStatus.values()) {
            if (code.equals(reprotType.code)) {
                return reprotType.status;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
