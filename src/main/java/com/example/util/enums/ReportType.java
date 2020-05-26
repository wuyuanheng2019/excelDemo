package com.example.util.enums;

public enum ReportType {

    STUDENT("1", "StudentServiceImpl");

    private String code;
    private String beanName;

    ReportType(String code, String beanName) {
        this.code = code;
        this.beanName = beanName;
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
        for (ReportType reportType : ReportType.values()) {
            if (code.equals(reportType.code)) {
                return reportType.beanName;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getBeanName() {
        return beanName;
    }
}
