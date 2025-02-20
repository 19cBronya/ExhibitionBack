package com.cuit.common.common;

/**
 * 常用枚举项
 *
 * @author sccl
 */
public class CommonConstants {

    /**
     * 自定义编码
     */
    public enum ConstantsCode {
        成功("200"),
        错误("400"),
        系统错误("500");

        private final String value;

        ConstantsCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 角色权限
     */
    public enum UserRole {
        普通观众("1"),
        专业观众("2"),
        参展商("3"),
        办展机构("4"),
        展览馆("5"),
        管理员("6");

        private final String value;

        UserRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     *  状态标志
     */
    public enum Status {

        启用("0"),
        禁用("1"),
        审核中("2");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static String getTextFromValue(String value) {
            for (Status status : Status.values()) {
                if (status.getValue().equals(value)) {
                    return status.name();
                }
            }
            return null;
        }

    }

    /**
     * 删除标志
     */
    public enum DeleteFlag {

        正常("0"),
        删除("1");

        private final String value;

        DeleteFlag(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

    /**
     * 支付标志
     */
    public enum PayStatus{

        未支付("0"),
        支付成功("1"),
        退款成功("2");

        private final String value;

        PayStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static String getTextFromValue(String value) {
            for (PayStatus payStatus : PayStatus.values()) {
                if (payStatus.getValue().equals(value)) {
                    return payStatus.name();
                }
            }
            return null;
        }
    }

    /**
     * 售出状态
     */
    public enum SoldStatus{

        未售出("0"),
        已售出("1");

        private final String value;

        SoldStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static String getTextFromValue(String value) {
            for (SoldStatus soldStatus : SoldStatus.values()) {
                if (soldStatus.getValue().equals(value)) {
                    return soldStatus.name();
                }
            }
            return null;
        }
    }

    /**
     * 购买类型
     */
    public enum PurchaseType{

        展会订票("0"),
        展位租赁("1");

        private final String value;

        PurchaseType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static String getTextFromValue(String value) {
            for (PurchaseType purchaseType : PurchaseType.values()) {
                if (purchaseType.getValue().equals(value)) {
                    return purchaseType.name();
                }
            }
            return null;
        }
    }

}
